import org.w3c.dom.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;

public class BST implements TreeInterface {

    public String toBeRemovedElement;
    private BaseNode<String> root_ = null;
    /***
     * Using methods map
     */
    private Map<String, Consumer<Vector<String>>> methodsWithNoReturns = new HashMap<>();
    private Vector<String> logOutput = new Vector<>();
    public BST(BaseNode<String> root)
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
        Vector<BaseNode<String>> pathToSender = new Vector<>();
        Vector<BaseNode<String>> pathToReceiver = new Vector<>();
        Vector<BaseNode<String>> merged = new Vector<>();
        //merged.addAll(Collections.reverse(pathToSender));
        findMessageSender(fromElement, lowestCommonAncestor(fromElement, toElement), pathToSender);
        findMessageReceiver(fromElement ,toElement, lowestCommonAncestor(fromElement, toElement), pathToReceiver);
        if(pathToReceiver.size() > 1){
            var tomerge = pathToReceiver.subList(1, pathToReceiver.size());
            Collections.reverse(tomerge);
            merged.addAll(tomerge);
        }
        if(pathToSender.size() > 0){
            //Collections.reverse(pathToSender);
            merged.addAll(pathToSender);
        }
        int size = merged.size() - 1;
        while(size >= 0){
            if(size == merged.size() - 1){
                if(!merged.get(size).getElement().equals(toElement) && !merged.get(size).getElement().equals(fromElement)){
                logOutput.add(merged.get(size).getElement() + ": Transmission from: " + fromElement + " receiver: " + toElement +" sender:" + fromElement);
                }
            }
            else{
                if(!merged.get(size).getElement().equals(toElement) && !merged.get(size).getElement().equals(fromElement)){
                    logOutput.add(merged.get(size).getElement() + ": Transmission from: " + merged.get(size + 1).getElement() + " receiver: " + toElement +" sender:" + fromElement);
                }
}
            size--;
        }
        logOutput.add(toElement + ": Received message from: " + fromElement);
    }
    /***
     * removeNode uses recursive remove node function to remove.
     * @param element
     */
    @Override
    public void removeNode(String element)
    {
        toBeRemovedElement = element;
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
    private BaseNode<String> addNodeRecursive(String element, BaseNode<String> parent)
    {
        if( parent == null )
            return new BaseNode<>( element, null, null );

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
    private BaseNode<String> removeNodeRecursive(String element, BaseNode<String> parent)
    {
        if( parent == null )
            return parent;

        logRemovedElementWithLeafInfo(parent, element);
        int compareResult = element.compareTo(parent.getElement());
        if( compareResult < 0 ){
            parent.setLeftNode(removeNodeRecursive(element, parent.getLeftNode()));
        }
        else if( compareResult > 0 ){
            parent.setRightNode(removeNodeRecursive(element, parent.getRightNode()));
        }
        else {

            if (parent.getLeftNode() == null) {
                return parent.getRightNode();
            } else if (parent.getRightNode() == null)
                return parent.getLeftNode();

            parent.setElement(inOrderSuccessor(parent.getRightNode()));
            parent.setRightNode(removeNodeRecursive(parent.getElement(),parent.getRightNode()));
        }
        return parent;
    }
    private String inOrderSuccessor(BaseNode<String> root) {
        String minimum = root.getElement();
        while (root.getLeftNode() != null) {
            minimum = root.getLeftNode().getElement();
            root = root.getLeftNode();
        }
        return minimum;
    }

    public BaseNode<String> lowestCommonAncestor(String fromElement, String toElement){
        BaseNode<String> node = root_;
        while (node != null) {
            int compareResultfromElement = node.getElement().compareTo(toElement);
            int compareResultToElement = node.getElement().compareTo(fromElement);
            if (compareResultfromElement > 0 && compareResultToElement > 0 ) {
                node = node.getLeftNode();
            } else if (compareResultfromElement < 0 && compareResultToElement < 0) {
                node = node.getRightNode();
            } else return node;
        }
        return null;
    }
    /***
     * Create logs with leaf information for the deleted node.
     * @param binaryNode
     * @param element
     */
    private void logRemovedElementWithLeafInfo(BaseNode<String> binaryNode, String element)
    {
        if(!element.equals(toBeRemovedElement)){
            return;
        }
        BaseNode<String> toBeRemovedElement;
        if(checkIsLeft(binaryNode, element)){
            toBeRemovedElement = binaryNode.getLeftNode();
            CreateLog(binaryNode, toBeRemovedElement);
        }
        else if(checkIsRight(binaryNode, element)){
            toBeRemovedElement = binaryNode.getRightNode();
            CreateLog(binaryNode, toBeRemovedElement);
        }
    }

    private void CreateLog(BaseNode<String> binaryNode, BaseNode<String> toBeRemovedElement) {
        var nodeType = returnNodeType(toBeRemovedElement);
        switch (nodeType){
            case None :
                break;
            case SingleChildNode:
                logOutput.add(binaryNode.getElement() + ": Node with single child Deleted: " + toBeRemovedElement.getElement() );
                break;
            case NonLeafNode:
               if(toBeRemovedElement.getRightNode().getLeftNode() != null){
                   logOutput.add(binaryNode.getElement() + ": Non Leaf Node Deleted; removed: "+ toBeRemovedElement.getElement()  +" replaced: "+ toBeRemovedElement.getRightNode().getLeftNode().getElement());
               }
               else{
                   logOutput.add(binaryNode.getElement() + ": Non Leaf Node Deleted; removed: "+ toBeRemovedElement.getElement()  +" replaced: "+ toBeRemovedElement.getRightNode().getElement());
               }
               break;
           case LeafNode:
                logOutput.add(binaryNode.getElement() + ": Leaf Node Deleted: " + toBeRemovedElement.getElement() );
                break;
            }
    }

    private boolean checkIsLeft(BaseNode<String> node, String element){
        if(node.getLeftNode() != null && node.getLeftNode().getElement().equals(element)){
            return true;
        }
        return false;
    }
    private boolean checkIsRight(BaseNode<String> node, String element){
        if(node.getRightNode() != null && node.getRightNode().getElement().equals(element)){
            return true;
        }
        return false;
    }
    private NodeType returnNodeType(BaseNode<String> node){
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
     * Finding sender from root recursively.
     * Adds the path to the vector pathToSender
     * @param element
     * @param parent
     * @param pathToSender
     * @return BaseNode<String>
     */
    private BaseNode<String> findMessageSender( String element, BaseNode<String> parent, Vector<BaseNode<String>> pathToSender)
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
     * @return BaseNode<String>
     */
    private BaseNode<String> findMessageReceiver(String fromElement,String element, BaseNode<String> parent, Vector<BaseNode<String>> pathToReceiver)
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