package BLL;

import DTO.*;
import DAL.ConnectWindowServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSHExample {
    public static boolean accessNetworkShare(String username, String password, String networkPath) {
        try {
            // Xóa thông tin xác thực cũ nếu đã tồn tại
            String deleteCmd = String.format("cmdkey /delete:%s", networkPath);
            System.out.println(deleteCmd);
            Process deleteProcess = Runtime.getRuntime().exec(deleteCmd);
            deleteProcess.waitFor();

            // Thêm thông tin xác thực mới
            String addCmd = String.format("cmdkey /add:%s /user:%s /pass:%s", networkPath, username, password);
            System.out.println(addCmd);
            Process addProcess = Runtime.getRuntime().exec(addCmd);
            int exitCode = addProcess.waitFor();

            if (exitCode == 0) {
                System.out.println("Thông tin xác thực đã được lưu thành công.");
                return true;
            } else {
                try (BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(addProcess.getErrorStream()))) {
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        System.err.println("Lỗi: " + errorLine);
                    }
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static public boolean setAccount(String Host, String User, String Password) {
        if (ConnectWindowServer.setAccount(Host, User, Password)) {
            accessNetworkShare(User, Password, "\\\\" + DTO.Host.dnsServer + "\\SDriver");
            return true;
        } else {
            return false;
        }
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
                    folderList.add(new File_Folder(name,
                            lastWriteTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a"))));
                } catch (DateTimeParseException e) {
                    System.out.println("Could not parse date: " + lastWriteTimeStr);
                }
            } else {
                System.out.println("Skipping line with empty date: " + line);
            }
        }
        return folderList;
    }

    public static List<UserAccess> getAccessList(String path) {
        List<UserAccess> accessList = new ArrayList<>();
        try {
            // Chạy lệnh icacls trên đường dẫn
            Process process = Runtime.getRuntime().exec("icacls \"" + path + "\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println("Debug: " + line);
                // Bỏ qua dòng đầu chứa đường dẫn
                if (line.trim().startsWith(path)) {
                    line = line.trim().replace(path, "");
                }

                // Lọc các dòng chứa username và quyền
                if (line.contains("(") && line.contains(")")) {
                    String[] parts = line.trim().split("\\:", 2); // Tách username và quyền
                    if (parts.length == 2) {
                        String username = parts[0].trim(); // Tên user
                        String permissions = parts[1].trim();
                        boolean isIh = false;
                        if (line.contains("\\(I\\)"))
                            isIh = true;
                        // Loại bỏ các ký tự (I)(OI)(CI) và chỉ giữ quyền chính
                        permissions = permissions.replaceAll("\\(I\\)", "")
                                .replaceAll("\\(OI\\)", "")
                                .replaceAll("\\(CI\\)", "")
                                .replaceAll("\\(IO\\)", "")
                                .trim();

                        // Kiểm tra các loại quyền
                        String access = null;
                        if (permissions.contains("(F)")) {
                            access = "Fullcontrol";
                        } else if (permissions.contains("(R)")) {
                            access = "Readonly";
                        } else if (permissions.contains("(M)")) {
                            access = "Read/Write";
                        }

                        // Chỉ thêm vào danh sách nếu quyền hợp lệ và không phải nhóm BUILTIN
                        if (access != null && !username.startsWith("BUILTIN\\")
                                && !username.equalsIgnoreCase("Administrators")) {
                            // Kiểm tra nếu quyền của Thanhan chưa được thêm
                            if (username.equalsIgnoreCase("PBL4\\Thanhan") && !accessList.stream()
                                    .anyMatch(u -> u.getUsername().equals("PBL4\\Thanhan"))) {
                                accessList.add(new UserAccess(username, access, isIh));
                            } else if (!username.equalsIgnoreCase("PBL4\\Thanhan")) {
                                accessList.add(new UserAccess(username, access, isIh));
                            }
                        }
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessList;
    }

    // public static ArrayList<File_Folder> FindFolder(String FolderName) throws
    // Exception {
    // String information = ConnectWindowServer.FindFolder(FolderName);
    // System.out.println(information);
    // return parseInputToFileFolderList(information);
    // }

    // Phương thức liệt kê file và folder và trả về ArrayList<File_Folder>
    public static ArrayList<File_Folder> FindFolder(String Path) {
        String directoryPath = Path.replace("C:", "\\\\" + Host.dnsServer);
        ArrayList<File_Folder> fileList = new ArrayList<>();

        // Tạo đối tượng File cho thư mục
        File directory = new File(directoryPath);

        // Kiểm tra xem thư mục có tồn tại và có phải thư mục không
        if (directory.exists() && directory.isDirectory()) {
            // Liệt kê tất cả các tệp và thư mục trong thư mục
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    long lastModifiedMillis = file.lastModified();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String lastModified = sdf.format(new Date(lastModifiedMillis));
                    fileList.add(new File_Folder(file.getName(), lastModified));
                }
            }
        } else {
            System.out.println("Đường dẫn không hợp lệ hoặc không phải là thư mục.");
        }

        return fileList;
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

    static public ArrayList<String> listDomainUsersWithFilter(String filter) throws Exception {
        ArrayList<String> users = new ArrayList<>();
        String output = ConnectWindowServer.listDomainUsersWithFilter(filter);
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
