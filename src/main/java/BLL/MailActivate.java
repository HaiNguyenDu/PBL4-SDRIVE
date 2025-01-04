package BLL;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import DTO.Mail;

public class MailActivate {
    public static Socket socket;
    public static DataInputStream din;
    public static DataOutputStream dos;
    public static Thread listenThread;
    public static String oldMessage = "";
    public static ArrayList<String> newMessages = new ArrayList<>();
    public static String newMessage = "";
    public static boolean isNewMess = false;
    public static String host = "192.168.10.160";

    static public void sendMail(Mail mail_send) {
        // Tạo thread gửi tin nhắn
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.print("Client: ");
                        String msg = mail_send.getUsername_send() + "|" + mail_send.getUsername_receive() + "|"
                                + mail_send.getDate() + "|" + mail_send.getItem_name() + "|" + mail_send.getPath() + "|"
                                + mail_send.getSeen() + "|" + mail_send.getAccess_modifier(); // Người dùng nhập dữ liệu
                        dos.writeUTF(msg); // Gửi thông điệp đến server
                        dos.flush();
                        break;
                    }
                } catch (IOException ex) {
                    System.out.println("Đã có lỗi xảy ra khi gửi dữ liệu: " + ex.getMessage());
                }
            }
        }).start();
    }

    static public void listenThread() {
        listenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        oldMessage = newMessage;
                        newMessage = din.readUTF();
                        if (!oldMessage.equals(newMessage)) {
                            isNewMess = true;
                            newMessages.add(newMessage);
                        } else {
                            isNewMess = false;
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Đã có lỗi xảy ra khi đọc dữ liệu: " + ex.getMessage());
                }
            }
        });
        listenThread.start();
    }

    static public String init(String username) {
        try {
            socket = new Socket(host, 6000);
            din = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            // Gửi yêu cầu kết nối ban đầu
            dos.writeUTF(username + "|connect");
            dos.flush();
            String serverResponse = "";
            // Nhận phản hồi từ server
            try {
                socket.setSoTimeout(5000); // Set timeout to 5 seconds
                serverResponse = din.readUTF();
            } catch (IOException e) {
                throw new RuntimeException("Failed to receive server response", e);
            }
            System.out.println("Server: " + serverResponse);
            if (serverResponse.equals("Connected")) {
                listenThread();
            }
            if (serverResponse.equals("")) {
                return "không connect được với Host";
            }
            return "Success";
        } catch (UnknownHostException e) {
            // Xử lý lỗi không tìm thấy host
            return "không connect được với Host";
        } catch (IOException e) {
            // Xử lý lỗi I/O khi tạo socket
            return "không tạo được socket";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
