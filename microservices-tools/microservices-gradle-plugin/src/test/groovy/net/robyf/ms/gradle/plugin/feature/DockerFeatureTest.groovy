package net.robyf.ms.gradle.plugin.feature

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.springframework.boot.gradle.plugin.SpringBootPlugin

import java.util.stream.Collectors

import static org.assertj.core.api.Assertions.assertThat

class DockerFeatureTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    void testApplyFeatureWithoutSpringBoot() throws Exception {
        File tmpDir = folder.newFolder();

        Project project = ProjectBuilder.builder().withProjectDir(tmpDir).build();
        project.getPlugins().apply('java');
        project.getPlugins().apply(TestPlugin.class);

        project.evaluate();

        assertThat(project.getConfigurations().findByName("docker")).isNotNull();
    }

    @Test
    void testApplyFeatureWithSpringBoot() throws Exception {
        File tmpDir = folder.newFolder();

        Project project = ProjectBuilder.builder().withProjectDir(tmpDir).build();
        project.getPlugins().apply('java');
        project.getPlugins().apply(SpringBootPlugin.class);
        project.getPlugins().apply(TestPlugin.class);

        project.evaluate();

        assertThat(project.getConfigurations().findByName("docker")).isNotNull();
    }

    static final class TestPlugin implements Plugin<Project> {

        @Override
        void apply(Project project) {
            new DockerFeature().apply(project);
        }

    }

}
