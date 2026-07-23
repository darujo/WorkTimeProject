package ru.darujo.dto.file;

public class FileDto {
    private Long id;
    private String fileForDisk;
    private String userName;
    private String fileName;
    private String size;
    private Boolean delete;
    private String objectType;
    private String objectId;


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
    public String getSize() {
        return size;
    }

    @SuppressWarnings("unused")
    public Boolean getDelete() {
        return delete;
    }

    @SuppressWarnings("unused")
    public String getObjectType() {
        return objectType;
    }

    @SuppressWarnings("unused")
    public String getObjectId() {
        return objectId;
    }

    @SuppressWarnings("unused")
    public FileDto() {
    }

    public FileDto(Long id, String objectType, String objectId, String fileForDisk, String userName, String fileName, String size, Boolean delete) {
        this.id = id;
        this.fileForDisk = fileForDisk;
        this.userName = userName;
        this.fileName = fileName;
        this.size = size;
        this.delete = delete;
        this.objectType = objectType;
        this.objectId = objectId;
    }
}
