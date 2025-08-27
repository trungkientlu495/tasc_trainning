package ntk.project.services;

import ntk.project.exception.CustomException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SearchLogServices {
    private final FileValidServices fileValidServices;

    public SearchLogServices(FileValidServices fileValidServices) {
        this.fileValidServices = fileValidServices;
    }

    public Set<String> readFileLog(String pathFileName, long countThread
    , String[] allowedExtensions, String[] allowedMimeTypes) throws IOException, InterruptedException {
        // check file truyen vao co hop le khong
        boolean isValidFile = fileValidServices.isValidFile(
                pathFileName,5000,0,allowedExtensions,allowedMimeTypes
        );
        if(isValidFile==false){
            throw new CustomException("You enter file is not valid");
        }
        List<String> listLog = Collections.synchronizedList(new ArrayList<>());
        int lines =(int) Files.lines(Paths.get(pathFileName)).count();
        System.out.println("Kien: "+lines);
        // tao thread pool
        ExecutorService executor = Executors.newFixedThreadPool((int)countThread);
        int chunkSize = lines/(int)countThread;
        try (BufferedReader br = new BufferedReader(new FileReader(pathFileName))) {
            List<String> listLogString = br.lines().collect(Collectors.toList());
            for(int i=0;i<countThread;i++){
                final int start = i * chunkSize;
                final int end = (i == countThread-1) ? lines : start + chunkSize;
                executor.submit(() -> {
                    for (int j = start; j < end; j++) {
                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Task bị interrupt, dừng tại dòng " + j);
                            return; // thoát task ngay
                        }
                        String line = listLogString.get(j);
                        listLog.add(line);
                    }
                });
            }

        }catch (Exception e) {
            executor.shutdownNow();
            throw new CustomException(e.getMessage());
        }
        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("Timeout! Dừng chương trình.");
            executor.shutdownNow();
            System.exit(1);
        }
        return new HashSet<>(listLog);
    }

    


}
