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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import redSocial.DAO.PostDao;
import redSocial.DAO.UserDao;
import redSocial.model.User;
import redSocial.utils.Log;
import redSocial.utils.Tools;
import redSocial.utils.Valid;
import redSocial.utils.Windows;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileC implements Initializable {

    private List<PostDao> posts;
    List<User> followed = Data.principalUser.getFollowed();
    List<String> pstFile;

    private ObservableList<User> observableUsers = FXCollections.observableArrayList(followed);

    @FXML
    private TextField nickname;

    @FXML
    private TextField photoText;

    @FXML
    private PasswordField password;

    @FXML
    private ImageView profileImage;

    @FXML
    private Button photoEditBtn;

    @FXML
    private Button nickNameEditBtn;

    @FXML
    private Button passwordEditBtn;

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
    private GridPane gridPane = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nickname.setText(Data.principalUser.getName());
        profileImage.setImage(new Image(new ByteArrayInputStream(Data.principalUser.getAvatar())));
        photoText.setVisible(false);
        loadUserPosts();

        followed= Data.principalUser.getFollowed();
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
        List<PostDao> ls = PostDao.getAllByUser(Data.principalUser.getId());

        return ls;
    }

    public void editNickName(ActionEvent event){
        String newNickName = nickname.getText();
        nickname.setText(newNickName);
        Data.principalUser.setName(newNickName);
        Data.principalUser.update();
        Windows.mostrarInfo("editNickName","NickName","NickName editado correctamente");
    }

    public void editPassword(ActionEvent event){
        String newPassword = password.getText();
        password.setText(newPassword);
        newPassword = Valid.sha256(newPassword);
        Data.principalUser.setPassword(newPassword);
        Data.principalUser.update();
        Windows.mostrarInfo("editPassword","Password","Password editada correctamente");
    }

    public void editPhoto(ActionEvent event){
        photoText.setDisable(true);

        String path = Tools.getFilePathFromFileChooser();

        if (path!=null){
            Data.principalUser.setAvatar(path.getBytes());
            Data.principalUser.update();
            Windows.mostrarInfo("editPhoto","Foto","Fhoto editada correctamente");
        }else {
            Windows.mostrarInfo("editPhoto","Foto","Foto no seleccionada");
        }


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

