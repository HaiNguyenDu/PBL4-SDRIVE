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

    public static void shareFolder(String username, String folderPath, String access) {
        try {
            String shareName = new File(folderPath).getName();
            String command = "icacls \"" + folderPath + "\" /grant \"" + username + "\":" + access;
            System.err.println(command);
            Process process = Runtime.getRuntime().exec("cmd /c " + command);
            process.waitFor();

            if (process.exitValue() == 0) {
                System.out.println("Thư mục đã được chia sẻ thành công!");

                // Define destination path
                String destinationPath = "\\\\" + Host.dnsServer + "\\Driver\\" + ConnectWindowServer.user + "\\"
                        + shareName;
                File sourceDir = new File(folderPath);
                File destDir = new File(destinationPath);

                // Copy folder
                copyDirectory(sourceDir, destDir);
                System.out.println("Đã sao chép thư mục vào " + destinationPath);
                // Set permissions: Read-only for "Thanhan"
                setPermissions(destDir.getAbsolutePath(), username, access);
                // Grant full control to admins

            } else {
                System.out.println("Không thể chia sẻ thư mục.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    // public static void modifyUserPermissions(String folderPath, String userName, String permissions) {
    //     String command = String.format("icacls \"%s\" /grant:r \"%s:%s\" /T", folderPath, userName, permissions);
    //     String disableInheritance = String.format("icacls \"%s\" /inheritance:r", folderPath);
    //     try {
    //         // Reset permissions inheritance
    //         ProcessBuilder disableInheritanceBuilder = new ProcessBuilder("cmd.exe", "/c", disableInheritance);
    //         disableInheritanceBuilder.inheritIO();
    //         Process inheritanceProcess = disableInheritanceBuilder.start();
    //         int inheritanceExitCode = inheritanceProcess.waitFor();
    //         if (inheritanceExitCode != 0) {
    //             System.err.println("Failed to disable inheritance for folder: " + folderPath);
    //         }
            
    //         // Execute the main permissions command
    //         ProcessBuilder permissionBuilder = new ProcessBuilder("cmd.exe", "/c", command);
    //         permissionBuilder.inheritIO();
    //         Process permissionProcess = permissionBuilder.start();
    //         int permissionExitCode = permissionProcess.waitFor();
            
    //         if (permissionExitCode == 0) {
    //             System.out.println("Permissions updated for " + userName + " on " + folderPath);
    //         } else {
    //             System.err.println("Failed to set permissions for " + userName + " on " + folderPath);
    //         }
    //     } catch (IOException | InterruptedException e) {
    //         e.printStackTrace();
    //     }
    // }
    

    // private static void setPermissions(String folderPath, String username, String access) {
    //     try {
    //         // Create batch file
    //         String batchFilePath = "set_permissions.bat";
    //         FileWriter fileWriter = new FileWriter(batchFilePath);
    //         PrintWriter printWriter = new PrintWriter(fileWriter);
            
    //         // Write commands to the batch file
    //         printWriter.println("@echo off");
    //         printWriter.println("icacls \"" + folderPath + "\" /inheritance:r"); // Disable inheritance
    //         printWriter.println("icacls \"" + folderPath + "\" /grant \"PBL4\\Administrators:F\""); // Full control for Administrators
    //         printWriter.println("icacls \"" + folderPath + "\" /grant \"PBL4\\" + ConnectWindowServer.user + ":F\""); // Full control for specific user
    
    //         // Set permissions for the specific user based on access
    //         String domainUser = "PBL4\\" + username;
    //         switch (access) {
    //             case "F": // Full control
    //                 printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":F");
    //                 break;
    //             case "R": // Read-only access
    //                 printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R");
    //                 break;
    //             case "D": // Deny access entirely
    //                 printWriter.println("icacls \"" + folderPath + "\" /deny \"" + domainUser + "\":(F)");
    //                 break;
    //             case "RW": // Custom read and write access
    //                 printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R /deny \"" + domainUser + "\":W");
    //                 break;
    //             default: // Default to read-only
    //                 printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R");
    //                 System.out.println("Unrecognized access type. Defaulting to read-only.");
    //                 break;
    //         }
    
    //         // Write permissions for all files and subdirectories, granting full control to specific users
    //         printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"PBL4\\Administrators:F\" /T");
    //         printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"PBL4\\" + ConnectWindowServer.user + ":F\" /T");
    //         printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"" + domainUser + "\":" + access + " /T");
            
    //         // Now grant read-only access to Everyone last to ensure it doesn't affect the specific permissions
    //         printWriter.println("icacls \"" + folderPath + "\" /grant \"Everyone:R\"");
    //         printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"Everyone:R\" /T");
    
    //         // Close the PrintWriter
    //         printWriter.close();
    
    //         // Execute the batch file
    //         Process process = Runtime.getRuntime().exec("cmd /c start " + batchFilePath);
    //         process.waitFor();
    //         System.out.println("Created and executed the batch file to set permissions for folder " + folderPath);
    //     } catch (IOException | InterruptedException e) {
    //         e.printStackTrace();
    //     }
    // }
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
            
            // Grant appropriate permissions for Thanhan
            switch (access) {
                case "M": // Modify access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":M"); 
                    break;
                case "F": // Full control
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":F");
                    break;
                case "R": // Read-only access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":(RX)");
               //     printWriter.println("icacls \"" + folderPath + "\" /deny \"" + domainUser + "\":(W)");
                    break;
                case "D": // Deny access entirely
                    printWriter.println("icacls \"" + folderPath + "\" /deny \"" + domainUser + "\":(F)");
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
            printWriter.println("icacls \"" + folderPath + "\" /grant \"PBL4\\"+ConnectWindowServer.user+"\":(OI)(CI)F");
            // Enable inheritance again to ensure new folders inherit permissions
       //     printWriter.println("icacls \"" + folderPath + "\" /inheritance:e");
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
    
    
    public static void UpdatePermissions(String folderPath, String username, String access) {
        try {
            // Tạo batch file
            String batchFilePath = "set_permissions.bat";
            FileWriter fileWriter = new FileWriter(batchFilePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
    
            printWriter.println("@echo off");
    
            String domainUser = "PBL4\\" + username;
    
            // Tắt kế thừa quyền cho thư mục và tất cả thư mục con
            printWriter.println("icacls \"" + folderPath + "\" /inheritance:r /T");
    
            // Xóa tất cả quyền cũ của user trước
            printWriter.println("icacls \"" + folderPath + "\" /remove \"" + domainUser + "\" /T");
    
            // Thiết lập quyền mới dựa trên tham số "access"
            switch (access) {
                case "F": // Full control
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":F /T");
                    break;
                case "M": // Modify access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":M /T");
                    break;
                case "R": // Read-only access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R"); // Grant Read-only
                    printWriter.println("icacls \"" + folderPath + "\" /deny \"" + domainUser + "\":W"); // Deny Write access
                    break;
                case "D": // Deny access entirely
                    printWriter.println("icacls \"" + folderPath + "\" /deny \"" + domainUser + "\":(F) /T");
                    break;
                case "RW": // Custom read and write access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R /deny \"" + domainUser + "\":W /T");
                    break;
                default: // Default to read-only
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R /T");
                    System.out.println("Unrecognized access type. Defaulting to read-only.");
                    break;
            }
    
            // Đóng PrintWriter
            printWriter.close();
    
            // Thực thi batch file
            Process process = Runtime.getRuntime().exec("cmd /c start " + batchFilePath);
            process.waitFor();
            System.out.println("Updated permissions for folder " + folderPath + " for user " + username + " with access " + access);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public static void setPermissions(String folderPath, String username, String access) {
        try {
            // Create batch file
            String batchFilePath = "set_permissions.bat";
            FileWriter fileWriter = new FileWriter(batchFilePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
    
            // Write commands to the batch file
            printWriter.println("@echo off");
            printWriter.println("icacls \"" + folderPath + "\" /inheritance:e"); // Enable inheritance
            printWriter.println("icacls \"" + folderPath + "\" /grant \"PBL4\\Administrators:(OI)(CI)F\""); // Full control for Administrators
            printWriter.println("icacls \"" + folderPath + "\" /grant \"PBL4\\" + ConnectWindowServer.user + ":(OI)(CI)F\""); // Full control for specific user
    
            // Set permissions for the specific user based on access
            String domainUser = "PBL4\\" + username;
            switch (access) {
                case "M": // Modify access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":M"); 
                    break;
                case "F": // Full control
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":F");
                    break;
                case "R": // Read-only access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R");
                    break;
                case "D": // Deny access entirely
                    printWriter.println("icacls \"" + folderPath + "\" /deny \"" + domainUser + "\":(F)");
                    break;
                case "RW": // Custom read and write access
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R /deny \"" + domainUser + "\":W");
                    break;
                default: // Default to read-only
                    printWriter.println("icacls \"" + folderPath + "\" /grant \"" + domainUser + "\":R");
                    System.out.println("Unrecognized access type. Defaulting to read-only.");
                    break;
            }
    
            // Apply permissions recursively to subdirectories and files
            printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"PBL4\\Administrators:(OI)(CI)F\" /T");
            printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"PBL4\\" + ConnectWindowServer.user + ":(OI)(CI)F\" /T");
            printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"" + domainUser + "\":" + access + " /T");
    
            // Grant read-only access to Everyone to avoid overwriting specific permissions
            printWriter.println("icacls \"" + folderPath + "\" /grant \"Everyone:R\"");
            printWriter.println("icacls \"" + folderPath + "\\*\" /grant \"Everyone:R\" /T");
    
            // Close the PrintWriter
            printWriter.close();
    
            // Execute the batch file
            Process process = Runtime.getRuntime().exec("cmd /c start " + batchFilePath);
            process.waitFor();
            System.out.println("Created and executed the batch file to set permissions for folder " + folderPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
}
