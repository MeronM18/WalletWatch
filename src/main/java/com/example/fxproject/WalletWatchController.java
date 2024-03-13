package com.example.fxproject;

import com.almasb.fxgl.entity.action.Action;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static javafx.stage.StageStyle.UNDECORATED;

public class WalletWatchController {
    @FXML
    private Button exitButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Label setDate;
    @FXML
    private Label budgetValue;
    @FXML
    private PieChart spendingChart;
    @FXML
    private Button removeIncome;
    @FXML
    private Button addIncome;
    @FXML
    private ListView<Double> listIncome;
    @FXML
    private TextField incomeText;
    @FXML
    private TextField spendingText;
    @FXML
    private ChoiceBox<String> spendingChoiceBox;
    private String[] spending = {"Food", "Transportation", "Shopping", "Entertainment", "Expenses"};
    @FXML
    private Button addSpending;
    @FXML
    private Button removeSpending;
    @FXML
    private ListView<String> listSpending;
    @FXML
    private Label choiceBoxText;

    public void exitButtonOnAction(ActionEvent e) { //closes the window when the exit button is clicked
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    public void homeButtonOnAction(ActionEvent e) { //home button action
        //no action because the page is already on home
    }

    public void logoutButtonOnAction(ActionEvent e) { //logout button action
        Stage stage = (Stage) logoutButton.getScene().getWindow();

        // Close the current window
        stage.close();

        try {
            // Open the original login window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = fxmlLoader.load();
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED); // Set the stage style before showing it
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private double budget = 0.0; // Initialize budget to 0

    public void addIncomeOnAction(ActionEvent e) { //add income method
        String incomeAmountText = incomeText.getText(); //gets input from income text box
        if (!incomeAmountText.isEmpty()) {
            // Remove the dollar sign if it exists
            if (incomeAmountText.startsWith("$")) {
                incomeAmountText = incomeAmountText.substring(1);
            }

            double incomeAmount = Double.parseDouble(incomeAmountText); //ensures that the value is double

            // Add the income amount to the ListView and incomes list
            listIncome.getItems().add(incomeAmount);
            incomes.add(incomeAmount);

            // Update the budget
            updateBudgetValue();
            incomeText.setText("");
        }
    }

    public void removeIncomeOnAction(ActionEvent e) { //remove income method
        int selectedIndex = listIncome.getSelectionModel().getSelectedIndex(); //sets the index selected to a integer
        if (selectedIndex != -1) {
            double removedIncome = listIncome.getItems().remove(selectedIndex);
            incomes.remove(removedIncome); // Remove the income from the incomes list
            // Update the budget
            budget -= removedIncome;
            updateBudgetValue(); //calls method
        }
    }
    private void updatePieChart() { //pie chart method
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        ObservableMap<String, Double> spendingMap = FXCollections.observableHashMap();

        for (String item : listSpending.getItems()) { //loops through the string of choicebox options
            String[] parts = item.split(" - "); //splits the spending into two parts
            String type = parts[1]; //first part is the type of spending
            double amount = Double.parseDouble(parts[0]); //second part is the amount of spending

            if (spendingMap.containsKey(type)) { //checks if the type of spending is already listed
                spendingMap.put(type, spendingMap.get(type) + amount); //if so it will update the already existing sector on the chart
            } else {
                spendingMap.put(type, amount); //if not found, create a new sector on pie chart
            }
        }

        for (String type : spendingMap.keySet()) {
            pieChartData.add(new PieChart.Data(type, spendingMap.get(type))); //creates a new piechart data object
        }

        spendingChart.setData(pieChartData); //sets the data of pie chart on scenebuilder
    }

    public void addSpendingOnAction(ActionEvent e) { //add spending method
        String spendingAmountText = spendingText.getText(); //gets amount
        String spendingType = spendingChoiceBox.getValue(); //gets type

        if (!spendingAmountText.isEmpty() && spendingType != null) {
            // Remove the dollar sign if it exists
            if (spendingAmountText.startsWith("$")) {
                spendingAmountText = spendingAmountText.substring(1);
            }

            double spendingAmount = Double.parseDouble(spendingAmountText); //spending amount converts to double

            // Add the spending amount and type to the ListView and spendings list
            listSpending.getItems().add(spendingAmount + " - " + spendingType);
            spendings.add(spendingAmount);

            // Update the budget and pie chart
            updateBudgetValue();
            updatePieChart();

            spendingText.setText("");
            spendingChoiceBox.setValue(null); // Reset the choice box
        }
    }

    public void removeSpendingOnAction(ActionEvent e) { //remove spending method
        int selectedIndex = listSpending.getSelectionModel().getSelectedIndex(); //index selected is matched with an integer
        if (selectedIndex != -1) {
            String selectedSpending = listSpending.getItems().get(selectedIndex); //gets item from that index
            String[] parts = selectedSpending.split(" - "); //splits into two parts
            double removedSpending = Double.parseDouble(parts[0]); //removes amount
            listSpending.getItems().remove(selectedIndex);
            spendings.remove(removedSpending); //removes type of spending
            budget += removedSpending; // Adding back the removed spending to update the budget
            updateBudgetValue();
            updatePieChart(); // Update the pie chart
        }
    }

    private ObservableList<Double> incomes = FXCollections.observableArrayList();
    private ObservableList<Double> spendings = FXCollections.observableArrayList();

    private void updateBudgetValue() {
        double totalIncome = incomes.stream().mapToDouble(Double::doubleValue).sum(); //gets all income values
        double totalSpending = spendings.stream().mapToDouble(Double::doubleValue).sum(); //gets all spending values
        budget = totalIncome - totalSpending; //calculate budget

        String budgetText = "$" + String.valueOf(budget); //adds dollar sign before budget
        budgetValue.setText(budgetText); //sets the budget text to calculated budget value
        if (budget < 0.0) {
            budgetValue.setStyle("-fx-text-fill: red;"); //if budget is negative, font color will be red
        } else {
            budgetValue.setStyle(""); // Reset the style
        }
    }

    public void initialize() {
        // Set the date label to the current date
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //portray the date
        String formattedDate = currentDate.format(formatter);
        setDate.setText(formattedDate); //sets the date label to current date

        String budgetText = "$" + String.valueOf(budget);
        budgetValue.setText(budgetText);
        if (budget < 0.0) {
            budgetValue.setStyle("-fx-text-fill: red;");
        }

        spendingChoiceBox.getItems().addAll(spending);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList( //pie chart logic
                new PieChart.Data("Food", Math.random() * 1000),
                new PieChart.Data("Transportation", Math.random() * 1000),
                new PieChart.Data("Shopping", Math.random() * 1000),
                new PieChart.Data("Entertainment", Math.random() * 1000),
                new PieChart.Data("Expenses", Math.random() * 1000)
        );
        spendingChart.setData(pieChartData);
    }

}