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
        URL buildMaven = Main.class.getClassLoader().getResource("pom.xml");
        // gradle 9
        ProjectDependencies projectDependencies = new GradleGroovyDependencies(Paths.get(buildGradle.toURI()));
        logger.info(projectDependencies.getDependencies().size());
//        projectDependencies.getDependencies().forEach(logger::info);
        // kotlin 24
//        projectDependencies = new GradleKotlinDependencies(Paths.get(buildGradleKotlin.toURI()));
//        logger.info(projectDependencies.getDependencies().size());
//        projectDependencies.getDependencies().forEach(logger::info);

        projectDependencies = new MavenDependencies(Paths.get(buildMaven.toURI()));
        logger.info(projectDependencies.getDependencies().size());
        projectDependencies.getDependencies().forEach(logger::info);

    }


}