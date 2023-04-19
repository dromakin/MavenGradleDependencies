/*
 * File:     Dependencies
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
package org.dromakin.project;

import org.dromakin.dependency.Dependency;

import java.util.List;

public interface ProjectDependencies {
    List<Dependency> getDependencies();
}
