package com.example.appcipher;

import Cipher.Alphabet.Alphabet;
import Cipher.Alphabet.IAlphabet;
import Cipher.Alphabet.Language;
import Cipher.Cipher.Cipher;
import Cipher.Cipher.TrithemiusCipher;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        GridPane gridPane = createRegistrationFormPane();
        createUIControls(gridPane);

        Scene scene = new Scene(gridPane, 800, 420);
        stage.setMaxHeight(420);
        stage.setMaxWidth(800);

        stage.setTitle("Cipher");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
    private GridPane createRegistrationFormPane() {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        ColumnConstraints columnOneConstraints = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        ColumnConstraints columnTwoConstrains = new ColumnConstraints(150,150, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }
    private void createUIControls(GridPane gridPane){
        //Шапка
        Label nameCipher = new Label("Шифр Тритемиуса");
        nameCipher.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        gridPane.add(nameCipher, 0,0, 2, 1);
        GridPane.setHalignment(nameCipher, HPos.CENTER);
        //
        Label text = new Label("Введите текст: ");
        gridPane.add(text, 0, 1);
        GridPane.setHalignment(text, HPos.LEFT);

        TextField textField = new TextField();
        gridPane.add(textField, 1, 1);
        //Линия 2
        ToggleGroup groupLanguage = new ToggleGroup();

        RadioButton englishLanguage = new RadioButton("Английский");
        gridPane.add(englishLanguage, 0,2);
        englishLanguage.setToggleGroup(groupLanguage);
        englishLanguage.setSelected(true);

        RadioButton russianLanguage = new RadioButton("Русский");
        gridPane.add(russianLanguage, 1, 2);
        russianLanguage.setToggleGroup(groupLanguage);
        //Линия 3
        ToggleGroup groupCrypt = new ToggleGroup();

        RadioButton buttonEncryption = new RadioButton("Шифрование");
        gridPane.add(buttonEncryption, 0, 3);
        buttonEncryption.setToggleGroup(groupCrypt);
        buttonEncryption.setSelected(true);

        RadioButton buttonDecryption = new RadioButton("Дешифрование");
        gridPane.add(buttonDecryption, 1, 3);
        buttonDecryption.setToggleGroup(groupCrypt);
        //
        Label resultText = new Label("Результат");
        gridPane.add(resultText, 0, 4);
        GridPane.setHalignment(resultText, HPos.LEFT);

        TextField textResult = new TextField();
        gridPane.add(textResult, 1, 4);
        textResult.setEditable(false);
        //
        Button crypt = new Button("Начать операцию");
        crypt.setDefaultButton(false);
        gridPane.add(crypt, 0, 5, 2, 1);
        GridPane.setHalignment(crypt, HPos.CENTER);

        crypt.setOnAction(actionEvent -> {
            String txt = textField.getText();
            String result = "";

            if (txt.equals("")) {
                textResult.setText("");
                return;
            }

            RadioButton language = (RadioButton) groupLanguage.getSelectedToggle();
            RadioButton operation = (RadioButton) groupCrypt.getSelectedToggle();

            txt = txt.replace("ё","ѐ");
            txt = txt.replace("Ё","ѐ");

            IAlphabet alphabet = null;
            if (language.equals(englishLanguage))
                alphabet = new Alphabet(Language.ENGLISH);
            if (language.equals(russianLanguage))
                alphabet = new Alphabet(Language.RUSSIAN);

            Cipher cipher = new TrithemiusCipher(txt, alphabet);
            if (operation.equals(buttonEncryption))
                result = cipher.encryption();
            if (operation.equals(buttonDecryption))
                result = cipher.decryption();

            textResult.setText(result);
        });
    }
}