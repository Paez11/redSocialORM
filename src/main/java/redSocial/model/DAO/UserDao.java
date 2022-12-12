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

public class UserDao extends User{
    private static Connection con = null;

    private static EntityManager manager;
    private static EntityManagerFactory emf=Persistence.createEntityManagerFactory("MySQL");

    private final static String INSERT = "INSERT INTO user(id,name,password,avatar) VALUES (NULL,?,?,?)";
    private final static String DELETE = "DELETE FROM user WHERE id=?";
    private final static String UPDATE = "UPDATE user SET name=?, password=?, avatar=? WHERE id=?";
    private final static String SELECTBYID = "SELECT id,name,password,avatar FROM user WHERE id=?";
    private final static String SELECTBYNAME = "SELECT id,name,password,avatar FROM user WHERE name=?";
    private final static String SELECTALL = "SELECT id,name,avatar FROM user";

    public UserDao(int id, String name){
        super(id,name);
    }
    public UserDao(int id, String name,byte[] avatar){
        super(id,name,avatar);
    }
    public UserDao(int id, String name,String password,byte[] avatar){
        super(id,name,password,avatar);
    }
    public UserDao(String name,String password,byte[] avatar){
        super(name,password,avatar);
    }
    public UserDao(){
        super();
    }
    public UserDao(User user){
        super(user.getId(),user.getName());
    }
    public UserDao(int id){
        this.getById(id);
    }

    public void save() {
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
            try {
                manager.createQuery("INSERT INTO user(id,name,password,avatar) VALUES (NULL,?,?,?)")
                        .setParameter(1, this.getName())
                        .setParameter(2, this.getPassword())
                        .setParameter(3, this.getAvatar());
                manager.persist(this);
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
                manager.persist(this);
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al eliminar usuario: " + e.getMessage());
            }
    }

    public void update() {
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
            try {
                manager.createQuery(UPDATE)
                        .setParameter(1, this.getName())
                        .setParameter(2, this.getPassword())
                        .setParameter(3, this.getAvatar())
                        .setParameter(4, this.getId());
                manager.persist(this);
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al actualizar usuario: " + e.getMessage());
            }
    }

    public User getById(int id){
        UserDao user = new UserDao(id,name,password,avatar);
        if (id!=-1){
            manager = emf.createEntityManager();
            manager.getTransaction().begin();
            try {
                user = manager.createQuery(SELECTBYID,UserDao.class)
                        .setParameter(1, id)
                        .getSingleResult();
                manager.persist(user);
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al obtener usuario: " + e.getMessage());
            }
        }
        return user;
    }

    public User getByName(String name){
        UserDao user = new UserDao(id,name,password,avatar);
        if (name!=null){
            manager = emf.createEntityManager();
            manager.getTransaction().begin();
            try {
                user = manager.createQuery(SELECTBYNAME,UserDao.class)
                        .setParameter(1, name)
                        .getSingleResult();
                manager.persist(user);
                manager.getTransaction().commit();
                manager.close();
            } catch (Exception e) {
                Log.severe("Error al obtener usuario: " + e.getMessage());
            }
        }
        return user;
    }

    private List<User> getAllUsers(){
        List<User> users = null;
        manager = emf.createEntityManager();
        manager.getTransaction().begin();
        try {
            users = manager.createQuery(SELECTALL).getResultList();
            manager.persist(users);
            manager.getTransaction().commit();
            manager.close();
        } catch (Exception e) {
            Log.severe("Error al obtener usuarios: " + e.getMessage());
        }
        return users;
    }
}
