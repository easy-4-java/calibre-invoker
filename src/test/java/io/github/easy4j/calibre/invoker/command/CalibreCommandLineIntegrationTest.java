package io.github.easy4j.calibre.invoker.command;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import io.github.easy4j.calibre.invoker.request.DefaultFetchEbookMetadataInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultLrf2lrsInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultLrfviewerInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultLrs2lrfInvocationRequest;

public class CalibreCommandLineIntegrationTest {

    @Rule
    public TemporaryFolder home = new TemporaryFolder();

    @Test
    public void buildsLrfviewerCommandWithItsOwnExecutableAndOptions() throws Exception {
        File executable = executable("lrfviewer");
        LrfviewerCommandLineBuilder builder = new LrfviewerCommandLineBuilder();
        builder.setCalibreHome(home.getRoot());
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        request.setLrsFile(new File("book.lrs"));
        request.setProfile(true);

        Commandline command = builder.build(request);

        assertTrue(command.getExecutable().contains(executable.getCanonicalPath()));
        assertArrayEquals(new String[]{"--profile", new File("book.lrs").getAbsolutePath()},
                command.getArguments());
    }

    @Test
    public void buildsLrs2lrfCommandWithOutputDirectory() throws Exception {
        File executable = executable("lrs2lrf");
        Lrs2lrfCommandLineBuilder builder = new Lrs2lrfCommandLineBuilder();
        builder.setCalibreHome(home.getRoot());
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();
        request.setLrsFile(new File("book.lrs"));
        request.setLrs(true);
        request.setOutputDirectory(home.getRoot());

        Commandline command = builder.build(request);

        assertTrue(command.getExecutable().contains(executable.getCanonicalPath()));
        assertArrayEquals(new String[]{"--lrs", "-o", home.getRoot().getCanonicalPath(),
                new File("book.lrs").getAbsolutePath()}, command.getArguments());
    }

    @Test
    public void buildsLrf2lrsCommandWithResourceOption() throws Exception {
        File executable = executable("lrf2lrs");
        Lrf2lrsCommandLineBuilder builder = new Lrf2lrsCommandLineBuilder();
        builder.setCalibreHome(home.getRoot());
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        request.setLrfFile(new File("book.lrf"));
        request.setDontOutputResources(true);

        Commandline command = builder.build(request);

        assertTrue(command.getExecutable().contains(executable.getCanonicalPath()));
        assertArrayEquals(new String[]{"--dont-output-resources", new File("book.lrf").getAbsolutePath()},
                command.getArguments());
    }

    @Test
    public void buildsMetadataCommandWithPluginAndCover() throws Exception {
        File executable = executable("fetch-ebook-metadata");
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        builder.setCalibreHome(home.getRoot());
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setCoverFile(new File("cover.jpg"));
        request.setAllowedPlugin("Google");

        Commandline command = builder.build(request);

        assertTrue(command.getExecutable().contains(executable.getCanonicalPath()));
        assertArrayEquals(new String[]{"--allowed-plugin", "Google", new File("cover.jpg").getAbsolutePath()},
                command.getArguments());
    }

    private File executable(String name) throws Exception {
        return home.newFile(name + (Os.isFamily("windows") ? ".exe" : ""));
    }
}
