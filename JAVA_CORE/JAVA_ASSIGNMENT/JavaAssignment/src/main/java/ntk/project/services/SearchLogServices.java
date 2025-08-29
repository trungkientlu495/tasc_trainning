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
            List<List<String>> listLogString = chiaFile(pathFileName,chunkSize);
            for(long i=0;i<countThread;i++){
                final long start = i * chunkSize;
                final long end = (i == countThread-1) ? lines : start + chunkSize;
                final int index = (int)i;
                executor.submit(() -> {
                    for (long j = start; j < end; j++) {
                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Task bị interrupt, dừng tại dòng " + j);
                            return; // thoát task ngay
                        }
                        String line = listLogString.get(index).get((int)j);
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
        System.out.println("Tổng thời gian chạy chương trình : " + (endTime - startTime) + " ms");
        return new ArrayList<>(listLog);
    }

    public Set<Object[]> searchDataFromFileLog(List<Object[]> listDataLogFile
            ,int countThread
            ,SearchLog searchLog,long lines
            ,long chunkSize) throws InterruptedException, FileNotFoundException {
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
        System.out.println("Tổng thời gian chạy chương trìnha: " + (endTime - startTime) + " ms");
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


    public List<List<String>> chiaFile(String filePath,long chunkSize) throws FileNotFoundException {
        List<List<String>> chunks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            List<String> bf = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                bf.add(line);
                if (bf.size() == chunkSize) {
                    chunks.add(new ArrayList<>(bf));
                    bf.clear();
                }
            }

            if (!bf.isEmpty()) {
                chunks.add(new ArrayList<>(bf));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chunks;
    }

    public List<List<Object[]>> chiaFileThanhPhan(List<Object[]> listData, long chunkSize) throws FileNotFoundException {
        List<List<Object[]>> chunks = new ArrayList<>();
        for(int i=0;i<listData.size();i++) {
            List<Object[]> bf = new ArrayList<>();
            bf.add(listData.get(i));
            if (bf.size() == chunkSize) {
                chunks.add(new ArrayList<>());
            }
        }
        return chunks;
    }


}