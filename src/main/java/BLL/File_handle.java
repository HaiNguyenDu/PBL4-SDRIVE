package BLL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import DAL.ConnectWindowServer;

public class File_handle {

    public static void upLoadFile(String filePath, String destinationDir) {
        String fileName = new File(filePath).getName();
        File sourceFile = new File(filePath);
        File destFile = new File(destinationDir, fileName);
        if (sourceFile.canRead()) {
            try {
                java.nio.file.Files.copy(sourceFile.toPath(), destFile.toPath());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("Cannot read file: " + filePath);
                e.printStackTrace();
            }
            System.out.println("File copied to " + destFile.getAbsolutePath());
        } else {
            System.out.println("Cannot read file: " + filePath);
        }
    }

    public static void modifyUserFilePermissions(String filePath, String username, String access) {
        try {
            String batchFilePath = "modify_file_permissions.bat";
            String domainUser = "PBL4\\" + username;

            // Create the batch file to modify permissions for a single file
            try (FileWriter fileWriter = new FileWriter(batchFilePath);
                    PrintWriter printWriter = new PrintWriter(fileWriter)) {

                printWriter.println("@echo off");
                // Disable inheritance on the file and remove any existing permissions for the
                // user
                printWriter.println("icacls \"" + filePath + "\" /inheritance:r");
                printWriter.println("icacls \"" + filePath + "\" /remove \"" + domainUser + "\"");

                // Set permissions based on the access type
                switch (access) {
                    case "M": // Modify access
                        printWriter.println("icacls \"" + filePath + "\" /grant \"" + domainUser + "\":M");
                        break;
                    case "R": // Read-only access
                        printWriter.println("icacls \"" + filePath + "\" /grant \"" + domainUser + "\":R");
                        break;
                    case "D": // Deny access
                        printWriter.println("icacls \"" + filePath + "\" /deny \"" + domainUser + "\":F");
                        break;
                    case "RW": // Custom read and write access without delete
                        printWriter.println("icacls \"" + filePath + "\" /grant \"" + domainUser + "\":R /deny \""
                                + domainUser + "\":W");
                        break;
                    default:
                        System.out.println("Unrecognized access type. Defaulting to read-only.");
                        printWriter.println("icacls \"" + filePath + "\" /grant \"" + domainUser + "\":R");
                        break;
                }

                // Grant full control to admin users to maintain access
                printWriter.println("icacls \"" + filePath + "\" /grant \"PBL4\\Administrator:F\"");
                printWriter.println("icacls \"" + filePath + "\" /grant \"PBL4\\" + ConnectWindowServer.user + ":F\"");
            }

            // Execute the batch file
            Process process = Runtime.getRuntime().exec("cmd /c start " + batchFilePath);
            process.waitFor();
            System.out.println("Permissions modified for " + username + " on file " + filePath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void shareFile(String username, String filePath, String access, String destinationDir) {
        try {
            String fileName = new File(filePath).getName();
            String command = "icacls \"" + filePath + "\" /grant \"" + username + "\":" + access;
            Process process = Runtime.getRuntime().exec("cmd /c " + command);
            process.waitFor();

            if (process.exitValue() == 0) {
                System.out.println("File successfully shared!");

                // Define destination path and copy the file
                File sourceFile = new File(filePath);
                File destFile = new File(destinationDir, fileName);

                if (sourceFile.canRead()) {
                    java.nio.file.Files.copy(sourceFile.toPath(), destFile.toPath());
                    System.out.println("File copied to " + destFile.getAbsolutePath());

                    // Set permissions on the destination file
                    modifyUserFilePermissions(destFile.getAbsolutePath(), username, access);
                } else {
                    System.out.println("Cannot read file: " + filePath);
                }
            } else {
                System.out.println("Unable to share file.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
