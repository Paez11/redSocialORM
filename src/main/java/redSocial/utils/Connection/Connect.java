package redSocial.utils.Connection;

import redSocial.utils.Log;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Connect {
    public static EntityManagerFactory emf;
    private static Connect _newInstance;
    private static EntityManager manager;

    private void Connection() {
        emf = Persistence.createEntityManagerFactory("MySQL");
        if(emf==null) {
            Log.severe("Error al conectar con la base de datos");
        }
    }

    public static EntityManagerFactory getConnect() {
        if(_newInstance==null) {
            _newInstance=new Connect();
        }
        return emf;
    }

    public static void close() {
        if(emf != null) {
            emf.close();
        }
    }



}
