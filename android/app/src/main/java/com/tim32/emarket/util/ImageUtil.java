package com.tim32.emarket.util;


import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ImageUtil {

    public static MultiValueMap<String, Object> wrapImageInMultiValueMap(String filePath) {
        MultiValueMap<String, Object> mvMap = new LinkedMultiValueMap<>();
        mvMap.add("file", new FileSystemResource(filePath));
        return mvMap;
    }
}
