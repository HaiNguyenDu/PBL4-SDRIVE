package DTO;

import java.util.List;

public class Mail {
    String username_send;
    String username_receive;
    String date;
    String item_name;
    String path;
    boolean seen;
    String access_modifier;

    public Mail(String username_send, String username_receive, String date, String item_name, boolean seen, String access_modifier) {
        this.username_send = username_send;
        this.username_receive = username_receive;
        this.date = date;
        this.item_name = item_name;
        this.seen = seen;
        this.access_modifier = access_modifier;
    };

    public Mail(String username_send, String username_receive, String date, String item_name, boolean seen, String access_modifier, String path) {
        this.username_send = username_send;
        this.username_receive = username_receive;
        this.date = date;
        this.item_name = item_name;
        this.seen = seen;
        this.access_modifier = access_modifier;
        this.path = path;
    };
    public File_Folder parseToFile_Floder(){
        File_Folder file_Folder = new File_Folder(item_name, access_modifier);
        return file_Folder;
    }
    // Getter và Setter cho username_send
    public String getUsername_send() {
        return username_send;
    }

    public void setUsername_send(String username_send) {
        this.username_send = username_send;
    }

    // Getter và Setter cho username_receive
    public String getUsername_receive() {
        return username_receive;
    }

    public void setUsername_receive(String username_receive) {
        this.username_receive = username_receive;
    }

    // Getter và Setter cho date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Getter và Setter cho item_name
    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    // Getter và Setter cho seen
    public boolean getSeen() { // getter cho boolean thường dùng "is"
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    // Getter và Setter cho access_modifier
    public String getAccess_modifier() {
        return access_modifier;
    }

    public void setAccess_modifier(String access_modifier) {
        this.access_modifier = access_modifier;
    }

}
