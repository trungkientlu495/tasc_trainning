package ntk.project.services;

import ntk.project.dto.SearchLog;
import ntk.project.exception.CustomException;
import ntk.project.utills.DateTimeParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SearchLogServices extends DateTimeParser {
    private final FileValidServices fileValidServices;

    public SearchLogServices(FileValidServices fileValidServices) {
        this.fileValidServices = fileValidServices;
    }

    public Set<Object[]> readFileLog(String pathFileName, int countThread
    , String[] allowedExtensions, String[] allowedMimeTypes) throws IOException, InterruptedException {
        // check file truyen vao co hop le khong
        boolean isValidFile = fileValidServices.isValidFile(
                pathFileName,5000,0,allowedExtensions,allowedMimeTypes
        );
        if(isValidFile==false){
            throw new CustomException("You enter file is not valid");
        }
        List<Object[]> listLog = Collections.synchronizedList(new ArrayList<>());
        int lines =(int) Files.lines(Paths.get(pathFileName)).count();
        System.out.println("Kien: "+lines);
        // tao thread pool
        ExecutorService executor = Executors.newFixedThreadPool(countThread);
        int chunkSize = lines/countThread;
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
                        Object[] dataLog = Arrays.stream(line.split(" ")).map(
                                x -> x.replaceAll("[\\[\\]]", "")
                                )
                                .filter(x -> x.equals("-") == false).toArray(Object[]::new);
                        listLog.add(dataLog);
                    }
                });
            }

        }catch (Exception e) {
            executor.shutdownNow();
            throw new CustomException(e.getMessage());
        }
        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("Timeout! Dừng chương trình.");
            executor.shutdownNow();
            System.exit(1);
        }
        return new HashSet<>(listLog);
    }

    public Set<Object[]> searchDataFromFileLog(Set<Object[]> dataLogFile,int countThread
            ,SearchLog searchLog) throws InterruptedException {
        Set<Object[]> a = Collections.synchronizedSet(new HashSet<>());
        int lines = dataLogFile.size();
        ExecutorService executor = Executors.newFixedThreadPool(countThread);
        int chunkSize = lines/countThread;
        List<Object[]> listDataLogFile = dataLogFile.stream().collect(Collectors.toList());
        for(int i=0;i<countThread;i++){
            final int start = i * chunkSize;
            final int end = (i == countThread-1) ? lines : start + chunkSize;
            executor.submit(() -> {
                for (int j = start; j < end; j++) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Task bị interrupt, dừng tại dòng " + j);
                        return; // thoát task ngay
                    }
                    String time = listDataLogFile.get(j)[0]+ " "+listDataLogFile.get(j)[1];
                    String logName = listDataLogFile.get(j)[2].toString();
                    String logServicesName = listDataLogFile.get(j)[3].toString();
                    String message = "";
                    for(int k=4;k<listDataLogFile.get(j).length;k++){
                        message = message + listDataLogFile.get(j)[k].toString();
                    }
                    try {
                        LocalDateTime c = parseDataLogFile(time);
                        if(c.isAfter(parseDataLogFile(searchLog.getStartTimeLogSearch()))
                                &&
                                c.isBefore(parseDataLogFile(searchLog.getEndTimeLogSearch()))
                                && logName.equals(searchLog.getLogName())
                                && message.contains(searchLog.getLogMessage())

                        ) {
                            a.add(listDataLogFile.get(j));
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }
        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("Timeout! Dừng chương trình.");
            executor.shutdownNow();
            System.exit(1);
        }
        return a;
    }

    public void exportDataLogToFile(Set<Object[]> dataLogFile) {
        String fileName = "output.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for(Object[] dataLog : dataLogFile) {
                String data = "";
                for(int i=0;i<dataLog.length;i++) {
                    data = data + dataLog[i].toString() + " ";
                }
                writer.write(data);
                writer.newLine();
                System.out.println(data);
            }
            System.out.println("Ghi file thành công!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LocalDateTime parseDataLogFile(String time) throws InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        return dateTime;
    }
}
