package redSocial.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import redSocial.model.DAO.CommentDao;
import redSocial.model.DataObject.Comment;
import redSocial.utils.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CommentsC  implements Initializable {
    @FXML
    private GridPane commentGrid;
    @FXML
    private Label nickname;
    @FXML
    private TextArea CommentText;
    @FXML
    private Button publish;
    @FXML
    private Button back;
    @FXML
    private AnchorPane anchorPane;

    private List<Comment> comments;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComments();
    }

    public void loadComments(){
        comments= new ArrayList<>(comments());
        int columns=0;
        int rows=1;

        try{
            for (int i = 0; i < comments.size(); i++){
                FXMLLoader fxmlLoader= new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Comment.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                CommentC commentC= fxmlLoader.getController();
                commentC.setData(comments.get(i));
                if(columns == 1) {
                    columns = 0;
                    ++rows;
                }
                commentGrid.add(anchorPane, columns++, rows);
                commentGrid.setMargin(anchorPane, new Insets(10));
            }

        }catch (IOException e){
            Log.severe("Error al cargar los comentarios"+e.getMessage());
        }
    }

    @FXML
    private void publishComment(){
        Comment cd = new Comment();
        if (!CommentText.getText().isEmpty()) {
            cd.setTextComment(CommentText.getText());
            cd.setUserComment(Data.principalUser);
            CommentText.clear();
            cd.setPost(Data.p);
            CommentDao.save(cd);
            refreshComments();
        }
    }

    private List<Comment> comments(){
        List<Comment> ls = CommentDao.getAllByPost(Data.p);

        return ls;
    }

    public void refreshComments(){
        commentGrid.getChildren().clear();
        loadComments();
    }

    public void back(){
        App.closeScene((Stage) anchorPane.getScene().getWindow());
    }

}
