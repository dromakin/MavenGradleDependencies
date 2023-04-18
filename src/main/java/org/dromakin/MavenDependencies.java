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
package org.dromakin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MavenDependencies implements ProjectDependencies {

    private static final Logger logger = LogManager.getLogger(MavenDependencies.class);

    private Path buildFile;

    @Override
    public List<Dependency> getDependencies() {

        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(Files.newBufferedReader(buildFile));

            // Get the parent POM
            Parent parent = model.getParent();
            if (parent != null) {
                System.out.println("Parent POM:");
                System.out.println("GroupId: " + parent.getGroupId());
                System.out.println("ArtifactId: " + parent.getArtifactId());
                System.out.println("Version: " + parent.getVersion());
                System.out.println();
            }

            // Get the properties
            Properties properties = model.getProperties();

            // Print the properties
            System.out.println("Properties:");
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String propertyString = entry.getKey() + "=" + entry.getValue();
                System.out.println(propertyString);
            }

            // Get the list of dependencies
            List<org.apache.maven.model.Dependency> dependencies = model.getDependencies();

            // Print the list of dependencies
            System.out.println("Dependencies:");
            for (org.apache.maven.model.Dependency dependency : dependencies) {
                String dependencyString = dependency.getGroupId() + ":" + dependency.getArtifactId()
                        + ":" + dependency.getVersion() + ":" + dependency.getScope();
                System.out.println(dependencyString);
            }

        } catch (Exception e) {
            System.err.println("Failed to read pom.xml: " + e.getMessage());
        }

        return null;
    }
}
