/*
 * File:     MavenDependencies
 * Package:  org.dromakin
 * Project:  RegExpDependencies
 *
 * Created by dromakin as 19.04.2023
 *
 * author - dromakin
 * maintainer - dromakin
 * version - 2023.04.19
 * copyright - ORGANIZATION_NAME Inc. 2023
 */
package org.dromakin.maven;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.dromakin.project.ProjectDependencies;
import org.dromakin.dependency.Dependency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MavenDependencies implements ProjectDependencies {

    private static final Logger logger = LogManager.getLogger(MavenDependencies.class);

    private static final String DEFAULT_SCOPE = "compile";

    private Path buildFile;

    @Override
    public List<Dependency> getDependencies() {
        List<Dependency> dependencies = new ArrayList<>();

        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(Files.newBufferedReader(buildFile));

            // Get the parent POM
            Parent parent = model.getParent();
            if (parent != null) {
                String groupId = parent.getGroupId();
                String artifactId = parent.getArtifactId();
                String version = parent.getVersion();

                Dependency dependency = Dependency.builder()
                        .group(groupId)
                        .name(artifactId)
                        .version(version)
                        .configuration(DEFAULT_SCOPE)
                        .build();

                dependencies.add(dependency);
            }

            // Get the properties
            Properties properties = model.getProperties();

            // Get the list of dependencies
            for (org.apache.maven.model.Dependency dependency : model.getDependencies()) {
                String groupId = dependency.getGroupId();
                String artifactId = dependency.getArtifactId();
                String version = dependency.getVersion();
                String configuration = dependency.getScope();

                if (version != null) {
                    if (version.startsWith("${") && version.endsWith("}")) {
                        String propertyName = version.substring(2, version.length() - 1);
                        String propertyValue = properties.getProperty(propertyName);
                        if (propertyValue != null) {
                            version = propertyValue;
                        }
                    }

                } else {
                    if (parent != null) {
                        version = parent.getVersion();
                    }
                }

                if (configuration == null) {
                    configuration = DEFAULT_SCOPE;
                }

                dependencies.add(
                        Dependency.builder()
                                .group(groupId)
                                .name(artifactId)
                                .version(version)
                                .configuration(configuration)
                                .build()
                );
            }

        } catch (XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        }

        return dependencies;
    }
}
