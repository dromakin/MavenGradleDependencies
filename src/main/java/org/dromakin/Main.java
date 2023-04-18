package org.dromakin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws URISyntaxException {
        URL res = Main.class.getClassLoader().getResource("build.gradle");
        // gradle
        GroovyDependencies groovyDependencies = new GroovyDependencies(Paths.get(res.toURI()));
        groovyDependencies.getGroovyDependencies().forEach(logger::info);

    }


}