/*
 * File:     GradleKotlinDependencies
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
public class GradleKotlinDependencies extends GradleDependencies {

    private static final Logger logger = LogManager.getLogger(GradleKotlinDependencies.class);

    private Path buildFile;

    private boolean lineStarts(String line) {
        return line.startsWith("implementation") || line.startsWith("compile") || line.startsWith("api") || line.startsWith("runtimeOnly") || line.startsWith("compileOnly") || line.startsWith("provided") || line.startsWith("testImplementation") || line.startsWith("testCompile") || line.startsWith("testRuntime") || line.startsWith("testApi") || line.startsWith("testRuntimeOnly");
    }

    public List<Dependency> getDependencies() {
        List<Dependency> dependencies = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(buildFile)) {
            boolean inTagBlock = false;
            int countTagBlockClosed = 0;
            int countTagBlockOpened = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.contains("//")) {
                    line = line.split("//").length > 0 ? line.split("//")[0].trim() : line.trim();
                }

                if (line.contains("{")) {
                    countTagBlockOpened++;
                }

                if (line.startsWith(tagName + " {")) {
                    inTagBlock = true;
                    countTagBlockOpened++;

                } else if (line.equals("}")) {
                    countTagBlockClosed++;
                }

                if (countTagBlockClosed == countTagBlockOpened && countTagBlockClosed > 0) {
                    inTagBlock = false;
                    countTagBlockClosed = 0;
                    countTagBlockOpened = 0;
                }

                if (inTagBlock) {

                    if (lineStarts(line)) {

                        // comments processing
                        line = line.split("//")[0].trim();

                        Matcher matcherPostProcess;

                        Matcher matcher = dependencyKotlinPattern.matcher(line);

                        if (matcher.matches()) {

                            logger.debug("match groups:");
                            for (int i = 1; i <= matcher.groupCount(); i++) {
                                logger.debug(matcher.group(i));
                            }

                            String configuration = matcher.group(1).toLowerCase();
                            String preprocessingStr = matcher.group(2);

                            if (preprocessingStr.contains("group") && preprocessingStr.contains("name") && preprocessingStr.contains("version")) {

                                matcherPostProcess = dependencyKotlinGroupPatternSecond.matcher(preprocessingStr);

                            } else if (preprocessingStr.contains(",")) {

                                matcherPostProcess = dependencyKotlinGroupPatternThird.matcher(preprocessingStr);

                            } else {

                                matcherPostProcess = dependencyKotlinGroupPatternFirst.matcher(preprocessingStr);

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
