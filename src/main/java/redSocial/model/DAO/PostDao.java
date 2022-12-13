package redSocial.model.DAO;

import redSocial.model.DataObject.Comment;
import redSocial.model.DataObject.Post;
import redSocial.model.DataObject.User;
import redSocial.utils.Connection.Connect;
import redSocial.utils.Log;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PostDao extends Post{
	private static Connection con = null;
	private static EntityManager manager;
	private static EntityManagerFactory emf=Persistence.createEntityManagerFactory("MySQL");


	private final static String INSERTLIKE = "INSERT INTO likes (id_user, id_post, id) VALUES (?,?,NULL)";
    private final static String DELETELIKE = "DELETE FROM likes WHERE id_user=?";
    private final static String SELECTALLLIKES = "SELECT id_user,id_post,id FROM likes WHERE id_post=?";

  
    
    public static boolean save(Post post) {
        boolean result = false;
        manager = emf.createEntityManager();
        try {
            post.setId(0);
            if(!manager.contains(post)) {
                manager.getTransaction().begin();
                manager.persist(post);
                result = true;
                manager.flush();
                manager.getTransaction().commit();
                manager.close();
            }
        } catch (Exception e) {
            Log.severe("Error al insertar el post " + e.getMessage());
        }
        return result;
    }

    public static boolean delete(Post post) {
        boolean result = false;
        manager = Connect.getConnect().createEntityManager();
            try {
                if(!manager.contains(post)) {
                    manager.getTransaction().begin();
                    manager.remove(post);
                    result = true;
                    manager.flush();
                    manager.getTransaction().commit();
                    manager.close();
                }
            } catch (Exception e) {
                Log.severe("Error al eliminar el post: " + e.getMessage());
            }
        return result;
    }
    
    public static boolean update(Post post) {
        boolean result = false;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            if(!manager.contains(post)) {
                manager.getTransaction().begin(); 
                post.setText(post.getText());
                post.setDateUpdate(Date.valueOf(LocalDate.now()));
                result = true;
                manager.flush();
                manager.getTransaction().commit();
                manager.close();
            }
        } catch (Exception e) {
            Log.severe("Error al actualizar el post: " + e.getMessage());
        }
        return result;
    }

   public static List<PostDao> getAll(){
    	List<PostDao> posts = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            posts = manager.createQuery("SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post ORDER BY fecha_creacion DESC").getResultList();
            manager.persist(posts);
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al obtener los posts: " + e.getMessage());
        }
        return posts;
    }
    
   public static Post getById(int id){
       Post post= new Post(id);
       if (id!=-1){
           manager = emf.createEntityManager();
           manager.getTransaction().begin();
           try {
               post = manager.find(Post.class, id);
               manager.close();
           } catch (Exception e) {
               Log.severe("Error al obtener usuario: " + e.getMessage());
           }
       }
       return post;
   }
    
   public static List<Post> getComments(User u){
   	List<Post> posts = null;
       manager = emf.createEntityManager();
       manager.getTransaction().begin();
       try {
           posts = manager.createQuery("SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post WHERE id_user="+ u.getId()).getResultList();
           manager.persist(posts);
           manager.getTransaction().commit();
           manager.close();
       } catch (Exception e) {
           Log.severe("Error al obtener los posts: " + e.getMessage());
       }
       return posts;
   }    
    
    public static List<Post> getAllByUser(int id){
    	List<Post> posts = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            posts = manager.createQuery("SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post WHERE id_user="+id +" ORDER BY fecha_modificacion DESC").getResultList();
            manager.persist(posts);
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al obtener los posts: " + e.getMessage());
        }
        return posts;
    }
    
    public static void saveLike(User u, Post p) {
    	manager = emf.createEntityManager();
        manager.getTransaction().begin();
            try {
                manager.createQuery(INSERTLIKE)
                        .setParameter(1, u.getId())
                        .setParameter(2, p.getId());
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al insertar Like " + e.getMessage());
            }
    }
    
    public static void deleteLike(User u) {
    	manager = emf.createEntityManager();
        manager.getTransaction().begin();
            try {
                manager.createQuery(DELETELIKE)
                        .setParameter(1, u.getId());
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
            	
            }
    }
    
    public static Set<User> getAllLikes(Post p){
    	Set<User> likes = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
        	likes = (Set<User>) manager.createQuery(SELECTALLLIKES).
            		setParameter(1, p.getId())
            		.getResultList();
            manager.persist(likes);
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al obtener los likes: " + e.getMessage());
        }
        return likes;	
    }
}
