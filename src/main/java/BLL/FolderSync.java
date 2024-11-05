package BLL;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import DAL.ConnectWindowServer;
import DTO.Host;

public class FolderSync {
    private static final Path localPath = Paths.get("C:\\data"+ConnectWindowServer.user);
    private static final Path remotePath = Paths.get("\\\\"+Host.dnsServer+"\\Share\\"+ConnectWindowServer.user);
    // public static void main(String[] args) {
    //     ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    //     Runnable syncTask = () -> {
    //         try {
    //             syncFolders(localPath, remotePath);
    //             System.out.println("Sync completed at " + java.time.LocalTime.now());
    //         } catch (IOException e) {
    //             System.err.println("Error during sync: " + e.getMessage());
    //         }
    //     };

    //     scheduler.scheduleAtFixedRate(syncTask, 0, 3, TimeUnit.MINUTES);
    // }
    // String source = "C:\\DataXPhuc";
    // String destination = "\\\\192.168.1.15\\SharingDriver\\XPhuc\\DataXPhuc";
    public static void robocop(String source , String destination){
        String command = String.format("robocopy \"%s\" \"%s\" /MIR", source, destination);
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Đồng bộ thành công!");
            } else {
                System.out.println("Đã xảy ra lỗi trong quá trình đồng bộ. Mã thoát: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    public static void syncFolders(Path source, Path target) throws IOException {
        Files.walk(source).forEach(sourcePath -> {
            Path targetPath = target.resolve(source.relativize(sourcePath));
            try {
                if (Files.isDirectory(sourcePath)) {
                    if (!Files.exists(targetPath)) {
                        Files.createDirectory(targetPath);
                    }
                } else {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                System.err.println("Failed to copy " + sourcePath + " to " + targetPath + ": " + e.getMessage());
            }
        });
    }
}
