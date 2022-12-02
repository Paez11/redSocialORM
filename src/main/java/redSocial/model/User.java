package redSocial.model;

import java.util.List;
import java.util.Objects;

public class User {
    protected int id;
    protected String name;
    protected String password;
    protected byte[] avatar;
    protected List<Post> posts;
    protected List<Comment> comments;
    protected List<Post> likes;
    protected List<User> followed;
    protected List<User> follower;

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String name, String password,byte[] avatar) {
        this.name = name;
        this.password = password;
        this.avatar = avatar;
    }

    public User(int id, String name, byte[] avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public User(int id, String name, List<Post> posts) {
        this.id = id;
        this.name = name;
        this.posts = posts;
    }

    public User(int id, String name, byte[] avatar, List<Post> posts, List<Post> likes) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.posts = posts;
        this.likes = likes;
    }

    public User(int id, String name, byte[] avatar, List<Comment> comments) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.comments = comments;
    }

    public User(int id, String name, String password, byte[] avatar) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.avatar = avatar;
    }

    public User(String name, byte[] avatar, List<User> followed, List<User> follower) {
        this.name = name;
        this.avatar = avatar;
        this.followed = followed;
        this.follower = follower;
    }

    public User(int id, String name, String password, byte[] avatar,
                List<Post> posts, List<Comment> comments, List<Post> likes,
                List<User> followed, List<User> follower) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.avatar = avatar;
        this.posts = posts;
        this.comments = comments;
        this.likes = likes;
        this.followed = followed;
        this.follower = follower;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Post> getLikes() {
        return likes;
    }

    public void setLikes(List<Post> likes) {
        this.likes = likes;
    }

    public List<User> getFollowed() {
        return followed;
    }

    public void setFollowed(List<User> followed) {
        this.followed = followed;
    }

    public List<User> getFollower() {
        return follower;
    }

    public void setFollower(List<User> follower) {
        this.follower = follower;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", posts=" + posts +
                ", comments=" + comments +
                ", likes=" + likes +
                ", followed=" + followed +
                ", follower=" + follower +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
