package com.ymyang.test;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

public class FileTest {

    @Test
    public void filename() {
        String file = "C:/Users/yangym/Downloads/7653059384ef4db68a8557ce0d3cff77.jpg";
        System.out.println("getPrefix: " + FilenameUtils.getPrefix(file));
        System.out.println("getExtension: " + FilenameUtils.getExtension(file));
        System.out.println("getBaseName: " + FilenameUtils.getBaseName(file));
    }
}
