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
        Set<Object[]> test = searchLogServices.readFileLog(pathFileName,countThread,allowedExtensions,allowedMimeTypes);
        System.out.println("MMMMMMMMMMMMMMMMMMMMMM: "+test.size());
        SearchLog searchLog = new SearchLog();
        searchLog.setStartTimeLogSearch("2025-08-26 03:58:23");
        searchLog.setEndTimeLogSearch("2025-08-26 03:58:26");
        searchLog.setLogName("DEBUG");
        searchLog.setLogMessage("D");
        List<Object[]> test2 = searchLogServices.searchDataFromFileLog(test,countThread,
                searchLog).stream().toList();
        searchLogServices.exportDataLogToFile(searchLogServices.searchDataFromFileLog(test,countThread,
                searchLog));
    }
}