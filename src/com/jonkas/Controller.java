package com.jonkas;

import com.sun.javaws.jnl.JavaFXRuntimeDesc;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

//ToDo: ToDO countdown
public class Controller implements Initializable{

    @FXML
    private Label label_task;
    @FXML
    private Label label_score;
    @FXML
    private Button button_plus;
    @FXML
    private Button button_minus;
    @FXML
    private Button button_multiplie;
    @FXML
    private Button button_divide;
    @FXML
    private Button button_restart;
    @FXML
    private Menu menu_settings;
    @FXML
    private Label label_timer;

    private int score = 0;

    volatile private int countDownTime;

    private enum TASK{
        PLUS,
        MINUS,
        MULTIPLIE,
        DIVIDE
    }
    private TASK currentTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert label_task != null : "fx:id=\"label_task\" was not injected: check your FXML file 'simple.fxml'.";
        assert label_score != null : "fx:id=\"label_score\" was not injected: check your FXML file 'simple.fxml'.";
        assert button_plus != null : "fx:id=\"button_plus\" was not injected: check your FXML file 'simple.fxml'.";
        assert button_minus != null : "fx:id=\"button_minus\" was not injected: check your FXML file 'simple.fxml'.";
        assert button_multiplie != null : "fx:id=\"button_multiplie\" was not injected: check your FXML file 'simple.fxml'.";
        assert button_divide != null : "fx:id=\"button_divide\" was not injected: check your FXML file 'simple.fxml'.";
        assert button_restart != null : "fx:id=\"button_restart\" was not injected: check your FXML file 'simple.fxml'.";
        assert menu_settings != null : "fx:id=\"menu_settings\" was not injected: check your FXML file 'simple.fxml'.";
        assert label_timer != null : "fx:id=\"label_timer\" was not injected: check your FXML file 'simple.fxml'.";

        Image image_plus = new Image(getClass().getResourceAsStream("/img/plus50x50.png"));
        button_plus.setGraphic(new ImageView(image_plus));
        button_plus.setBackground(null);

        Image image_minus = new Image(getClass().getResourceAsStream("/img/minus50x50.png"));
        button_minus.setGraphic(new ImageView(image_minus));
        button_minus.setBackground(null);

        Image image_multiply = new Image(getClass().getResourceAsStream("/img/multiply50x50.png"));
        button_multiplie.setGraphic(new ImageView(image_multiply));
        button_multiplie.setBackground(null);

        Image image_divide = new Image(getClass().getResourceAsStream("/img/divide50x50.png"));
        button_divide.setGraphic(new ImageView(image_divide));
        button_divide.setBackground(null);


        generateTask();
    }


    private void checkTask(TASK task){
        if(task==currentTask){
            System.out.println("Richtig");
            score++;
            label_score.setText("Score: "+score);
            generateTask();
        }else{
            System.out.println("Falsch");
            label_score.setText("Score: "+score);
            countDownTime-=3;
        }
    }

    public void restartGame(){
        button_restart.setVisible(false);
        button_plus.setDisable(false);
        button_minus.setDisable(false);
        button_multiplie.setDisable(false);
        button_divide.setDisable(false);
        generateTask();
        countDownTimer(30);
    }

    private void generateTask(){
        countDownTimer(30);
        switch (ThreadLocalRandom.current().nextInt(1, 4 + 1)){
            case 1:
                currentTask=TASK.PLUS;
                break;
            case 2:
                currentTask=TASK.MINUS;
                break;
            case 3:
                currentTask=TASK.MULTIPLIE;
                break;
            case 4:
                currentTask=TASK.DIVIDE;
                break;
        }


        int ran1 = ThreadLocalRandom.current().nextInt(3, 99 + 1);
        int ran2 = ThreadLocalRandom.current().nextInt(3, 99 + 1);
        switch (currentTask){
            case PLUS:
                label_task.setText(""+ran1+" ? "+ran2+" = "+(ran1+ran2) );
                break;
            case MINUS:
                label_task.setText(""+ran1+" ? "+ran2+" = "+(ran1-ran2));
                break;
            case MULTIPLIE:
                label_task.setText(""+ran1+" ? "+ran2+" = "+ran1*ran2 );
                break;
            case DIVIDE:
                    while (ran1 % ran2 != 0){
                        ran1 = ThreadLocalRandom.current().nextInt(3, 99 + 1);
                        ran2 = ThreadLocalRandom.current().nextInt(3, 99 + 1);
                    }
                    label_task.setText("" + ran1 + " ? " + ran2 + " = " + ran1 / ran2);
                break;
        }


    }

    private void countDownTimer(int time){
        countDownTime = time;

//ToDo: Macht noch Probleme
        Thread thread = new Thread(new Runnable() {
            volatile long startTime = System.currentTimeMillis();
            @Override
            public void run() {

                while(countDownTime>0 ){
                    if(System.currentTimeMillis()-startTime>=1000) {
                        startTime = System.currentTimeMillis();
                        countDownTime--;
                        System.out.println(countDownTime);

                        Platform.runLater(() -> {
                            //if you change the UI, do it here !
                            label_timer.setText("Time: " + countDownTime);
                        });
                    }
                }
                gameOver();

            }
        });
        thread.start();

    }

    private void gameOver(){
        button_restart.setVisible(true);
        button_plus.setDisable(true);
        button_minus.setDisable(true);
        button_multiplie.setDisable(true);
        button_divide.setDisable(true);
        score=0;
    }

    public void onButton_plus(){
        System.out.println("onButton_plus");
        checkTask(TASK.PLUS);
    }
    public void onButton_minus(){
        System.out.println("onButton_minus");
        checkTask(TASK.MINUS);
    }
    public void onButton_multiplie(){
        System.out.println("onButton_multiplie");
        checkTask(TASK.MULTIPLIE);

    }
    public void onButton_divide(){
        System.out.println("onButton_divide");
        checkTask(TASK.DIVIDE);

    }


}
