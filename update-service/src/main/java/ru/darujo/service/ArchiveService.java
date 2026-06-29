package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.sevenz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tukaani.xz.LZMA2Options;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class ArchiveService {
//    @PostConstruct
//    private void unpackArh() {
//        SevenZOutputFile sevenZOutputFile = createArh(new File(pathFile + "/archive_new.7z"));
//        addFileArh(sevenZOutputFile, Path.of("new/input.txt"), "файлик".getBytes());
//        addFileArh(sevenZOutputFile, Path.of("new/disp/db.7z"), new File("c:/11/disp.7z"));
//
//        saveArh(sevenZOutputFile);
//        unpackArh(new File(pathFile + "/archive_new.7z"), null, Path.of(pathFile));
//
//    }

    public SevenZOutputFile createArh(File fileArh) {
        try {

            SevenZOutputFile out = new SevenZOutputFile(fileArh);
            /* установим степень сжатия */
            List<SevenZMethodConfiguration> methods = new ArrayList<>();
            LZMA2Options lzma2Options = new LZMA2Options();
            lzma2Options.setPreset(LZMA2Options.PRESET_MAX);
            SevenZMethodConfiguration lzmaConfig =
                    new SevenZMethodConfiguration(SevenZMethod.LZMA2, lzma2Options);
            methods.add(lzmaConfig);
            out.setContentMethods(methods);

            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Path pathPoint = Path.of("." + File.separator);

    public void addFileArh(SevenZOutputFile out, Path path, File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            addFileArh(out, path, fileInputStream.readAllBytes());
        } catch (FileNotFoundException e) {
            log.error("Файл не найден", e);
        } catch (IOException e) {
            log.error("Не удалось прочитать файл", e);
        }

    }

    public void addFileArh(SevenZOutputFile out, Path path, InputStream fileInputStream) {
        //            log.error(Integer.toString(path.));
        Path pathDir = addADirArh(out, path);
        SevenZArchiveEntry entry = out.createArchiveEntry(pathDir.toFile(), pathPoint.resolve(pathDir).toString());

        try {
            out.write(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.putArchiveEntry(entry);
        try {
            out.closeArchiveEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void addFileArh(SevenZOutputFile out, Path pathFile, byte[] body) {
        //            log.error(Integer.toString(path.));
        addFileArh(out, pathFile, new ByteArrayInputStream(body));
    }

    private Path addADirArh(SevenZOutputFile sevenZOutputFile, Path pathFile) {
        SevenZArchiveEntry entry;

        Path pathDir = null;
        Iterator<Path> pathIterator = pathFile.iterator();
        for (int i = 0; i < pathFile.getNameCount() - 1; i++) {
            Path pathDirNext = pathIterator.next();
            if (pathDir == null) {
                pathDir = pathDirNext;
            } else {
                pathDir = pathDir.resolve(pathDirNext);
            }
            entry = sevenZOutputFile.createArchiveEntry(pathDir.toFile(), pathPoint.resolve(pathDir).toString());
            entry.setDirectory(true);
            sevenZOutputFile.putArchiveEntry(entry);
            try {
                sevenZOutputFile.closeArchiveEntry();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        Path pathDirNext = pathIterator.next();
        if (pathDir == null) {
            pathDir = pathDirNext;
        } else {
            pathDir = pathDir.resolve(pathDirNext);
        }
        return pathDir;
    }

    public void saveArh(SevenZOutputFile out) {

        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Value("${update.save-into}")
    private String pathFile;

    private void unpackArh(File fileArh, String password, Path outputDir) {

        try (final SevenZFile sevenZFile = SevenZFile.builder().setFile(fileArh).setPassword(password == null ? null : password.getBytes(StandardCharsets.UTF_16LE)).get()) {
// todo только 7 степень сжатия
            SevenZArchiveEntry sevenZArchiveEntry;
            sevenZArchiveEntry = sevenZFile.getNextEntry();
            while (sevenZArchiveEntry != null) {

                Path entryPath = outputDir.resolve(sevenZArchiveEntry.getName());
                if (sevenZArchiveEntry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {

                    FileOutputStream out = new FileOutputStream(sevenZArchiveEntry.resolveIn(outputDir).toFile());
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
