package ru.darujo.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Getter
@Setter
@NoArgsConstructor
public class ResultFile {
    private File file;
    private byte[] content = null;
    private Boolean archive;
    private SevenZOutputFile sevenZOutputFile;

    public void setFile(File file, byte[] content) {
        if (archive == null) {
            this.file = file;
            this.content = content;
            archive = false;
        } else if (!archive) {
            String fileName;
            fileName = "archive.7z";
            File fileArch = new File(fileName);
            fileArch.deleteOnExit();
            sevenZOutputFile = ArchiveService.createArchive(fileArch);
            ArchiveService.addFileArchive(sevenZOutputFile, this.file.toPath(), this.content);
            this.file = fileArch;
            this.content = null;
            archive = true;
            ArchiveService.addFileArchive(sevenZOutputFile, file.toPath(), content);
        } else {
            ArchiveService.addFileArchive(sevenZOutputFile, file.toPath(), content);
        }
    }


    public InputStreamResource getISR() throws FileNotFoundException {
        if (sevenZOutputFile != null) {
            ArchiveService.saveArchive(sevenZOutputFile);
        }
        if (content != null) {
            return new InputStreamResource(new ByteArrayInputStream(content));
        }

        return new InputStreamResource(new FileInputStream(file));
    }

    public long getLength() {
        if (content != null) {
            return content.length;
        }
        return file.length();
    }
}
