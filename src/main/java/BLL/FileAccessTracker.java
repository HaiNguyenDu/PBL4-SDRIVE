package BLL;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Controller.MyItemController;
import DTO.Host;

public class FileAccessTracker {
    public  String FILE_PATH = ""; // Đường dẫn tệp cần theo dõi
    public String ListUser = ""; 
    public Thread thread;
    public boolean isAlive = true;
    public  Socket client;
    public void beginTracker(String path) {
        FILE_PATH = path;
        try {
            client = new Socket(Host.dnsServer, 5000);
            DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            outputStream.writeUTF(FILE_PATH);
            thread = new Thread(() -> listener(inputStream));
            thread.start();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public  void end() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            isAlive = false;
            FILE_PATH = "";
            try {
                client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

   public void listener(DataInputStream inputStream) {
        while (isAlive) {
            String string = "";
            try {
                string = inputStream.readUTF();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            }
            ListUser = string;
            System.out.println("List user: " + ListUser);
        }
    }
}
