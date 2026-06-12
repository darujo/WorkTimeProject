package ru.darujo.service;

import jakarta.annotation.PostConstruct;
import org.apache.commons.compress.archivers.sevenz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tukaani.xz.LZMA2Options;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArchiveService {
    public void packArh() {
        try (SevenZOutputFile out = new SevenZOutputFile(new File(pathFile + "/archive_new.7z"))) {
            List<SevenZMethodConfiguration> methods = new ArrayList<>();


            LZMA2Options lzma2Options = new LZMA2Options();
            lzma2Options.setPreset(LZMA2Options.PRESET_MAX);
            SevenZMethodConfiguration lzmaConfig =
                    new SevenZMethodConfiguration(SevenZMethod.LZMA2, lzma2Options);
            methods.add(lzmaConfig);
            out.setContentMethods(methods);
            SevenZArchiveEntry entry = out.createArchiveEntry(new File("new"), "new");
            entry.setDirectory(true);
            out.putArchiveEntry(entry);
            out.closeArchiveEntry();

            entry = out.createArchiveEntry(new File("new", "input.txt"), "input.txt");

            out.putArchiveEntry(entry);
            out.write("Текст файла".getBytes());
            out.closeArchiveEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void unpackArh() {
        packArh();
        unpackArh(null);

    }

    @Value("${update.save-into}")
    private String pathFile;

    private void unpackArh(String pas) {
        Path outputDir = Path.of(pathFile);
        try (final SevenZFile sevenZFile = SevenZFile.builder().setFile(new File(pathFile + "/archive_new.7z")).setPassword(pas == null ? null : pas.getBytes(StandardCharsets.UTF_8)).get()) {
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
