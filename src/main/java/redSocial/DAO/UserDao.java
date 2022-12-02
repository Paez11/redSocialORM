package redSocial.DAO;

import redSocial.interfaces.Dao;
import redSocial.model.User;
import redSocial.utils.Connection.Connect;
import redSocial.utils.Log;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.*;
import java.util.List;

public class UserDao extends User implements Dao {
    private static Connection con = null;

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

    @Override
    public void save() {
        con = Connect.getConnect();
        if (con != null){
            try {
                PreparedStatement st = con.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS);
                st.setString(1,this.name);
                st.setString(2,this.password);
                st.setBinaryStream(3, new ByteArrayInputStream(this.avatar), this.avatar.length);
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al insertar usuario: " + e.getMessage());
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
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al eliminar usuario: " + e.getMessage());
            }
        }
    }

    @Override
    public void update() {
        con = Connect.getConnect();
        if (con != null){
            try {
                PreparedStatement st = con.prepareStatement(UPDATE);
                st.setInt(4,this.id);
                st.setString(1,name);
                st.setString(2,password);
                try {
                    File imageBlob = new File(new String(this.avatar));
                    FileInputStream in = new FileInputStream(imageBlob);
                    st.setBinaryStream(3,in,(int)imageBlob.length());
                } catch (FileNotFoundException e) {
                    st.setBinaryStream(3, new ByteArrayInputStream(this.avatar), this.avatar.length);
                }
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al actualizar usuario: " + e.getMessage());
            }
        }
    }

    public User getById(int id){
        UserDao user = new UserDao(id,name,password,avatar);
        if (id!=-1){
            con = Connect.getConnect();
            if (con != null){
                try {
                    PreparedStatement st = st = con.prepareStatement(SELECTBYID);
                    st.setInt(1,id);
                    if (st.execute()){
                        ResultSet rs = st.getResultSet();
                        if (rs.next()){
                            user.id=rs.getInt(1);
                            user.name = rs.getString("name");
                            user.password = rs.getString("password");
                            Blob imageBlob = rs.getBlob("avatar");
                            byte[] bdata = imageBlob.getBytes(1, (int) imageBlob.length());
                            user.avatar = bdata;

                        }
                        rs.close();
                    }
                    st.close();
                } catch (SQLException e) {
                    Log.severe("Error al obtener usuario: " + e.getMessage());
                }
            }
        }
        return user;
    }

    public User getByName(String name){
        UserDao user = new UserDao(id,name,password,avatar);
        con = Connect.getConnect();
        if (con != null){

            try {
                PreparedStatement st = con.prepareStatement(SELECTBYNAME);
                st.setString(1,name);
                if (st.execute()){
                    ResultSet rs = st.getResultSet();
                    if (rs.next()){
                        this.name=rs.getString("name");
                        this.id = rs.getInt("id");
                        this.password = rs.getString("password");
                        Blob imageBlob = rs.getBlob("avatar");
                        byte[] bdata = imageBlob.getBytes(1, (int) imageBlob.length());
                        this.avatar = bdata;

                    }
                    rs.close();
                }
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al obtener usuario: " + e.getMessage());
            }
        }
        user.setName(this.name);
        user.setId(this.id);
        user.setPassword(this.password);
        user.setAvatar(this.avatar);
        return user;
    }

    private List<User> getAllUsers(){
        List<User> users = null;
        con = Connect.getConnect();
        if (con != null){
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(SELECTALL);
                if (st.execute()){
                    ResultSet rs = st.getResultSet();
                    while (rs.next()){
                        Blob imageBlob = rs.getBlob("avatar");
                        byte[] bdata = imageBlob.getBytes(1, (int) imageBlob.length());
                        avatar = bdata;
                        User u = new User(rs.getInt("id"),rs.getString("name"),
                                rs.getString("avatar").getBytes());
                        users.add(u);
                    }
                    rs.close();
                }
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al obtener usuarios: " + e.getMessage());
            }
        }
        return users;
    }
}
