package net.robyf.ms.gradle.plugin;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class MicroservicesPluginTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testApplyPlugin() throws Exception {
        File tmpDir = folder.newFolder();

        Project project = ProjectBuilder.builder().withProjectDir(tmpDir).build();
        project.getPlugins().apply("java");
        project.getPlugins().apply(MicroservicesPlugin.class);

        assertThat(project.getPlugins().hasPlugin(MicroservicesPlugin.class)).isTrue();
    }

}
