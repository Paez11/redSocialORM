package redSocial.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import redSocial.model.DAO.CommentDao;
import redSocial.model.DataObject.Comment;
import redSocial.model.DataObject.User;

import java.io.ByteArrayInputStream;

public class CommentC {

    private Comment c;

    @FXML
    private ImageView profileImage;
    @FXML
    protected static Button profilePhotoComment;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label name;
    @FXML
    private Label comment2;
    @FXML
    private Label date;

    public void setData(Comment comment){
        Comment cd= CommentDao.getById(comment.getId());
        User u= Data.ud.getByName(cd.getUser().getName());
        name.setText(u.getName());
        comment2.setText(comment.getTextComment());
        date.setText(comment.getDate().toString());
        this.c=cd;
        Data.aux= this.c.getUser();
        profileImage.setImage(new Image(new ByteArrayInputStream(Data.aux.getAvatar())));
    }

    public void switchProfile(){
        Data.aux= this.c.getUser();
        Data.caux = this.c;

        if (Data.principalUser.getId()==this.c.getId()) {
            App.loadScene(new Stage(), "Profile", "RedSocial", false, false);
            App.closeScene((Stage) anchorPane.getScene().getWindow());
        }else{
            App.loadScene(new Stage(), "Followed", "RedSocial", false, false);
            App.closeScene((Stage) anchorPane.getScene().getWindow());
        }
    }
}
