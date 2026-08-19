/**
 * Copyright (c) 2018, Loong Wan (https://github.com/loong10k).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.easy4j.calibre.invoker.command;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.AbstractInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;
import io.github.easy4j.calibre.invoker.request.Lrs2lrfInvocationRequest;

/** Tests for {@link Lrs2lrfCommandLineBuilder}. */
public class Lrs2lrfCommandLineBuilderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void acceptsItsRequestInterfaceAndEmitsExactTokens() throws Exception {
        File input = temporaryFolder.newFile("book with spaces.lrs");
        File outputDirectory = temporaryFolder.newFolder("output with spaces");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrs(true);
        request.setOutputDirectory(outputDirectory);
        request.setLrsFile(input);

        Commandline cli = new Commandline();
        new Lrs2lrfCommandLineBuilder().doCommandInternal(request, cli);

        assertArrayEquals(new String[] {
                "--lrs", "-o", outputDirectory.getCanonicalPath(), input.getAbsolutePath()
        }, cli.getArguments());
    }

    @Test
    public void defaultOptionsEmitOnlyTheRequiredInputFile() throws Exception {
        File input = temporaryFolder.newFile("default-book.lrs");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrsFile(input);

        Commandline cli = new Commandline();
        new Lrs2lrfCommandLineBuilder().doCommandInternal(request, cli);

        assertArrayEquals(new String[] {input.getAbsolutePath()}, cli.getArguments());
    }

    @Test
    public void usesLrs2lrfExecutableResolvedFromCalibreHome() throws Exception {
        File input = temporaryFolder.newFile("book.lrs");
        File calibreHome = temporaryFolder.newFolder("calibre-home");
        File executable = createExecutable(calibreHome, "lrs2lrf");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrsFile(input);
        Lrs2lrfCommandLineBuilder builder = new Lrs2lrfCommandLineBuilder();
        builder.setCalibreHome(calibreHome);

        Commandline cli = builder.build(request);

        assertEquals(executable.getCanonicalPath(), cli.getCommandline()[0]);
    }

    @Test
    public void rejectsWrongRequestTypeWithCheckedError() {
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrs2lrfCommandLineBuilder().doCommandInternal(
                        new DefaultWeb2diskInvocationRequest(), new Commandline()));
        assertTrue(exception.getMessage().contains("Lrs2lrfInvocationRequest"));
    }

    @Test
    public void rejectsNullRequestWithoutNullPointerException() {
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrs2lrfCommandLineBuilder().build(null));
        assertTrue(exception.getMessage().contains("Lrs2lrfInvocationRequest"));
    }

    @Test
    public void rejectsMissingLrsFileWithFieldSpecificError() {
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrs2lrfCommandLineBuilder().doCommandInternal(
                        new InterfaceOnlyRequest(), new Commandline()));
        assertTrue(exception.getMessage().contains("lrsFile"));
    }

    @Test
    public void rejectsNonexistentLrsFileWithoutLeakingItsAbsolutePath() {
        File missing = new File(temporaryFolder.getRoot(), "private/missing-book.lrs");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrsFile(missing);

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrs2lrfCommandLineBuilder().doCommandInternal(request, new Commandline()));
        assertTrue(exception.getMessage().contains("lrsFile"));
        assertFalse(exception.getMessage().contains(missing.getAbsolutePath()));
    }

    @Test
    public void rejectsDirectoryAsLrsFile() throws Exception {
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrsFile(temporaryFolder.newFolder("not-a-file.lrs"));

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrs2lrfCommandLineBuilder().doCommandInternal(request, new Commandline()));
        assertTrue(exception.getMessage().contains("lrsFile"));
        assertTrue(exception.getMessage().contains("file"));
    }

    private File createExecutable(File calibreHome, String commandName) throws Exception {
        String executableName = Os.isFamily("windows") ? commandName + ".exe" : commandName;
        File executable = new File(calibreHome, executableName);
        assertTrue(executable.createNewFile());
        assertTrue(executable.setExecutable(true) || Os.isFamily("windows"));
        return executable;
    }

    private static final class InterfaceOnlyRequest extends AbstractInvocationRequest
            implements Lrs2lrfInvocationRequest {

        private boolean lrs;
        private File lrsFile;
        private File outputDirectory;

        @Override
        public boolean isLrs() {
            return lrs;
        }

        @Override
        public File getLrsFile() {
            return lrsFile;
        }

        @Override
        public File getOutputDirectory() {
            return outputDirectory;
        }

        @Override
        public InvocationRequest setLrs(boolean value) {
            lrs = value;
            return this;
        }

        @Override
        public InvocationRequest setOutputDirectory(File value) {
            outputDirectory = value;
            return this;
        }

        @Override
        public InvocationRequest setLrsFile(File value) {
            lrsFile = value;
            return this;
        }
    }
}
