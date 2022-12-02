package redSocial.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Windows {
    public static void mostrarAlerta(String title, String header, String description) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(description);
        alert.showAndWait();
    }

    public static void mostrarInfo(String title, String header, String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(description);
        alert.showAndWait();
    }

    public static void closeRequest(Stage stage){
        stage.setOnCloseRequest(windowEvent -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Confirmacion de cierre");
            a.setHeaderText("¿Esta seguro de salir del programa?");
            Stage s =(Stage)a.getDialogPane().getScene().getWindow();
            s.initOwner(stage);
            s.toFront();
            a.showAndWait().filter(buttonType -> buttonType== ButtonType.OK).ifPresentOrElse(buttonType -> {
                Platform.exit();},windowEvent::consume);
        });
    }
}
