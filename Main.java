import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene accountCreationScene;
    private Scene accountListScene;
    private Scene accountDetailsScene;
    private List<Account> accounts = new ArrayList<>();
    private ListView<String> accountListView;
    private Label accountDetailsLabel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("JDoodle Banking App");

        // Create a main menu scene
        VBox mainMenuLayout = new VBox(10);
        Button createAccountButton = new Button("Create Account");
        Button listAccountsButton = new Button("List Accounts");
        createAccountButton.setOnAction(e -> switchToAccountCreationScene());
        listAccountsButton.setOnAction(e -> switchToAccountListScene());
        mainMenuLayout.getChildren().addAll(createAccountButton, listAccountsButton);
        mainMenuScene = new Scene(mainMenuLayout, 300, 250);

        // Load the CSS file to apply styling
        mainMenuScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Create the account creation scene
        VBox accountCreationLayout = new VBox(10);
        Label accountCreationLabel = new Label("Account Creation");
        TextField accountNameField = new TextField();
        ToggleGroup accountTypeToggleGroup = new ToggleGroup();
        RadioButton savingsRadioButton = new RadioButton("Savings Account");
savingsRadioButton.setUserData("Savings");  // Set user data to "Savings" for the savingsRadioButton

        RadioButton checkingRadioButton = new RadioButton("Checking Account");
checkingRadioButton.setUserData("Checking");  // Set user data to "Checking" for the checkingRadioButton
        savingsRadioButton.setToggleGroup(accountTypeToggleGroup);
        checkingRadioButton.setToggleGroup(accountTypeToggleGroup);
        Button createAccountSubmitButton = new Button("Create Account");
        createAccountSubmitButton.setOnAction(e -> createAccount(accountNameField.getText(), accountTypeToggleGroup.getSelectedToggle()));
        accountCreationLayout.getChildren().addAll(accountCreationLabel, accountNameField, savingsRadioButton, checkingRadioButton, createAccountSubmitButton);
        accountCreationScene = new Scene(accountCreationLayout, 300, 250);

        // Create the account list scene
        VBox accountListLayout = new VBox(10);
        Label accountListLabel = new Label("Account List");
        accountListView = new ListView<>();
        accountListView.setOnMouseClicked(e -> showAccountDetails());
        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(e -> switchToMainMenuScene());
        accountListLayout.getChildren().addAll(accountListLabel, accountListView, backButton);
        accountListScene = new Scene(accountListLayout, 300, 250);

        // Create the account details scene
        VBox accountDetailsLayout = new VBox(10);
        accountDetailsLabel = new Label();
        Button backToListButton = new Button("Back to Account List");
        backToListButton.setOnAction(e -> switchToAccountListScene());
        accountDetailsLayout.getChildren().addAll(accountDetailsLabel, backToListButton);
        accountDetailsScene = new Scene(accountDetailsLayout, 300, 250);

        // Set the initial scene
        primaryStage.setScene(mainMenuScene);

        primaryStage.show();
    }

    private void switchToAccountCreationScene() {
        primaryStage.setScene(accountCreationScene);
    }

    private void switchToAccountListScene() {
        updateAccountListView();
        primaryStage.setScene(accountListScene);
    }

    private void switchToMainMenuScene() {
        primaryStage.setScene(mainMenuScene);
    }

    private void showAccountDetails() {
        String selectedAccount = accountListView.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            String accountName = selectedAccount.split(" \\(")[0];
            Account account = findAccountByName(accountName);
            if (account != null) {
                accountDetailsLabel.setText("Account Details\nName: " + account.getName() + "\nType: " + account.getType() + "\nBalance: $" + account.getBalance());
                primaryStage.setScene(accountDetailsScene);
            }
        }
    }

    private void createAccount(String accountName, Toggle selectedToggle) {
        if (selectedToggle == null) {
            System.out.println("Please select an account type.");
            return;
        }

        Account account;
        if (selectedToggle.getUserData().equals("Savings")) {
            account = new SavingsAccount(accountName);
        } else {
            account = new CheckingAccount(accountName);
        }

        accounts.add(account);
        System.out.println("Account created with name: " + accountName + " (" + selectedToggle.getUserData() + ")");
        switchToAccountListScene();
    }

    private void updateAccountListView() {
        List<String> accountNames = new ArrayList<>();
        for (Account account : accounts) {
            accountNames.add(account.getName() + " (" + account.getType() + ")");
        }
        accountListView.getItems().setAll(accountNames);
    }

    private Account findAccountByName(String name) {
        for (Account account : accounts) {
            if (account.getName().equals(name)) {
                return account;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Account {
    private String name;
    private double balance;

    public Account(String name) {
        this.name = name;
        this.balance = 0.0;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return "Account";
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds.");
        }
    }
}

class SavingsAccount extends Account {
    public SavingsAccount(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "Savings Account";
    }
}

class CheckingAccount extends Account {
    public CheckingAccount(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "Checking Account";
    }
}

