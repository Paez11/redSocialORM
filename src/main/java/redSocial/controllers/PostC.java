package redSocial.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import redSocial.model.DAO.PostDao;
import redSocial.model.DataObject.Post;
import redSocial.model.DataObject.User;
import redSocial.utils.Windows;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class PostC implements Initializable {

    private Post p;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label username;

    @FXML
    private Label date;

    @FXML
    private Label content;

    @FXML
    private Label editLabel;

    @FXML
    private Label likesLabel;

    @FXML
    private Button likes;

    @FXML
    private Button comments;

    @FXML
    private Button delete;

    @FXML
    private Button update;

    @FXML
    private ImageView profileImage;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editLabel.setVisible(false);
    }

    public void setDataPost(Post p){
        User aux2 = Data.ud.getById(p.getUser().getId());
        username.setText(aux2.getName());
        profileImage.setImage(new Image(new ByteArrayInputStream(aux2.getAvatar())));
        content.setText(p.getText());
        Data.p=p;
        Data.p.setLikes(Data.pd.getAllLikes(Data.p));
        if (Data.p.getLikes()!=null){
            if (Data.pd.likePost(Data.p,Data.principalUser)){
                likes.setText("no me gusta");
            }else{
                likes.setText("me gusta");
            }
        }
        likesLabel.setText(String.valueOf(Data.p.getLikes().size()));

        if (p.getDateUpdate()!=null){
            date.setText(p.getDateUpdate().toString());
            editLabel.setVisible(true);
        }else {
            date.setText(p.getDateCreate().toString());
        }
        this.p = p;
        if (Data.principalUser.getId()==p.getUser().getId()) {
            delete.setVisible(true);
            update.setVisible(true);
        }else{
            delete.setVisible(false);
            update.setVisible(false);
        }
    }

    public void deletePost(){
        if (Data.principalUser.getId()==p.getUser().getId()){
        	PostDao pd= new PostDao();
            pd.delete(p);
            Windows.mostrarAlerta("Eliminar","Eliminar","post eliminado");
            App.loadScene(new Stage(), "Home", "RedSocial", false, false);
            App.closeScene((Stage) anchorPane.getScene().getWindow());
        }else {
            Windows.mostrarAlerta("ERROR","ERROR","No puedes borrar este post");
        }
    }

    public void updatePost(){
        if (Data.principalUser.getId()==p.getUser().getId()){
            Data.paux = p;
            App.loadScene(new Stage(), "UpdatePost", "RedSocial", true, false);
            App.closeScene((Stage) anchorPane.getScene().getWindow());
        }else {
            Windows.mostrarAlerta("ERROR","ERROR","No puedes editar este post");
        }
    }

    public void openComments(){
        Data.p = this.p;
        Platform.runLater(() -> {
            App.loadScene(new Stage(), "Comments", "RedSocial", true, false);
        });
    }

    public void switchProfile(){
        Data.aux= this.p.getUser();
        Data.p = this.p;
        if (Data.principalUser.getId()==Data.aux.getId()) {
            App.loadScene(new Stage(), "Profile", "RedSocial", false, false);
            App.closeScene((Stage) anchorPane.getScene().getWindow());
        }else{
            App.loadScene(new Stage(), "Followed", "RedSocial", false, false);
            App.closeScene((Stage) anchorPane.getScene().getWindow());
        }
    }

    public void likePost(){
        Data.paux= this.p;
        if (likes.getText().equals("me gusta")){
                Data.paux.getLikes().add(Data.principalUser);
                Data.pd.saveLike(Data.principalUser,Data.paux);
                likes.setText("no me gusta");
                likesLabel.setText(String.valueOf(Data.paux.getLikes().size()));
        }else if (likes.getText().equals("no me gusta")){
                    Data.pd.deleteLike(Data.principalUser,Data.paux);
                    Data.paux.getLikes().remove(Data.principalUser);
                    likes.setText("me gusta");
                    likesLabel.setText(String.valueOf(Data.paux.getLikes().size()));
        }
    }
}
