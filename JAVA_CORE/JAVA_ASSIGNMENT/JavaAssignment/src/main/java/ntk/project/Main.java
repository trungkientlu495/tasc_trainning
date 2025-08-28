package ntk.project;
import ntk.project.dto.SearchLog;
import ntk.project.services.FileValidServicesImpl;
import ntk.project.services.SearchLogServices;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis(); // bắt đầu đo
        int countThread;
        countThread = Runtime.getRuntime().availableProcessors();
        System.out.println("Quantity Thread: " + countThread);
        String pathFileName = "sample_log.txt";
        String[] allowedExtensions = new String[]{"txt"};
        String[] allowedMimeTypes = new String[]{"text/plain"};
        SearchLogServices searchLogServices = new SearchLogServices(new FileValidServicesImpl());
        long lines;
        try (Stream<String> streamLines = Files.lines(Paths.get(pathFileName))) {
            lines = streamLines.count();
        }
        long chunkSize = lines / countThread;
        List<Object[]> listDataLogFile = searchLogServices.readFileLog(pathFileName,countThread
                ,allowedExtensions,allowedMimeTypes,lines,chunkSize);
        System.out.println("Kích thước của file data: "+lines);
        SearchLog searchLog = new SearchLog();
        searchLog.setStartTimeLogSearch("2025-08-26 03:58:25");
        searchLog.setEndTimeLogSearch("2025-08-26 03:58:27");
        searchLog.setLogName("DEBUG");
        searchLog.setLogMessage("expired");
        searchLog.setServicesName("AuthService");
        searchLogServices.exportDataLogToFile(searchLogServices.searchDataFromFileLog(listDataLogFile,countThread,
                searchLog,lines,chunkSize));
        long endTime = System.currentTimeMillis(); // kết thúc đo
        System.out.println("Tổng thời gian chạy chương trình: " + (endTime - startTime) + " ms");
    }
}