package net.robyf.ms.gradle.plugin.feature

import net.robyf.ms.gradle.plugin.Feature
import org.gradle.api.Project
import org.sonarqube.gradle.SonarQubePlugin

class SonarqubeFeature implements Feature {

    @Override
    void apply(Project project) {
        project.getPlugins().apply('jacoco');
        project.getPlugins().apply(SonarQubePlugin.class);

        project.afterEvaluate {
            project.tasks.jacocoTestReport {
                reports {
                    xml.enabled true
                }
            }

            project.tasks.test.finalizedBy(project.tasks.jacocoTestReport)
            project.tasks.sonarqube.dependsOn(project.tasks.check)
        }
    }

}
