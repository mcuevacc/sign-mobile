package pe.edu.uni.www.vitalsign;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SendMessage extends Thread {

    private Context context;
    private Activity activity;
    private String path;
    private String message;

    SendMessage(Context context, Activity activity,String path, String message){
        this.context = context;
        this.activity = activity;
        this.path = path;
        this.message = message;
    }

    public void run() {
        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(this.context).getConnectedNodes();
        try {
            List<Node> nodes = Tasks.await(nodeListTask);
            for (Node node : nodes) {
                Task<Integer> sendMessageTask = Wearable.getMessageClient(this.activity).sendMessage(node.getId(), path, message.getBytes());
            }
        } catch (ExecutionException exception) {
        } catch (InterruptedException exception) {
        }
    }
}