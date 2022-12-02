package redSocial.DAO;

import redSocial.interfaces.Dao;
import redSocial.model.Comment;
import redSocial.model.Post;
import redSocial.model.User;
import redSocial.utils.Connection.Connect;
import redSocial.utils.Log;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostDao extends Post implements Dao {

    private final static String INSERT = "INSERT INTO post (id,id_user,fecha_creacion,texto) VALUES (NULL,?,?,?)";
    private final static String UPDATE = "UPDATE post SET fecha_modificacion=?,texto=? WHERE id=?";
    private final static String DELETE = "DELETE FROM post WHERE id=?";
    private final static String SELECTBYID = "SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post WHERE id=?";
    private final static String SELECTALL = "SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post ORDER BY fecha_creacion DESC";
    private final static String SELECTBYUSER = "SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post WHERE id_user=? ORDER BY fecha_modificacion DESC";
    private final static String SELECTCOMMENTS = "SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post WHERE id_user=?";
    private final static String INSERTLIKE = "INSERT INTO likes (id_user, id_post, id) VALUES (?,?,NULL)";
    private final static String DELETELIKE = "DELETE FROM likes WHERE id_user=?";
    private final static String SELECTALLLIKES = "SELECT id_user,id_post,id FROM likes WHERE id_post=?";

    public PostDao(User u, int id){
        super(u,id);
    }
    public PostDao(User u,int id, Date fCreacion, Date fModificacion,String texto){
        super(u,id,fCreacion,fModificacion,texto);
    }
    public PostDao(User u, int id, Date fCreacion, Date fModificacion, String texto, List<Comment> comentarios, Set<User> likes){
        super(u,id,fCreacion,fModificacion,texto,comentarios,likes);
    }
    public PostDao(){
        super();
    }
    public PostDao(int id){
        this.getById(id);
    }

    @Override
    public void save() {
        //INSERT
        Connection cn = Connect.getConnect();
        if(cn != null) {
            PreparedStatement ps;
            try {
                ps = cn.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, this.getUserName().getId());
                ps.setDate(2, Date.valueOf(LocalDate.now()));
                ps.setString(3, this.getText());
                ps.executeUpdate();  //devuelve 1 si ha salido bien
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    this.setId(rs.getInt(1));
                }
                ps.close();
                rs.close();
            } catch (SQLException e) {
                Log.severe("Error al insertar el post: " + e.getMessage());
            }
        }
}

    @Override
    public void delete() {
        //UPDATE
        Connection cn = Connect.getConnect();
        if(cn != null) {
            PreparedStatement ps;
            try {
                ps = cn.prepareStatement(DELETE);
                ps.setInt(1, this.getId());
                if(ps.executeUpdate()==1) {
                    this.setId(-1);
                }
                ps.close();
            } catch (SQLException e) {
                Log.severe("Error al borrar el post: "+e.getMessage());
            }

        }
    }

    @Override
    public void update() {
        if(this.getId()!=-1) {
            //UPDATE
            Connection cn = Connect.getConnect();
            if(cn != null) {
                PreparedStatement ps;
                try {
                    ps = cn.prepareStatement(UPDATE);
                    ps.setDate(1, Date.valueOf(LocalDate.now()));
                    ps.setString(2, this.getText());
                    ps.setInt(3, this.getId());
                    ps.executeUpdate();  //devuelve 1 si funciona
                    ps.close();
                } catch (SQLException e) {
                    Log.severe("Error al actualizar el post: "+e.getMessage());
                }

            }
        }
    }

    public static List<PostDao> getAll(){
        List<PostDao> result = new ArrayList();
        Connection cn = Connect.getConnect();
        UserDao user = new UserDao();
        if(cn != null) {
            PreparedStatement ps;
            try {
                ps = cn.prepareStatement(SELECTALL);
                if(ps.execute()) {
                    ResultSet rs = ps.getResultSet();
                    while(rs.next()) {
                        PostDao p = new PostDao(user.getById(rs.getInt("id_user")),
                                rs.getInt("id"),
                                rs.getDate("fecha_creacion"),
                                rs.getDate("fecha_modificacion"),
                                rs.getString("texto"));
                        result.add(p);
                    }
                    rs.close();
                }
                ps.close();
            } catch (SQLException e) {
                Log.severe("Error al obtener todos los posts"+e.getMessage());
            }
        }
        return result;
    }

    public Post getById(int id) {
        Connection cn = Connect.getConnect();
        UserDao user = new UserDao();
        PostDao post = new PostDao(userName, id, dateCreate, dateUpdate, text, comments, likes);
        CommentDao comments = null;
        if(cn != null) {
            PreparedStatement ps;
            try {
                ps = cn.prepareStatement(SELECTBYID);
                ps.setInt(1, id);
                if(ps.execute()) {
                    ResultSet rs = ps.getResultSet();
                    if(rs.next()) {
                        id=rs.getInt(1);
                        userName = user.getById(rs.getInt("id_user"));
                        post.dateCreate = rs.getDate("fecha_creacion");
                        post.dateUpdate = rs.getDate("fecha_modificacion");
                        post.text = rs.getString("texto");
                    }
                }
                ps.close();
            } catch (SQLException e) {
                Log.severe("Error al obtener el post con id: "+id+"\n"+e.getMessage());
            }

        }
        return post;
    }

    public List<Comment> getComments(){
        List<Comment> comments = new ArrayList();
        Connection cn = Connect.getConnect();
        if (cn != null) {
            PreparedStatement ps = null;
            try {
                ps = cn.prepareStatement(SELECTCOMMENTS);
                ps.setInt(1, this.id);
                if (ps.execute()) {
                    ResultSet rs = ps.getResultSet();
                    while (rs.next()) {

                    }
                    rs.close();
                }
                ps.close();
            } catch (SQLException e) {
                Log.severe("Error al obtener los comentarios del post con id: "+id+"\n"+e.getMessage());
            }
        }
        return comments;
    }

    public static List<PostDao> getAllByUser(int id){
        UserDao user = new UserDao();
        Connection cn = Connect.getConnect();
        List<PostDao> posts = new ArrayList<PostDao>();

        if(cn != null) {
            try {
                PreparedStatement ps = cn.prepareStatement(SELECTBYUSER);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    PostDao p = new PostDao();
                    p.setId(rs.getInt("id"));
                    p.setText(rs.getString("texto"));
                    p.setDateCreate(rs.getDate("fecha_creacion"));
                    p.setUserName(user.getById(rs.getInt("id_user")));
                    posts.add(p);
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }

    public void saveLike(User u, Post p) {
        Connection con = Connect.getConnect();
        con = Connect.getConnect();
        if (con != null){
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(INSERTLIKE);
                st.setInt(1, u.getId());
                st.setInt(2,p.getId());
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al guardar el like: " + e.getMessage());
            }
        }
    }

    public void deleteLike(User u) {
        Connection con = Connect.getConnect();
        con = Connect.getConnect();
        if (con != null){
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(DELETELIKE);
                st.setInt(1,u.getId());
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al borrar el like: " + e.getMessage());
            }
        }
    }

    public Set<User> getAllLikes(Post p){
        Connection con = Connect.getConnect();
        con = Connect.getConnect();
        Set<User> likeds = new HashSet<>();
        if (con != null){
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(SELECTALLLIKES);
                st.setInt(1,p.getId());
                if (st.execute()){
                    ResultSet rs = st.getResultSet();
                    while (rs.next()){
                        UserDao liked = new UserDao();
                        liked.setId(rs.getInt(1));
                        UserDao add= (UserDao) liked.getById(liked.getId());
                        likeds.add(add);
                    }
                    rs.close();
                }
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al obtener los likes: " + e.getMessage());
            }
        }
        return likeds;
    }
}
