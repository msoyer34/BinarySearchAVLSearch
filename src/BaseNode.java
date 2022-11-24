

public class BaseNode<String> {
    private String element_;
    private BaseNode<String> left_;
    private BaseNode<String> right_;
    private int height_ = 0;


    /***
     * Constructor with only element.
     * @param theElement
     */
    public BaseNode(String element){
        this( element, null, null );
    }
    /***
     * Constructor of BinaryNode
     * @param element
     * @param left
     * @param right
     */
    BaseNode(String element, BaseNode<String> left, BaseNode<String> right){
        element_ = element;
        left_ = left;
        right_ = right;
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
    public BaseNode<String> getLeftNode(){
        return left_;
    }
    /***
     * getRightNode method
     * @return rightNode
     */
    public BaseNode<String> getRightNode(){
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
    public void setLeftNode(BaseNode<String> left){
        left_ = left;
    }

    /***
     * setRightNode
     * @param right
     */
    public void setRightNode(BaseNode<String> right){
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
