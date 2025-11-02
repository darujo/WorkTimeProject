package ru.darujo.service;

import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class FileService {
    private final Map<String, File> files = new HashMap<>();

    public String addFile(String name, String body) {
        try {
            File file = File.createTempFile(String.valueOf(Objects.requireNonNull(body).hashCode()), ".tmp");

            file.deleteOnExit();

            try (PrintWriter out = new PrintWriter(file, StandardCharsets.UTF_8)) {
                out.println(body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            files.put(name,file);
            return name;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public File getFile(String name) {
        return files.get(name);
    }

    public File delFile(String name) {
        return files.remove(name);
    }

    @PostConstruct
    private void init() {
        files.put("hi", resourceToFile("hi.jpg"));
    }

    public File resourceToFile(String fileName) {

        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            File f = File.createTempFile(String.valueOf(Objects.requireNonNull(in).hashCode()), ".tmp");
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
            System.out.println(e.getMessage());
            return null;
        }
    }
}
