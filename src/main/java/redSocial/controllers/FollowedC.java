package redSocial.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import redSocial.DAO.FollowDao;
import redSocial.DAO.PostDao;
import redSocial.DAO.UserDao;
import redSocial.model.User;
import redSocial.utils.Log;
import redSocial.utils.Windows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FollowedC implements Initializable {

    private List<PostDao> posts;

    List<User> followed = Data.principalUser.getFollowed();

    private ObservableList<User> observableUsers = FXCollections.observableArrayList(followed);

    @FXML
    private Label nickname;

    @FXML
    private PasswordField password;

    @FXML
    private ImageView profileImage;

    @FXML
    private Button Followbtn;


    @FXML
    private Button homeBtn;

    @FXML
    private Button profileBtn;

    @FXML
    private Button newBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private Button configBtn;

    @FXML
    private TableView<User> followedTable;

    @FXML
    private TableColumn<User,String> followedColumn;

    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private AnchorPane anchorPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Data.principalUser.getFollowed().contains(Data.aux)){
            Followbtn.setText("UnFollow");
        }
        nickname.setText(Data.aux.getName());
        loadUserPosts();

        followed= Data.principalUser.getFollowed();
        profileImage.setImage(new Image(new ByteArrayInputStream(Data.aux.getAvatar())));
        observableUsers= FXCollections.observableArrayList(followed);
        followedList();
        followedTable.setItems(FXCollections.observableArrayList(observableUsers));
        refresh();
        followedTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Node node = ((Node) event.getTarget()).getParent();
                    TableRow row;
                    if (node instanceof TableRow) {
                        row = (TableRow) node;

                    } else {
                        // clicking on text part
                        row = (TableRow) node.getParent();
                    }
                    Data.aux= (UserDao) row.getItem();
                    App.loadScene(new Stage(), "Followed", "RedSocial", false, false);
                    App.closeScene((Stage) borderPane.getScene().getWindow());
                }
            }
        });

        refresh();

        Platform.runLater(()->{
            Windows.closeRequest((Stage) borderPane.getScene().getWindow());
        });
    }

    public void followedList(){
        followedColumn.setCellValueFactory(follower ->{
            SimpleStringProperty ssp = new SimpleStringProperty();
            ssp.setValue(follower.getValue().getName());
            return ssp;
        });
    }

    public void loadUserPosts(){
        posts = new ArrayList<>(Userposts());
        int columns = 0;
        int row = 1;
        try {
            for (int i = 0; i < posts.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Post.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                PostC post = fxmlLoader.getController();
                post.setDataPost(posts.get(i));
                if(columns == 1) {
                    columns = 0;
                    ++row;
                }
                gridPane.add(anchorPane, columns++, row);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            Log.severe("Error al cargar los posts "+e.getMessage());
        }
    }

    private List<PostDao> Userposts() {
        List<PostDao> ls = PostDao.getAllByUser(Data.aux.getId());

        return ls;
    }

    public void switchPane(ActionEvent event){
        Object source = event.getSource();
        if (homeBtn.equals(source)) {
            go("Home",true);
        } else if (profileBtn.equals(source)) {
            go("Profile",true);
        } else if (newBtn.equals(source)) {
            go("CreatePost",false);
        } else if (logoutBtn.equals(source)) {
            go("LogIn",true);
        } else if (searchBtn.equals(source)) {
            Windows.mostrarAlerta("Error", "SearchBar", "No implementado");
        } else if (configBtn.equals(source)) {
            Windows.mostrarAlerta("Error", "Configuration", "No implementado");
        }
    }

    @FXML
    public void FollowUser(){
        if (Followbtn.getText().equals("Follow")){
            if (!Data.principalUser.getFollowed().contains(Data.aux)){
                Data.principalUser.getFollowed().add(Data.aux);
                FollowDao fd= new FollowDao(Data.principalUser, Data.aux);
                fd.save();
                Followbtn.setText("UnFollow");
                refresh();
            }
        }else if (Followbtn.getText().equals("UnFollow")){
            if (Data.principalUser.getFollowed().contains(Data.aux)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Dejar de Seguir");
                alert.setHeaderText("¿Estas seguro?");
                alert.setContentText("¿Estas seguro de que quieres dejar de seguir a este usuario?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    FollowDao fd= new FollowDao(Data.principalUser, Data.aux);
                    fd.deletebyusers(Data.principalUser, Data.aux);
                    Data.principalUser.getFollowed().remove(Data.aux);
                    Followbtn.setText("Follow");
                    refresh();
                }
            }
        }
    }

    public void go(String fxml,boolean windowed){
        if (windowed) {
            App.loadScene(new Stage(), fxml, "RedSocial", false, false);
            App.closeScene((Stage) borderPane.getScene().getWindow());
        }else{
            App.loadScene(new Stage(), fxml, "redSocial", true, false);
        }
    }

    public void refresh() {
        observableUsers.removeAll(observableUsers);
        observableUsers.addAll(followed);
    }
}
