import java.util.Vector;

public class AVLNode<String> {
    private String element_;
    private AVLNode<String> left_;
    private AVLNode<String> right_;

    private int height_;

    /***
     * Constructor used in main.
     * @param theElement
     */
    public AVLNode( String theElement ) {
        this( theElement, null, null );

    }

    /***
     * Constructor of AVLNode
     * @param element
     * @param left
     * @param right
     * height is default 0
     */
    AVLNode( String element, AVLNode<String> left, AVLNode<String> right ) {
        element_  = element;
        left_     = left;
        right_    = right;
        height_ = 0;
    }

    /***
     * getElement method
     * @return String element
     */
    public String getElement(){
        return element_;
    }
    /***
     * getLeftNode method
     * @return leftNode
     */
    public AVLNode<String> getLeftNode(){
        return left_;
    }
    /***
     * getRightNode method
     * @return rightNode
     */
    public AVLNode<String> getRightNode(){
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
    public void setLeftNode(AVLNode<String> left){
        left_ = left;
    }

    /***
     * setRightNode
     * @param right
     */
    public void setRightNode(AVLNode<String> right){
        right_ = right;
    }

    /***
     * getHeight
     * @return int height
     */
    public int getHeight(){
        return height_;
    }

    /***
     * setHeight
     * @param height
     */
    public void setHeight(int height){
        height_ = height;
    }
}
