package BLL;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketTracker {
    public String Path = "";
    public String result = "";

    public void init(String path) {
        try {
            Path = path;
            Socket client = new Socket("10.10.27.25", 5000);
            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            outputStream.writeUTF(Path);
            Thread thread = new Thread(() -> this.listener(inputStream));
            thread.start();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void listener(DataInputStream inputStream) {
        while (true) {
            String string = "";
            try {
                string = inputStream.readUTF();
                result = string;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(string);
        }
    }
}
