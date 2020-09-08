package net.robyf.ms.gradle.plugin.feature

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import java.util.stream.Collectors

import static org.assertj.core.api.Assertions.assertThat

class RepositoryFeatureTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    void testApplyFeatureWithMavenPublish() throws Exception {
        File tmpDir = folder.newFolder();

        Project project = ProjectBuilder.builder().withProjectDir(tmpDir).build();
        project.getPlugins().apply('maven-publish');
        project.getPlugins().apply(TestPlugin.class);

        project.evaluate();

        List<String> dependencyRepos = project.getRepositories().stream().map{it.getName()}.collect(Collectors.toList());
        assertThat(dependencyRepos).containsExactly("BintrayJCenter", "MavenLocal");

        PublishingExtension publishingExtension = project.getExtensions().findByName('publishing');
        List<String> publishingRepos = publishingExtension.getRepositories().stream().map{it.getName()}.collect(Collectors.toList());
        assertThat(publishingRepos).containsExactly("MavenLocal");
    }

    @Test
    void testApplyFeatureWithoutMavenPublish() throws Exception {
        File tmpDir = folder.newFolder();

        Project project = ProjectBuilder.builder().withProjectDir(tmpDir).build();
        project.getPlugins().apply(TestPlugin.class);

        project.evaluate();

        List<String> dependencyRepos = project.getRepositories().stream().map{it.getName()}.collect(Collectors.toList());
        assertThat(dependencyRepos).containsExactly("BintrayJCenter", "MavenLocal");
    }

    static final class TestPlugin implements Plugin<Project> {

        @Override
        void apply(Project project) {
            new RepositoryFeature().apply(project);
        }

    }

}
