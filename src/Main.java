import java.io.IOException;
import java.util.HashMap;



public class Main {

    public static void main(String[] args) throws IOException {
        var inputDeserializer = new InputDeserializer(args[0]);
        var inputs = inputDeserializer.getInputs();
        var BST = new BST(new BinaryNode(inputs.get(0)[0]));
        for(int i = 1; i < inputs.size() ; i++){
            BST.MakeOperation(inputs.get(i));
        }
        BST.printLogsToTerminal();




//HashMap<String, TreeInterface> hashMap = new HashMap<>();


//        hashMap.put("BST", new BST());
//        hashMap.put("AVL", new AVL());
//
//        hashMap.get(args[0]).addNode();
//        hashMap.get(args[0]).removeNode();
//        hashMap.get(args[0]).sendMessage();
//        hashMap.get(args[0]).addNode();
//        hashMap.get(args[0]).removeNode();
//
//        hashMap.get(args[0]).printLogsToTerminal();
    }

}