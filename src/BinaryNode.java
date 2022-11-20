import java.util.Vector;

public class BinaryNode<String> {
    private String element_;
    private BinaryNode<String> left_;
    private BinaryNode<String> right_;
    private Vector<java.lang.String> logOutput;
    /***
     * Constructor used in main.
     * @param theElement
     */
    public BinaryNode( String theElement ) {
        this( theElement, null, null );
    }

    /***
     * Constructor of BinaryNode
     * @param element
     * @param left
     * @param right
     */
    BinaryNode( String element, BinaryNode<String> left, BinaryNode<String> right ) {
        element_  = element;
        left_     = left;
        right_    = right;
    }
    /***
     * getElement method
     * @return element
     */
    public String getElement(){
        return element_;
    }
    /***
     * getLeftNode method
     * @return leftNode
     */
    public BinaryNode<String> getLeftNode(){
        return left_;
    }
    /***
     * getRightNode method
     * @return rightNode
     */
    public BinaryNode<String> getRightNode(){
        return right_;
    }
    /***
     * setElement
     * @param element
     */
    public void setElement(String element){
        element_ = element;
    }
    /***
     * setLeftNode
     * @param left
     */
    public void setLeftNode(BinaryNode<String> left){
        left_ = left;
    }
    /***
     * setRightNode
     * @param right
     */
    public void setRightNode(BinaryNode<String> right){
        right_ = right;
    }
}
