import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class InputDeserializer {

    /***
     * Takes inputFile as input and read all lines in it. During reading, it sets an array [] and holds inputs as String[] in vector
     * @param filePath
     */
    public InputDeserializer(String filePath){
        inputs = new Vector<String[]>();
        List<String> listOfOperations = Collections.emptyList();
        try{
            listOfOperations = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        }
        catch (IOException ex){
            System.out.println("Something went wrong while reading the file.");
        }
        for(var operation : listOfOperations){
            this.inputs.add(operation.split("\\s"));
        }
    }

    /***
     * gets the inputs which are constructed.
     * @return vector of the inputs.
     */
    public Vector<String[]> getInputs(){
        return inputs;
    }
    private Vector<String[]> inputs;


}