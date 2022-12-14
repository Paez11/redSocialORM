import org.junit.Test;
import redSocial.model.DAO.CommentDao;
import redSocial.model.DAO.PostDao;
import redSocial.model.DAO.UserDao;
import redSocial.model.DataObject.Comment;
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
    public void deleteUserTest(){
        UserDao ud = new UserDao();
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        System.out.println(UserDao.save(user));
        System.out.println(user);
        assertEquals(true, UserDao.delete(user));
    }

    @Test
    public void createPostTest(){
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
    public void getAllPostTest(){
        PostDao pd = new PostDao();
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        UserDao.save(user);
        Post post = new Post(user, "test1");
        Post post2 = new Post(user,"test2");
        System.out.println(post);
        System.out.println(post2);
        System.out.println(PostDao.save(post));
        System.out.println(PostDao.save(post2));
        PostDao.getAll();
        System.out.println(PostDao.getAll());
        assertEquals(2, PostDao.getAll().size());

        User prueba = UserDao.getByName("test");
        assertEquals(2,prueba.getPosts().size());
    }

    @Test
    public void followTest(){
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        User user2 = new User("test2", "test2", avatar);

        UserDao.save(user);
        UserDao.save(user2);
        UserDao.follow(user,user2);
        User prueba = UserDao.getByName("test");
        assertEquals("test",prueba.getName());
        assertEquals(prueba.getFollowed().size(),1);
        assertEquals(0,prueba.getFollower().size());
    }

    @Test
    public void unfollowTest(){
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        User user2 = new User("test2", "test2", avatar);

        UserDao.save(user);
        UserDao.save(user2);
        UserDao.follow(user,user2);
        User prueba = UserDao.getByName("test");
        assertEquals("test",prueba.getName());
        assertEquals(prueba.getFollowed().size(),1);
        UserDao.unfollow(user,user2);
        prueba = UserDao.getByName("test");
        assertEquals(prueba.getFollowed().size(),0);
    }

    @Test
    public void likePost(){
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        User user2 = new User("test2", "test2", avatar);
        UserDao.save(user);
        UserDao.save(user2);
        System.out.println(user.getId());
        Post post = new Post(user,"test1");
        PostDao.save(post);

    }

    @Test
    public void unlikePost(){
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        User user2 = new User("test2", "test2", avatar);
        UserDao.save(user);
        UserDao.save(user2);
        Post post = new Post(user, "test1");
        System.out.println(post);
        System.out.println(PostDao.save(post));
        PostDao.saveLike(user2,post);
        PostDao.deleteLike(user2,post);
        assertEquals(0,post.getLikes().size());
    }

    @Test
    public void commentPost(){
        byte[] avatar = new byte[0];
        User user = new User("test", "test", avatar);
        User user2 = new User("test2", "test2", avatar);
        UserDao.save(user);
        UserDao.save(user2);
        Post post = new Post(user, "test1");
        System.out.println(post);
        System.out.println(PostDao.save(post));
        Comment comment = new Comment(user2,"test",post);
        CommentDao.save(comment);
        Comment c = CommentDao.getById(comment.getId());
        assertEquals(comment,c);
    }

}