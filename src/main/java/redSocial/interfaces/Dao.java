package redSocial.interfaces;

public interface Dao {
    public void save();
    public void delete();
    public void update();

    private Object getById(Object o) {
        return o;
    }
}
