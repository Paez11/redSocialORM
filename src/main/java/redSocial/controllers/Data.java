package redSocial.controllers;

import redSocial.model.DAO.CommentDao;
import redSocial.model.DAO.PostDao;
import redSocial.model.DAO.UserDao;
import redSocial.model.DataObject.Comment;
import redSocial.model.DataObject.Post;
import redSocial.model.DataObject.User;
import redSocial.utils.contador.Read;

public class Data {
    protected static Read c = new Read();
    protected static Thread t = new Thread(c);
    protected static User aux = new User();
    protected static User principalUser = new User();
    protected static UserDao ud = UserDao.getInstance();
    protected static Post p = new Post();
    protected static Post paux = new Post();
    protected static PostDao pd = PostDao.getInstance();
    protected static Comment caux = new Comment();
}
