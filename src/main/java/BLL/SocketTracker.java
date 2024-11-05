package BLL;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import DTO.Host;

public class SocketTracker {
    private Socket client;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public void tracker(String path) {
        try {
            client = new Socket(Host.dnsServer, 6000);
            outputStream = new DataOutputStream(client.getOutputStream());
            inputStream = new DataInputStream(client.getInputStream());
            outputStream.writeUTF(path);
            Thread thread = new Thread(() -> folderHandle());
            thread.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void folderHandle() {
        try {
            while (true) {
                // Attempt to read data from the input stream
                String contentString = inputStream.readUTF();
                if (!contentString.isEmpty()) {
                    System.out.println(contentString);
                }
            }
        } catch (Exception e) {
            System.out.println("Server has closed the connection or no more data to read.");
        } finally {
            // Ensure resources are closed in case of an error or end of stream
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (client != null && !client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
