package redSocial.model.DataObject;

public class Follow {
    private int id;
    private User user;
    private User followed;

    public Follow() {
    }

    public Follow(User user, User followed) {
        this.user = user;
        this.followed = followed;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFollowed() {
        return followed;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }
}
