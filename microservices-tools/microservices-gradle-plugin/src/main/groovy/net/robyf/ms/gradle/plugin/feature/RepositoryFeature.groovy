package net.robyf.ms.gradle.plugin.feature

import net.robyf.ms.gradle.plugin.Feature
import org.gradle.api.Project

class RepositoryFeature implements Feature {

    @Override
    void apply(Project project) {
        project.repositories {
            jcenter()
            maven {
                name = "MavenLocal"
                url = System.getenv("MICROSERVICES_REPO_PATH")
            }
        }
        if (project.getPlugins().hasPlugin('maven-publish')) {
            project.publishing {
                repositories {
                    maven {
                        name = "MavenLocal"
                        url = System.getenv("MICROSERVICES_REPO_PATH")
                    }
                }
            }
        }
    }

}
