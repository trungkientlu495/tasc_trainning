package ntk.project.services;

import java.io.IOException;

public interface FileValidServices {
    boolean isValidFile(String pathFileName,long maxSize,long minSize
                        ,String[] allowedExtensions,String[] allowedMimeTypes
    ) throws IOException;

    boolean isFileExists(String pathFileName);

    boolean isValidFileSize(String pathFileName);

    boolean isValidExtension(String pathFileName,String[] allowedExtensions);

    boolean isValidMimeType(String pathFileName,String[] allowedMimeTypes) throws IOException;
}
