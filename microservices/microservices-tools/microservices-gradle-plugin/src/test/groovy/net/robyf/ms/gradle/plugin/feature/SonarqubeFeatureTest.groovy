package net.robyf.ms.gradle.plugin.feature

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.sonarqube.gradle.SonarQubePlugin

import static org.assertj.core.api.Assertions.assertThat

class SonarqubeFeatureTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    void testApplyFeature() throws Exception {
        File tmpDir = folder.newFolder();

        Project project = ProjectBuilder.builder().withProjectDir(tmpDir).build();
        project.getPlugins().apply('java');
        project.getPlugins().apply(TestPlugin.class);

        project.evaluate();

        assertThat(project.getPlugins().hasPlugin('jacoco')).isTrue();
        assertThat(project.getPlugins().hasPlugin(SonarQubePlugin.class));
    }

    static final class TestPlugin implements Plugin<Project> {

        @Override
        void apply(Project project) {
            new SonarqubeFeature().apply(project);
        }

    }

}
