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
        //this.getById(id);
    }

    public void save() {
    	manager = emf.createEntityManager();
        manager.getTransaction().begin();
            try {
                manager.createQuery(INSERT)
                        .setParameter(1, this.getUserName().getId())
                        .setParameter(2, Date.valueOf(LocalDate.now()))
                        .setParameter(3, this.getText());
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al insertar usuario: " + e.getMessage());
            }
	}

    public void delete() {
    	manager = emf.createEntityManager();
        manager.getTransaction().begin();
            try {
                manager.createQuery(DELETE)
                        .setParameter(1, this.getId());
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
            	
            }
                
    }

    public void update() {
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
            try {
                manager.createQuery(UPDATE)
                        .setParameter(1,Date.valueOf(LocalDate.now()))
                        .setParameter(2, this.getText())
                        .setParameter(3, this.getId());                     
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al actualizar usuario: " + e.getMessage());
            }
    }

    public static List<PostDao> getAll(){
    	List<PostDao> posts = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            posts = manager.createQuery(SELECTALL).getResultList();
            manager.persist(posts);
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al obtener los posts: " + e.getMessage());
        }
        return posts;
    }
    
    
    public Post getById(int id) {
    	PostDao post= new PostDao(id);
        if (id!=-1){
            manager = emf.createEntityManager();
            manager.getTransaction().begin();
            try {
                post = manager.createQuery(SELECTCOMMENTS,PostDao.class)
                        .setParameter(1, id)
                        .getSingleResult();    
                manager.persist(post);
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al obtener el post: " + e.getMessage());
            }
        }
        return post;
    }
    
    
	public List<Comment> getComments() {
		List<Comment> comments = null;
		manager = emf.createEntityManager();
		manager.getTransaction().begin();
		try {
			comments = manager.createQuery(SELECTCOMMENTS).getResultList();
			manager.persist(comments);
            manager.getTransaction().commit();
            manager.close();
		} catch (Exception e) {
			Log.severe("Error al obtener los comments: " + e.getMessage());
		}

		return comments;
	}
    
    
    public static List<PostDao> getAllByUser(int id){
    	List<PostDao> posts = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            posts = manager.createQuery(SELECTBYUSER).getResultList();
            manager.persist(posts);
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al obtener los posts: " + e.getMessage());
        }
        return posts;
    }
    
    public void saveLike(User u, Post p) {
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
    
    public void deleteLike(User u) {
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
    
    public Set<User> getAllLikes(Post p){
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
