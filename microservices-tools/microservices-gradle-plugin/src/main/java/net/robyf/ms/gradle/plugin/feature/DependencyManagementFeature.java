package net.robyf.ms.gradle.plugin.feature;

import io.spring.gradle.dependencymanagement.DependencyManagementPlugin;
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension;
import net.robyf.ms.gradle.plugin.Feature;
import net.robyf.ms.gradle.plugin.PluginProperties;
import org.gradle.api.Project;

public class DependencyManagementFeature implements Feature {

    private static final String BOM_COORDINATES = "net.robyf.ms:microservices-dependencies:";

    @Override
    public void apply(final Project project) {
        project.getPlugins().apply(DependencyManagementPlugin.class);

        PluginProperties props = new PluginProperties();

        project.getExtensions().findByType(DependencyManagementExtension.class)
                .imports(importsHandler -> importsHandler.mavenBom(BOM_COORDINATES + props.getProperty(PluginProperties.PLUGIN_VERSION)));
    }

}
