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

    public void exitButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    public void homeButtonOnAction(ActionEvent e) { //home button action

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

    public void addIncomeOnAction(ActionEvent e) {
        String incomeAmountText = incomeText.getText();
        if (!incomeAmountText.isEmpty()) {
            // Remove the dollar sign if it exists
            if (incomeAmountText.startsWith("$")) {
                incomeAmountText = incomeAmountText.substring(1);
            }

            double incomeAmount = Double.parseDouble(incomeAmountText);

            // Add the income amount to the ListView and incomes list
            listIncome.getItems().add(incomeAmount);
            incomes.add(incomeAmount);

            // Update the budget
            updateBudgetValue();
            incomeText.setText("");
        }
    }

    public void removeIncomeOnAction(ActionEvent e) {
        int selectedIndex = listIncome.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            double removedIncome = listIncome.getItems().remove(selectedIndex);
            incomes.remove(removedIncome); // Remove the income from the incomes list
            // Update the budget
            budget -= removedIncome;
            updateBudgetValue();
        }
    }
    private void updatePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        ObservableMap<String, Double> spendingMap = FXCollections.observableHashMap();

        for (String item : listSpending.getItems()) {
            String[] parts = item.split(" - ");
            String type = parts[1];
            double amount = Double.parseDouble(parts[0]);

            if (spendingMap.containsKey(type)) {
                spendingMap.put(type, spendingMap.get(type) + amount);
            } else {
                spendingMap.put(type, amount);
            }
        }

        for (String type : spendingMap.keySet()) {
            pieChartData.add(new PieChart.Data(type, spendingMap.get(type)));
        }

        spendingChart.setData(pieChartData);
    }

    public void addSpendingOnAction(ActionEvent e) {
        String spendingAmountText = spendingText.getText();
        String spendingType = spendingChoiceBox.getValue();

        if (!spendingAmountText.isEmpty() && spendingType != null) {
            // Remove the dollar sign if it exists
            if (spendingAmountText.startsWith("$")) {
                spendingAmountText = spendingAmountText.substring(1);
            }

            double spendingAmount = Double.parseDouble(spendingAmountText);

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

    public void removeSpendingOnAction(ActionEvent e) {
        int selectedIndex = listSpending.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedSpending = listSpending.getItems().get(selectedIndex);
            String[] parts = selectedSpending.split(" - ");
            double removedSpending = Double.parseDouble(parts[0]);
            listSpending.getItems().remove(selectedIndex);
            spendings.remove(removedSpending);
            budget += removedSpending; // Adding back the removed spending to update the budget
            updateBudgetValue();
            updatePieChart(); // Update the pie chart
        }
    }

    private ObservableList<Double> incomes = FXCollections.observableArrayList();
    private ObservableList<Double> spendings = FXCollections.observableArrayList();

    private void updateBudgetValue() {
        double totalIncome = incomes.stream().mapToDouble(Double::doubleValue).sum();
        double totalSpending = spendings.stream().mapToDouble(Double::doubleValue).sum();
        budget = totalIncome - totalSpending;

        String budgetText = "$" + String.valueOf(budget);
        budgetValue.setText(budgetText);
        if (budget < 0.0) {
            budgetValue.setStyle("-fx-text-fill: red;");
        } else {
            budgetValue.setStyle(""); // Reset the style
        }
    }

    public void initialize() {
        // Set the date label to the current date
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);
        setDate.setText(formattedDate);

        String budgetText = "$" + String.valueOf(budget);
        budgetValue.setText(budgetText);
        if (budget < 0.0) {
            budgetValue.setStyle("-fx-text-fill: red;");
        }

        spendingChoiceBox.getItems().addAll(spending);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Food", Math.random() * 1000),
                new PieChart.Data("Transportation", Math.random() * 1000),
                new PieChart.Data("Shopping", Math.random() * 1000),
                new PieChart.Data("Entertainment", Math.random() * 1000),
                new PieChart.Data("Expenses", Math.random() * 1000)
        );
        spendingChart.setData(pieChartData);
    }



}