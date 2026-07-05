package ru.darujo.dto.file;

public class FileDto {
    private Long id;
    private String fileForDisk;
    private String userName;
    private String fileName;
    private Long size;

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getFileForDisk() {
        return fileForDisk;
    }

    @SuppressWarnings("unused")
    public String getUserName() {
        return userName;
    }

    @SuppressWarnings("unused")
    public String getFileName() {
        return fileName;
    }

    @SuppressWarnings("unused")
    public Long getSize() {
        return size;
    }

    @SuppressWarnings("unused")
    public FileDto() {
    }

    public FileDto(Long id, String fileForDisk, String userName, String fileName, Long size) {
        this.id = id;
        this.fileForDisk = fileForDisk;
        this.userName = userName;
        this.fileName = fileName;
        this.size = size;
    }
}
