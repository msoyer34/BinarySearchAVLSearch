import java.io.IOException;

interface TreeInterface {

    void addNode(String element);
    void removeNode(String element);
    void sendMessage(String fromElement, String toElement);
    void printLogsToTerminal() throws IOException;
}
