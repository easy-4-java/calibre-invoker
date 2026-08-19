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

import static org.junit.Assert.*;

import java.io.File;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.DefaultFetchEbookMetadataInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;
import io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest;

/**
 * Tests for {@link FetchEbookMetadataCommandLineBuilder}.
 */
public class FetchEbookMetadataCommandLineBuilderTest {

    private static final String LEGACY_INTERFACE_SOURCE =
            "package io.github.easy4j.calibre.invoker.request;\n"
            + "import java.io.File;\n"
            + "public interface FetchEbookMetadataInvocationRequest extends InvocationRequest {\n"
            + " File getCoverFile(); String getAllowedPlugin(); boolean isAuthors();\n"
            + " boolean isIsbn(); boolean isOpf(); boolean isTitle(); long getTimeout();\n"
            + " InvocationRequest setAllowedPlugin(String value);\n"
            + " InvocationRequest setAuthors(boolean value); InvocationRequest setIsbn(boolean value);\n"
            + " InvocationRequest setCoverFile(File value); InvocationRequest setOpf(boolean value);\n"
            + " InvocationRequest setTimeout(long value); InvocationRequest setTitle(boolean value);\n"
            + "}\n";

    private static final String LEGACY_IMPLEMENTATION_SOURCE =
            "package legacy.fixture;\n"
            + "import java.io.File;\n"
            + "import io.github.easy4j.calibre.invoker.request.AbstractInvocationRequest;\n"
            + "import io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest;\n"
            + "import io.github.easy4j.calibre.invoker.request.InvocationRequest;\n"
            + "public final class LegacyFetchMetadataRequest extends AbstractInvocationRequest "
            + "implements FetchEbookMetadataInvocationRequest {\n"
            + " public File getCoverFile() { return null; }\n"
            + " public String getAllowedPlugin() { return \"Google\"; }\n"
            + " public boolean isAuthors() { return true; } public boolean isIsbn() { return false; }\n"
            + " public boolean isOpf() { return false; } public boolean isTitle() { return false; }\n"
            + " public long getTimeout() { return 30; }\n"
            + " public InvocationRequest setAllowedPlugin(String value) { return this; }\n"
            + " public InvocationRequest setAuthors(boolean value) { return this; }\n"
            + " public InvocationRequest setIsbn(boolean value) { return this; }\n"
            + " public InvocationRequest setCoverFile(File value) { return this; }\n"
            + " public InvocationRequest setOpf(boolean value) { return this; }\n"
            + " public InvocationRequest setTimeout(long value) { return this; }\n"
            + " public InvocationRequest setTitle(boolean value) { return this; }\n"
            + "}\n";

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldExtendAbstractCommandLineBuilder() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        assertTrue(builder instanceof AbstractCommandLineBuilder);
    }

    @Test
    public void shouldSetAllowedPlugin() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setAllowedPlugin("Google");

        Commandline cli = new Commandline();
        builder.setAllowedPlugin(request, cli);

        boolean foundPlugin = false;
        boolean foundValue = false;
        for (int i = 0; i < cli.getArguments().length; i++) {
            if ("--allowed-plugin".equals(cli.getArguments()[i])) {
                foundPlugin = true;
                if (i + 1 < cli.getArguments().length && "Google".equals(cli.getArguments()[i + 1])) {
                    foundValue = true;
                }
            }
        }
        assertTrue(foundPlugin);
        assertTrue(foundValue);
    }

    @Test
    public void shouldNotSetAllowedPluginWhenNull() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();

        Commandline cli = new Commandline();
        builder.setAllowedPlugin(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetAuthors() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setAuthors("Eric Evans");

        Commandline cli = new Commandline();
        builder.setAuthors(request, cli);

        assertArrayEquals(new String[] {"--authors", "Eric Evans"}, cli.getArguments());
    }

    @Test
    public void shouldNotSetAuthorsWhenFalse() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();

        Commandline cli = new Commandline();
        builder.setAuthors(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetCoverFile() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setCoverFile(new File("/tmp/cover.jpg"));

        Commandline cli = new Commandline();
        builder.setCoverFile(request, cli);

        boolean foundCover = false;
        for (int i = 0; i < cli.getArguments().length; i++) {
            if ("--cover".equals(cli.getArguments()[i])) {
                foundCover = true;
                break;
            }
        }
        assertTrue(foundCover);
    }

    @Test
    public void shouldNotSetCoverFileWhenNull() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();

        Commandline cli = new Commandline();
        builder.setCoverFile(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetIsbn() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setIsbn("9780321125217");

        Commandline cli = new Commandline();
        builder.setIsbn(request, cli);

        assertArrayEquals(new String[] {"--isbn", "9780321125217"}, cli.getArguments());
    }

    @Test
    public void shouldSetOpf() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setOpf(true);

        Commandline cli = new Commandline();
        builder.setOpf(request, cli);

        boolean found = false;
        for (String arg : cli.getArguments()) {
            if ("--opf".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldSetTimeout() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setTimeout(60);

        Commandline cli = new Commandline();
        builder.setTimeout(request, cli);

        boolean foundTimeout = false;
        boolean foundValue = false;
        for (int i = 0; i < cli.getArguments().length; i++) {
            if ("--timeout".equals(cli.getArguments()[i])) {
                foundTimeout = true;
                if (i + 1 < cli.getArguments().length && "60".equals(cli.getArguments()[i + 1])) {
                    foundValue = true;
                }
            }
        }
        assertTrue(foundTimeout);
        assertTrue(foundValue);
    }

    @Test
    public void shouldNotSetTimeoutWhenZero() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setTimeout(0);

        Commandline cli = new Commandline();
        builder.setTimeout(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetTitle() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setTitle("Domain-Driven Design");

        Commandline cli = new Commandline();
        builder.setTitle(request, cli);

        assertArrayEquals(new String[] {"--title", "Domain-Driven Design"}, cli.getArguments());
    }

    @Test
    public void shouldRejectWrongRequestType() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> builder.doCommandInternal(request, new Commandline()));
        assertTrue(exception.getMessage().contains("FetchEbookMetadataInvocationRequest"));
    }

    @Test
    public void shouldEmitExactMetadataTokenOrder() throws Exception {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        File cover = new File("target/cover files/domain driven design.jpg");
        request.setAllowedPlugins(java.util.Arrays.asList("Google", "Open Library"));
        request.setAuthors("Eric Evans");
        request.setCoverFile(cover);
        request.setIsbn("9780321125217");
        request.setOpf(true);
        request.setTimeout(30);
        request.setTitle("Domain-Driven Design");

        Commandline cli = new Commandline();
        builder.doCommandInternal(request, cli);

        assertArrayEquals(new String[] {
                "--allowed-plugin", "Google",
                "--allowed-plugin", "Open Library",
                "--authors", "Eric Evans",
                "--cover", cover.getCanonicalPath(),
                "--isbn", "9780321125217",
                "--opf",
                "--timeout", "30",
                "--title", "Domain-Driven Design"
        }, cli.getArguments());
    }

    @Test
    public void shouldAllowNullCoverWithoutThrowing() throws Exception {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setTitle("Domain-Driven Design");

        Commandline cli = new Commandline();
        new FetchEbookMetadataCommandLineBuilder().doCommandInternal(request, cli);

        assertArrayEquals(new String[] {
                "--timeout", "30", "--title", "Domain-Driven Design"
        }, cli.getArguments());
    }

    @Test
    public void shouldRequireAtLeastOneTypedLookupValue() {
        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new FetchEbookMetadataCommandLineBuilder().doCommandInternal(
                        new DefaultFetchEbookMetadataInvocationRequest(), new Commandline()));
        assertTrue(exception.getMessage().contains("title, authors or ISBN"));
    }

    @Test
    public void shouldRequirePositiveTimeout() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setTitle("Domain-Driven Design");
        request.setTimeout(0);

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new FetchEbookMetadataCommandLineBuilder().doCommandInternal(
                        request, new Commandline()));
        assertTrue(exception.getMessage().contains("timeout"));
    }

    @Test
    public void legacyTrueWithoutAuthorsValueFailsByFieldName() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setAuthors(true);

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new FetchEbookMetadataCommandLineBuilder().doCommandInternal(
                        request, new Commandline()));
        assertTrue(exception.getMessage().contains("authors"));
    }

    @Test
    public void legacyBinaryImplementationUsesDefaultsInsteadOfAbstractMethodError() throws Exception {
        Class<?> legacyType = compileAndLoadLegacyRequest();
        assertFalse(Arrays.stream(legacyType.getDeclaredMethods())
                .anyMatch(method -> Arrays.asList(
                        "getAllowedPlugins", "getAuthors", "getIsbn", "getTitle")
                        .contains(method.getName())));

        FetchEbookMetadataInvocationRequest request =
                (FetchEbookMetadataInvocationRequest) legacyType.getDeclaredConstructor().newInstance();
        assertEquals(Collections.singletonList("Google"), request.getAllowedPlugins());
        assertNull(request.getAuthors());
        assertNull(request.getIsbn());
        assertNull(request.getTitle());

        CommandLineConfigurationException exception = assertThrows(
                CommandLineConfigurationException.class,
                () -> new FetchEbookMetadataCommandLineBuilder().doCommandInternal(
                        request, new Commandline()));
        assertTrue(exception.getMessage().contains("authors"));
    }

    private Class<?> compileAndLoadLegacyRequest() throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull("Tests require a JDK compiler", compiler);
        Path sourceRoot = temporaryFolder.newFolder("legacy-source").toPath();
        Path classesRoot = temporaryFolder.newFolder("legacy-classes").toPath();
        Path interfaceSource = sourceRoot.resolve(
                "io/github/easy4j/calibre/invoker/request/FetchEbookMetadataInvocationRequest.java");
        Path implementationSource = sourceRoot.resolve(
                "legacy/fixture/LegacyFetchMetadataRequest.java");
        Files.createDirectories(interfaceSource.getParent());
        Files.createDirectories(implementationSource.getParent());
        Files.write(interfaceSource, LEGACY_INTERFACE_SOURCE.getBytes(StandardCharsets.UTF_8));
        Files.write(implementationSource, LEGACY_IMPLEMENTATION_SOURCE.getBytes(StandardCharsets.UTF_8));

        int exitCode = compiler.run(null, null, null,
                "-classpath", System.getProperty("java.class.path"),
                "-d", classesRoot.toString(),
                interfaceSource.toString(), implementationSource.toString());
        assertEquals("Legacy fixture must compile against the old interface", 0, exitCode);

        try (URLClassLoader loader = new URLClassLoader(
                new java.net.URL[] {classesRoot.toUri().toURL()},
                FetchEbookMetadataInvocationRequest.class.getClassLoader())) {
            return Class.forName("legacy.fixture.LegacyFetchMetadataRequest", true, loader);
        }
    }
}
