package redSocial.utils.contador;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Read extends Thread{
    private IntegerProperty hora;
    private IntegerProperty minuto;
    private IntegerProperty segundo;

    //Texto que se mostrara por pantalla
    private StringProperty StringHora;
    private StringProperty StringMinuto;
    private StringProperty StringSegundo;



    public Read() {
        this.hora = new SimpleIntegerProperty(0);
        this.minuto = new SimpleIntegerProperty(0);
        this.segundo = new SimpleIntegerProperty(0);
        this.StringHora = new SimpleStringProperty("00");
        this.StringMinuto = new SimpleStringProperty("00");
        this.StringSegundo = new SimpleStringProperty("00");
    }

    public IntegerProperty getHora() {
        return hora;
    }
    public void setHora(IntegerProperty hora) {
        this.hora = hora;
    }
    public IntegerProperty getMinuto() {
        return minuto;
    }
    public void setMinuto(IntegerProperty minuto) {
        this.minuto = minuto;
    }
    public IntegerProperty getSegundo() {
        return segundo;
    }
    public void setSegundo(IntegerProperty segundo) {
        this.segundo = segundo;
    }
    public StringProperty getFraseHora() {
        return StringHora;
    }
    public void setFraseHora(StringProperty fraseHora) {
        this.StringHora = fraseHora;
    }
    public StringProperty getFraseMinuto() {
        return StringMinuto;
    }
    public void setFraseMinuto(StringProperty fraseMinuto) {
        this.StringMinuto = fraseMinuto;
    }
    public StringProperty getFraseSegundo() {
        return StringSegundo;
    }
    public void setFraseSegundo(StringProperty fraseSegundo) {
        this.StringSegundo = fraseSegundo;
    }

    public void run() {
        // TODO Auto-generated method stub

        while (!this.isInterrupted()) {

            Platform.runLater(() -> {
                segundo.set(segundo.get() + 1);

                /*
                 * Si los segundos son menos de 10,
                 * imprimira un 0 delante del segundo en caso contrario implimira los segundos.
                 *
                 * Para conseguir el formato:
                 *
                 * -En caso de ser menor de 10: 00:00:02
                 * - En caso de ser mayor de 10: 00:00:12
                 *
                 * */
                if(segundo.get()<10) {
                    StringSegundo.set("0"+segundo.get());
                }else {
                    StringSegundo.set(segundo.get()+"");
                }

            });
            /*
             * Cuando los segundos sean igual a 59,aumentara en 1 los minutos
             * la cuenta se segundos se pondra a 0 y volvera a empezar, asi sucesibamente.
             *
             * Y para imprimir el tiempo usuaremos el mismo sistema explicado antes.
             * */
            if (segundo.get() == 59) {
                Platform.runLater(() -> {
                    minuto.set(minuto.get() + 1);
                    if(minuto.get()<10) {
                        StringMinuto.set("0"+minuto.get());
                    }else {
                        StringMinuto.set(minuto.get()+"");
                    }
                    segundo.set(0);
                    StringSegundo.set(segundo.get()+"0");
                });

                if (minuto.get() == 59) {
                    Platform.runLater(() -> {
                        minuto.set(0);
                        StringMinuto.set(minuto.get()+"");
                        hora.set(hora.get() + 1);
                        StringHora.set(hora.get()+"");
                    });

                    if (hora.get() == 24) {
                        Platform.runLater(() -> {
                            hora.set(0);
                            StringHora.set(hora.get()+"");
                        });

                    }
                }
            }
            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                //System.exit(0);
                Platform.runLater(() -> {
                    segundo.set(0);
                    StringSegundo.set(segundo.get()+"0");

                    minuto.set(0);
                    StringMinuto.set(minuto.get()+"0");

                    hora.set(0);
                    StringHora.set(hora.get()+"0");
                });

            }


        }

    }

}
