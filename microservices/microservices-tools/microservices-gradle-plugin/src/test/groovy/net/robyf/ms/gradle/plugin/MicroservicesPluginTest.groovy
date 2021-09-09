package net.robyf.ms.gradle.plugin

import net.robyf.ms.gradle.plugin.MicroservicesPlugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.assertj.core.api.Assertions.assertThat

class MicroservicesPluginTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    void testApplyPlugin() throws Exception {
        File tmpDir = folder.newFolder();

        Project project = ProjectBuilder.builder().withProjectDir(tmpDir).build();
        project.getPlugins().apply("java");
        project.getPlugins().apply(MicroservicesPlugin.class);

        project.evaluate();

        assertThat(project.getPlugins().hasPlugin(MicroservicesPlugin.class)).isTrue();
    }

}
