package net.robyf.ms.gradle.plugin.feature;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringBootFeatureTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testApplyFeature() throws Exception {
        File tmpDir = folder.newFolder();

        Project project = ProjectBuilder.builder().withProjectDir(tmpDir).build();
        project.getPlugins().apply(TestPlugin.class);

        assertThat(project.getPlugins().hasPlugin(SpringBootPlugin.class));
    }

    final static class TestPlugin implements Plugin<Project> {

        @Override
        public void apply(final Project project) {
            new SpringBootFeature().apply(project);
        }

    }

}
