package redSocial.model.DAO;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import redSocial.model.DataObject.User;
import redSocial.utils.Connection.Connect;
import redSocial.utils.Log;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao{

    private static UserDao instance;

    private static EntityManager manager;
    private static EntityManagerFactory emf= Persistence.createEntityManagerFactory("MySQL");

    public static UserDao getInstance() {
        if(instance==null) {
            instance=new UserDao();
        }
        return instance;
    }

    public static boolean save(User user) {
        boolean result = false;
        manager = emf.createEntityManager();
        try {
            user.setId(0);
            manager.getTransaction().begin();
            User mock = new User(user.getName(), user.getPassword(), user.getAvatar());
            manager.persist(mock);
            manager.flush();
            manager.getTransaction().commit();
            result = true;
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al insertar usuario: " + e.getMessage());
        }
        return result;
    }
    public static boolean delete(User user) {
        boolean result = false;
        manager = emf.createEntityManager();
        manager.find(User.class,user.getId());
            try {
                user = manager.merge(user);
                if(user != null) {
                    manager.getTransaction().begin();
                    manager.remove(user);
                    manager.getTransaction().commit();
                    result = true;
                    manager.close();
                }
            } catch (Exception e) {
                Log.severe("Error al eliminar usuario: " + e.getMessage());
            }
        return result;
    }

    public static boolean update(User user) {
        boolean result = false;
        manager = emf.createEntityManager();
        User aux = manager.find(User.class,user.getId());
        try {
            manager.getTransaction().begin();
            aux.setName(user.getName());
            aux.setPassword(user.getPassword());
            manager.getTransaction().commit();
            result = true;
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al actualizar usuario: " + e.getMessage());
        }
        return result;
    }

    public static boolean updateAvatar(User user) {
        boolean result = false;
        manager = emf.createEntityManager();
        User aux = manager.find(User.class,user.getId());
        try {
            manager.getTransaction().begin();
            File imageBlob = new File(new String(user.getAvatar()));
            FileInputStream in = new FileInputStream(imageBlob);
            aux.setAvatar(in.readAllBytes());
            manager.getTransaction().commit();
            result = true;
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al actualizar usuario: " + e.getMessage());
        }
        return result;
    }

    public static User getById(int id){
        User user = new User(id);
        if (id!=-1){
            manager = emf.createEntityManager();
            manager.getTransaction().begin();
            try {
                user = manager.find(User.class, id);
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al obtener usuario: " + e.getMessage());
            }
        }
        return user;
    }

    public static User getByName(String name){
        List<User> users = new ArrayList<User>();
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        Query q = manager.createNativeQuery("SELECT id,name,avatar,password FROM user WHERE name = ?", User.class);
        q.setParameter(1, name);
        users = q.getResultList();
        User user = users.get(0);
        manager.close();
        return user;
    }
    public static boolean follow(User user, User followed){
        boolean result = false;
        manager = emf.createEntityManager();

        try {
            manager.getTransaction().begin();
            User u = manager.find(User.class,user.getId());
            u.getFollowed().add(followed);
            //TODO: MODIFICAR EL OTRO SI ENCARTA
            result = true;
            manager.flush();
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al seguir usuario: " + e.getMessage());
        }
        return result;
    }

    public static boolean unfollow(User user, User followed){
        boolean result = false;
        manager = emf.createEntityManager();

        try {
            manager.getTransaction().begin();
            User u = manager.find(User.class,user.getId());
            u.getFollowed().remove(followed);
            result = true;
            manager.flush();
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al dejar de seguir usuario: " + e.getMessage());
        }
        return result;
    }

    public static User getFollowedByName(String name){
        User user = new User(name);
        if (name!=null){
            manager = emf.createEntityManager();
            manager.getTransaction().begin();
            try {
                user = manager.find(User.class, name);
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al obtener seguido: " + e.getMessage());
            }
        }
        return user;
    }

    private static List<User> getAllUsers(){
        List<User> users = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            users = manager.createNativeQuery("SELECT id,name,avatar FROM user").getResultList();
            manager.persist(users);
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al obtener usuarios: " + e.getMessage());
        }
        return users;
    }
}
