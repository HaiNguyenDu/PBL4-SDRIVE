package BLL;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mysql.cj.result.Row;

import DAL.ConnectWindowServer;

public class File_handle {

    public static void openFile(String filePath) {
        // Tạo đối tượng File từ đường dẫn
        File file = new File(filePath);

        if (file.exists()) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(file);
                    System.out.println("Đang mở file: " + filePath);
                } catch (IOException e) {
                    System.out.println("Không thể mở file: " + e.getMessage());
                }
            } else {
                System.out.println("Hệ thống không hỗ trợ ứng dụng Desktop.");
            }
        } else {
            System.out.println("File không tồn tại: " + filePath);
        }
    }

    public static void closeFile(String filePath) {
        try {
            String processName = "winword.exe";
            ProcessBuilder processBuilder = new ProcessBuilder("taskkill", "/F", "/IM", processName);
            processBuilder.start();
            System.out.println("Closing the file (and the application)...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewFile(String directoryPath, String fileName) {
        if (fileName.contains(".xlsx")) {
            createExcelFile(directoryPath, fileName);
            return;
        }
        Path path = Paths.get(directoryPath, fileName); // Kết hợp đường dẫn thư mục và tên file

        try {
            // Kiểm tra nếu file đã tồn tại
            if (Files.exists(path)) {
                System.out.println("File already exists: " + path);
            } else {
                // Tạo file mới
                Files.createFile(path);
                System.out.println("File created: " + path);
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
        }
    }

    public static void createExcelFile(String directoryPath, String fileName) {
        try {
            // Tạo workbook mới
            XSSFWorkbook workbook = new XSSFWorkbook();

            // Tạo sheet trong workbook
            XSSFSheet sheet = workbook.createSheet("Sheet1");

            // Tạo row và cell trong sheet
            XSSFRow row = sheet.createRow(0); // Tạo dòng đầu tiên
            XSSFCell cell = row.createCell(0); // Tạo cell tại vị trí (0, 0)
            cell.setCellValue("Hello, Excel!"); // Ghi giá trị vào cell

            // Đặt đường dẫn đầy đủ cho file
            String filePath = directoryPath + "/" + fileName;

            // Ghi workbook vào file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            // Đóng workbook
            workbook.close();

            System.out.println("Excel file created successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error creating Excel file: " + e.getMessage());
        }
    }

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
            if (username.equals("Everyone")) {
                domainUser = username;
            }
            // Create the batch file to modify permissions for a single file
            try (FileWriter fileWriter = new FileWriter(batchFilePath);
                    PrintWriter printWriter = new PrintWriter(fileWriter)) {

                printWriter.println("@echo off");
                // Disable inheritance on the file and remove any existing permissions for the
                // user
                // Grant full control to admin users to maintain access
                printWriter.println("icacls \"" + filePath + "\" /grant \"PBL4\\Administrator:F\"");
                printWriter.println("icacls \"" + filePath + "\" /grant \"PBL4\\" + ConnectWindowServer.user + ":F\"");
                printWriter.println("icacls \"" + filePath + "\" /inheritance:r");
                // Grant full control to admin users to maintain access
                printWriter.println("icacls \"" + filePath + "\" /grant \"PBL4\\Administrator:F\"");
                printWriter.println("icacls \"" + filePath + "\" /grant \"PBL4\\" + ConnectWindowServer.user + ":F\"");
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
                printWriter.println("icacls \"" + filePath + "\" /inheritance:d");
                // Grant full control to admin users to maintain access
                printWriter.println("icacls \"" + filePath + "\" /grant \"PBL4\\Administrator:F\"");
                printWriter.println("icacls \"" + filePath + "\" /grant \"PBL4\\" + ConnectWindowServer.user + ":F\"");
            }

            Process process = Runtime.getRuntime().exec(batchFilePath);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Permissions successfully modified for " + username + " on folder " + filePath);
            } else {
                System.err.println("Failed to modify permissions. Exit code: " + exitCode);
            }
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
