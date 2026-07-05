package ru.darujo.converter;

import ru.darujo.dto.file.FileDto;
import ru.darujo.model.FileModel;

public class FileConverter {
    public static FileDto getFileModel(FileModel file) {
        return new FileDto(file.getId(), file.getFileForDisk(), file.getUserName(), file.getFileName(), file.getSize());
    }
}
