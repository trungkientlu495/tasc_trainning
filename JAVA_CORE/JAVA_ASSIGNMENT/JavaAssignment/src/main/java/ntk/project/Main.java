package ntk.project;
import ntk.project.services.FileValidServicesImpl;
import ntk.project.services.SearchLogServices;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        long countThread;
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhập số thread cần dùng trong thread pool là: ");
        countThread = sc.nextLong();
        String pathFileName = "sample_log.txt";
        String[] allowedExtensions = new String[]{"txt"};
        String[] allowedMimeTypes = new String[]{"text/plain"};
        SearchLogServices searchLogServices = new SearchLogServices(new FileValidServicesImpl());
        Set<String> test = searchLogServices.readFileLog(pathFileName,(int)countThread,allowedExtensions,allowedMimeTypes);
        System.out.println("MMMMMMMMMMMMMMMMMMMMMM: "+test.size());
        // check cac thread lay ra co dung voi du lieu tu file ban dau khong - start
        //Set<String> listFromFile = Files.readAllLines(Paths.get(pathFileName)).stream().collect(Collectors.toSet()); // đọc file gốc
        //Set<String> setFromFile = new HashSet<>(listFromFile);
        // bỏ qua thứ tự, check tất cả phần tử có tồn tại
        //boolean allPresent = setFromFile.containsAll(test);
        //System.out.println(allPresent ? "Đầy đủ" : "Thiếu dòng");
        // end



    }
}