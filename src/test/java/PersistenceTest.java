import org.junit.Test;
import redSocial.model.DAO.PostDao;
import redSocial.model.DAO.UserDao;
import redSocial.model.DataObject.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PersistenceTest {


    @Test
    public void CreateUserTest() {
        byte[] avatar = new byte[0];
        User user = new UserDao(1,"test", "test", avatar);
        user.save();
        UserDao user2 = new UserDao();
        user2 = (UserDao) user2.getById(user.getId());
        System.out.println(user);
        System.out.println(user2);
        assertEquals(user, user2);

    }

    @Test
    public void CreatePostTest(){
        PostDao post = new PostDao();
        post.save();
        PostDao post2 = new PostDao();
        post2.getById(post.getId());
        System.out.println(post);
        System.out.println(post2);
        assertEquals(post,post2);
    }
}