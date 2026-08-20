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
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.AbstractInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;
import io.github.easy4j.calibre.invoker.request.Lrf2lrsInvocationRequest;

/** Tests for {@link Lrf2lrsCommandLineBuilder}. */
public class Lrf2lrsCommandLineBuilderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void acceptsItsRequestInterfaceAndEmitsExactTokens() throws Exception {
        File input = temporaryFolder.newFile("book with spaces.lrf");
        File outputDirectory = temporaryFolder.newFolder("output with spaces");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setDontOutputResources(true);
        request.setOutputDirectory(outputDirectory);
        request.setLrfFile(input);

        Commandline cli = new Commandline();
        new Lrf2lrsCommandLineBuilder().doCommandInternal(request, cli);

        File outputFile = new File(outputDirectory, "book with spaces.lrs");
        assertFalse(outputFile.exists());
        assertArrayEquals(new String[] {
                "--dont-output-resources", "-o", outputFile.getCanonicalPath(),
                input.getAbsolutePath()
        }, cli.getArguments());
    }

    @Test
    public void derivesOutputNameForInputWithoutExtension() throws Exception {
        File input = temporaryFolder.newFile("extensionless");
        File outputDirectory = temporaryFolder.newFolder("extensionless-output");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(input);
        request.setOutputDirectory(outputDirectory);

        Commandline cli = new Commandline();
        new Lrf2lrsCommandLineBuilder().doCommandInternal(request, cli);

        assertArrayEquals(new String[] {
                "-o", new File(outputDirectory, "extensionless.lrs").getCanonicalPath(),
                input.getAbsolutePath()
        }, cli.getArguments());
    }

    @Test
    public void replacesOnlyLastExtensionForMultiDotInput() throws Exception {
        File input = temporaryFolder.newFile("archive.book.final.lrf");
        File outputDirectory = temporaryFolder.newFolder("multi-dot-output");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(input);
        request.setOutputDirectory(outputDirectory);

        Commandline cli = new Commandline();
        new Lrf2lrsCommandLineBuilder().doCommandInternal(request, cli);

        assertArrayEquals(new String[] {
                "-o", new File(outputDirectory, "archive.book.final.lrs").getCanonicalPath(),
                input.getAbsolutePath()
        }, cli.getArguments());
    }

    @Test
    public void rejectsDerivedOutputSymlinkThatEscapesOutputDirectory() throws Exception {
        File input = temporaryFolder.newFile("escape.lrf");
        File outputDirectory = temporaryFolder.newFolder("escape-output");
        File externalDirectory = temporaryFolder.newFolder("private-external-output");
        File externalTarget = new File(externalDirectory, "escape.lrs");
        assertTrue(externalTarget.createNewFile());
        Path outputLink = new File(outputDirectory, "escape.lrs").toPath();
        createSymbolicLinkOrSkip(outputLink, externalTarget.toPath());
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(input);
        request.setOutputDirectory(outputDirectory);

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrf2lrsCommandLineBuilder().doCommandInternal(
                        request, new Commandline()));

        assertTrue(exception.getMessage().contains("outputDirectory"));
        assertFalse(exception.getMessage().contains(externalTarget.getAbsolutePath()));
    }

    @Test
    public void acceptsDerivedOutputSymlinkWhoseCanonicalTargetRemainsInsideDirectory()
            throws Exception {
        File input = temporaryFolder.newFile("internal-link.lrf");
        File outputDirectory = temporaryFolder.newFolder("internal-link-output");
        File nestedDirectory = new File(outputDirectory, "nested");
        assertTrue(nestedDirectory.mkdir());
        File internalTarget = new File(nestedDirectory, "actual.lrs");
        assertTrue(internalTarget.createNewFile());
        Path outputLink = new File(outputDirectory, "internal-link.lrs").toPath();
        createSymbolicLinkOrSkip(outputLink, internalTarget.toPath());
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(input);
        request.setOutputDirectory(outputDirectory);

        Commandline cli = new Commandline();
        new Lrf2lrsCommandLineBuilder().doCommandInternal(request, cli);

        assertArrayEquals(new String[] {
                "-o", internalTarget.getCanonicalPath(), input.getAbsolutePath()
        }, cli.getArguments());
    }

    @Test
    public void defaultOptionsEmitOnlyTheRequiredInputFile() throws Exception {
        File input = temporaryFolder.newFile("default-book.lrf");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(input);

        Commandline cli = new Commandline();
        new Lrf2lrsCommandLineBuilder().doCommandInternal(request, cli);

        assertArrayEquals(new String[] {input.getAbsolutePath()}, cli.getArguments());
    }

    @Test
    public void usesLrf2lrsExecutableResolvedFromCalibreHome() throws Exception {
        File input = temporaryFolder.newFile("book.lrf");
        File calibreHome = temporaryFolder.newFolder("calibre-home");
        File executable = createExecutable(calibreHome, "lrf2lrs");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(input);
        Lrf2lrsCommandLineBuilder builder = new Lrf2lrsCommandLineBuilder();
        builder.setCalibreHome(calibreHome);

        Commandline cli = builder.build(request);

        assertEquals(executable.getCanonicalPath(), cli.getCommandline()[0]);
    }

    @Test
    public void honorsRelativeExecutableOverrideWithinCalibreHome() throws Exception {
        File input = temporaryFolder.newFile("relative-executable-book.lrf");
        File calibreHome = temporaryFolder.newFolder("relative-executable-calibre-home");
        File executable = createExecutable(calibreHome, "custom-lrf2lrs");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(input);
        Lrf2lrsCommandLineBuilder builder = new Lrf2lrsCommandLineBuilder();
        builder.setCalibreHome(calibreHome);
        builder.setCalibreExecutable(new File("custom-lrf2lrs"));

        Commandline cli = builder.build(request);

        assertEquals(executable.getCanonicalPath(), cli.getCommandline()[0]);
    }

    @Test
    public void rejectsWrongRequestTypeWithCheckedError() {
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrf2lrsCommandLineBuilder().doCommandInternal(
                        new DefaultWeb2diskInvocationRequest(), new Commandline()));
        assertTrue(exception.getMessage().contains("Lrf2lrsInvocationRequest"));
    }

    @Test
    public void rejectsNullRequestWithoutNullPointerException() {
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrf2lrsCommandLineBuilder().build(null));
        assertTrue(exception.getMessage().contains("Lrf2lrsInvocationRequest"));
    }

    @Test
    public void rejectsMissingLrfFileWithFieldSpecificError() {
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrf2lrsCommandLineBuilder().doCommandInternal(
                        new InterfaceOnlyRequest(), new Commandline()));
        assertTrue(exception.getMessage().contains("lrfFile"));
    }

    @Test
    public void rejectsNonexistentLrfFileWithoutLeakingItsAbsolutePath() {
        File missing = new File(temporaryFolder.getRoot(), "private/missing-book.lrf");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(missing);

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrf2lrsCommandLineBuilder().doCommandInternal(request, new Commandline()));
        assertTrue(exception.getMessage().contains("lrfFile"));
        assertFalse(exception.getMessage().contains(missing.getAbsolutePath()));
    }

    @Test
    public void rejectsDirectoryAsLrfFile() throws Exception {
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(temporaryFolder.newFolder("not-a-file.lrf"));

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrf2lrsCommandLineBuilder().doCommandInternal(request, new Commandline()));
        assertTrue(exception.getMessage().contains("lrfFile"));
        assertTrue(exception.getMessage().contains("file"));
    }

    @Test
    public void rejectsMissingOutputDirectoryWithoutLeakingItsPath() throws Exception {
        File input = temporaryFolder.newFile("missing-output-book.lrf");
        File missing = new File(temporaryFolder.getRoot(), "private/missing-output");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(input);
        request.setOutputDirectory(missing);

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrf2lrsCommandLineBuilder().doCommandInternal(request, new Commandline()));
        assertTrue(exception.getMessage().contains("outputDirectory"));
        assertFalse(exception.getMessage().contains(missing.getAbsolutePath()));
    }

    @Test
    public void rejectsFileAsOutputDirectory() throws Exception {
        File input = temporaryFolder.newFile("file-output-book.lrf");
        InterfaceOnlyRequest request = new InterfaceOnlyRequest();
        request.setLrfFile(input);
        request.setOutputDirectory(temporaryFolder.newFile("not-an-output-directory"));

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new Lrf2lrsCommandLineBuilder().doCommandInternal(request, new Commandline()));
        assertTrue(exception.getMessage().contains("outputDirectory"));
        assertTrue(exception.getMessage().contains("directory"));
    }

    private void createSymbolicLinkOrSkip(Path link, Path target) throws Exception {
        try {
            Files.createSymbolicLink(link, target);
        } catch (UnsupportedOperationException exception) {
            Assume.assumeNoException("Symbolic links are unsupported by this JVM/filesystem.",
                    exception);
        } catch (FileSystemException exception) {
            if (Os.isFamily("windows") && isWindowsSymlinkPrivilegeFailure(exception)) {
                Assume.assumeNoException("Windows symbolic-link privilege is unavailable.",
                        exception);
            }
            throw exception;
        }
    }

    private boolean isWindowsSymlinkPrivilegeFailure(FileSystemException exception) {
        String details = String.valueOf(exception.getReason()) + " " + exception.getMessage();
        String normalized = details.toLowerCase(Locale.ENGLISH);
        return exception instanceof AccessDeniedException
                || normalized.contains("privilege")
                || normalized.contains("access is denied")
                || normalized.contains("not permitted");
    }

    private File createExecutable(File calibreHome, String commandName) throws Exception {
        String executableName = Os.isFamily("windows") ? commandName + ".exe" : commandName;
        File executable = new File(calibreHome, executableName);
        assertTrue(executable.createNewFile());
        assertTrue(executable.setExecutable(true) || Os.isFamily("windows"));
        return executable;
    }

    private static final class InterfaceOnlyRequest extends AbstractInvocationRequest
            implements Lrf2lrsInvocationRequest {

        private boolean dontOutputResources;
        private File lrfFile;
        private File outputDirectory;

        @Override
        public boolean isDontOutputResources() {
            return dontOutputResources;
        }

        @Override
        public File getLrfFile() {
            return lrfFile;
        }

        @Override
        public File getOutputDirectory() {
            return outputDirectory;
        }

        @Override
        public InvocationRequest setDontOutputResources(boolean value) {
            dontOutputResources = value;
            return this;
        }

        @Override
        public InvocationRequest setOutputDirectory(File value) {
            outputDirectory = value;
            return this;
        }

        @Override
        public InvocationRequest setLrfFile(File value) {
            lrfFile = value;
            return this;
        }
    }
}
