package org.example;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Decorator {

    public static String getRealFilePath(String path) throws IOException{
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    public static String getRealFilePathFrontend(String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource("/frontend" + path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }
}