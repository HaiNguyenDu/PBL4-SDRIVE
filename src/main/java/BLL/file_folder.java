package BLL;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class file_folder {

    public static void rename(String path, String newName) {
        Path oldPath = Paths.get(path); // Đường dẫn hiện tại
        Path newPath = oldPath.resolveSibling(newName); // Tạo đường dẫn mới với tên mới

        try {
            // Kiểm tra xem đường dẫn cũ có tồn tại hay không
            if (Files.exists(oldPath)) {
                if (Files.isDirectory(oldPath)) {
                    // Nếu là thư mục
                    Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Folder renamed to: " + newPath);
                } else if (Files.isRegularFile(oldPath)) {
                    // Nếu là file
                    Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File renamed to: " + newPath);
                } else {
                    System.out.println("The path is neither a file nor a directory.");
                }
            } else {
                System.out.println("The path does not exist: " + oldPath);
            }
        } catch (IOException e) {
            System.err.println("Error renaming: " + e.getMessage());
        }
    }

    // Phương thức xóa file
    public static void deleteFile(String path) {
        Path filePath = Paths.get(path); // Đường dẫn tới file

        try {
            // Kiểm tra nếu file tồn tại và là một file
            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                Files.delete(filePath);
                System.out.println("File deleted: " + filePath);
            } else {
                System.out.println("No file found or not a file: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }

    // Phương thức xóa thư mục và tất cả nội dung trong thư mục
    public static void deleteFolder(String path) {
        Path folderPath = Paths.get(path); // Đường dẫn tới thư mục

        try {
            // Kiểm tra nếu thư mục tồn tại và là thư mục
            if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
                // Xóa thư mục và tất cả nội dung bên trong
                Files.walkFileTree(folderPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file); // Xóa từng file
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir); // Xóa thư mục sau khi xóa các file trong thư mục
                        return FileVisitResult.CONTINUE;
                    }
                });
                System.out.println("Folder and all its contents deleted: " + folderPath);
            } else {
                System.out.println("No folder found or not a directory: " + folderPath);
            }
        } catch (IOException e) {
            System.err.println("Error deleting folder: " + e.getMessage());
        }
    }

    public static boolean isFile(String path) {
        Path inputPath = Paths.get(path);
        return Files.exists(inputPath) && Files.isRegularFile(inputPath);
    }

    // Phương thức chính để kiểm tra và gọi các phương thức xóa
    public static void deletePath(String path) {
        Path inputPath = Paths.get(path);

        if (Files.exists(inputPath)) {
            if (Files.isDirectory(inputPath)) {
                // Nếu là thư mục
                deleteFolder(path);
            } else if (Files.isRegularFile(inputPath)) {
                // Nếu là file
                deleteFile(path);
            } else {
                System.out.println("The path is neither a file nor a directory.");
            }
        } else {
            System.out.println("Path does not exist: " + path);
        }
    }

}
