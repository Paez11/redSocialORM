package redSocial.model.DataObject;

import javax.persistence.*;

//@Entity
//@Table(name = "follower")
public class Follow {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    //@Column(name = "id_follower")
    //@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Transient
    private User user;
    //@Column(name = "id_follow")
    //@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Transient
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
