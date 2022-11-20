import java.io.IOException;
public class Main {

    public static void main(String[] args) throws IOException {
        //Deserialize inputs.
        var inputDeserializer = new InputDeserializer(args[0]);
        var inputs = inputDeserializer.getInputs();
        //Create BST with root.
        var BST = new BST(new BinaryNode<String>(inputs.get(0)[0]));
        //MakeOperations with BST
        for(int i = 1; i < inputs.size() ; i++){
            BST.MakeOperation(inputs.get(i));
        }
        //Create Output File BST.
        BST.printLogsToTerminal(args[1]);

        //Create AVL Tree with root.
        var AVL = new AVL(new AVLNode<String>(inputs.get(0)[0]));
        //MakeOperations with AVL
        for(int i = 1; i < inputs.size() ; i++){
            AVL.MakeOperation(inputs.get(i));
        }
        //Create Output File AVL.
        AVL.printLogsToTerminal(args[1]);
    }

}