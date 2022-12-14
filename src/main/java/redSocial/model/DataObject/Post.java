package redSocial.model.DataObject;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    protected Date dateCreate;
    @Column(name = "fecha_modificacion")
    protected Date dateUpdate;
    @Column(name = "texto")
    protected String text;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    protected List<Comment> comments;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "likes", joinColumns = @JoinColumn(name = "id_post"), inverseJoinColumns = @JoinColumn(name = "id_user"))
    //@EmbeddedId
    //@Transient
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

    public Post(User userName) {
        this.userName = userName;
    }

    public Post(User userName, int id, Date dateCreate, Date dateUpdate, String text) {
        this.userName = userName;
        this.id = id;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.text = text;
    }

    public Post(User userName, int id, Date dateCreate, Date dateUpdate, String text, List<Comment> comments, Set<User> likes) {
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

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
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
        this.comments = comments;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
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
