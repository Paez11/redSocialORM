package redSocial.model.DataObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

@Entity
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected int id = -1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
	protected User user;
    @Column(name = "fecha_creacion")
    protected LocalDateTime dateCreate;
    @Column(name = "fecha_modificacion")
    protected LocalDateTime dateUpdate;
    @Column(name = "texto")
    protected String text;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "post")
    protected List<Comment> comments;

	@ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "likes", joinColumns = @JoinColumn(name = "id_post"), inverseJoinColumns = @JoinColumn(name = "id_user"))
    protected List<User> likes;

    public Post() {
    }

    public Post(int id) {
        this.id = id;;
    }

    public Post(User user, int id) {
        this.user = user;
        this.id = id;
    }

    public Post(User user, String text) {
        this.user = user;
        this.text = text;
    }

    public Post(User user) {
        this.user = user;
    }

    public Post(User user, int id, LocalDateTime dateCreate, LocalDateTime dateUpdate, String text) {
        this.user = user;
        this.id = id;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.text = text;
    }

    public Post(User user, int id, LocalDateTime dateCreate, LocalDateTime dateUpdate, String text, List<Comment> comments, List<User> likes) {
        this.user = user;
        this.id = id;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.text = text;
        this.comments = comments;
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userName) {
        this.user = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDateTime getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(LocalDateTime dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        if (comments == null) return;
        for (Comment c: comments) {
            this.addUserComments(c);
        };
    }

    public List<User> getLikes() {
        return likes;
    }

    public void setLikes(List<User> likes) {
        this.likes = likes;
    }

    public boolean addUserComments(Comment c) {
        boolean result = false;
        if (this.comments == null) {
            this.comments = new ArrayList<>();
            this.comments.add(c);
            result = true;
        } else {
            this.comments.add(c);
            result = true;
        }
        return result;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", userName=" + user +
                ", dateCreate=" + dateCreate +
                ", dateUpdate=" + dateUpdate +
                ", text='" + text + '\'' +
                ", comments=" + comments +
                ", likes=" + likes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
