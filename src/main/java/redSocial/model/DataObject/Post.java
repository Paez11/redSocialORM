package redSocial.model.DataObject;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

@Entity
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
	protected User userName;
    @Column(name = "fecha_creacion")
    protected LocalDateTime dateCreate;
    @Column(name = "fecha_modificacion")
    protected LocalDateTime dateUpdate;
    @Column(name = "texto")
    protected String text;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    protected List<Comment> comments;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "likes", joinColumns = @JoinColumn(name = "id_post"), inverseJoinColumns = @JoinColumn(name = "id_user"))
    protected Set<User> likes;

    public Post() {
    }

    public Post(int id) {
        this.id = id;;
    }

    public Post(User userName, int id) {
        this.userName = userName;
        this.id = id;
    }

    public Post(User userName, String text) {
        this.userName = userName;
        this.text = text;
    }

    public Post(User userName) {
        this.userName = userName;
    }

    public Post(User userName, int id, LocalDateTime dateCreate, LocalDateTime dateUpdate, String text) {
        this.userName = userName;
        this.id = id;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.text = text;
    }

    public Post(User userName, int id, LocalDateTime dateCreate, LocalDateTime dateUpdate, String text, List<Comment> comments, Set<User> likes) {
        this.userName = userName;
        this.id = id;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.text = text;
        this.comments = comments;
        this.likes = likes;
    }

    public User getUserName() {
        return userName;
    }

    public void setUserName(User userName) {
        this.userName = userName;
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

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
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
