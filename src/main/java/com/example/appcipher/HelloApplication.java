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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class HelloApplication extends Application {
    private Stage javaFXC;
    private String inputFilePath;
    private String outputFilePath;

    private Label nameCipher;
    private Label text;
    private TextField textField;
    private Label publicKey;
    private TextField publicKeyText;
    private Label signature;
    private TextField signatureText;
    private ToggleGroup groupLanguage;
    private RadioButton englishLanguage;
    private RadioButton russianLanguage;
    private ToggleGroup groupCrypt;
    private RadioButton buttonEncryption;
    private RadioButton buttonDecryption;
    private CheckBox checkOutputFile;
    private Button openOutputFile;
    private CheckBox checkInputFile;
    private Button openInputFile;
    private CheckBox integrityCheck;
    private Label resultIntegrity;
    private TextField resultIntegrityText;
    private Label resultCrypto;
    private TextField resultCryptoText;
    private Button btnResult;

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) {
        GridPane gridPane = createRegistrationFormPane();
        createUIControls(gridPane);

        Scene scene = new Scene(gridPane, 800, 420);
        stage.setResizable(false);

        stage.setTitle("Cipher");
        stage.setScene(scene);
        stage.show();

        inputFilePath = Objects.requireNonNull(getClass().getResource("input.txt")).getPath();
        outputFilePath = Objects.requireNonNull(getClass().getResource("output.txt")).getPath();
        javaFXC = stage;
    }

    private GridPane createRegistrationFormPane() {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(50, 60, 50, 60));

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
        int numberLines = 0;
        //Шапка
        nameCipher = new Label("Шифр Тритемиуса");
        nameCipher.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        gridPane.add(nameCipher, 0,numberLines++, 2, 1);
        GridPane.setHalignment(nameCipher, HPos.CENTER);
        //
        text = new Label("Введите текст: ");
        gridPane.add(text, 0, numberLines);
        GridPane.setHalignment(text, HPos.LEFT);

        textField = new TextField();
        gridPane.add(textField, 1, numberLines++);
        //
        publicKey = new Label("Public Key");
        gridPane.add(publicKey, 0, numberLines);

        publicKeyText = new TextField();
        gridPane.add(publicKeyText, 1, numberLines++);
        //
        signature = new Label("SiqNature");
        gridPane.add(signature, 0, numberLines);

        signatureText = new TextField();
        gridPane.add(signatureText, 1, numberLines++);
        //Линия 2
        groupLanguage = new ToggleGroup();

        englishLanguage = new RadioButton("Английский");
        gridPane.add(englishLanguage, 0,numberLines);
        englishLanguage.setToggleGroup(groupLanguage);
        englishLanguage.setSelected(true);

        russianLanguage = new RadioButton("Русский");
        gridPane.add(russianLanguage, 1, numberLines++);
        russianLanguage.setToggleGroup(groupLanguage);
        //Линия 3
        groupCrypt = new ToggleGroup();

        buttonEncryption = new RadioButton("Шифрование");
        gridPane.add(buttonEncryption, 0, numberLines);
        buttonEncryption.setToggleGroup(groupCrypt);
        buttonEncryption.setSelected(true);

        buttonDecryption = new RadioButton("Дешифрование");
        gridPane.add(buttonDecryption, 1, numberLines++);
        buttonDecryption.setToggleGroup(groupCrypt);
        //File
        checkOutputFile = new CheckBox("Из файла");
        gridPane.add(checkOutputFile, 0,numberLines);

        openOutputFile = new Button("Выбрать файл");
        openOutputFile.setTooltip(new Tooltip("Выберите файл из которого\nнадо достать текст"));
        gridPane.add(openOutputFile, 1,numberLines);

        checkInputFile = new CheckBox("В файл");
        gridPane.add(checkInputFile, 2,numberLines);

        openInputFile = new Button("В файл");
        openInputFile.setTooltip(new Tooltip("Выберите файл в который\nнадо записать результат операции"));
        gridPane.add(openInputFile, 3, numberLines++);
        //
        integrityCheck = new CheckBox("Проверка целостности");
        integrityCheck.setTooltip(new Tooltip("Проверять ли на целостность?"));
        gridPane.add(integrityCheck, 0, numberLines++, 2, 1);
        GridPane.setHalignment(integrityCheck, HPos.CENTER);
        //
        resultIntegrity = new Label("Результат целостности: ");
        gridPane.add(resultIntegrity, 0, numberLines);

        resultIntegrityText = new TextField();
        gridPane.add(resultIntegrityText, 1, numberLines++);
        //
        resultCrypto = new Label("Результат");
        gridPane.add(resultCrypto, 0, numberLines);
        GridPane.setHalignment(resultCrypto, HPos.LEFT);

        resultCryptoText = new TextField();
        gridPane.add(resultCryptoText, 1, numberLines++);
        resultCryptoText.setEditable(false);
        //
        btnResult = new Button("Начать операцию");
        btnResult.setDefaultButton(false);
        gridPane.add(btnResult, 0, numberLines++, 2, 1);
        GridPane.setHalignment(btnResult, HPos.CENTER);
    }

    private void controller(){
        openOutputFile.setOnAction(actionEvent -> {
            if (checkOutputFile.isSelected()) {
                File myChoosenFile = getFile();
                if (myChoosenFile != null) {
                    String path = myChoosenFile.getPath();

                    textField.setText(myChoosenFile.getName());
                    inputFilePath = path;
                    outputFilePath = path;
                }
            }
        });
        openInputFile.setOnAction(actionEvent -> {
            if (checkInputFile.isSelected()){
                File myChoosenFile = getFile();
                if (myChoosenFile != null)
                    outputFilePath = myChoosenFile.getPath();
            }
        });
        btnResult.setOnAction(actionEvent -> {
            String txt = textField.getText();
            String result = "";

            if (checkOutputFile.isSelected()){
                try {
                    txt = outputText(inputFilePath);
                }catch (Exception e){
                    e.printStackTrace();
                    return;
                }
            }
            if (txt.equals("")) {
                resultCryptoText.setText("");
                return;
            }

            RadioButton language = (RadioButton) groupLanguage.getSelectedToggle();
            RadioButton operation = (RadioButton) groupCrypt.getSelectedToggle();

            if (txt.contains("ё") || txt.contains("Ё")) {
                txt = txt.replace("ё", "ѐ");
                txt = txt.replace("Ё", "ѐ");
            }

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

            resultCryptoText.setText(result);

            if (checkInputFile.isSelected()){
                try {
                    inputText(outputFilePath, result);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private File getFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбрать текст");

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Текст", "*.txt");

        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(javaFXC);

        if (file != null)
            System.out.println("File choose path: " + file.getPath());

        return file;
    }
    private String createCopyFile(File myFile){
        String oldNameFile = myFile.getName();
        String newNameFile = myFile.getName().replace(".txt","");
        if (newNameFile.contains("(")){
            int idx_open = newNameFile.indexOf("(");
            int idx_clos = newNameFile.lastIndexOf(")");
            int countCopy = Integer.parseInt(newNameFile.substring(idx_open+1,idx_clos));
            newNameFile = newNameFile.substring(0, idx_open+1) + (countCopy+1) + newNameFile.substring(idx_clos);
        }
        else {
            newNameFile += "(1)";
        }
        newNameFile += ".txt";

        File file = new File(myFile.getPath().replace(oldNameFile, newNameFile));
        try {
            if (file.createNewFile()){
                System.out.println("Created new file! Path: " + file.getPath());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return file.getPath();
    }

    private void inputText(String path, String text){
        if (path.isEmpty())
            path = outputFilePath;

        File file = new File(path);
        if (inputFilePath.equals(outputFilePath)){
            file = new File(createCopyFile(file));
        }

        try (PrintWriter out = new PrintWriter(file, StandardCharsets.UTF_8)) {
            out.print(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String outputText(String path){
        if (path.isEmpty())
            path = inputFilePath;

        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            String line = reader.readLine();
            StringBuilder text = new StringBuilder();

            while (line != null) {
                text.append(line).append("\n");
                line = reader.readLine();
            }
            return text.toString().strip();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    // ----------------------------------------------------------------
    // Bounty Castle methods

    public static byte[] GenerateSignature(String plaintext, PrivateKey keys)
            throws SignatureException, UnsupportedEncodingException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchProviderException
    {
        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
        ecdsaSign.initSign(keys);
        ecdsaSign.update(plaintext.getBytes("UTF-8"));
        return ecdsaSign.sign();
    }

    public static boolean ValidateSignature(String plaintext, PublicKey pair, byte[] signature) throws SignatureException,
            InvalidKeyException, UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchProviderException
    {
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "BC");
        ecdsaVerify.initVerify(pair);
        ecdsaVerify.update(plaintext.getBytes("UTF-8"));
        return ecdsaVerify.verify(signature);
    }

    public static KeyPair GenerateKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException
    {
        //  Other named curves can be found in http://www.bouncycastle.org/wiki/display/JA1/Supported+Curves+%28ECDSA+and+ECGOST%29
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("B-571");
        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");
        g.initialize(ecSpec, new SecureRandom());
        return g.generateKeyPair();
    }
    // My methods

    private static String getStringFromPublicKey(KeyPair key){
        return Base64.getEncoder().encodeToString(key.getPublic().getEncoded());
    }
    private static String getStringFromPublicKey(PublicKey key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private static PublicKey getPublicKeyFromSting(String stringPublicKey)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException
    {
        byte[] bytesPublicKey = Base64.getDecoder().decode(stringPublicKey);

        KeyFactory factory = KeyFactory.getInstance("ECDSA", "BC");
        return (ECPublicKey) factory.generatePublic(new X509EncodedKeySpec(bytesPublicKey));
    }

    private static String bytearrayToString(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static byte[] stringToByteArray(String string){
        return Base64.getDecoder().decode(string);
    }
}