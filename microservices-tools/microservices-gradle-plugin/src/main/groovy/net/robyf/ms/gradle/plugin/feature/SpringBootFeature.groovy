package net.robyf.ms.gradle.plugin.feature

import net.robyf.ms.gradle.plugin.Feature
import org.gradle.api.Project
import org.springframework.boot.gradle.plugin.SpringBootPlugin

class SpringBootFeature implements Feature {

    @Override
    void apply(Project project) {
        project.getPlugins().apply(SpringBootPlugin.class);
    }

}
