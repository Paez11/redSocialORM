package redSocial.controllers;

import javafx.scene.input.KeyCode;
import redSocial.DAO.FollowDao;
import redSocial.utils.Valid;
import redSocial.utils.Windows;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInC implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField Nickname;
    @FXML
    private PasswordField Password;
    @FXML
    private Button LogIn;
    @FXML
    private Button SignUp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            Windows.closeRequest((Stage) anchorPane.getScene().getWindow());
        });
    }

    @FXML
    public void LogIn(){

        String nickname = Nickname.getText();
        String password = Password.getText();

        if (nickname.isEmpty() || password.isEmpty()){
            Windows.mostrarAlerta("Error","Error","Rellene todos los campos");
        }else{
            password = Valid.sha256(password);
            Data.principalUser.getByName(nickname);
            Data.principalUser.setName(nickname);           //No se por que?, pero getByName no setea el nombre del usuario
            if (Data.principalUser.getName()!=null && Data.principalUser.getPassword().equals(password)){
                FollowDao fd = new FollowDao();
                Data.principalUser.setFollowed(fd.getByName(Data.principalUser));
                App.loadScene(new Stage(), "Home", "RedSocial", false, false);
                App.closeScene((Stage) anchorPane.getScene().getWindow());
            }else{
                Windows.mostrarAlerta("Error", "Usuario o contraseña incorrectos", "Usuario o contraseña incorrectos");
                Nickname.setText("");
                Password.setText("");
            }
        }
    }

    @FXML
    private void switchToRegister(ActionEvent event) throws IOException {
        App.loadScene(new Stage(),"SignUp","RedSocial",false,false);
        App.closeScene((Stage) anchorPane.getScene().getWindow());
    }

    public void enter(){
        Password.setOnKeyPressed( event -> {
            if(event.getCode() == KeyCode.ENTER) {
                LogIn();
            }
        });
    }
    public void switchDown(){
        Nickname.setOnKeyPressed( event -> {
            if(event.getCode() == KeyCode.DOWN) {
                Password.requestFocus();
            }
        });
    }

}
