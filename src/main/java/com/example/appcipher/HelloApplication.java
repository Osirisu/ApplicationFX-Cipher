package com.example.appcipher;

import Cipher.Alphabet.Alphabet;
import Cipher.Alphabet.IAlphabet;
import Cipher.Alphabet.Language;
import Cipher.Cipher.Cipher;
import Cipher.Cipher.TrithemiusCipher;
import File.FileSupport;
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

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class HelloApplication extends Application {

    private FileSupport fileSupport;

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
        Security.addProvider(new BouncyCastleProvider());

        GridPane gridPane = createRegistrationFormPane();
        createUIControls(gridPane);
        controller();

        Scene scene = new Scene(gridPane, 800, 420);
        stage.setResizable(false);

        stage.setTitle("Cipher");
        stage.setScene(scene);
        stage.show();

        fileSupport = new FileSupport(stage);
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
                fileSupport.chooseOutputFile();

                if (!fileSupport.isOutputFileEmpty())
                    textField.setText(fileSupport.getOutputFileName());
                else
                    System.err.println("No output file");
            }
        });
        openInputFile.setOnAction(actionEvent -> {
            if (checkInputFile.isSelected()){
                fileSupport.chooseInputFile();

                if (fileSupport.isInputFileEmpty())
                    System.err.println("No input file");
            }
        });
        btnResult.setOnAction(actionEvent -> {
            String txtToCrypto = getTextToCrypto();
            if (txtToCrypto == null) return;

            String result = getResultCipher(txtToCrypto, getAlphabet());
            resultCryptoText.setText(result);
            if (checkInputFile.isSelected()) insertText(result);

            try {
                String integrityTxt = isOperationEncryption() ? txtToCrypto : result;
                integrityCheck(integrityTxt);
            } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException |
                     UnsupportedEncodingException | SignatureException | InvalidKeyException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getTextToCrypto(){
        String txt = textField.getText();
        if (checkOutputFile.isSelected()){
            try {
                if (!fileSupport.isOutputFileEmpty()) txt = fileSupport.outputText();
                else System.err.println("Output file is empty");
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        if (txt.isEmpty()) {
            resultCryptoText.setText("");
            System.err.println("Text is empty");
            return null;
        }

        txt = txt.contains("ё") ? txt.replace("ё", "ѐ") : txt;
        txt = txt.contains("Ё") ? txt.replace("Ё", "ѐ") : txt;
        return txt;
    }
    private void insertText(String result){
        try {
            if (!fileSupport.isInputFileEmpty()) fileSupport.inputText(result);
            else System.err.println("Input file is empty");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getResultCipher(String cipherText, IAlphabet alphabet){
        String result = "";

        Cipher cipher = new TrithemiusCipher(cipherText, alphabet);
        if (isOperationEncryption()) result = cipher.encryption();
        else result = cipher.decryption();

        return result;
    }
    private IAlphabet getAlphabet(){
        RadioButton language = (RadioButton) groupLanguage.getSelectedToggle();
        IAlphabet alphabet = null;
        if (language.equals(englishLanguage))
            alphabet = new Alphabet(Language.ENGLISH);
        if (language.equals(russianLanguage))
            alphabet = new Alphabet(Language.RUSSIAN);

        return alphabet;
    }
    private boolean isOperationEncryption(){
        RadioButton operation = (RadioButton) groupCrypt.getSelectedToggle();
        return operation.equals(buttonEncryption);
    }

    private void integrityCheck(final String plaintext)
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException,
            UnsupportedEncodingException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        if (!isIntegrityCheck()) {
            return;
        }

        String publicKeyString = "";
        String signatureString = "";
        if (isOperationEncryption()) {
            final KeyPair keys = GenerateKeys();
            final byte[] signature = GenerateSignature(plaintext, keys.getPrivate());

            publicKeyString = getStringFromPublicKey(keys.getPublic());
            signatureString = bytearrayToString(signature);

            publicKeyText.setText(publicKeyString);
            signatureText.setText(signatureString);
        }
        else {
            publicKeyString = publicKeyText.getText();
            signatureString = signatureText.getText();

            final PublicKey public_key = getPublicKeyFromSting(publicKeyString);
            final byte[] newSignature = stringToByteArray(signatureString);

            boolean isValidated = ValidateSignature(plaintext, public_key, newSignature);
            resultIntegrityText.setText(isValidated ? "true" : "false");
        }
    }
    private boolean isIntegrityCheck(){
        return integrityCheck.isSelected();
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
        KeyPairGenerator keys = KeyPairGenerator.getInstance("ECDSA", "BC");
        keys.initialize(ecSpec, new SecureRandom());
        return keys.generateKeyPair();
    }
    // My methods Bounty Castle

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