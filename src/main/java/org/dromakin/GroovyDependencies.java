/*
 * File:     GroovyDependencies
 * Package:  org.dromakin
 * Project:  RegExpDependencies
 *
 * Created by dromakin as 18.04.2023
 *
 * author - dromakin
 * maintainer - dromakin
 * version - 2023.04.18
 * copyright - ORGANIZATION_NAME Inc. 2023
 */
package org.dromakin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroovyDependencies {

    private static final Logger logger = LogManager.getLogger(GroovyDependencies.class);

    private Path buildFileGroovy;

    private static final String tagName = "dependencies";
    private static final Pattern dependencyPatternGNV = java.util.regex.Pattern.compile("\\s*(\\w+)\\s+(.+)");
    private static final Pattern dependencyPatternNoGNV = Pattern.compile("\\s*(\\w*)\\s*\\(?'?\"?([a-zA-Z0-9.\\-:]+)'?\"?\\)?");
    private static final Pattern dependencyGroupPatternFirst = Pattern.compile("([a-zA-Z0-9.-]+):([a-zA-Z0-9.-]+):([a-zA-Z0-9.-]+)");
    private static final Pattern dependencyGroupPatternSecond = Pattern.compile("group:\\s*'([a-zA-Z0-9.-]+)',\\s*name:\\s*'([a-zA-Z0-9.-]+)',\\s*version:\\s*'([a-zA-Z0-9.-]+)'");

    public List<Dependency> getGroovyDependencies() {
        List<Dependency> dependencies = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(buildFileGroovy)) {
            boolean inTagBlock = false;
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith(tagName + " {")) {
                    inTagBlock = true;
                } else if (line.equals("}")) {
                    inTagBlock = false;
                }

                if (inTagBlock) {

                    if (line.startsWith("implementation") || line.startsWith("compile") || line.startsWith("testCompile") || line.startsWith("testRuntime") || line.startsWith("runtimeOnly") || line.startsWith("testImplementation") || line.startsWith("testRuntimeOnly")) {

                        Matcher matcher;
                        Matcher matcherPostProcess;

                        if (line.contains("group:") && line.contains("name:") && line.contains("version:")) {

                            matcher = dependencyPatternGNV.matcher(line);

                        } else {

                            matcher = dependencyPatternNoGNV.matcher(line);

                        }

                        if (matcher.matches()) {

                            logger.debug("match groups:");
                            for (int i = 1; i <= matcher.groupCount(); i++) {
                                logger.debug(matcher.group(i));
                            }

                            String configuration = matcher.group(1).toLowerCase();
                            String preprocessingStr = matcher.group(2);

                            if (preprocessingStr.contains("group:") && preprocessingStr.contains("name:") && preprocessingStr.contains("version:")) {

                                matcherPostProcess = dependencyGroupPatternSecond.matcher(preprocessingStr);

                            } else {

                                matcherPostProcess = dependencyGroupPatternFirst.matcher(preprocessingStr);

                            }

                            if (matcherPostProcess.matches()) {

                                logger.debug("match groups:");
                                logger.debug(matcherPostProcess.group());
                                for (int i = 1; i <= matcherPostProcess.groupCount(); i++) {
                                    logger.debug(matcherPostProcess.group(i));
                                }

                                String group = matcherPostProcess.group(1);
                                String name = matcherPostProcess.group(2);
                                String version = matcherPostProcess.groupCount() == 3 ? matcherPostProcess.group(3) : "";

                                dependencies.add(
                                        Dependency.builder()
                                                .configuration(configuration)
                                                .group(group)
                                                .name(name)
                                                .version(version)
                                                .build()
                                );

                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return dependencies;
    }


}
