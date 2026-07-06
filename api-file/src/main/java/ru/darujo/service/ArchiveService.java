package ru.darujo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.sevenz.*;
import org.springframework.stereotype.Service;
import org.tukaani.xz.LZMA2Options;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

@Slf4j
@Service
public class ArchiveService {
//    @PostConstruct
//    private void unpackArchive() {
//        SevenZOutputFile sevenZOutputFile = createArchive(new File(pathFile + "/archive_new.7z"));
//        addFileArchive(sevenZOutputFile, Path.of("new/input.txt"), "файлик".getBytes());
//        addFileArchive(sevenZOutputFile, Path.of("new/disp/db.7z"), new File("c:/11/disp.7z"));
//
//        saveArchive(sevenZOutputFile);
//        unpackArchive(new File(pathFile + "/archive_new.7z"), null, Path.of(pathFile));
//
//    }

    public static SevenZOutputFile createArchive(File fileArchive) {
        try {

            SevenZOutputFile out = new SevenZOutputFile(fileArchive);
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

    private static final Path pathPoint = Path.of("." + File.separator);

    public static void addFileArchive(SevenZOutputFile out, Path path, File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            addFileArchive(out, path, fileInputStream.readAllBytes());
        } catch (FileNotFoundException e) {
            log.error("Файл не найден", e);
        } catch (IOException e) {
            log.error("Не удалось прочитать файл", e);
        }

    }

    public static void addFileArchive(SevenZOutputFile out, Path path, InputStream fileInputStream) {
        //            log.error(Integer.toString(path.));
        Path pathDir = addDirArchive(out, path);

        SevenZArchiveEntry entry = out.createArchiveEntry(pathDir.toFile(), pathDir.toString());
        out.putArchiveEntry(entry);
        try {
            out.write(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            out.closeArchiveEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void addFileArchive(SevenZOutputFile out, Path pathFile, byte[] body) {
        //            log.error(Integer.toString(path.));
        addFileArchive(out, pathFile, new ByteArrayInputStream(body));
    }

    private static Path addDirArchive(SevenZOutputFile sevenZOutputFile, Path pathFile) {
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

    public static void saveArchive(SevenZOutputFile out) {

        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unpackArchive(File fileArchive, String password, Path outputDir) throws NoSuchFileException {
        unpackArchive(fileArchive, password, outputDir, (file, content) -> {
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void unpackArchive(File fileArchive, BiConsumer<File, byte[]> fileBiConsumer) throws NoSuchFileException {
        unpackArchive(fileArchive, null, fileBiConsumer);
    }

    public static void unpackArchive(File fileArchive, String password, BiConsumer<File, byte[]> fileBiConsumer) throws NoSuchFileException {
        unpackArchive(fileArchive, password, null, fileBiConsumer);
    }

    public static void unpackArchive(File fileArchive, String password, Path outputDir, BiConsumer<File, byte[]> fileBiConsumer) throws NoSuchFileException {
        {
            try (final SevenZFile sevenZFile = SevenZFile.builder().setFile(fileArchive).setPassword(password == null ? null : password.getBytes(StandardCharsets.UTF_16LE)).get()) {
// todo только 7 степень сжатия
                SevenZArchiveEntry sevenZArchiveEntry;
                sevenZArchiveEntry = sevenZFile.getNextEntry();
                while (sevenZArchiveEntry != null) {


                    if (sevenZArchiveEntry.isDirectory()) {
                        if (outputDir != null) {
                            Path entryPath = outputDir.resolve(sevenZArchiveEntry.getName());
                            Files.createDirectories(entryPath);
                        }
                    } else {

                        byte[] content = new byte[(int) sevenZArchiveEntry.getSize()];
                        sevenZFile.read(content, 0, content.length);
                        fileBiConsumer.accept(outputDir == null ? new File(sevenZArchiveEntry.getName().startsWith(".") ? sevenZArchiveEntry.getName().substring(2) : sevenZArchiveEntry.getName()) : sevenZArchiveEntry.resolveIn(outputDir).toFile(), content);

                    }
                    sevenZArchiveEntry = sevenZFile.getNextEntry();
                }
            } catch (NoSuchFileException exception) {
                throw exception;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    }
}
