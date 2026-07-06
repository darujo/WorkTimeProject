package ru.darujo.converter;

import org.apache.commons.io.FileUtils;
import ru.darujo.dto.file.FileDto;
import ru.darujo.model.FileModel;

public class FileConverter {
    public static FileDto getFileModel(FileModel file) {
        return new FileDto(file.getId(), file.getObjectType(), file.getObjectId(), file.getFileForDisk(), file.getUserName(), file.getFileName(), file.getSize() == null ? null : FileUtils.byteCountToDisplaySize(file.getSize()), file.getDelete());
    }
}
