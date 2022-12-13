package redSocial.model.DAO;

import redSocial.model.DataObject.User;
import redSocial.utils.Connection.Connect;
import redSocial.utils.Log;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.List;

public class UserDao{
    private static Connection con = null;

    private static EntityManager manager;
    private static EntityManagerFactory emf= Persistence.createEntityManagerFactory("MySQL");

    public static boolean save(User user) {
        boolean result = false;
        manager = emf.createEntityManager();
        try {
            //user.setId(0);
            if(!manager.contains(user)) {
                manager.getTransaction().begin();
                manager.persist(user);
                result = true;
                manager.flush();
                manager.getTransaction().commit();
                manager.close();
            }
        } catch (Exception e) {
            Log.severe("Error al insertar usuario: " + e.getMessage());
        }
        return result;
    }
    public static boolean delete(User user) {
        boolean result = false;
        manager = Connect.getConnect().createEntityManager();
            try {
                if(!manager.contains(user)) {
                    manager.getTransaction().begin();
                    manager.remove(user);
                    result = true;
                    manager.flush();
                    manager.getTransaction().commit();
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
        manager.getTransaction().begin();
        try {
            if(!manager.contains(user)) {
                manager.getTransaction().begin();
                user.setName(user.getName());
                user.setPassword(user.getPassword());
                user.setAvatar(user.getAvatar());
                manager.merge(user);
                result = true;
                manager.flush();
                manager.getTransaction().commit();
                manager.close();
            }
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
        User user = new User(name);
        if (name!=null){
            manager = emf.createEntityManager();
            manager.getTransaction().begin();
            try {
                user = manager.find(User.class, name);
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al obtener usuario: " + e.getMessage());
            }
        }
        return user;
    }

    public static boolean follow(User user, User followed){
        boolean result = false;
        manager = emf.createEntityManager();

        try {
            manager.getTransaction().begin();

            result = true;
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

            result = true;
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
        manager = Connect.getConnect().createEntityManager();
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
