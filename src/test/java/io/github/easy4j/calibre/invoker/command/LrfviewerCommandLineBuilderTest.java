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
import io.github.easy4j.calibre.invoker.request.LrfviewerInvocationRequest;

/** Tests for {@link LrfviewerCommandLineBuilder}. */
public class LrfviewerCommandLineBuilderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void acceptsItsRequestInterfaceAndEmitsExactTokens() throws Exception {
        File input = temporaryFolder.newFile("book with spaces.lrf");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setDisableHyphenation(true);
        request.setProfile(true);
        request.setVisualDebug(true);
        request.setWhiteBackground(true);
        request.setLrsFile(input);

        Commandline cli = new Commandline();
        new LrfviewerCommandLineBuilder().doCommandInternal(request, cli);

        assertArrayEquals(new String[] {
                "--disable-hyphenation", "--profile", "--visual-debug",
                "--white-background", input.getAbsolutePath()
        }, cli.getArguments());
    }

    @Test
    public void defaultOptionsEmitOnlyTheRequiredInputFile() throws Exception {
        File input = temporaryFolder.newFile("default-book.lrf");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrsFile(input);

        Commandline cli = new Commandline();
        new LrfviewerCommandLineBuilder().doCommandInternal(request, cli);

        assertArrayEquals(new String[] {input.getAbsolutePath()}, cli.getArguments());
    }

    @Test
    public void usesLrfviewerExecutableResolvedFromCalibreHome() throws Exception {
        File input = temporaryFolder.newFile("book.lrf");
        File calibreHome = temporaryFolder.newFolder("calibre-home");
        File executable = createExecutable(calibreHome, "lrfviewer");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrsFile(input);
        LrfviewerCommandLineBuilder builder = new LrfviewerCommandLineBuilder();
        builder.setCalibreHome(calibreHome);

        Commandline cli = builder.build(request);

        assertEquals(executable.getCanonicalPath(), cli.getCommandline()[0]);
    }

    @Test
    public void rejectsWrongRequestTypeWithCheckedError() {
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new LrfviewerCommandLineBuilder().doCommandInternal(
                        new DefaultWeb2diskInvocationRequest(), new Commandline()));
        assertTrue(exception.getMessage().contains("LrfviewerInvocationRequest"));
    }

    @Test
    public void rejectsNullRequestWithoutNullPointerException() {
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new LrfviewerCommandLineBuilder().build(null));
        assertTrue(exception.getMessage().contains("LrfviewerInvocationRequest"));
    }

    @Test
    public void rejectsMissingLrsFileWithFieldSpecificError() {
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new LrfviewerCommandLineBuilder().doCommandInternal(
                        new InterfaceOnlyRequest(), new Commandline()));
        assertTrue(exception.getMessage().contains("lrsFile"));
    }

    @Test
    public void rejectsNonexistentLrsFileWithoutLeakingItsAbsolutePath() {
        File missing = new File(temporaryFolder.getRoot(), "private/missing-book.lrf");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrsFile(missing);

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new LrfviewerCommandLineBuilder().doCommandInternal(request, new Commandline()));
        assertTrue(exception.getMessage().contains("lrsFile"));
        assertFalse(exception.getMessage().contains(missing.getAbsolutePath()));
    }

    @Test
    public void rejectsDirectoryAsLrsFile() throws Exception {
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrsFile(temporaryFolder.newFolder("not-a-file.lrf"));

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new LrfviewerCommandLineBuilder().doCommandInternal(request, new Commandline()));
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
            implements LrfviewerInvocationRequest {

        private boolean disableHyphenation;
        private boolean profile;
        private File lrsFile;
        private boolean visualDebug;
        private boolean whiteBackground;

        @Override
        public boolean isDisableHyphenation() {
            return disableHyphenation;
        }

        @Override
        public boolean isProfile() {
            return profile;
        }

        @Override
        public File getLrsFile() {
            return lrsFile;
        }

        @Override
        public boolean isVisualDebug() {
            return visualDebug;
        }

        @Override
        public boolean isWhiteBackground() {
            return whiteBackground;
        }

        @Override
        public InvocationRequest setDisableHyphenation(boolean value) {
            disableHyphenation = value;
            return this;
        }

        @Override
        public InvocationRequest setProfile(boolean value) {
            profile = value;
            return this;
        }

        @Override
        public InvocationRequest setVisualDebug(boolean value) {
            visualDebug = value;
            return this;
        }

        @Override
        public InvocationRequest setWhiteBackground(boolean value) {
            whiteBackground = value;
            return this;
        }

        @Override
        public InvocationRequest setLrsFile(File value) {
            lrsFile = value;
            return this;
        }
    }
}
