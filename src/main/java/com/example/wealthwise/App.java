package com.example.wealthwise;


import com.example.wealthwise.Models.Model;
import com.example.wealthwise.Views.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage stage)  {
        Model.getInstance().getViewFactory().showloginWindow();
    }
}
