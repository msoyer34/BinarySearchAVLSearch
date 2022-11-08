import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;

class BST implements TreeInterface {

    private BinaryNode<String> root_ = null;
    private Map<String, Consumer<Vector<String>>> methodsWithNoReturns = new HashMap<>();
    private Vector<String> logOutput = new Vector<>();
    public BST(BinaryNode<String> root)
    {
        root_ = root;
        methodsWithNoReturns.put("ADDNODE", (k) -> this.addNode(k.get(0)));
        methodsWithNoReturns.put("DELETE", (k) -> this.removeNode(k.get(0)));
        methodsWithNoReturns.put("SEND", (k) -> this.sendMessage(k.get(0), k.get(1)));
    }
    @Override
    public void addNode(String element)
    {
        root_ = addNodeRecursive(element, root_);
    }
    @Override
    public void sendMessage(String fromElement, String toElement) {
        logOutput.add(fromElement+ ": Sending message to: " + toElement);
        Vector<BinaryNode<String>> pathToSender = new Vector<>();
        Vector<BinaryNode<String>> pathToReceiver = new Vector<>();
        findMessageSender(fromElement, root_, pathToSender);
        findMessageReceiver(fromElement ,toElement, root_, pathToReceiver);
        for(int i = pathToSender.size() - 1; i >= 0 ; i--){
            if(i-1 < pathToReceiver.size()){
                logOutput.add(pathToSender.get(i).getElement() + ": Transmission from: " + toElement +" receiver: "+ toElement + " sender:" + fromElement);
            }
            else{
                logOutput.add(pathToSender.get(i).getElement() + ": Transmission from: " + pathToSender.get(i-1).getElement() +" receiver: "+ toElement + " sender:" + fromElement);
            }
               }
        for (int i = 0; i < pathToReceiver.size(); i++) {
            if(pathToReceiver.size() < i+1){
                logOutput.add(pathToReceiver.get(i).getElement() + ": Transmission from: " + pathToReceiver.get(i + 1).getElement() + " receiver: " + toElement   + " sender:" +fromElement );
            }
            else{
                logOutput.add(pathToReceiver.get(i).getElement() + ": Transmission from: " + toElement + " receiver: " + toElement   + " sender:" +fromElement );
            }
        }
        logOutput.add(toElement + ": Received message from: " +fromElement);
    }
    @Override
    public void removeNode(String element)
    {
        root_ = removeNodeRecursive(element, root_);
    }
    @Override
    public void printLogsToTerminal() throws IOException
    {
        FileWriter writer = new FileWriter("output.txt");
        for(var log : logOutput){
            writer.write(log + "\n");
        }
        writer.close();
    }
    private BinaryNode<String> addNodeRecursive(String element, BinaryNode<String> parent)
    {
        if( parent == null )
            return new BinaryNode<>( element, null, null );

        int compareResult = element.compareTo(parent.getElement());

        if( compareResult < 0 ){
            logOutput.add(parent.getElement() + ": New Node being added with IP:" + element);
            parent.setLeftNode(addNodeRecursive(element, parent.getLeftNode()));
        }
        else if( compareResult > 0 )
        {
            logOutput.add(parent.getElement() + ": New Node being added with IP:" + element);
            parent.setRightNode(addNodeRecursive(element, parent.getRightNode()));
        }
        return parent;
    }

    private BinaryNode<String> removeNodeRecursive(String element, BinaryNode<String> parent)
    {
        if( parent == null )
            return parent;
        int compareResult = element.compareTo(parent.getElement());
        logRemovedElementWithLeafInfo(parent, element);
        if( compareResult < 0 )
            parent.setLeftNode(removeNodeRecursive(element, parent.getLeftNode()));
        else if( compareResult > 0 )
            parent.setRightNode(removeNodeRecursive( element, parent.getRightNode()));
        else if( parent.getLeftNode() != null && parent.getRightNode() != null )
        {
            parent.setElement(findMinRecursive(parent.getRightNode()).getElement());
            parent.setRightNode(removeNodeRecursive(parent.getElement(), parent.getRightNode()));
        }
        else
            parent = ( parent.getLeftNode() != null ) ? parent.getLeftNode() : parent.getRightNode();
        return parent;
    }
    private void logRemovedElementWithLeafInfo(BinaryNode<String> binaryNode, String element)
    {
        BinaryNode<String> toBeRemovedElement;
        if(binaryNode.getLeftNode() != null && binaryNode.getLeftNode().getElement().equals(element)){
            toBeRemovedElement = binaryNode.getLeftNode();
           if(toBeRemovedElement.getLeftNode() == null && toBeRemovedElement.getRightNode() == null ){
               logOutput.add(binaryNode.getElement() + ": Leaf Node Deleted: " + element);
           }
           else if(toBeRemovedElement.getLeftNode() == null || toBeRemovedElement.getRightNode() == null){
               logOutput.add(binaryNode.getElement() + ": Node with single child Deleted: " + element);
           }
        }
        else if(binaryNode.getRightNode() != null && binaryNode.getRightNode().getElement().equals(element)){
            toBeRemovedElement = binaryNode.getRightNode();
            if(toBeRemovedElement.getLeftNode() == null && toBeRemovedElement.getRightNode() == null ){
                logOutput.add(binaryNode.getElement() + ": Leaf Node Deleted: " + element);
            }
            else if(toBeRemovedElement.getLeftNode() == null || toBeRemovedElement.getRightNode() == null){
                logOutput.add(binaryNode.getElement() + ": Node with single child Deleted: " + element);
            }
            else{
                logOutput.add(binaryNode.getElement() + ": Non Leaf Node Deleted; removed: "+ element +" replaced: "+ toBeRemovedElement.getRightNode().getElement());
            }
        }
    }
    private BinaryNode<String> findMinRecursive( BinaryNode<String> parent)
    {
        if( parent == null )
            return null;
        else if( parent.getLeftNode() == null )
            return parent;
        return findMinRecursive(parent.getLeftNode());
    }
    private BinaryNode<String> findMessageSender( String element, BinaryNode<String> parent, Vector<BinaryNode<String>> pathToSender)
    {
        if( parent == null)
            return null;
        if(parent.getElement().equals(element))
            return parent;
        else
            if(!(element.equals(root_.getElement()))) {
                pathToSender.add(parent);
            }
            int compareResult = element.compareTo(parent.getElement());
            if( compareResult < 0 )
                parent = parent.getLeftNode();
            else if( compareResult > 0 )
                parent = parent.getRightNode();
            findMessageSender(element ,parent, pathToSender);
            return null;
    }
    private BinaryNode<String> findMessageReceiver(String fromElement,String element, BinaryNode<String> parent, Vector<BinaryNode<String>> pathToReceiver)
    {
        if( parent == null)
            return null;
        if(parent.getElement().equals(element))
            return parent;
        else
            if(!(fromElement.equals(root_.getElement()))){
                pathToReceiver.add(parent);
            }
            int compareResult = element.compareTo(parent.getElement());
            if( compareResult < 0 )
                parent = parent.getLeftNode();
            else if( compareResult > 0 )
                parent = parent.getRightNode();
            findMessageReceiver(fromElement, element ,parent, pathToReceiver);
        return null;
    }

    public void MakeOperation(String[] input)
    {
        Vector<String> listOfParameters = new Vector<String>();
        for(int i = 1; i < input.length; i++){
            listOfParameters.add(input[i]);
        }
        if(methodsWithNoReturns.containsKey(input[0])){
            methodsWithNoReturns.get(input[0]).accept(listOfParameters);
        }
    }

}