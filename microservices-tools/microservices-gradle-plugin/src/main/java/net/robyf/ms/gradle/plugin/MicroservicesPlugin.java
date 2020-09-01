package net.robyf.ms.gradle.plugin;

import net.robyf.ms.gradle.plugin.feature.DependencyManagementFeature;
import net.robyf.ms.gradle.plugin.feature.SpringBootFeature;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MicroservicesPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        PluginProperties props = new PluginProperties();
        System.out.println(props.getProperty(PluginProperties.PLUGIN_VERSION));
        System.out.println(props.getProperty(PluginProperties.SPRING_BOOT_VERSION));

        new DependencyManagementFeature().apply(project);
        new SpringBootFeature().apply(project);
    }

}
