package BLL;

import DTO.*;
import DAL.ConnectWindowServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSHExample {
    static public boolean setAccount(String Host, String User, String Password) {
        ConnectWindowServer.setAccount(Host, User, Password);
        return ConnectWindowServer.testAccount();
    }

     private static ArrayList<File_Folder> parseInputToFileFolderList(String input) {
        ArrayList<File_Folder> folderList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");

        String[] lines = input.split("\n");
        for (int i = 2; i < lines.length; i++) {
            String line = lines[i].trim();
            String name = line.replaceAll("\\d{1,2}/\\d{1,2}/\\d{4}.*", "").trim();
            String lastWriteTimeStr = line.substring(name.length()).trim();

            if (!lastWriteTimeStr.isEmpty()) {
                try {
                    LocalDateTime lastWriteTime = LocalDateTime.parse(lastWriteTimeStr, formatter);
                    folderList.add(new File_Folder(name, lastWriteTime));
                } catch (DateTimeParseException e) {
                    System.out.println("Could not parse date: " + lastWriteTimeStr);
                }
            } else {
                System.out.println("Skipping line with empty date: " + line);
            }
        }
        return folderList;
    }

    public static ArrayList<File_Folder> FindFolder(String FolderName) throws Exception {
        String information = ConnectWindowServer.FindFolder(FolderName);
        System.out.println(information);
        return parseInputToFileFolderList(information);
    }
    // Phương thức chia sẻ thư mục với người dùng trong domain với quyền truy cập
    // xác định
    static public void ShareFolder(String folderPath, String username, String access) throws Exception {
        String reString = ConnectWindowServer.SharedFolder(folderPath, username, access);
        System.err.println(reString);
    }

    static public ArrayList<String> listDomainUsers() throws Exception {
        ArrayList<String> users = new ArrayList<>();
        String output = ConnectWindowServer.listDomainUsers();
        String[] lines = output.toString().split("\n");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (!line.isEmpty()) {
                users.add(line);
                System.err.println(line);
            }
        }
        return users;
    }
}
