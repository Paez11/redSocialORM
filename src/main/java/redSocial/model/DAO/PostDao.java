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
import javax.persistence.Query;

public class PostDao extends Post{

    private static PostDao instance;

	private static EntityManager manager;
	private static EntityManagerFactory emf=Persistence.createEntityManagerFactory("MySQL");

    public static PostDao getInstance() {
        if(instance==null) {
            instance=new PostDao();
        }
        return instance;
    }

    public static boolean save(Post post) {
        boolean result = false;
        manager = emf.createEntityManager();
        try {
            if(!manager.contains(post)) {
                manager.getTransaction().begin();
                manager.persist(post);
                manager.flush();
                manager.getTransaction().commit();
                result = true;
                manager.close();
            }
        } catch (Exception e) {
            Log.severe("Error al insertar el post " + e.getMessage());
        }
        return result;
    }

    public static boolean delete(Post post) {
        boolean result = false;
        manager = emf.createEntityManager();
            try {
                if(manager.contains(post)) {
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
            if(manager.contains(post)) {
                manager.getTransaction().begin(); 
                post.setText(post.getText());
                post.setDateUpdate(post.getDateUpdate());
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

   public static List<Post> getAll(){
    	List<Post> posts = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            Query q = manager.createNativeQuery("SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post ORDER BY fecha_creacion DESC");
            posts = q.getResultList();
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
           posts = manager.createNativeQuery("SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post WHERE id_user="+ u.getId()).getResultList();
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
            posts = manager.createNativeQuery("SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post WHERE id_user="+id +" ORDER BY fecha_modificacion DESC").getResultList();
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
                manager.createNativeQuery("INSERT INTO likes (id_user, id_post, id) VALUES (?,?,NULL)")
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
                manager.createNativeQuery("DELETE FROM likes WHERE id_user=?")
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
        	likes = (Set<User>) manager.createNativeQuery("SELECT id_user,id_post,id FROM likes WHERE id_post=?").
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
