/*
 * File:     Dependency
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

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter(AccessLevel.PUBLIC)
@ToString
public class Dependency {
    @ToString.Include
    private String configuration;
    @ToString.Include
    private String group;
    @ToString.Include
    private String name;
    @ToString.Include
    private String version;
}
