package net.robyf.ms.gradle.plugin.feature;

import net.robyf.ms.gradle.plugin.Feature;
import org.gradle.api.Project;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;

public class SpringBootFeature implements Feature {

    @Override
    public void apply(final Project project) {
        project.getPlugins().apply(SpringBootPlugin.class);
    }

}
