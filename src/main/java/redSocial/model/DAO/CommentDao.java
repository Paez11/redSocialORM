package redSocial.model.DAO;

import redSocial.model.DataObject.Comment;
import redSocial.model.DataObject.Post;
import redSocial.model.DataObject.User;
import redSocial.utils.Connection.Connect;
import redSocial.utils.Log;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDao extends Comment{

    private static Connection con = null;

    private static EntityManager manager;
    private static EntityManagerFactory emf= Persistence.createEntityManagerFactory("MySQL");

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

    public static void save(Comment c) {
        //INSERT
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try{
            manager.createQuery(INSERT)
                    .setParameter(1, c.getUserComment().getId())
                    .setParameter(2, c.getTextComment())
                    .setParameter(3, c.getPost().getId())
                    .setParameter(4, java.sql.Date.valueOf(LocalDate.now()));
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            Log.severe("Error al guardar el comentario: " + e.getMessage());
        }
        /**
         * con = Connect.getConnect();
         *         if(con != null) {
         *             PreparedStatement ps;
         *             try {
         *                 ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
         *                 ps.setInt(1, this.getUserComment().getId());
         *                 ps.setString(2, this.getTextComment());
         *                 ps.setInt(3, this.getPost().getId());
         *                 ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
         *                 ps.executeUpdate();  //devuelve 1 si ha salido bien
         *                 ResultSet rs = ps.getGeneratedKeys();
         *                 if(rs.next()) {
         *                     this.setId(rs.getInt(1));
         *                 }
         *                 ps.close();
         *                 rs.close();
         *             } catch (SQLException e) {
         *                 Log.severe("Error al insertar comentario: " + e.getMessage());
         *             }
         *         }
         */
    }

    public static void delete(int id) {

        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try{
            manager.createQuery(DELETE)
                    .setParameter(1,id);
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            Log.severe("Error al borrar el comentario: " + e.getMessage());
        }
        /**
         * con = Connect.getConnect();
         *         if (con != null){
         *             PreparedStatement st = null;
         *             try {
         *                 st = con.prepareStatement(DELETE);
         *                 st.setInt(1,this.id);
         *                 if (st.executeUpdate()==1){
         *                     this.id=-1;
         *                 }
         *                 st.close();
         *             } catch (SQLException e) {
         *                 Log.severe("Error al borrar comentario: " + e.getMessage());
         *             }
         *         }
         */
}

    public static void update(Comment comment) {
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try{
            manager.createQuery(UPDATE)
                    .setParameter(3,comment.getId())
                    .setParameter(1,comment.getTextComment())
                    .setParameter(2, java.sql.Date.valueOf(LocalDate.now()));
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            Log.severe("Error al actualizar el comentario: " + e.getMessage());
        }
        /**
         * con = Connect.getConnect();
         *         if (con != null){
         *             PreparedStatement st = null;
         *             try {
         *                 st = con.prepareStatement(UPDATE);
         *                 st.setInt(3,this.id);
         *                 st.setString(1,this.textComment);
         *                 st.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
         *                 st.executeUpdate();
         *                 st.close();
         *             } catch (SQLException e) {
         *                 Log.severe("Error al actualizar comentario: " + e.getMessage());
         *             }
         *         }
         */

    }

    public static Comment getById(int id) {

        Comment comment = new Comment(id);

        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try{

            comment = manager.createQuery("SELECT id,id_user,texto,id_post,fecha FROM comments WHERE id=?", CommentDao.class)
                    .setParameter(1, id)
                    .getSingleResult();
            manager.persist(comment);
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e){
            Log.severe("Error al traer el comentario por id: " + e.getMessage());
        }
        /**
         *  con = Connect.getConnect();
         *         if(con != null) {
         *             PreparedStatement ps;
         *             try {
         *                 ps = con.prepareStatement(SELECTBYID);
         *                 ps.setInt(1, id);
         *                 ResultSet rs = ps.executeQuery();
         *                 if(rs.next()) {
         *                     aux = (UserDao) aux.getById(rs.getInt("id_user"));
         *                     id = rs.getInt(1);
         *                     comment.UserComment = aux;
         *                     comment.textComment = rs.getString("texto");
         *                     aux2 = (PostDao) aux2.getById(rs.getInt("id_post"));
         *                     comment.post = aux2;
         *                     comment.date = rs.getDate("fecha");
         *                 }
         *                 ps.close();
         *                 rs.close();
         *             } catch (SQLException e) {
         *                 Log.severe("Error al obtener comentario por id: " + e.getMessage());
         *             }
         *         }
         */
        return comment;
    }

    public static List<Comment> getAllByUser(User userByS, Post postByS){

        List<Comment> result = new ArrayList<Comment>();

        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            result = manager.createQuery(SELECTBYUSERPOST)
                    .setParameter(1, userByS.getId())
                    .setParameter(2, postByS.getId())
                    .getResultList();
            manager.persist(result);
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e) {
                Log.severe("Error al obtener los comentarios por usuario: " + e.getMessage());
        }
        /**
         * con = Connect.getConnect();
         *         if(con != null) {
         *             PreparedStatement ps;
         *             try {
         *                 ps = con.prepareStatement(SELECTBYUSERPOST);
         *                 ps.setInt(1,userByS.getId());
         *                 ps.setInt(2,postByS.getId());
         *
         *                 if(ps.execute()) {
         *                     ResultSet rs = ps.getResultSet();
         *                     while(rs.next()) {
         *                         int id=rs.getInt("id");
         *                         user = (UserDao) user.getById(rs.getInt("id_user"));
         *                         String texto = rs.getString("texto");
         *                         Date date = rs.getDate("fecha");
         *                         result.add(new Comment(id,user, texto, postByS,date));
         *
         *                     }
         *                     rs.close();
         *                 }
         *                 ps.close();
         *             } catch (SQLException e) {
         *                 Log.severe("Error al obtener comentarios por usuario y post: " + e.getMessage());
         *             }
         *         }
         */
        return result;
    }

    public static List<Comment> getAllByPost(Post postByS){


        List<Comment> result = new ArrayList<Comment>();

        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            result = manager.createQuery(SELECTBYPOST)
                    .setParameter(1, postByS.getId())
                    .getResultList();
            manager.persist(result);
            manager.getTransaction().commit();
            manager.close();
        }catch (Exception e) {
            Log.severe("Error al obtener los comentarios por usuario: " + e.getMessage());
        }

        /**
         * con = Connect.getConnect();
         *         if(con != null) {
         *             PreparedStatement ps;
         *             try {
         *                 ps = con.prepareStatement(SELECTBYPOST);
         *                 ps.setInt(1,postByS.getId());
         *
         *                 if(ps.execute()) {
         *                     ResultSet rs = ps.getResultSet();
         *                     while(rs.next()) {
         *                         int id=rs.getInt("id");
         *                         user = new UserDao( rs.getInt("id_user"));
         *                         String texto = rs.getString("texto");
         *                         Post post= new Post(rs.getInt("id_post"));
         *                         Date date = rs.getDate("fecha");
         *                         result.add(new CommentDao(id,user, texto, post,date));
         *
         *                     }
         *                     rs.close();
         *                 }
         *                 ps.close();
         *             } catch (SQLException e) {
         *                 Log.severe("Error al obtener comentarios por post: " + e.getMessage());
         *             }
         *         }
         */
        return result;
    }
}
