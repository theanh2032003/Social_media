package com.example.demo.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUpLoad {

    public List<String> saveFile(String uploadDir, String fileName,
                                 MultipartFile[] multipartFiles) throws IOException {

        if(multipartFiles == null){
            return null;
        }

        if(multipartFiles[0] == null){

            List<String> list = new ArrayList<>();
            list.add(null);
            return list;
        }



        List<String> saveFilePath = new ArrayList<>();

        Path uploadPath = Paths.get(uploadDir);

        Path filePath = uploadPath.resolve(fileName);

        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath);
        }

        try {
            for (MultipartFile file : multipartFiles) {
                byte[] bytes = file.getBytes();
                Path path = filePath.resolve( file.getOriginalFilename());
                Files.write(path, bytes);
                saveFilePath.add(path.toString());
            }

        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }

        return saveFilePath;
    }
}
