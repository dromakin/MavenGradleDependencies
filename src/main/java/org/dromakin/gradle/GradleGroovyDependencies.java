/*
 * File:     GradleGroovyDependencies
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
package org.dromakin.gradle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dromakin.dependency.Dependency;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GradleGroovyDependencies extends GradleDependencies {

    private static final Logger logger = LogManager.getLogger(GradleGroovyDependencies.class);

    private Path buildFile;

    @Override
    public List<Dependency> getDependencies() {
        List<Dependency> dependencies = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(buildFile)) {
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

                        // comments processing
                        line = line.split("//")[0].trim();

                        Matcher matcher;
                        Matcher matcherPostProcess;

                        if (line.contains("group") && line.contains("name") && line.contains("version")) {

                            matcher = dependencyGroovyPatternGNV.matcher(line);

                        } else {

                            matcher = dependencyGroovyPatternNoGNV.matcher(line);

                        }

                        if (matcher.matches()) {

                            logger.debug("match groups:");
                            for (int i = 1; i <= matcher.groupCount(); i++) {
                                logger.debug(matcher.group(i));
                            }

                            String configuration = matcher.group(1).toLowerCase();
                            String preprocessingStr = matcher.group(2);

                            if (preprocessingStr.contains("group") && preprocessingStr.contains("name") && preprocessingStr.contains("version")) {

                                matcherPostProcess = dependencyGroovyGroupPatternSecond.matcher(preprocessingStr);

                            } else {

                                matcherPostProcess = dependencyGroovyGroupPatternFirst.matcher(preprocessingStr);

                            }

                            extractDependency(dependencies, matcherPostProcess, configuration);
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
