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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import redSocial.DAO.PostDao;
import redSocial.DAO.UserDao;
import redSocial.model.User;
import redSocial.utils.Log;
import redSocial.utils.Windows;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static redSocial.controllers.Data.c;
import static redSocial.controllers.Data.t;

public class HomeC implements Initializable {
    private List<PostDao> posts;
    List<User> followed = Data.principalUser.getFollowed();

    private int postNumber = 0;
    private ObservableList<User> observableUsers = FXCollections.observableArrayList(followed);
    private String secs;
    private String min;
    private String hours;

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
    private Button loadMore;

    @FXML
    private TableView<User> followedTable;

    @FXML
    private TableColumn<User,String> followedColumn;

    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox pnItems = null;
    @FXML
    private GridPane gridPane = null;
    @FXML
    private Label Label_Minuto;
    @FXML
    private Label Label_Segundo;
    @FXML
    private Label Label_Hora;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Label_Hora.textProperty().bind(c.getFraseHora());
        Label_Minuto.textProperty().bind(c.getFraseMinuto());
        Label_Segundo.textProperty().bind(c.getFraseSegundo());

        loadPosts(postNumber);

        followed= Data.principalUser.getFollowed();
        observableUsers=FXCollections.observableArrayList(followed);
        followedList();
        followedTable.setItems(FXCollections.observableArrayList(observableUsers));
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
    private List<PostDao> posts() {
        List<PostDao> ls = PostDao.getAll();

        return ls;
    }

    public void loadPosts(int p){
        posts = new ArrayList<>(posts());
        int columns = 0;
        int row = 1;
        try {
            for (int i=p ;i < posts.size(); i++) {
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
                ++postNumber;
                if (i == 2) {
                    i = posts.size();
                }else if(loadMore.isPressed()){
                    refreshPosts();
                }
            }
        } catch (IOException e) {
            Log.severe("Error al cargar los posts "+e.getMessage());
        }
    }

    public void refreshPosts(){
        gridPane.getChildren().clear();
        loadPosts(postNumber);
        if(postNumber==posts.size() || postNumber>=posts.size()){
            Windows.mostrarInfo("No hay más posts", "No hay más posts", "Estos son los ultimos posts");
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
            t.interrupt();
            c.interrupt();
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
            App.closeScene((Stage) borderPane.getScene().getWindow());
        }
    }

    public void refresh() {
        observableUsers.removeAll(observableUsers);
        observableUsers.addAll(followed);
    }
}
