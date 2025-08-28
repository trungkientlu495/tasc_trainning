package ntk.project.services;

import ntk.project.dto.SearchLog;
import ntk.project.exception.CustomException;
import ntk.project.utills.DateTimeParser;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

public class SearchLogServices extends DateTimeParser {
    private final FileValidServices fileValidServices;
    public SearchLogServices(FileValidServices fileValidServices) {
        this.fileValidServices = fileValidServices;
    }

    public List<Object[]> readFileLog(String pathFileName, int countThread
            , String[] allowedExtensions, String[] allowedMimeTypes,long lines,long chunkSize
    ) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis(); // bắt đầu đo
        // check file truyen vao co hop le khong
        boolean isValidFile = fileValidServices.isValidFile(
                pathFileName,5000,0,allowedExtensions,allowedMimeTypes
        );
        if(isValidFile==false){
            throw new CustomException("You enter file is not valid");
        }
        List<Object[]> listLog = Collections.synchronizedList(new ArrayList<>());
        // tao thread pool
        ExecutorService executor = Executors.newFixedThreadPool(countThread);
        try (BufferedReader br = new BufferedReader(new FileReader(pathFileName))) {
            List<String> listLogString = br.lines().toList();
            for(long i=0;i<countThread;i++){
                final long start = i * chunkSize;
                final long end = (i == countThread-1) ? lines : start + chunkSize;
                executor.submit(() -> {
                    for (long j = start; j < end; j++) {
                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Task bị interrupt, dừng tại dòng " + j);
                            return; // thoát task ngay
                        }
                        String line = listLogString.get((int)j);

                        Object[] dataLog = Arrays.stream(line.split(" ")).map(
                                        x -> x.replace("[","").replace("]","")
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
        if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
            System.out.println("Timeout! Dừng chương trình.");
            executor.shutdownNow();
            System.exit(1);
        }
        long endTime = System.currentTimeMillis(); // kết thúc đo
        System.out.println("Tổng thời gian chạy chương trình: " + (endTime - startTime) + " ms");
        return new ArrayList<>(listLog);
    }

    public Set<Object[]> searchDataFromFileLog(List<Object[]> listDataLogFile
            ,int countThread
            ,SearchLog searchLog,long lines
    ,long chunkSize) throws InterruptedException {
        long startTime = System.currentTimeMillis(); // bắt đầu đo
        Set<Object[]> setSearchDataFromFileLog = ConcurrentHashMap.newKeySet();
        ExecutorService executor = Executors.newFixedThreadPool(countThread);
        // Parse startTime và endTime 1 lần ngoài loop
        LocalDateTime startSearch = parseDataLogFile(searchLog.getStartTimeLogSearch());
        LocalDateTime endSearch = parseDataLogFile(searchLog.getEndTimeLogSearch());
        for(long i=0;i<countThread;i++){
            final long start = i * chunkSize;
            final long end = (i == countThread-1) ? lines : start + chunkSize;
            executor.submit(() -> {
                for (long j = start; j < end; j++) {
                    StringBuilder sbMessage = new StringBuilder();
                    StringBuilder sbTime = new StringBuilder();
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Task bị interrupt, dừng tại dòng " + j);
                        return; // thoát task ngay
                    }
                    sbTime.append(listDataLogFile.get((int)j)[0]);
                    sbTime.append(" ");
                    sbTime.append(listDataLogFile.get((int)j)[1]);
                    String logName = listDataLogFile.get((int)j)[2].toString();
                    String logServicesName = listDataLogFile.get((int)j)[3].toString();
                    for(int k=4;k<listDataLogFile.get((int)j).length;k++){
                        sbMessage.append(listDataLogFile.get((int)j)[k].toString());
                    }
                    try {
                        LocalDateTime c = parseDataLogFile(sbTime.toString());
                        if(c.isAfter(startSearch)
                                &&
                                c.isBefore(endSearch)
                                && logName.equals(searchLog.getLogName())
                                && logServicesName.equals(searchLog.getServicesName())
                                && sbMessage.toString().contains(searchLog.getLogMessage())

                        ) {
                            setSearchDataFromFileLog.add(listDataLogFile.get((int)j));
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }
        executor.shutdown();
        if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
            System.out.println("Timeout! Dừng chương trình.");
            executor.shutdownNow();
            System.exit(1);
        }
        long endTime = System.currentTimeMillis(); // kết thúc đo
        System.out.println("Tổng thời gian chạy chương trình: " + (endTime - startTime) + " ms");
        return setSearchDataFromFileLog;
    }

    public void exportDataLogToFile(Set<Object[]> dataLogFile) {
        String fileName = "output.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for(Object[] dataLog : dataLogFile) {
                StringBuilder sbData = new StringBuilder();
                for(int i=0;i<dataLog.length;i++) {
                    sbData.append(dataLog[i].toString());
                    sbData.append(" ");
                }
                writer.write(sbData.toString());
                writer.newLine();
            }
            System.out.println("Ghi file thành công!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LocalDateTime parseDataLogFile(String time) throws InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, formatter);
    }

    public Object[] parseLogLineFast(String line) {
        // Tách dòng theo khoảng trắng
        String[] tokens = line.split(" "); // array tạm token

        // List để lưu token hợp lệ
        List<String> result = new ArrayList<>(tokens.length);

        // Duyệt từng token
        for (String token : tokens) {
            if (token.equals("-")) continue; // bỏ token là "-"

            // Dùng StringBuilder để loại bỏ dấu '[' và ']'
            token = token.replace("[","").replace("]","");

            // Thêm token đã xử lý vào list
            result.add(token);
        }

        // Chuyển sang Object[] nếu cần
        return result.toArray(new Object[0]);
    }
}