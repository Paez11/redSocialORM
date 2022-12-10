package redSocial.controllers;

import redSocial.model.DAO.CommentDao;
import redSocial.model.DAO.PostDao;
import redSocial.model.DAO.UserDao;
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
