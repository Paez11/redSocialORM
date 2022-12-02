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
import java.util.Date;
import java.util.List;

public class CommentDao extends Comment implements Dao {

    private static Connection con = null;

    private final static String INSERT = "INSERT INTO comments (id_user,texto,id_post,fecha) VALUES (?,?,?,?)";
    private final static String DELETE = "DELETE FROM comments WHERE id=?";
    private final static String UPDATE = "UPDATE comments SET texto=?, fecha=? WHERE id=? FROM comments";
    private final static String SELECTBYID = "SELECT id,id_user,texto,id_post,fecha FROM comments WHERE id=?";
    private final static String SELECTBYUSERPOST = "SELECT id,id_user,texto,id_post,fecha FROM comments where id_user=? and id_post=? ORDER BY fecha DESC";
    private final static String SELECTBYPOST = "SELECT id,id_user,texto,id_post,fecha FROM comments where id_post=? ORDER BY fecha DESC";


    public CommentDao(int id, User userComment, String textComment, Post post, Date date) {
        super(id,userComment,textComment,post,date);
    }

    public CommentDao() {
        super();
    }

    @Override
    public void save() {
        //INSERT
        con = Connect.getConnect();
        if(con != null) {
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, this.getUserComment().getId());
                ps.setString(2, this.getTextComment());
                ps.setInt(3, this.getPost().getId());
                ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
                ps.executeUpdate();  //devuelve 1 si ha salido bien
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    this.setId(rs.getInt(1));
                }
                ps.close();
                rs.close();
            } catch (SQLException e) {
                Log.severe("Error al insertar comentario: " + e.getMessage());
            }
        }
    }

    @Override
    public void delete() {
        con = Connect.getConnect();
        if (con != null){
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(DELETE);
                st.setInt(1,this.id);
                if (st.executeUpdate()==1){
                    this.id=-1;
                }
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al borrar comentario: " + e.getMessage());
            }
        }
}

    @Override
    public void update() {
        con = Connect.getConnect();
        if (con != null){
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(UPDATE);
                st.setInt(3,this.id);
                st.setString(1,this.textComment);
                st.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al actualizar comentario: " + e.getMessage());
            }
        }
    }

    public Comment getById(int id) {
        CommentDao comment = new CommentDao(id, UserComment, textComment, post, date);
        UserDao aux = new UserDao();
        PostDao aux2 = new PostDao();
        con = Connect.getConnect();
        if(con != null) {
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(SELECTBYID);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    aux = (UserDao) aux.getById(rs.getInt("id_user"));
                    id = rs.getInt(1);
                    comment.UserComment = aux;
                    comment.textComment = rs.getString("texto");
                    aux2 = (PostDao) aux2.getById(rs.getInt("id_post"));
                    comment.post = aux2;
                    comment.date = rs.getDate("fecha");
                }
                ps.close();
                rs.close();
            } catch (SQLException e) {
                Log.severe("Error al obtener comentario por id: " + e.getMessage());
            }
        }
        return comment;
    }

    public List<Comment> getAllByUser(User userByS, Post postByS){
        List<Comment> result = new ArrayList<Comment>();
        UserDao user = null;
        con = Connect.getConnect();
        if(con != null) {
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(SELECTBYUSERPOST);
                ps.setInt(1,userByS.getId());
                ps.setInt(2,postByS.getId());

                if(ps.execute()) {
                    ResultSet rs = ps.getResultSet();
                    while(rs.next()) {
                        int id=rs.getInt("id");
                        user = (UserDao) user.getById(rs.getInt("id_user"));
                        String texto = rs.getString("texto");
                        Date date = rs.getDate("fecha");
                        result.add(new Comment(id,user, texto, postByS,date));

                    }
                    rs.close();
                }
                ps.close();
            } catch (SQLException e) {
                Log.severe("Error al obtener comentarios por usuario y post: " + e.getMessage());
            }
        }
        return result;
    }

    public static List<CommentDao> getAllByPost(Post postByS){
        List<CommentDao> result = new ArrayList<CommentDao>();
        UserDao user = null;
        con = Connect.getConnect();
        if(con != null) {
            PreparedStatement ps;
            try {
                ps = con.prepareStatement(SELECTBYPOST);
                ps.setInt(1,postByS.getId());

                if(ps.execute()) {
                    ResultSet rs = ps.getResultSet();
                    while(rs.next()) {
                        int id=rs.getInt("id");
                        user = new UserDao( rs.getInt("id_user"));
                        String texto = rs.getString("texto");
                        Post post= new Post(rs.getInt("id_post"));
                        Date date = rs.getDate("fecha");
                        result.add(new CommentDao(id,user, texto, post,date));

                    }
                    rs.close();
                }
                ps.close();
            } catch (SQLException e) {
                Log.severe("Error al obtener comentarios por post: " + e.getMessage());
            }
        }
        return result;
    }
}
