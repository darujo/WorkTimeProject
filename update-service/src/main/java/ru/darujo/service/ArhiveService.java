package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ArhiveService {
    public void dddd() {
//        try (SevenZOutputFile out = new SevenZOutputFile(new File("archive.7z"))) {
//            SevenZArchiveEntry entry = out.createArchiveEntry(fileToArchive, name);
//            out.putArchiveEntry(entry);
//            out.write(contentOfEntry);
//            out.closeArchiveEntry();
//        }
    }

    @PostConstruct
    private void unpackArh() {
        unpackArh(null);
    }

    @Value("${update.save-into}")
    private String pathFile;

    private void unpackArh(String pas) {
        Path outputDir = Path.of(pathFile);
        try (final SevenZFile sevenZFile = SevenZFile.builder().setFile(new File(pathFile + "/archive.7z")).setPassword(pas == null ? null : pas.getBytes(StandardCharsets.UTF_8)).get()) {
// todo только 7 степень сжатия
            SevenZArchiveEntry sevenZArchiveEntry;
            sevenZArchiveEntry = sevenZFile.getNextEntry();
            while (sevenZArchiveEntry != null) {
                Path entryPath = outputDir.resolve(sevenZArchiveEntry.getName());
                if (sevenZArchiveEntry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    FileOutputStream out = new FileOutputStream(sevenZArchiveEntry.getName());
                    byte[] content = new byte[(int) sevenZArchiveEntry.getSize()];
                    sevenZFile.read(content, 0, content.length);
                    out.write(content);
                    out.close();
                }
                sevenZArchiveEntry = sevenZFile.getNextEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
