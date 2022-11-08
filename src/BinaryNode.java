import java.util.Vector;

public class BinaryNode<String> {
    private String element_;
    private BinaryNode<String> left_;
    private BinaryNode<String> right_;
    private Vector<java.lang.String> logOutput;

    BinaryNode( String theElement ) {
        this( theElement, null, null );
    }

    BinaryNode( String element, BinaryNode<String> left, BinaryNode<String> right ) {
        element_  = element;
        left_     = left;
        right_    = right;
    }
    public String getElement(){
        return element_;
    }
    public BinaryNode<String> getLeftNode(){
        return left_;
    }
    public BinaryNode<String> getRightNode(){
        return right_;
    }
    public void setElement(String element){
        element_ = element;
    }
    public void setLeftNode(BinaryNode<String> left){
        left_ = left;
    }
    public void setRightNode(BinaryNode<String> right){
        right_ = right;
    }
}
