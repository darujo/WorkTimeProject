package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class FileService {
    private final Map<String, File> files = new HashMap<>();

    public String addFile(String name, byte[] body) {
        return addFile(name, null, body);
    }

    public void saveFile(String name, byte[] body) {
        addFile(name, name, body);
    }

    public String addFile(String name, String fileName, byte[] body) {
        try {
            File file;
            if (fileName == null) {
                file = File.createTempFile(String.valueOf(Objects.requireNonNull(name).hashCode()), ".tmp");
                file.deleteOnExit();
            } else {
                file = new File(fileName);
            }

            log.info(name);
            log.info(fileName);
            log.info(file.getAbsolutePath());

            String filePath = "file.txt";

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(body);
            }
            addFile(name, file);
            return name;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void addFile(String name, File file) {
        files.put(name, file);
    }

    public File getFile(String name) {

        File file = files.get(name);
        if (file == null) {
            file = new File(name);
            addFile(name, file);
        }
        return file;
    }

    public byte[] getFileBody(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            log.info(e.getMessage(), e);
            return null;
        }
    }

    public void delFile(String name) {
        File file = files.get(name);
        if (file == null) {
            return;
        }
        boolean flag = file.delete();
        log.info("файл удален {}? {}", name, flag);
        files.remove(name);

    }


    public File resourceToFile(String fileName) {

        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            File f = File.createTempFile(String.valueOf(Objects.requireNonNull(in).hashCode()), ".tmp");
            log.info(f.getAbsolutePath());
            f.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(f)) {
                byte[] buffer = new byte[1024];

                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return f;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
