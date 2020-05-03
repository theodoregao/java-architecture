package com.sg.shopping.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-upload-prod.properties")
@Component
public class FileUpload {

    private String ImageUserFaceLocation;

    private String imageServerUrl;

    public String getImageUserFaceLocation() {
        return ImageUserFaceLocation;
    }

    public void setImageUserFaceLocation(String imageUserFaceLocation) {
        ImageUserFaceLocation = imageUserFaceLocation;
    }

    public String getImageServerUrl() {
        return imageServerUrl;
    }

    public void setImageServerUrl(String imageServerUrl) {
        this.imageServerUrl = imageServerUrl;
    }
}
