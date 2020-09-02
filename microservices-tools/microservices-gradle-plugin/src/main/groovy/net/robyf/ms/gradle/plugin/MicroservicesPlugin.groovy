package net.robyf.ms.gradle.plugin

import net.robyf.ms.gradle.plugin.feature.DependencyManagementFeature
import net.robyf.ms.gradle.plugin.feature.RepositoryFeature
import net.robyf.ms.gradle.plugin.feature.SpringBootFeature
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.PluginAware

class MicroservicesPlugin implements Plugin<PluginAware> {

    @Override
    void apply(PluginAware pluginAware) {
        if (pluginAware instanceof Project) {
            doApply(pluginAware)
        } else if (pluginAware instanceof Settings) {
            pluginAware.gradle.allprojects { p ->
                p.plugins.apply(MicroservicesPlugin)
            }
        } else if (pluginAware instanceof Gradle) {
            pluginAware.allprojects { p ->
                p.plugins.apply(MicroservicesPlugin)
            }
        } else {
            throw new IllegalArgumentException("${pluginAware.getClass()} is currently not supported as an apply target, please report if you need it")
        }
    }

    private void doApply(final Project project) {
        PluginProperties props = new PluginProperties();
        System.out.println(props.getProperty(PluginProperties.PLUGIN_VERSION));
        System.out.println(props.getProperty(PluginProperties.SPRING_BOOT_VERSION));

        new DependencyManagementFeature().apply(project);
        new SpringBootFeature().apply(project);
        new RepositoryFeature().apply(project);

    }

}
