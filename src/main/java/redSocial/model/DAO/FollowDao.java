package redSocial.model.DAO;

import redSocial.model.DataObject.Follow;
import redSocial.model.DataObject.User;
import redSocial.utils.Connection.Connect;
import redSocial.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowDao extends Follow {
    private static Connection con = null;
    private final static String INSERT = "INSERT INTO follower (id_follow,id_follower,id) VALUES (?,?,NULL)";
    private final static String DELETE = "DELETE FROM follower WHERE id=?";
    private final static String DELETEBYUSERS = "DELETE FROM follower WHERE id_follower=? AND id_follow=?";
    private final static String GETBYUSER = "SELECT id_follow FROM follower WHERE id_follower=?";

    public FollowDao(User user, User followed) {
        super(user, followed);
    }

    public FollowDao() {
        super();
    }

    public void save() {
        con = Connect.getConnect();
        if (con != null){
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(INSERT);
                st.setInt(1,this.getFollowed().getId());
                st.setInt(2,this.getUser().getId());
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al seguir al usuario: " + e.getMessage());
            }
        }
    }

    public void delete() {
        con = Connect.getConnect();
        if (con != null){
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(DELETE);
                st.setInt(1,this.getId());
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al dejar de seguir al usuario: " + e.getMessage());
            }
        }
    }

    public void deletebyusers(User user, User followed) {
        con = Connect.getConnect();
        if (con != null){
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(DELETEBYUSERS);
                st.setInt(1,user.getId());
                st.setInt(2,followed.getId());
                st.executeUpdate();
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al dejar de seguir al usuario: " + e.getMessage());
            }
        }
    }

    public List<User> getByName(User user){
        con = Connect.getConnect();
        List<User> followeds = new ArrayList<>();
        if (con != null){
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(GETBYUSER);
                st.setInt(1,user.getId());
                if (st.execute()){
                    ResultSet rs = st.getResultSet();
                    while (rs.next()){
                        UserDao followed = new UserDao();
                        followed.setId(rs.getInt(1));
                        UserDao add= (UserDao) followed.getById(followed.getId());
                        followeds.add(add);
                    }
                    rs.close();
                }
                st.close();
            } catch (SQLException e) {
                Log.severe("Error al obtener usuario: " + e.getMessage());
            }
        }
        return followeds;
    }
}
