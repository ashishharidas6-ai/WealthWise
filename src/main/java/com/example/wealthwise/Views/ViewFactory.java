package com.example.wealthwise.Views;

import com.example.wealthwise.Controller.Client.ClientController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {
    //client views
    private final StringProperty clientSelectedMenuItem ;
    private AnchorPane dashboardView;
    private AnchorPane transactionView;
    private AnchorPane investmentView;
    private AnchorPane accountView;
    private AnchorPane profileView;
    private AnchorPane reportView;

    public ViewFactory(){
        this.clientSelectedMenuItem =new SimpleStringProperty();
    }
    public StringProperty getClientSelectedMenuItem(){
        return clientSelectedMenuItem;
    }
    // src/main/java/com/example/wealthwise/Views/ViewFactory.java

    public AnchorPane getDashboardView(){
        if (dashboardView == null) {
            try {
                dashboardView = new FXMLLoader(
                        getClass().getResource("/Fxml/Client/Dashboard.fxml")
                ).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public AnchorPane getTransactionView() {
        if (transactionView == null) {
            try {
                transactionView = new FXMLLoader(
                        getClass().getResource("/Fxml/Client/Transactions.fxml")
                ).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return transactionView;
    }

    public AnchorPane getInvestmentView() {
        if (investmentView == null) {
            try {
                investmentView = new FXMLLoader(
                        getClass().getResource("/Fxml/Client/Investment.fxml")
                ).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return investmentView;
    }
    public AnchorPane getAccountView(){
        if (accountView == null) {
            try {
                accountView = new FXMLLoader(
                        getClass().getResource("/Fxml/Client/accounts.fxml")
                ).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return accountView;
    }
    public AnchorPane getProfileView(){
        if(profileView==null){
            try{
                profileView=new FXMLLoader(
                        getClass().getResource("/Fxml/Client/profile.fxml")
                ).load();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return profileView;
    }
    public AnchorPane getReportView(){
        if(reportView==null){
            try{
                reportView=new FXMLLoader(
                        getClass().getResource("/Fxml/Client/report.fxml")
                ).load();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return reportView;
    }



    public void showloginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }
    public void showClientWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        createStage(loader);
    }


    private void createStage(FXMLLoader loader){
        Scene scene = null ;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("WealthWise");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();

    }

}
