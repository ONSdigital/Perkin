package com.github.onsdigital.perkin.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public abstract class FileHelper {

    public static void saveFile(byte[] bytes, String filename) throws IOException {

        Path path = Paths.get("target/" + filename);

        Files.write(path, bytes);

        log.info("saved file target/" + filename);
    }

    public static byte[] loadFileAsBytes(String filename) throws IOException {

        InputStream in = FileHelper.class.getClassLoader().getResourceAsStream(filename);
        log.info("loaded file:  " + filename + " as: " + in);
        return IOUtils.toByteArray(in);
    }

    public static String loadFile(String filename) throws IOException {
        return new String(loadFileAsBytes(filename), StandardCharsets.UTF_8);
    }
}