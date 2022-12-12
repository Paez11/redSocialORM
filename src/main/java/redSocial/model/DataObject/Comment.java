package redSocial.model.DataObject;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "comment")
public class Comment {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    protected User UserComment;
    @Column(name = "texto")
    protected String textComment;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    protected Post post;
    @Column(name = "fecha")
    protected Date date;

    public Comment(int id, User userComment, String textComment, Post post, Date date) {
        this.id = id;
        this.UserComment = userComment;
        this.textComment = textComment;
        this.post = post;
        this.date = date;
    }

    public Comment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextComment() {
        return textComment;
    }

    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }

    public User getUserComment() {
        return UserComment;
    }

    public void setUserComment(User userComment) {
        UserComment = userComment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && Objects.equals(UserComment, comment.UserComment) && Objects.equals(post, comment.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, UserComment, post);
    }
}
