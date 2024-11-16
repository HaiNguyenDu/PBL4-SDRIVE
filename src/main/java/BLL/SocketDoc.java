package BLL;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketDoc {
    public static String readDoc(String path){
        String content = "";
        try {
            Socket client  = new Socket("192.168.1.7",6000);
            new DataOutputStream(client.getOutputStream()).writeUTF(path);
            content = new DataInputStream(client.getInputStream()).readUTF();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content;
    }
    public static void writeDoc(String path,String content){
        DocFile conFile = new DocFile();conFile.content= content;conFile.path= path;
        try {
            Socket client  = new Socket("192.168.1.7",6069);
            new ObjectOutputStream(client.getOutputStream()).writeObject(conFile);
            content = new DataInputStream(client.getInputStream()).readUTF();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
class DocFile{
    String path;
    String content;
}