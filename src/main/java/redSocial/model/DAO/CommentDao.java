package redSocial.model.DAO;

import redSocial.model.DataObject.Comment;
import redSocial.model.DataObject.Post;
import redSocial.model.DataObject.User;
import redSocial.utils.Connection.Connect;
import redSocial.utils.Log;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDao {

    private static EntityManager manager;
    private static EntityManagerFactory emf= Persistence.createEntityManagerFactory("MySQL");

    private final static String SELECTBYUSERPOST = "SELECT id,id_user,texto,id_post,fecha FROM comments where id_user=? and id_post=? ORDER BY fecha DESC";
    private final static String SELECTBYPOST = "SELECT id,id_user,texto,id_post,fecha FROM comments where id_post=? ORDER BY fecha DESC";


    public static boolean save(Comment c) {
        boolean result = false;
        manager = emf.createEntityManager();
        try{
                manager.getTransaction().begin();
                Post aux = manager.find(Post.class,c.getPost().getId());
                User u = manager.find(User.class,c.getUser().getId());
                Comment cm = new Comment(u,c.getTextComment(),aux,c.getDate());
                manager.persist(cm);
                manager.flush();
                manager.getTransaction().commit();
                manager.close();

        }catch (Exception e){
            Log.severe("Error al guardar el comentario: " + e.getMessage());
        }
        return result;
    }

    public static void delete(Comment c) {

        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            if(manager.contains(c)) {
                manager.getTransaction().begin();
                manager.remove(c);
                manager.flush();
                manager.getTransaction().commit();
                manager.close();
            }
        } catch (Exception e) {
            Log.severe("Error al eliminar comentario: " + e.getMessage());
        }
    }

    public static void update(Comment comment) {
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try{
            if(manager.contains(comment)) {
                manager.getTransaction().begin();
                comment.setTextComment(comment.getTextComment());
                manager.merge(comment);
                manager.flush();
                manager.getTransaction().commit();
                manager.close();
            }
        }catch (Exception e){
            Log.severe("Error al actualizar el comentario: " + e.getMessage());
        }
    }

    public static Comment getById(int id) {
        Comment comment = new Comment(id);
        if (id!=-1){
            manager = emf.createEntityManager();
            manager.getTransaction().begin();
            try {
                comment = manager.find(Comment.class, id);
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al obtener comentario: " + e.getMessage());
            }
        }
        return comment;
    }

    public static List<Comment> getAllByUser(User userByS, Post postByS){
        List<Comment> result = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            Query q = manager.createNativeQuery(SELECTBYUSERPOST,Comment.class)
                    .setParameter(1, userByS.getId())
                    .setParameter(2, postByS.getId());
            result = q.getResultList();
            manager.close();
        }catch (Exception e) {
                Log.severe("Error al obtener los comentarios por usuario: " + e.getMessage());
        }
        return result;
    }

    public static List<Comment> getAllByPost(Post postByS){
        List<Comment> result = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            Query q = manager.createNativeQuery(SELECTBYPOST,Comment.class).setParameter(1, postByS.getId());
            result = q.getResultList();
            manager.close();
        }catch (Exception e) {
            Log.severe("Error al obtener los comentarios por usuario: " + e.getMessage());
        }
        return result;
    }
}
