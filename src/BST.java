import org.w3c.dom.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;

public class BST implements TreeInterface {

    private enum NodeType{
        LeafNode,
        NonLeafNode,
        SingleChildNode,
        None
    }
    private BinaryNode<String> root_ = null;
    /***
     * Using methods map
     */
    private Map<String, Consumer<Vector<String>>> methodsWithNoReturns = new HashMap<>();
    private Vector<String> logOutput = new Vector<>();
    public BST(BinaryNode<String> root)
    {
        root_ = root;
        methodsWithNoReturns.put("ADDNODE", (k) -> this.addNode(k.get(0)));
        methodsWithNoReturns.put("DELETE", (k) -> this.removeNode(k.get(0)));
        methodsWithNoReturns.put("SEND", (k) -> this.sendMessage(k.get(0), k.get(1)));
    }
    /***
     * addNode uses recursive add node function to add.
     * @param element
     */
    @Override
    public void addNode(String element)
    {
        root_ = addNodeRecursive(element, root_);
    }
    /***
     * Send Message Function which uses recursivity to create
     * Paths with different methods from sender to reciever.
     * @param fromElement
     * @param toElement
     */
    @Override
    public void sendMessage(String fromElement, String toElement) {
        logOutput.add(fromElement+ ": Sending message to: " + toElement);
        Vector<BinaryNode<String>> pathToSender = new Vector<>();
        Vector<BinaryNode<String>> pathToReceiver = new Vector<>();
        findMessageSender(fromElement, root_, pathToSender);
        findMessageReceiver(fromElement ,toElement, root_, pathToReceiver);
        for(int i = pathToSender.size() - 1 ; i >= 0 ; i--){
            if(i == pathToSender.size() - 1){
                logOutput.add(pathToSender.get(i).getElement() + ": Transmission from: " + fromElement +" receiver: "+ toElement + " sender:" + fromElement);
            }
            if(i - 1 >= 0){
                logOutput.add(pathToSender.get(i - 1).getElement() + ": Transmission from: " + pathToSender.get(i).getElement()  +" receiver: "+ toElement + " sender:" + fromElement);
            }
       }
        for (int i = 0; i < pathToReceiver.size() ; i++) {
            if(i+1 != pathToReceiver.size()){
                logOutput.add(pathToReceiver.get(i + 1).getElement() + ": Transmission from: " + pathToReceiver.get(i).getElement() + " receiver: " + toElement   + " sender:" +fromElement );
            }
       }
        logOutput.add(toElement + ": Received message from: " +fromElement);
    }

    /***
     * removeNode uses recursive remove node function to remove.
     * @param element
     */
    @Override
    public void removeNode(String element)
    {
        root_ = removeNodeRecursive(element, root_);
    }

    /***
     * Create Logfile to output path.
     * @param outputPath
     * @throws IOException
     */
    @Override
    public void printLogsToTerminal(String outputPath) throws IOException
    {
        FileWriter writer = new FileWriter(outputPath + "_bst.txt");
        for(var log : logOutput){
            writer.write(log + "\n");
        }
        writer.close();
    }

    /**
     * Recursive method to insert into a subtree. Implemented based on PS.
     * @param element the item to insert.
     * @param parent the node that roots the subtree.
     * @return the new root of the subtree.
     */
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

    /**
     * Recursive method to remove a node from a subtree. Implemented based on PS.
     * @param element the item to delete.
     * @param parent the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<String> removeNodeRecursive(String element, BinaryNode<String> parent)
    {
        if( parent == null )
            return parent;
        int compareResult = element.compareTo(parent.getElement());
        if( compareResult < 0 ){
            logRemovedElementWithLeafInfo(parent, element);
            parent.setLeftNode(removeNodeRecursive(element, parent.getLeftNode()));
        }
        else if( compareResult > 0 ){
            logRemovedElementWithLeafInfo(parent, element);
            parent.setRightNode(removeNodeRecursive(element, parent.getRightNode()));
        }
        else if( parent.getLeftNode() != null && parent.getRightNode() != null )
        {
            parent.setElement(findMinRecursive(parent.getRightNode()).getElement());
            parent.setRightNode(removeNodeRecursive(parent.getElement(), parent.getRightNode()));
        }
        else
            parent = ( parent.getLeftNode() != null ) ? parent.getLeftNode() : parent.getRightNode();
        return parent;
    }
    private boolean IsLogCreated = false;
    /***
     * Create logs with leaf information for the deleted node.
     * @param binaryNode
     * @param element
     */
    private void logRemovedElementWithLeafInfo(BinaryNode<String> binaryNode, String element)
    {
        BinaryNode<String> toBeRemovedElement;
        if(checkIsLeft(binaryNode, element)){
            toBeRemovedElement = binaryNode.getLeftNode();
            IsLogCreated = CreateLog(binaryNode, toBeRemovedElement);
        }
        else if(checkIsRight(binaryNode, element)){
            toBeRemovedElement = binaryNode.getRightNode();
            IsLogCreated = CreateLog(binaryNode, toBeRemovedElement);
        }
//        if(binaryNode.getLeftNode() != null && binaryNode.getLeftNode().getElement().equals(element)){
//            toBeRemovedElement = binaryNode.getLeftNode();
//           if(toBeRemovedElement.getLeftNode() == null && toBeRemovedElement.getRightNode() == null ){
//               logOutput.add(binaryNode.getElement() + ": Leaf Node Deleted: " + element);
//           }
//           else if(toBeRemovedElement.getLeftNode() != null && toBeRemovedElement.getRightNode() == null){
//               logOutput.add(binaryNode.getElement() + ": Node with single child Deleted: " + element);
//           }
//           else if(toBeRemovedElement.getLeftNode() == null && toBeRemovedElement.getRightNode() != null){
//               logOutput.add(binaryNode.getElement() + ": Node with single child Deleted: " + element);
//           }
//           else{
//               if(toBeRemovedElement.getRightNode().getLeftNode() != null){
//                   logOutput.add(binaryNode.getElement() + ": Non Leaf Node Deleted; removed: "+ element +" replaced: "+ toBeRemovedElement.getRightNode().getLeftNode().getElement());
//               }
//               else{
//                   logOutput.add(binaryNode.getElement() + ": Non Leaf Node Deleted; removed: "+ element +" replaced: "+ toBeRemovedElement.getRightNode().getElement());
//               }
//           }
//        }
//        else if(binaryNode.getRightNode() != null && binaryNode.getRightNode().getElement().equals(element)){
//            toBeRemovedElement = binaryNode.getRightNode();
//            if(toBeRemovedElement.getLeftNode() == null && toBeRemovedElement.getRightNode() == null ){
//                logOutput.add(binaryNode.getElement() + ": Leaf Node Deleted: " + element);
//            }
//            else if(toBeRemovedElement.getLeftNode() != null && toBeRemovedElement.getRightNode() == null){
//                logOutput.add(binaryNode.getElement() + ": Node with single child Deleted: " + element);
//            }
//            else if(toBeRemovedElement.getLeftNode() == null && toBeRemovedElement.getRightNode() != null){
//                logOutput.add(binaryNode.getElement() + ": Node with single child Deleted: " + element);
//            }
//            else{
//                if(toBeRemovedElement.getRightNode().getLeftNode() != null){
//                    logOutput.add(binaryNode.getElement() + ": Non Leaf Node Deleted; removed: "+ element +" replaced: "+ toBeRemovedElement.getRightNode().getLeftNode().getElement());
//                }
//                else{
//                    logOutput.add(binaryNode.getElement() + ": Non Leaf Node Deleted; removed: "+ element +" replaced: "+ toBeRemovedElement.getRightNode().getElement());
//                }
//            }
//        }
    }

    private boolean CreateLog(BinaryNode<String> binaryNode, BinaryNode<String> toBeRemovedElement) {
        var nodeType = returnNodeType(toBeRemovedElement);
        switch (nodeType){
            case None :
                return false;
            case LeafNode:
                logOutput.add(binaryNode.getElement() + ": Node with single child Deleted: " + toBeRemovedElement.getElement() );
                return true;
            case NonLeafNode:
               if(toBeRemovedElement.getRightNode().getLeftNode() != null){
                   logOutput.add(binaryNode.getElement() + ": Non Leaf Node Deleted; removed: "+ toBeRemovedElement.getElement()  +" replaced: "+ toBeRemovedElement.getRightNode().getLeftNode().getElement());
               }
               else{
                   logOutput.add(binaryNode.getElement() + ": Non Leaf Node Deleted; removed: "+ toBeRemovedElement.getElement()  +" replaced: "+ toBeRemovedElement.getRightNode().getElement());
               }
               return true;
           }
           return false;
    }

    private boolean checkIsLeft(BinaryNode<String> node, String element){
        if(node.getLeftNode() != null && node.getLeftNode().getElement().equals(element)){
            return true;
        }
        return false;
    }
    private boolean checkIsRight(BinaryNode<String> node, String element){
        if(node.getRightNode() != null && node.getRightNode().getElement().equals(element)){
            return true;
        }
        return false;
    }
    private NodeType returnNodeType(BinaryNode<String> node){
        if(node.getLeftNode() == null && node.getRightNode() == null){
            return NodeType.LeafNode;
        }
        else if(node.getLeftNode() != null && node.getRightNode() == null){
            return NodeType.SingleChildNode;
        }
        else if(node.getLeftNode() == null && node.getRightNode() != null){
            return NodeType.SingleChildNode;
        }
        else if(node.getLeftNode() != null && node.getRightNode() != null){
            return NodeType.NonLeafNode;
        }
        else{
            return NodeType.None;
        }
    }
    /***
     * Returns minimum of tree.
     * @param parent
     * @return AVLNode<String>
     */
    private BinaryNode<String> findMinRecursive( BinaryNode<String> parent)
    {
        if( parent == null )
            return null;
        else if( parent.getLeftNode() == null )
            return parent;
        return findMinRecursive(parent.getLeftNode());
    }
    /***
     * Finding sender from root recursively.
     * Adds the path to the vector pathToSender
     * @param element
     * @param parent
     * @param pathToSender
     * @return AVLNode<String>
     */
    private BinaryNode<String> findMessageSender( String element, BinaryNode<String> parent, Vector<BinaryNode<String>> pathToSender)
    {
        if( parent == null)
            return null;
        if(parent.getElement().equals(element))
            return parent;
        else
            if(!element.equals(parent.getElement())){
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
    /***
     * Finding reciever from root recursively.
     * Adds the path to the vector pathToReciever
     * @param fromElement
     * @param parent
     * @param pathToReceiver
     * @return AVLNode<String>
     */
    private BinaryNode<String> findMessageReceiver(String fromElement,String element, BinaryNode<String> parent, Vector<BinaryNode<String>> pathToReceiver)
    {
        if( parent == null)
            return null;
        if(parent.getElement().equals(element))
            return parent;
        else
            pathToReceiver.add(parent);
            int compareResult = element.compareTo(parent.getElement());
            if( compareResult < 0 )
                parent = parent.getLeftNode();
            else if( compareResult > 0 )
                parent = parent.getRightNode();
            findMessageReceiver(fromElement, element ,parent, pathToReceiver);
        return null;
    }

    /***
     * Make Operations in the map based on inputs.
     * @param input
     */
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