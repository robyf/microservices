package net.robyf.ms.gradle.plugin.feature

import net.robyf.ms.gradle.plugin.Feature
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class DockerFeature implements Feature {

    @Override
    void apply(Project project) {
        project.configurations.create("docker")
        project.sourceSets {
            docker {
                resources {
                    srcDir 'src/main/docker'
                }
            }
        }
        project.tasks.processDockerResources {
            expand(project.properties)
        }

        project.tasks.register('copyDockerfile', Copy) {
            from 'build/resources/docker/Dockerfile'
            into 'build'
        }

        project.tasks.processDockerResources.finalizedBy(project.tasks.copyDockerfile)
        project.tasks.assemble.dependsOn(project.tasks.processDockerResources)

        project.afterEvaluate {
            if (project.getPlugins().hasPlugin('org.springframework.boot')) {
                project.tasks.bootJar {
                    archiveName "app.jar"
                }
            }
        }
    }

}
