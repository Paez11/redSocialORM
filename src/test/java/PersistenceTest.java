import org.junit.Test;
import redSocial.model.DAO.PostDao;
import redSocial.model.DAO.UserDao;
import redSocial.model.DataObject.Post;
import redSocial.model.DataObject.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PersistenceTest {


    @Test
    public void createUserTest() {
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        System.out.println(UserDao.save(user));
        //User user2 = UserDao.getById(user.getId());
        User user2 = UserDao.getByName(user.getName());
        System.out.println(user2);
        assertEquals(user, user2);
    }

    @Test
    public void DeleteUserTest(){
        UserDao ud = new UserDao();
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        System.out.println(UserDao.save(user));
        System.out.println(user);
        assertEquals(true, UserDao.delete(user));
    }

    @Test
    public void CreatePostTest(){
        PostDao pd = new PostDao();
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        UserDao.save(user);
        Post post = new Post(user);
        System.out.println(post);
        System.out.println(PostDao.save(post));
        Post post2 = PostDao.getById(post.getId());
        System.out.println(post2);
        assertEquals(post, post2);
    }

    @Test
    public void getPostTest(){

    }

}