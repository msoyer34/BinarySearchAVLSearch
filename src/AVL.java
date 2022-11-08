import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

class AVL implements TreeInterface {

    @Override
    public void addNode(String element) {
        logOutput.add("logAddNode");
    }

    @Override
    public void removeNode(String element) {

        logOutput.add("logRemoveNode");
    }

    @Override
    public void sendMessage(String fromElement, String toElement)  {
        logOutput.add("logSendMessage");
    }

    @Override
    public void printLogsToTerminal() throws IOException {
        FileWriter writer = new FileWriter("output.txt");
        for(var log : logOutput){
            writer.write(log + "\n");
        }
        writer.close();
    }
    private Vector<String> logOutput = new Vector<>();
}