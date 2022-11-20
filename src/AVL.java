import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;

public class AVL implements TreeInterface {
    private AVLNode<String> root_;
    /***
     * Using methods map
     */
    private Map<String, Consumer<Vector<String>>> methodsWithNoReturns = new HashMap<>();
    private final int ALLOWED_IMBALANCE = 1;
    private Vector<String> logOutput = new Vector<>();
    private boolean isRotatedLeft_ = false;
    private boolean isRotatedRight_ = false;
    public AVL(AVLNode<String> root )
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
        root_ = removeNodeRecursive( element, root_ );
    }
    /**
     * Recursive method to insert into a subtree. Implemented based on PS.
     * @param element the item to insert.
     * @param parent the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AVLNode<String> addNodeRecursive( String element, AVLNode<String> parent)
    {
        if( parent == null )
            return new AVLNode<>( element, null, null );

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
    private AVLNode<String> removeNodeRecursive( String element, AVLNode<String> parent )
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
    public void sendMessage(String fromElement, String toElement)  {
        logOutput.add(fromElement+ ": Sending message to: " + toElement);
        Vector<AVLNode<String>> pathToSender = new Vector<>();
        Vector<AVLNode<String>> pathToReceiver = new Vector<>();
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
     * @return AVLNode<String>
     */
    private AVLNode<String> findMin( AVLNode<String> node )
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
     * @return AVLNode<String>
     */
    private AVLNode<String> balance( AVLNode<String> parent)
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
        else if(isRotatedRight_ == true && isRotatedLeft_ == true){
            logOutput.add("Rebalancing: right-left rotation");
        }
        isRotatedLeft_ = false;
        isRotatedRight_ = false;
    }

    /***
     * Finding sender from root recursively.
     * Adds the path to the vector pathToSender
     * @param element
     * @param parent
     * @param pathToSender
     * @return AVLNode<String>
     */
    private AVLNode<String> findMessageSender( String element, AVLNode<String> parent, Vector<AVLNode<String>> pathToSender)
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
     * @return AVLNode<String>
     */
    private AVLNode<String> findMessageReceiver(String fromElement,String element, AVLNode<String> parent, Vector<AVLNode<String>> pathToReceiver)
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
     * @return AVLNode<String>
     */
    private AVLNode<String> rotateWithLeftChild( AVLNode<String> k2 )
    {
        isRotatedLeft_ = true;
        AVLNode<String> k1 = k2.getLeftNode();
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
    private int height( AVLNode<String> node)
    {
        return node == null ? -1 : node.getHeight();
    }
    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AVLNode<String> rotateWithRightChild( AVLNode<String> k1 )
    {
        isRotatedRight_ = true;
        AVLNode<String> k2 = k1.getRightNode();
        k1.setRightNode(k2.getLeftNode());
        k2.setLeftNode(k1);
        k1.setHeight(Math.max( height( k1.getLeftNode() ), height( k1.getRightNode() ) ) + 1);
        k2.setHeight(Math.max( height( k2.getRightNode() ), k1.getHeight() ) + 1);
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AVLNode<String> doubleWithLeftChild( AVLNode<String> k3 )
    {
        k3.setLeftNode(rotateWithRightChild(k3.getLeftNode()));
        return rotateWithLeftChild(k3);
    }
    private AVLNode<String> doubleWithRightChild( AVLNode<String> k1 )
    {
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
     * @param AvlNode
     * @param element
     */
    private void logRemovedElementWithLeafInfo(AVLNode<String> AvlNode, String element)
    {
        AVLNode<String> toBeRemovedElement;
        if(AvlNode.getLeftNode() != null && AvlNode.getLeftNode().getElement().equals(element)){
            toBeRemovedElement = AvlNode.getLeftNode();
            if(toBeRemovedElement.getLeftNode() == null && toBeRemovedElement.getRightNode() == null ){
                logOutput.add(AvlNode.getElement() + ": Leaf Node Deleted: " + element);
                return;
            }
            else if(toBeRemovedElement.getLeftNode() == null || toBeRemovedElement.getRightNode() == null){
                logOutput.add(AvlNode.getElement() + ": Node with single child Deleted: " + element);
                return;
            }
            else{
                if(toBeRemovedElement.getRightNode().getLeftNode() != null){
                    logOutput.add(AvlNode.getElement() + ": Non Leaf Node Deleted; removed: "+ element +" replaced: "+ toBeRemovedElement.getRightNode().getLeftNode().getElement());
                    return;
                }
                else{
                    logOutput.add(AvlNode.getElement() + ": Non Leaf Node Deleted; removed: "+ element +" replaced: "+ toBeRemovedElement.getRightNode().getElement());
                    return;
                }
            }
        }
        else if(AvlNode.getRightNode() != null && AvlNode.getRightNode().getElement().equals(element)){
            toBeRemovedElement = AvlNode.getRightNode();
            if(toBeRemovedElement.getLeftNode() == null && toBeRemovedElement.getRightNode() == null ){
                logOutput.add(AvlNode.getElement() + ": Leaf Node Deleted: " + element);
                return;
            }
            else if(toBeRemovedElement.getLeftNode() == null || toBeRemovedElement.getRightNode() == null){
                logOutput.add(AvlNode.getElement() + ": Node with single child Deleted: " + element);
                return;
            }
            else{
                if(toBeRemovedElement.getRightNode().getLeftNode() != null){
                    logOutput.add(AvlNode.getElement() + ": Non Leaf Node Deleted; removed: "+ element +" replaced: "+ toBeRemovedElement.getRightNode().getLeftNode().getElement());
                    return;
                }
                else{
                    logOutput.add(AvlNode.getElement() + ": Non Leaf Node Deleted; removed: "+ element +" replaced: "+ toBeRemovedElement.getRightNode().getElement());
                    return;
                }
       }
        }
    }
}