package org.dromakin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws URISyntaxException {
        URL buildGradle = Main.class.getClassLoader().getResource("build.gradle");
        URL buildGradleKotlin = Main.class.getClassLoader().getResource("build.gradle.kts");
        // gradle 9
        GradleDependencies gradleDependencies = new GradleGroovyDependencies(Paths.get(buildGradle.toURI()));
        gradleDependencies.getDependencies().forEach(logger::info);
        // kotlin 24
        gradleDependencies = new GradleKotlinDependencies(Paths.get(buildGradleKotlin.toURI()));
        gradleDependencies.getDependencies().forEach(logger::info);
//        System.out.println(gradleDependencies.getDependencies().size());
    }


}