package BLL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileAccessTracker {
    private static String FILE_PATH = ""; // Đường dẫn tệp cần theo dõi
    private ScheduledExecutorService scheduler;

    public static void beginTracker(String path) {
        FileAccessTracker.FILE_PATH = path;
        FileAccessTracker tracker = new FileAccessTracker();
        tracker.startTracking();
        try {
            Thread.sleep(600000); // Chạy trong 10 phút
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tracker.stopTracking();
    }

    public void startTracking() {
        scheduler = Executors.newScheduledThreadPool(1);
        
        // Theo dõi mỗi 4 giây
        scheduler.scheduleAtFixedRate(() -> {
            try {
                String users = getCurrentUsersAccessingFile(FILE_PATH);
                if (!users.isEmpty()) {
                    System.out.println("Người dùng đang truy cập vào tệp " + FILE_PATH + ": " + users);
                } else {
                    System.out.println("Không có người dùng nào đang truy cập vào tệp.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 4, TimeUnit.SECONDS);
        
        System.out.println("Bắt đầu theo dõi người dùng truy cập vào tệp: " + FILE_PATH);
    }

    public void stopTracking() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("Đã dừng theo dõi người dùng.");
        }
    }

    private String getCurrentUsersAccessingFile(String filePath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command",
                "Get-SmbOpenFile | Where-Object {$_.Path -eq '" + filePath.replace("\\", "\\\\") + "'} | Select-Object -ExpandProperty ClientUserName");
        System.out.println(processBuilder.command());
        Process process = processBuilder.start();
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append(", ");
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi thực thi lệnh. Đảm bảo rằng PowerShell có sẵn.");
        }
        try {
            if (process.waitFor() != 0) {
                System.err.println("Lỗi khi thực thi lệnh trên tệp: " + filePath);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result.toString().isEmpty() ? "" : result.toString().substring(0, result.length() - 2); // Xóa dấu phẩy cuối
    }
}
