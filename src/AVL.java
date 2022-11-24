import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;

public class AVL implements TreeInterface {
    private BaseNode<String> root_;
    /***
     * Using methods map
     */
    private Map<String, Consumer<Vector<String>>> methodsWithNoReturns = new HashMap<>();
    private final int ALLOWED_IMBALANCE = 1;
    private Vector<String> logOutput = new Vector<>();
    private boolean isRotatedLeft_ = false;
    private boolean isRotatedRight_ = false;
    private String toBeRemovedElement;
    public AVL(BaseNode<String> root )
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
        root_ = addNodeRecursive( element, root_ );
    }

    /***
     * removeNode uses recursive remove node function to remove.
     * @param element
     */
    @Override
    public void removeNode(String element)
    {
        toBeRemovedElement = element;
        root_ = removeNodeRecursive( element, root_ );
    }
    /**
     * Recursive method to insert into a subtree. Implemented based on PS.
     * @param element the item to insert.
     * @param parent the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BaseNode<String> addNodeRecursive( String element, BaseNode<String> parent)
    {
        if( parent == null )
            return new BaseNode<String>( element, null, null );

        int compareResult = element.compareTo(parent.getElement());
        if(compareResult < 0){
            logOutput.add(parent.getElement() + ": New Node being added with IP:" + element); // Create ADDNODE Log
            parent.setLeftNode(addNodeRecursive( element, parent.getLeftNode()));
        }
        else if(compareResult > 0){
            logOutput.add(parent.getElement() + ": New Node being added with IP:" + element); //Create ADDNODE Log
            parent.setRightNode(addNodeRecursive( element, parent.getRightNode()));
        }
        return balance(parent);
    }
    /**
     * Recursive method to remove a node from a subtree. Implemented based on PS.
     * @param element the item to insert.
     * @param parent the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BaseNode<String> removeNodeRecursive( String element, BaseNode<String> parent )
    {
        if( parent == null )
            return parent;
        logRemovedElementWithLeafInfo(parent, element); //creating logs for remove operations
        int compareResult = element.compareTo(parent.getElement());
        if( compareResult < 0 )
            parent.setLeftNode(removeNodeRecursive(element, parent.getLeftNode()));
        else if( compareResult > 0 )
            parent.setRightNode(removeNodeRecursive(element, parent.getRightNode()));
        else if( parent.getLeftNode() != null && parent.getRightNode() != null )
        {
            parent.setElement(findMin(parent.getRightNode()).getElement());
            parent.setRightNode(removeNodeRecursive( parent.getElement(), parent.getRightNode()));
        }
        else
            parent = ( parent.getLeftNode() != null ) ? parent.getLeftNode() : parent.getRightNode();
        return balance( parent);
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
     * Create Logfile to output path.
     * @param outputPath
     * @throws IOException
     */
    @Override
    public void printLogsToTerminal(String outputPath) throws IOException{
        FileWriter writer = new FileWriter(outputPath + "_avl.txt");
        for(var log : logOutput){
            writer.write(log + "\n");
        }
        writer.close();
    }

    /***
     * Returns minimum of tree. Method uses while. Recursive function also can be used.
     * @param node
     * @return BaseNode<String>
     */
    private BaseNode<String> findMin( BaseNode<String> node )
    {
        if( node == null )
            return node;

        while( node.getLeftNode() != null )
            node = node.getLeftNode();
        return node;
    }

    /***
     * Balance Function isRemoved
     * @param parent
     * @return BaseNode<String>
     */
    private BaseNode<String> balance( BaseNode<String> parent)
    {
        if( parent == null )
            return parent;
        if( height( parent.getLeftNode() ) - height( parent.getRightNode()) > ALLOWED_IMBALANCE )
            if( height( parent.getLeftNode().getLeftNode() ) >= height( parent.getLeftNode().getRightNode() ) )
                parent = rotateWithLeftChild( parent);
            else
                parent = doubleWithLeftChild( parent );
        else
        if( height( parent.getRightNode() ) - height( parent.getLeftNode() ) > ALLOWED_IMBALANCE )
            if( height( parent.getRightNode().getRightNode() ) >= height( parent.getRightNode().getLeftNode() ) )
                parent = rotateWithRightChild( parent );
            else
                parent = doubleWithRightChild( parent );
        setLogOfBalance();
        parent.setHeight(Math.max( height( parent.getLeftNode() ), height( parent.getRightNode() ) ) + 1);
        return parent;
    }
    private boolean isrightleft_ = false;
    private boolean isleftright_ = false;
    /***
     * Creates Log of the balance.
     * Gets rotation information and set them to default false.
     */
    private void setLogOfBalance(){
        if(isRotatedLeft_ == true && isRotatedRight_ == false){
            logOutput.add("Rebalancing: right rotation");

        }
        else if(isRotatedRight_ == true && isRotatedLeft_ == false){
            logOutput.add("Rebalancing: left rotation");

        }
        else if(isrightleft_ == true){
            logOutput.add("Rebalancing: left-right rotation");
        }
        else if(isleftright_ == true){
            logOutput.add("Rebalancing: right-left rotation");
        }
        isRotatedLeft_ = false;
        isleftright_ = false;
        isrightleft_ = false;
        isRotatedRight_ = false;
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
        if(!element.equals(root_.getElement())){
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
     * Rotate with the left child function.
     * This is based on PS.
     * @param k2
     * @return BaseNode<String>
     */
    private BaseNode<String> rotateWithLeftChild( BaseNode<String> k2 )
    {
        isRotatedLeft_ = true;
        BaseNode<String> k1 = k2.getLeftNode();
        k2.setLeftNode(k1.getRightNode());
        k1.setRightNode(k2);
        k2.setHeight(Math.max(height(k2.getLeftNode()), height(k2.getRightNode()) ) + 1);
        k1.setHeight(Math.max(height(k1.getLeftNode()), k2.getHeight()) + 1);
        return k1;
    }
    /***
     * Returns height of the node.
     * This is based on PS.
     * @param node
     * @return int height of the node
     */
    private int height( BaseNode<String> node)
    {
        return node == null ? -1 : node.getHeight();
    }
    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private BaseNode<String> rotateWithRightChild( BaseNode<String> k1 )
    {
        isRotatedRight_ = true;
        BaseNode<String> k2 = k1.getRightNode();
        k1.setRightNode(k2.getLeftNode());
        k2.setLeftNode(k1);
        k1.setHeight(Math.max( height( k1.getLeftNode() ), height( k1.getRightNode() ) ) + 1);
        k2.setHeight(Math.max( height( k2.getRightNode() ), k1.getHeight() + 1));
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private BaseNode<String> doubleWithLeftChild( BaseNode<String> k3 )
    {
        isrightleft_ = true;
        k3.setLeftNode(rotateWithRightChild(k3.getLeftNode()));
        return rotateWithLeftChild(k3);
    }
    private BaseNode<String> doubleWithRightChild( BaseNode<String> k1 )
    {
        isleftright_ = true;
        k1.setRightNode(rotateWithLeftChild(k1.getRightNode()));
        return rotateWithRightChild(k1);
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

    /***
     * Create Remove Element Logs with leaf information.
     * @param 
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
}