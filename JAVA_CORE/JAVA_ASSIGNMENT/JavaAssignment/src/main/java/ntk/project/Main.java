package ntk.project;
import ntk.project.dto.SearchLog;
import ntk.project.services.FileValidServicesImpl;
import ntk.project.services.SearchLogServices;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        int countThread;
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhập số thread cần dùng trong thread pool là: ");
        countThread = sc.nextInt();
        String pathFileName = "sample_log.txt";
        String[] allowedExtensions = new String[]{"txt"};
        String[] allowedMimeTypes = new String[]{"text/plain"};
        SearchLogServices searchLogServices = new SearchLogServices(new FileValidServicesImpl());
        Set<Object[]> setDataLogFile = searchLogServices.readFileLog(pathFileName,countThread,allowedExtensions,allowedMimeTypes);
        System.out.println("Kích thước của file data: "+setDataLogFile.size());
        SearchLog searchLog = new SearchLog();
        searchLog.setStartTimeLogSearch("2025-08-26 03:58:23");
        searchLog.setEndTimeLogSearch("2025-08-26 03:58:26");
        searchLog.setLogName("DEBUG");
        searchLog.setLogMessage("D");
        searchLogServices.exportDataLogToFile(searchLogServices.searchDataFromFileLog(setDataLogFile,countThread,
                searchLog));
    }
}