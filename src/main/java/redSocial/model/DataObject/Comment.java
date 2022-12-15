package redSocial.model.DataObject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id", nullable = false)
    protected User user;
    @Column(name = "texto")
    protected String textComment;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "id_post", referencedColumnName = "id", nullable = false)
    protected Post post;
    @Column(name = "fecha")
    protected LocalDateTime date;

    public Comment() {
    }

    public Comment(int id) {
        this.id = id;
    }

    public Comment(int id, User user, String textComment, Post post, LocalDateTime date) {
        this.id = id;
        this.user = user;
        this.textComment = textComment;
        this.post = post;
        this.date = date;
    }

    public Comment(User user, String textComment, Post post) {
        this.user = user;
        this.textComment = textComment;
        this.post = post;
    }

    public Comment(User user, String textComment, Post post, LocalDateTime date) {
        this.user = user;
        this.textComment = textComment;
        this.post = post;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTextComment() {
        return textComment;
    }

    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && Objects.equals(user, comment.user) && Objects.equals(post, comment.post);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", UserComment=" + user.getName() +
                ", textComment='" + textComment + '\'' +
                ", post=" + post.getId() +
                ", date=" + date +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, post);
    }
}
