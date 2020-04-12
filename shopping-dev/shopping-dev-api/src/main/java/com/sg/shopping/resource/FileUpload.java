package com.sg.shopping.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-upload-dev.properties")
@Component
public class FileUpload {

    private String ImageUserFaceLocation;

    public String getImageUserFaceLocation() {
        return ImageUserFaceLocation;
    }

    public void setImageUserFaceLocation(String imageUserFaceLocation) {
        ImageUserFaceLocation = imageUserFaceLocation;
    }
}
