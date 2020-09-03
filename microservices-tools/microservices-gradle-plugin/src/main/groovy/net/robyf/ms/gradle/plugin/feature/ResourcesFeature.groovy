package net.robyf.ms.gradle.plugin.feature

import net.robyf.ms.gradle.plugin.Feature
import org.gradle.api.Project

class ResourcesFeature implements Feature {

    @Override
    void apply(Project project) {
        project.afterEvaluate {
            project.tasks.processResources {
                expand(project.properties)
            }
        }
    }

}
