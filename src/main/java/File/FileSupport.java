package File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileSupport {
    private File inputFile;
    private File outputFile;
    private final Stage stage;

    public FileSupport(Stage stage){
        this.stage = stage;

        inputFile = null;
        outputFile = null;
    }

    public void chooseInputFile(){
        inputFile = getFile(stage);
    }
    public void chooseOutputFile(){
        outputFile = getFile(stage);
    }

    public String getInputFileName(){
        return inputFile.getName();
    }
    public String getOutputFileName(Stage stage){
        return outputFile.getName();
    }

    public boolean isInputFileEmpty(){
        return (inputFile == null);
    }
    public boolean isOutputFileEmpty(){
        return (outputFile == null);
    }

    public String outputText(){
        if (outputFile == null)
            return null;
        try {
            FileReader fr = new FileReader(outputFile);
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
    public void inputText(String text){
        if (inputFile == null)
            return;

        if (outputFile == null){
            inputFile = new File(createCopyFile(inputFile));
        }

        try (PrintWriter out = new PrintWriter(inputFile, StandardCharsets.UTF_8)) {
            out.print(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(Stage javaFXC){
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
}
