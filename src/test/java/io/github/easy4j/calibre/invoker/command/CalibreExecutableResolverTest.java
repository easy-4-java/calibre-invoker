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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;

/**
 * Tests for deterministic, cross-platform Calibre executable resolution.
 */
public class CalibreExecutableResolverTest {

    @Test
    public void requestHomeWinsOverEveryOtherSource() throws Exception {
        MapFileProbe files = executableFiles(
                "/request/web2disk",
                "/invoker/web2disk",
                "/property/web2disk",
                "/environment/web2disk",
                "/path/web2disk");
        CalibreExecutableResolver resolver = resolverWith(files, "Linux",
                "/property", "/environment", "/path");

        File result = resolver.resolve("web2disk", file("/request"), file("/invoker"));

        assertEquals(file("/request/web2disk"), result);
    }

    @Test
    public void invokerHomeWinsOverJvmProperty() throws Exception {
        MapFileProbe files = executableFiles("/invoker/web2disk", "/property/web2disk");
        CalibreExecutableResolver resolver = resolverWith(files, "Linux",
                "/property", null, null);

        assertEquals(file("/invoker/web2disk"),
                resolver.resolve("web2disk", null, file("/invoker")));
    }

    @Test
    public void jvmPropertyWinsOverEnvironment() throws Exception {
        MapFileProbe files = executableFiles("/property/web2disk", "/environment/web2disk");
        CalibreExecutableResolver resolver = resolverWith(files, "Linux",
                "/property", "/environment", null);

        assertEquals(file("/property/web2disk"), resolver.resolve("web2disk", null, null));
    }

    @Test
    public void environmentWinsOverPath() throws Exception {
        MapFileProbe files = executableFiles("/environment/web2disk", "/path/web2disk");
        CalibreExecutableResolver resolver = resolverWith(files, "Linux",
                null, "/environment", "/path");

        assertEquals(file("/environment/web2disk"), resolver.resolve("web2disk", null, null));
    }

    @Test
    public void pathIsUsedWhenNoHomeIsConfigured() throws Exception {
        MapFileProbe files = executableFiles("/usr/bin/web2disk");
        CalibreExecutableResolver resolver = resolverWith(files, "Linux", null, null,
                "/missing:/usr/bin");

        assertEquals(file("/usr/bin/web2disk"), resolver.resolve("web2disk", null, null));
    }

    @Test
    public void windowsAddsExeSuffixAndUsesWindowsPathSeparator() throws Exception {
        MapFileProbe files = executableFiles("C:/Calibre/web2disk.exe");
        CalibreExecutableResolver resolver = resolverWith(files, "Windows 11", null, null,
                "C:/Missing;C:/Calibre");

        assertEquals(file("C:/Calibre/web2disk.exe"),
                resolver.resolve("web2disk", null, null));
    }

    @Test
    public void macOsBundleIsLastFallback() throws Exception {
        MapFileProbe files = executableFiles(
                "/Applications/calibre.app/Contents/MacOS/web2disk");
        CalibreExecutableResolver resolver = resolverWith(files, "Mac OS X", null, null,
                "/missing");

        assertEquals(file("/Applications/calibre.app/Contents/MacOS/web2disk"),
                resolver.resolve("web2disk", null, null));
    }

    @Test
    public void invalidConfiguredDirectoryNamesSourceWithoutExposingValue() throws Exception {
        MapFileProbe files = executableFiles();
        files.directory("/secret/request", false);
        CalibreExecutableResolver resolver = resolverWith(files, "Linux",
                "/secret/property", "/secret/environment", "/secret/path");

        try {
            resolver.resolve("web2disk", file("/secret/request"), null);
            fail("Expected invalid request home to fail");
        } catch (CommandLineConfigurationException exception) {
            assertTrue(exception.getMessage().contains("request calibreHome"));
            assertFalse(exception.getMessage().contains("/secret/request"));
            assertFalse(exception.getMessage().contains("/secret/property"));
        }
    }

    @Test
    public void notFoundMessageNamesCheckedSourcesWithoutExposingValues() throws Exception {
        MapFileProbe files = executableFiles();
        CalibreExecutableResolver resolver = resolverWith(files, "Linux",
                null, null, "/secret/path");

        try {
            resolver.resolve("web2disk", null, null);
            fail("Expected missing executable to fail");
        } catch (CommandLineConfigurationException exception) {
            String message = exception.getMessage();
            assertTrue(message.contains("JVM property calibre.home"));
            assertTrue(message.contains("environment CALIBRE_HOME"));
            assertTrue(message.contains("PATH"));
            assertFalse(message.contains("/secret/path"));
        }
    }

    private static CalibreExecutableResolver resolverWith(MapFileProbe files, String osName,
            String propertyHome, String environmentHome, String path) {
        return new CalibreExecutableResolver(
                name -> "calibre.home".equals(name) ? propertyHome : null,
                name -> {
                    if ("CALIBRE_HOME".equals(name)) {
                        return environmentHome;
                    }
                    if ("PATH".equals(name)) {
                        return path;
                    }
                    return null;
                },
                files,
                osName);
    }

    private static MapFileProbe executableFiles(String... paths) {
        MapFileProbe files = new MapFileProbe();
        for (String path : paths) {
            files.executable(path, true);
            File parent = file(path).getParentFile();
            if (parent != null) {
                files.directory(parent.getPath(), true);
            }
        }
        return files;
    }

    private static File file(String path) {
        return new File(path);
    }

    private static final class MapFileProbe implements CalibreExecutableResolver.FileProbe {

        private final Map<String, Boolean> directories = new HashMap<String, Boolean>();
        private final Map<String, Boolean> executables = new HashMap<String, Boolean>();

        private void directory(String path, boolean directory) {
            directories.put(file(path).getPath(), directory);
        }

        private void executable(String path, boolean executable) {
            executables.put(file(path).getPath(), executable);
        }

        @Override
        public boolean isDirectory(File candidate) {
            return Boolean.TRUE.equals(directories.get(candidate.getPath()));
        }

        @Override
        public boolean isExecutable(File candidate) {
            return Boolean.TRUE.equals(executables.get(candidate.getPath()));
        }

        @Override
        public File canonical(File candidate) throws IOException {
            return candidate;
        }
    }
}
