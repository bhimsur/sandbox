package io.bhimsur.sandbox.dto;

public class ObjectStorageDTO {
    private String name;
    private String url;
    private String contentType;
    private byte[] object;

    public ObjectStorageDTO(String name, String url, String contentType) {
        this.name = name;
        this.url = url;
        this.contentType = contentType;
    }

    public ObjectStorageDTO() {
    }

    public ObjectStorageDTO(String name, String url, String contentType, byte[] object) {
        this.name = name;
        this.url = url;
        this.contentType = contentType;
        this.object = object;
    }

    @Override
    public String toString() {
        return "ObjectStorageDTO{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getObject() {
        return object;
    }

    public void setObject(byte[] object) {
        this.object = object;
    }
}
