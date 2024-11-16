package BLL;

import java.io.*;

import DAL.ConnectWindowServer;
import DTO.Host;

public class Folder_handle {
    public static void listSharedFiles(String remoteServer) {
        try {
            Process process = Runtime.getRuntime().exec("net view \\\\" + remoteServer);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            System.out.println("Danh sách thư mục được chia sẻ từ máy chủ " + remoteServer + ":");
            while ((line = reader.readLine()) != null) {
                if (line.contains("Share name")) {
                    continue; // Bỏ qua dòng tiêu đề
                }
                if (line.trim().isEmpty()) {
                    continue; // Bỏ qua dòng trống
                }
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void listSharedFolders() {
        try {
            // Chạy lệnh net share để liệt kê các thư mục đã được chia sẻ
            Process process = Runtime.getRuntime().exec("net share");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            System.out.println("Danh sách thư mục đã được chia sẻ:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Đợi cho lệnh hoàn thành
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void shareFolder(String username, String folderPath, String access, String pos) {
        try {
            String shareName = new File(folderPath).getName();
            String command = "icacls \"" + folderPath + "\" /grant \"" + username + "\":" + access;
            System.err.println(command);
            Process process = Runtime.getRuntime().exec("cmd /c " + command);
            process.waitFor();

            if (process.exitValue() == 0) {
                System.out.println("Thư mục đã được chia sẻ thành công!");

                // Define destination path
                String destinationPath = pos + "\\"
                        + shareName;
                File sourceDir = new File(folderPath);
                File destDir = new File(destinationPath);

                // Copy folder
                copyDirectory(sourceDir, destDir);
                System.out.println("Đã sao chép thư mục vào " + destinationPath);
                // Set permissions: Read-only for "Thanhan"
                modifyUserPermissions(destDir.getAbsolutePath(), username, access);
                // Grant full control to admins

            } else {
                System.out.println("Không thể chia sẻ thư mục.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareFile(String username, String folderPath, String access, String pos) {
        try {
            String shareName = new File(folderPath).getName();
            String command = "icacls \"" + folderPath + "\" /grant \"" + username + "\":" + access;
            System.err.println(command);
            Process process = Runtime.getRuntime().exec("cmd /c " + command);
            process.waitFor();
            if (process.exitValue() == 0) {
                System.out.println("Thư mục đã được chia sẻ thành công!");
                // Define destination path
                String destinationPath =  pos + "\\" + shareName;
                File sourceDir = new File(folderPath);
                File destDir = new File(destinationPath);
                // Copy folder
                copyFile(sourceDir, destDir);
                System.out.println("Đã sao chép thư mục vào " + destinationPath);
                modifyUserPermissionsFile(destDir.getAbsolutePath(), username, access);

            } else {
                System.out.println("Không thể chia sẻ thư mục.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(File sourceDir, File destDir) throws IOException {
        if (sourceDir.canRead()) {
            java.nio.file.Files.copy(sourceDir.toPath(), destDir.toPath());
        } else {
            System.out.println("Không thể đọc tệp: " + sourceDir.getAbsolutePath());
        }
    }

    private static void copyDirectory(File sourceDir, File destDir) throws IOException {
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        for (String file : sourceDir.list()) {
            File srcFile = new File(sourceDir, file);
            File destFile = new File(destDir, file);

            // Kiểm tra quyền truy cập trước khi sao chép
            if (srcFile.canRead()) {
                if (srcFile.isDirectory()) {
                    copyDirectory(srcFile, destFile);
                } else {
                    java.nio.file.Files.copy(srcFile.toPath(), destFile.toPath());
                }
            } else {
                System.out.println("Không thể đọc tệp: " + srcFile.getAbsolutePath());
            }
        }
    }

    private static void addUserPermissions(String folderPath, String username, String access) {
        try {
            String batchFilePath = "set_permissions.bat";
            FileWriter fileWriter = new FileWriter(batchFilePath, true); // Open in append mode
            PrintWriter printWriter = new PrintWriter(fileWriter);
            String domainUser = "PBL4\\" + username;
            printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":" + access);
            printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"" + domainUser + "\":" + access + " /T");
            printWriter.close();
            Process process = Runtime.getRuntime().exec("cmd /c start " + batchFilePath);
            process.waitFor();

            System.out.println("Permissions for user " + username + " have been added to " + folderPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void modifyUserPermissions(String folderPath, String username, String access) {
        try {
            String batchFilePath = "modify_permissions.bat";
            FileWriter fileWriter = new FileWriter(batchFilePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("@echo off");
            String domainUser = "PBL4\\" + username;
            // Disable inheritance on the parent folder, if necessary
            printWriter.println("icacls \"" + folderPath + "\" /inheritance:r");
            // Remove specific permissions for Thanhan to reset
            printWriter.println("icacls \"" + folderPath + "\" /remove \"" + domainUser + "\"");
            printWriter.println("icacls \"" + folderPath + "\" /remove \"" + domainUser + "\" /T");
//              M: Modify
//              DC: Delete Child (prevents creating or deleting items)
//              W: Write (prevents adding or changing files)
//              RD: Read Data
//              WD: Write Data
            // Grant appropriate permissions for Thanhan
            switch (access) {
                case "M": // Modify access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":(OI)(CI)M"); 
                    printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"PBL4\\" + username + ":(OI)(CI)M\" /T");
                    break;
                case "R": // Read-only access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":(OI)(CI)R");
                    printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"" + domainUser + "\":(OI)(CI)R /T");
                    // printWriter.println("icacls \"" + folderPath + "\\*\" /deny \"" + domainUser + "\":M");
                    // printWriter.println("icacls \"" + folderPath + "\\*\" /deny \"PBL4\\" + username + ":(M)\" /T");
                    break;
                // case "R": // Read-only access
                //     printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":(RX)");
                //     printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"" + domainUser + ":R\" /T");
                //     printWriter.println("icacls \"" + folderPath + "\\*\" /deny \"PBL4\\" + username + ":(M)\" /T");
               //     printWriter.println("icacls \"" + folderPath + "\" /deny \"" + domainUser + "\":(W)");
                    // break;
                case "D": // Deny access entirely
                    break;
                case "RW": // Custom read and write access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R /deny \"" + domainUser + "\":W");
                    break;
                default: // Default to read-only
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R");
                    System.out.println("Unrecognized access type. Defaulting to read-only.");
                    break;
            }
            // Grant XPhuc full control over the folder to maintain access to all contents
            printWriter.println("icacls \"" + folderPath + "\" /grant \"PBL4\\Administrator:(OI)(CI)F\""); // Full control for Administrators
            printWriter.println("icacls \"" + folderPath + "\" /grant \"PBL4\\" + ConnectWindowServer.user + ":(OI)(CI)F\""); // Full control for specific user
            printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"PBL4\\Administrator:(OI)(CI)F\" /T");
            printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"PBL4\\" + ConnectWindowServer.user + ":(OI)(CI)F\" /T");
            // Enable inheritance again to ensure new folders inherit permissions
            //printWriter.println("icacls \"" + folderPath + "\" /inheritance:e");
            // Close the PrintWriter
            printWriter.close();
            // Execute the batch file
            Process process = Runtime.getRuntime().exec("cmd /c start " + batchFilePath);
            process.waitFor();
            System.out.println("Created and executed the batch file to modify permissions for " + username + " on folder " + folderPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    } 

    public static void modifyUserPermissionsFile(String folderPath, String username, String access) {
        try {
            String batchFilePath = "modify_permissions.bat";
            FileWriter fileWriter = new FileWriter(batchFilePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("@echo off");
            String domainUser = "PBL4\\" + username;
            // Disable inheritance on the parent folder, if necessary
            printWriter.println("icacls \"" + folderPath + "\" /inheritance:r");
            // Remove specific permissions for Thanhan to reset
            printWriter.println("icacls \"" + folderPath + "\" /remove \"" + domainUser + "\"");
//              M: Modify
//              DC: Delete Child (prevents creating or deleting items)
//              W: Write (prevents adding or changing files)
//              RD: Read Data
//              WD: Write Data
            // Grant appropriate permissions for Thanhan
            switch (access) {
                case "M": // Modify access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":(OI)(CI)M"); 
                    break;
                case "R": // Read-only access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":(OI)(CI)R");
                    // printWriter.println("icacls \"" + folderPath + "\\*\" /deny \"" + domainUser + "\":M");
                    // printWriter.println("icacls \"" + folderPath + "\\*\" /deny \"PBL4\\" + username + ":(M)\" /T");
                    break;
                // case "R": // Read-only access
                //     printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":(RX)");
                //     printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"" + domainUser + ":R\" /T");
                //     printWriter.println("icacls \"" + folderPath + "\\*\" /deny \"PBL4\\" + username + ":(M)\" /T");
               //     printWriter.println("icacls \"" + folderPath + "\" /deny \"" + domainUser + "\":(W)");
                    // break;
                case "D": // Deny access entirely
                    break;
                case "RW": // Custom read and write access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R /deny \"" + domainUser + "\":W");
                    break;
                default: // Default to read-only
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R");
                    System.out.println("Unrecognized access type. Defaulting to read-only.");
                    break;
            }
    
            // Grant XPhuc full control over the folder to maintain access to all contents
            printWriter.println("icacls \"" + folderPath + "\" /grant \"PBL4\\Administrator:(OI)(CI)F\""); // Full control for Administrators
            printWriter.println("icacls \"" + folderPath + "\" /grant \"PBL4\\" + ConnectWindowServer.user + ":(OI)(CI)F\""); // Full control for specific user
            // Enable inheritance again to ensure new folders inherit permissions
            //printWriter.println("icacls \"" + folderPath + "\" /inheritance:e");
            // Close the PrintWriter
            printWriter.close();
            // Execute the batch file
            Process process = Runtime.getRuntime().exec("cmd /c start " + batchFilePath);
            process.waitFor();
            System.out.println("Created and executed the batch file to modify permissions for " + username + " on folder " + folderPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }   
}
