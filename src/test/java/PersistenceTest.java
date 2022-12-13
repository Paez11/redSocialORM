import org.junit.Test;
import redSocial.model.DAO.PostDao;
import redSocial.model.DAO.UserDao;
import redSocial.model.DataObject.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PersistenceTest {


    @Test
    public void CreateUserTest() {
        UserDao ud = new UserDao();
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        System.out.println(ud.save(user));
        User user2 = ud.getById(user.getId());
        System.out.println(user);
        System.out.println(user2);
        assertEquals(user, user2);

    }

    @Test
    public void CreatePostTest(){

    }
}