package com.idearfly.decouple.vo;

import lombok.Data;

@Data
public class FileObject {
    private String filename;
    private Boolean type;
    private String path;
    private String name;
    private String ext;
}
