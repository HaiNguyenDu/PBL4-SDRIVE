package BLL;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import DAL.Mail_DAL;
import DTO.Host;

public class file_folder {
    public static String checkPathAccess(String path) {
        Path inputPath = Paths.get(path);
        if (Files.exists(inputPath)) {
            if (Files.isReadable(inputPath)) {
                return "success";
            }
            return "Access denied";
        } else {
            return "Path does not exist: " + path;
        }
    }

    public static String checkAccess(String path) {
        Path inputPath = Paths.get(path);
        if (Files.exists(inputPath)) {
            if (Files.isWritable(inputPath)) {
                return "success";
            }
            return "Access denied";
        } else {
            return "Path does not exist: " + path;
        }
    }

    public static String rename(String path, String newName) {
        Path oldPath = Paths.get(path); // Đường dẫn hiện tại
        Path newPath = oldPath.resolveSibling(newName); // Tạo đường dẫn mới với tên mới
        System.out.println(oldPath + " " + newPath);
        if (oldPath.equals(newPath)) {
            return "Error new name equals old name";
        }
        try {
            // Kiểm tra xem đường dẫn cũ có tồn tại hay không
            if (Files.exists(oldPath)) {
                String oldName = oldPath.getFileName().toString();
                System.out.println("Old name: " + oldName);
                Mail_DAL.updateNameFile(newName, path.trim().replace("\\\\" + Host.dnsServer, ""), newPath.toString().trim().replace("\\\\" + Host.dnsServer, ""));
                if (Files.isDirectory(oldPath)) {
                    // Nếu là thư mục
                    try {
                        Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (AccessDeniedException e) {
                        System.err.println("Access denied: " + e.getMessage());
                        return "Access denied";
                    }
                    System.out.println("Folder renamed to: " + newPath);
                    return "success";
                } else if (Files.isRegularFile(oldPath)) {
                    // Nếu là file
                    try {
                        Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (AccessDeniedException e) {
                        System.err.println("Access denied: " + e.getMessage());
                        return "Access denied";
                    }
                    System.out.println("File renamed to: " + newPath);
                    return "success";
                } else {
                    return "The path is neither a file nor a directory.";
                }
            } else {
                return "The path does not exist: " + oldPath;
            }
        } catch (IOException e) {
            return "Error renaming: ";
        }
        catch (Exception e) {
            return "Error renaming: ";
        }
    }

    // Phương thức xóa file
    public static String deleteFile(String path) {
        Path filePath = Paths.get(path); // Đường dẫn tới file

        try {
            // Kiểm tra nếu file tồn tại và là một file
            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                Files.delete(filePath);
                return "success";
            } else {
               return "No file found or not a file: " + filePath;
            }
        } catch (IOException e) {
            return"Error deleting file: " + e.getMessage();
        }
    }

    // Phương thức xóa thư mục và tất cả nội dung trong thư mục
    public static String deleteFolder(String path) {
        Path folderPath = Paths.get(path); // Đường dẫn tới thư mục

        try {
            // Kiểm tra nếu thư mục tồn tại và là thư mục
            if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
                // Xóa thư mục và tất cả nội dung bên trong
                Files.walkFileTree(folderPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        try {
                            Files.delete(file); // Xóa từng file
                        } catch (AccessDeniedException e) {
                            System.err.println("Access denied: " + e.getMessage());
                            return FileVisitResult.TERMINATE;
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        try {
                            Files.delete(dir); // Xóa thư mục sau khi xóa các file trong thư mục
                        } catch (AccessDeniedException e) {
                            System.err.println("Access denied: " + e.getMessage());
                            return FileVisitResult.TERMINATE;
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
                System.out.println("Folder and all its contents deleted: " + folderPath);
                return "success";
            } else {
                return "No folder found or not a directory: " + folderPath;
            }
        } catch (IOException e) {
            return "Error deleting folder";
        }
    }

    public static boolean isFile(String path) {
        Path inputPath = Paths.get(path);
        return Files.exists(inputPath) && Files.isRegularFile(inputPath);
    }

    // Phương thức chính để kiểm tra và gọi các phương thức xóa
    public static String deletePath(String path) {
        Path inputPath = Paths.get(path);

        if (Files.exists(inputPath)) {
            if (Files.isDirectory(inputPath)) {
                // Nếu là thư mục
                return deleteFolder(path);
            } else if (Files.isRegularFile(inputPath)) {
                // Nếu là file
                return deleteFile(path);
            } else {
               return "The path is neither a file nor a directory.";
            }
        } else {
            return "Path does not exist: " + path;
        }
    }

}
