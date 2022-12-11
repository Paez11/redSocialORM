package redSocial.model.DataObject;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;
	@JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	protected User userName;
    @Column(name = "dateCreate")
    protected Date dateCreate;
    @Column(name = "dateUpdate")
    protected Date dateUpdate;
    @Column(name = "text")
    protected String text;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idComment", fetch = FetchType.LAZY)
    protected List<Comment> comments;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser", fetch = FetchType.LAZY)
    protected Set<User> likes;

    public Post() {
        this.likes= new HashSet<>();
    }

    public Post(int id) {
        this.id = id;
        this.likes= new HashSet<>();
    }

    public Post(User userName, int id) {
        this.userName = userName;
        this.id = id;
        this.likes= new HashSet<>();
    }

    public Post(User userName) {
        this.userName = userName;
        this.likes= new HashSet<>();
    }

    public Post(User userName, int id, Date dateCreate, Date dateUpdate, String text) {
        this.userName = userName;
        this.id = id;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.text = text;
        this.likes= new HashSet<>();
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
