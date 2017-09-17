package com.sentiance.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatasetModel {
    private int size;
    private String directory;
    private String name;

    public String getPath(int index) {
        return directory + "/" + name + "/" + name + Integer.toString(index);
    }

    public String getPath() {
        return directory + "/" + name;
    }

}
