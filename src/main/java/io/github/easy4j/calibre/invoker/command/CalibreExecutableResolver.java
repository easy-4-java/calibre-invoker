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

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import org.codehaus.plexus.util.StringUtils;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;

/**
 * Resolves Calibre tool executables from explicitly configured homes, process
 * configuration, {@code PATH}, and the standard macOS application bundle.
 */
public class CalibreExecutableResolver {

    private static final String CALIBRE_HOME_PROPERTY = "calibre.home";
    private static final String CALIBRE_HOME_ENVIRONMENT = "CALIBRE_HOME";
    private static final String PATH_ENVIRONMENT = "PATH";
    private static final File MAC_OS_BUNDLE_DIRECTORY =
            new File("/Applications/calibre.app/Contents/MacOS");

    private final PropertyProbe propertyProbe;
    private final EnvironmentProbe environmentProbe;
    private final FileProbe fileProbe;
    private final boolean windows;
    private final boolean macOs;

    /**
     * Creates a resolver backed by the current JVM, process environment, and file system.
     */
    public CalibreExecutableResolver() {
        this(System::getProperty, System::getenv, new DefaultFileProbe(),
                System.getProperty("os.name", ""));
    }

    CalibreExecutableResolver(PropertyProbe propertyProbe, EnvironmentProbe environmentProbe,
            FileProbe fileProbe, String osName) {
        this.propertyProbe = Objects.requireNonNull(propertyProbe,
                "propertyProbe must not be null");
        this.environmentProbe = Objects.requireNonNull(environmentProbe,
                "environmentProbe must not be null");
        this.fileProbe = Objects.requireNonNull(fileProbe, "fileProbe must not be null");
        String normalizedOsName = StringUtils.isEmpty(osName)
                ? ""
                : osName.toLowerCase(Locale.ENGLISH);
        this.windows = normalizedOsName.contains("windows");
        this.macOs = normalizedOsName.contains("mac");
    }

    /**
     * Resolves a Calibre command using request, invoker, JVM, environment, PATH, and
     * macOS bundle precedence.
     *
     * @param commandName Calibre command name without a platform suffix.
     * @param requestHome request-level Calibre home, or {@code null}.
     * @param invokerHome invoker-level Calibre home, or {@code null}.
     * @return canonical executable file.
     * @throws CommandLineConfigurationException if configuration is invalid or the
     *         executable cannot be found.
     */
    public File resolve(String commandName, File requestHome, File invokerHome)
            throws CommandLineConfigurationException {
        if (StringUtils.isBlank(commandName)) {
            throw new CommandLineConfigurationException("Calibre command name must not be blank.");
        }

        String executableName = windows ? commandName + ".exe" : commandName;
        File candidate = resolveConfiguredHome(requestHome, executableName,
                "request calibreHome");
        if (Objects.nonNull(candidate)) {
            return candidate;
        }

        candidate = resolveConfiguredHome(invokerHome, executableName,
                "invoker calibreHome");
        if (Objects.nonNull(candidate)) {
            return candidate;
        }

        candidate = resolveConfiguredHome(toFile(propertyProbe.get(CALIBRE_HOME_PROPERTY)),
                executableName, "JVM property calibre.home");
        if (Objects.nonNull(candidate)) {
            return candidate;
        }

        candidate = resolveConfiguredHome(toFile(environmentProbe.get(CALIBRE_HOME_ENVIRONMENT)),
                executableName, "environment CALIBRE_HOME");
        if (Objects.nonNull(candidate)) {
            return candidate;
        }

        candidate = resolveFromPath(executableName);
        if (Objects.nonNull(candidate)) {
            return candidate;
        }

        if (macOs) {
            candidate = executable(new File(MAC_OS_BUNDLE_DIRECTORY, executableName),
                    "macOS application bundle");
            if (Objects.nonNull(candidate)) {
                return candidate;
            }
        }

        throw new CommandLineConfigurationException(notFoundMessage());
    }

    /**
     * Resolves an optional executable override. Relative overrides are resolved only within
     * configured Calibre homes; absolute overrides and their platform suffix are still verified
     * and canonicalized by this resolver.
     *
     * @param commandName Default Calibre command name without a platform suffix.
     * @param executableOverride Absolute or Calibre-home-relative executable override.
     * @param requestHome Request-level Calibre home, or {@code null}.
     * @param invokerHome Invoker-level Calibre home, or {@code null}.
     * @return The canonical executable file.
     * @throws CommandLineConfigurationException If the override is invalid or cannot be found.
     */
    File resolve(String commandName, File executableOverride, File requestHome, File invokerHome)
            throws CommandLineConfigurationException {
        if (Objects.isNull(executableOverride)) {
            return resolve(commandName, requestHome, invokerHome);
        }
        if (StringUtils.isBlank(commandName)) {
            throw new CommandLineConfigurationException("Calibre command name must not be blank.");
        }

        File platformOverride = withPlatformSuffix(executableOverride);
        if (platformOverride.isAbsolute()) {
            File candidate = executable(platformOverride, "configured calibreExecutable");
            if (Objects.nonNull(candidate)) {
                return candidate;
            }
            throw new CommandLineConfigurationException(
                    "Configured Calibre executable was not found.");
        }

        validateRelativeOverride(platformOverride);
        File candidate = resolveRelativeConfiguredHome(requestHome, platformOverride,
                "request calibreHome");
        if (Objects.nonNull(candidate)) {
            return candidate;
        }

        candidate = resolveRelativeConfiguredHome(invokerHome, platformOverride,
                "invoker calibreHome");
        if (Objects.nonNull(candidate)) {
            return candidate;
        }

        candidate = resolveRelativeConfiguredHome(
                toFile(propertyProbe.get(CALIBRE_HOME_PROPERTY)), platformOverride,
                "JVM property calibre.home");
        if (Objects.nonNull(candidate)) {
            return candidate;
        }

        candidate = resolveRelativeConfiguredHome(
                toFile(environmentProbe.get(CALIBRE_HOME_ENVIRONMENT)), platformOverride,
                "environment CALIBRE_HOME");
        if (Objects.nonNull(candidate)) {
            return candidate;
        }

        throw new CommandLineConfigurationException(
                "Relative Calibre executable was not found in configured Calibre homes.");
    }

    private File resolveConfiguredHome(File home, String executableName, String source)
            throws CommandLineConfigurationException {
        if (Objects.isNull(home)) {
            return null;
        }
        if (!fileProbe.isDirectory(home)) {
            throw new CommandLineConfigurationException(
                    "Invalid Calibre directory from " + source + ".");
        }
        return executable(new File(home, executableName), source);
    }

    private File resolveRelativeConfiguredHome(File home, File relativeExecutable, String source)
            throws CommandLineConfigurationException {
        if (Objects.isNull(home)) {
            return null;
        }
        if (!fileProbe.isDirectory(home)) {
            throw new CommandLineConfigurationException(
                    "Invalid Calibre directory from " + source + ".");
        }

        File canonicalHome = canonical(home, source, "Calibre directory");
        File canonicalCandidate = canonical(new File(home, relativeExecutable.getPath()),
                source, "Calibre executable");
        if (!canonicalCandidate.toPath().startsWith(canonicalHome.toPath())) {
            throw new CommandLineConfigurationException(
                    "Configured relative Calibre executable must remain within " + source + ".");
        }
        if (!fileProbe.isExecutable(canonicalCandidate)) {
            return null;
        }
        return canonicalCandidate;
    }

    private File withPlatformSuffix(File executableOverride) {
        String path = executableOverride.getPath();
        if (windows && !path.toLowerCase(Locale.ENGLISH).endsWith(".exe")) {
            return new File(path + ".exe");
        }
        return executableOverride;
    }

    private void validateRelativeOverride(File executableOverride)
            throws CommandLineConfigurationException {
        if (executableOverride.toPath().normalize().startsWith("..")) {
            throw new CommandLineConfigurationException(
                    "Configured relative Calibre executable must remain within calibreHome.");
        }
    }

    private File canonical(File candidate, String source, String description)
            throws CommandLineConfigurationException {
        try {
            return fileProbe.canonical(candidate);
        } catch (IOException ignored) {
            throw new CommandLineConfigurationException(
                    "Cannot canonicalize " + description + " from " + source + ".");
        }
    }

    private File resolveFromPath(String executableName)
            throws CommandLineConfigurationException {
        String path = environmentProbe.get(PATH_ENVIRONMENT);
        if (StringUtils.isBlank(path)) {
            return null;
        }

        String separator = windows ? ";" : ":";
        String[] entries = path.split(separator, -1);
        for (String entry : entries) {
            if (StringUtils.isBlank(entry)) {
                continue;
            }
            File candidate = executable(new File(entry, executableName), "PATH");
            if (Objects.nonNull(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private File executable(File candidate, String source)
            throws CommandLineConfigurationException {
        if (!fileProbe.isExecutable(candidate)) {
            return null;
        }
        try {
            return fileProbe.canonical(candidate);
        } catch (IOException ignored) {
            throw new CommandLineConfigurationException(
                    "Cannot canonicalize Calibre executable from " + source + ".");
        }
    }

    private File toFile(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        return new File(path);
    }

    private String notFoundMessage() {
        String sources = "request calibreHome, invoker calibreHome, JVM property calibre.home, "
                + "environment CALIBRE_HOME, PATH";
        if (macOs) {
            sources += ", macOS application bundle";
        }
        return "Calibre executable was not found. Checked sources: " + sources + ".";
    }

    interface PropertyProbe {
        String get(String name);
    }

    interface EnvironmentProbe {
        String get(String name);
    }

    interface FileProbe {
        boolean isDirectory(File candidate);

        boolean isExecutable(File candidate);

        File canonical(File candidate) throws IOException;
    }

    private static final class DefaultFileProbe implements FileProbe {

        @Override
        public boolean isDirectory(File candidate) {
            return candidate.isDirectory();
        }

        @Override
        public boolean isExecutable(File candidate) {
            return candidate.isFile() && candidate.canExecute();
        }

        @Override
        public File canonical(File candidate) throws IOException {
            return candidate.getCanonicalFile();
        }
    }
}
