package redSocial.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreatePostC implements Initializable {

    @FXML
    private Button submitBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextArea content;

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void submit(ActionEvent actionEvent) {
        Data.p.setUserName(Data.principalUser);
        Data.p.setText(content.getText());
        Data.p.save();
        App.loadScene(new Stage(), "Home", "RedSocial", false, false);
        App.closeScene((Stage) anchorPane.getScene().getWindow());
    }

    public void cancel(ActionEvent actionEvent) {
        App.loadScene(new Stage(), "Home", "RedSocial", false, false);
        App.closeScene((Stage) anchorPane.getScene().getWindow());
    }


}
