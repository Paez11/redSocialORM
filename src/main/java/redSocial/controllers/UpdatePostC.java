package redSocial.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import redSocial.DAO.PostDao;
import redSocial.utils.Windows;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class UpdatePostC implements Initializable {

    @FXML
    private Button update;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextArea content;

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        content.setText(Data.paux.getText());
    }

    public void update(ActionEvent actionEvent) {
        if (content.getText().isEmpty()) {
            Windows.mostrarAlerta("Error", "Error", "El post no puede estar vacio");
        }else {
            Data.paux.setText(content.getText());
            Data.paux.update();
            App.loadScene(new Stage(), "Home", "RedSocial", false, false);
            App.closeScene((Stage) anchorPane.getScene().getWindow());
        }
    }

    public void cancel(ActionEvent actionEvent) {
        App.closeScene((Stage) anchorPane.getScene().getWindow());
    }


}
