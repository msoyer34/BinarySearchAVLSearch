import java.io.IOException;

public interface TreeInterface {
    public enum NodeType{
        LeafNode,
        NonLeafNode,
        SingleChildNode,
        None
    }
    void addNode(String element);
    void removeNode(String element);
    void sendMessage(String fromElement, String toElement);
    public void printLogsToTerminal(String outputPath) throws IOException;
}
