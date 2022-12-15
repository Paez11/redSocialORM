package redSocial.model.DAO;

import javafx.geometry.Pos;
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
        post= manager.find(Post.class,post.getId());
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
        post= manager.find(Post.class,post.getId());
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
            Query q = manager.createNativeQuery("SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post ORDER BY fecha_creacion DESC", Post.class);
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

    public static List<Post> getAllByUser(int id){
    	List<Post> posts = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            Query q = manager.createNativeQuery("SELECT id,id_user,fecha_creacion,fecha_modificacion,texto FROM post WHERE id_user="+id +" ORDER BY fecha_modificacion DESC",Post.class);
            posts = q.getResultList();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al obtener los posts: " + e.getMessage());
        }
        return posts;
    }
    
    public static boolean saveLike(User user, Post post) {
        boolean result = false;
    	manager = emf.createEntityManager();
        try {
            manager.getTransaction().begin();
            Post p = manager.find(Post.class, post.getId());
            User u = manager.find(User.class, user.getId());
            p.getLikes().add(u);
            result = true;
            manager.flush();
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al insertar Like " + e.getMessage());
        }
        return result;
    }
    
    public static boolean deleteLike(User user, Post post) {
        boolean result = false;
    	manager = emf.createEntityManager();
            try {
                Post p = manager.find(Post.class, post.getId());
                User u = manager.find(User.class, user.getId());
                manager.getTransaction().begin();
                p.getLikes().remove(u);
                manager.persist(post);
                result = true;
                manager.flush();
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al eliminar Like " + e.getMessage());
            }
        return result;
    }
    
    public static List<User> getAllLikes(Post p){
    	List<User> likes = new ArrayList<>();
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            p = manager.find(Post.class, p.getId());
            likes = p.getLikes();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al obtener los likes: " + e.getMessage());
        }
        return likes;	
    }

    public boolean likePost(Post p, User u) {
        boolean result = false;
        manager = emf.createEntityManager();
        p = manager.find(Post.class, p.getId());
        p.getLikes().size();
        for(User user : p.getLikes()) {
            if(user.getId()==u.getId()) {
                result = true;
            }
        }
        manager.close();
        return result;
    }
}
