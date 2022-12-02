package redSocial.controllers;

import redSocial.DAO.CommentDao;
import redSocial.DAO.PostDao;
import redSocial.DAO.UserDao;
import redSocial.utils.contador.Read;

public class Data {
    protected static Read c = new Read();
    protected static Thread t = new Thread(c);
    protected static UserDao aux = new UserDao();
    protected static UserDao principalUser = new UserDao();
    protected static PostDao p = new PostDao();
    protected static PostDao paux = new PostDao();
    protected static CommentDao caux = new CommentDao();
}
