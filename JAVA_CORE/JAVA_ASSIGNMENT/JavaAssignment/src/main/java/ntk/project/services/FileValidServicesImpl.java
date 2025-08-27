package ntk.project.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileValidServicesImpl implements FileValidServices {
    private static final long MAX_SIZE_FILE = 10000;
    private static final long MIN_SIZE_FILE = 1024;
    @Override
    public boolean isFileExists(String pathFileName) {
        File file = new File(pathFileName);
        return (file.exists() && file.isFile()) ? true : false;
    }

    @Override
    public boolean isValidFileSize(String pathFileName) {
        File file = new File(pathFileName);
        long fileSize = (long)file.length()/(1024*1024);
        return (file.exists() && file.isFile() && fileSize <= FileValidServicesImpl.MAX_SIZE_FILE) ? true : false;
    }

    @Override
    public boolean isValidExtension(String pathFileName, String[] allowedExtensions) {
        int indexStartExtension = pathFileName.lastIndexOf('.');
        String extension = pathFileName.substring(indexStartExtension+1);
        if(extension==null || extension.length()<1) return false;
        boolean checkValidExtension = Arrays.stream(allowedExtensions).anyMatch(
                x -> x.equals(extension)
        );
        return checkValidExtension;
    }

    @Override
    public boolean isValidMimeType(String pathFileName, String[] allowedMimeTypes) throws IOException {
        String mimeType = Files.probeContentType(Paths.get(pathFileName));
        if(mimeType == null) return false;
        boolean checkValidMimeType = Arrays.stream(allowedMimeTypes).anyMatch(
                x -> x.equals(mimeType)
        );
        return checkValidMimeType;
    }

    @Override
    public boolean isValidFile(String pathFileName, long maxSize, long minSize
            , String[] allowedExtensions, String[] allowedMimeTypes) throws IOException {
        boolean isValidFile = isFileExists(pathFileName)
                &&isValidFileSize(pathFileName)
                &&isValidExtension(pathFileName,allowedExtensions)
                &&isValidMimeType(pathFileName,allowedMimeTypes);
        return isValidFile;
    }
}
