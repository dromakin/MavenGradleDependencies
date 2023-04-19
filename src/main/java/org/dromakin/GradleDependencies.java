/*
 * File:     GradleDependencies
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class GradleDependencies implements ProjectDependencies {

    private static final Logger logger = LogManager.getLogger(GradleDependencies.class);

    protected static final String tagName = "dependencies";
    protected static final Pattern dependencyGroovyPatternGNV = java.util.regex.Pattern.compile("\\s*(\\w+)\\s+(.+)");
    protected static final Pattern dependencyGroovyPatternNoGNV = Pattern.compile("\\s*(\\w*)\\s*\\(?'?\"?([a-zA-Z0-9.\\-:]+)'?\"?\\)?");
    protected static final Pattern dependencyGroovyGroupPatternFirst = Pattern.compile("([a-zA-Z0-9.\\-]+):([a-zA-Z0-9.\\-]+):([a-zA-Z0-9.\\-]+)");
    protected static final Pattern dependencyGroovyGroupPatternSecond = Pattern.compile("group:\\s*'([a-zA-Z0-9.\\-]+)',\\s*name:\\s*'([a-zA-Z0-9.\\-]+)',\\s*version:\\s*'([a-zA-Z0-9.\\-]+)'");

//    protected static final Pattern dependencyKotlinPatternNoGNV = Pattern.compile("\\s*(\\w*)\\s*\\(\"([a-zA-Z0-9.\\-:]+)\"\\)");
    protected static final Pattern dependencyKotlinPattern = Pattern.compile("\\s*(\\w*)\\((.+)\\)\\s*\\{?");
    protected static final Pattern dependencyKotlinGroupPatternFirst = Pattern.compile("\"" + dependencyGroovyGroupPatternFirst.pattern() + "\"");
    protected static final Pattern dependencyKotlinGroupPatternSecond = Pattern.compile("group\\s*=\\s*\"([a-zA-Z0-9.\\-]+)\",\\s*name\\s*=\\s*\"([a-zA-Z0-9.\\-]+)\",\\s*version\\s*=\\s*\"([a-zA-Z0-9.\\-]+)\"");
    protected static final Pattern dependencyKotlinGroupPatternThird = Pattern.compile("\"([a-zA-Z0-9.\\-]+)\",\\s*\"([a-zA-Z0-9.\\-]+)\",\\s*\\s*\"([a-zA-Z0-9.\\-]+)\"");
    public abstract List<Dependency> getDependencies();

    protected void extractDependency(List<Dependency> dependencies, Matcher matcherPostProcess, String configuration) {
        if (matcherPostProcess.matches()) {

            logger.debug("match groups:");
            logger.debug(matcherPostProcess.group());
            for (int i = 1; i <= matcherPostProcess.groupCount(); i++) {
                logger.debug(matcherPostProcess.group(i));
            }

            String group = matcherPostProcess.group(1);
            String name = matcherPostProcess.groupCount() == 3 ? matcherPostProcess.group(2) : "";
            String version = matcherPostProcess.groupCount() == 3 ? matcherPostProcess.group(3) : "";

            if (!name.isEmpty()) {
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
