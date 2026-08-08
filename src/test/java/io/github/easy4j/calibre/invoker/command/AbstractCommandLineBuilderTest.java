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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Test;

import io.github.easy4j.calibre.invoker.InvokerLogger;
import io.github.easy4j.calibre.invoker.SystemOutLogger;
import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.DefaultEbookConvertInvocationRequest;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;

/**
 * Tests for {@link AbstractCommandLineBuilder}.
 */
public class AbstractCommandLineBuilderTest {

    /**
     * Concrete test subclass to test abstract methods.
     */
    private static class TestCommandLineBuilder extends AbstractCommandLineBuilder {
        @Override
        protected void doCommandInternal(InvocationRequest request, Commandline cli)
                throws CommandLineConfigurationException {
            // no-op for testing
        }

        @Override
        protected File findCalibreExecutable() throws CommandLineConfigurationException, IOException {
            // Return a dummy file for testing
            File dummy = new File("/tmp/calibre");
            return dummy;
        }
    }

    @Test
    public void shouldSetAndGetLogger() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        InvokerLogger logger = new SystemOutLogger();
        builder.setLogger(logger);
        assertSame(logger, builder.getLogger());
    }

    @Test
    public void shouldSetAndGetWorkingDirectory() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        File dir = new File("/tmp/work");
        builder.setWorkingDirectory(dir);
        assertEquals(dir, builder.getWorkingDirectory());
    }

    @Test
    public void shouldSetAndGetLocalRepositoryDirectory() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        File repo = new File("/tmp/repo");
        builder.setLocalRepositoryDirectory(repo);
        assertEquals(repo, builder.getLocalRepositoryDirectory());
    }

    @Test
    public void shouldSetAndGetCalibreHome() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        File home = new File("/opt/calibre");
        builder.setCalibreHome(home);
        assertEquals(home, builder.getCalibreHome());
    }

    @Test
    public void shouldSetAndGetCalibreExecutable() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        File exec = new File("/opt/calibre/calibre");
        builder.setCalibreExecutable(exec);
        assertEquals(exec, builder.getCalibreExecutable());
    }

    @Test
    public void shouldSetGoalsOnCommandLine() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();
        List<String> goals = Arrays.asList("goal1", "goal2");
        request.setGoals(goals);

        Commandline cli = new Commandline();
        builder.setGoals(request, cli);

        String[] args = cli.getArguments();
        assertTrue(args.length > 0);
    }

    @Test
    public void shouldNotSetGoalsWhenNull() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();

        Commandline cli = new Commandline();
        builder.setGoals(request, cli);

        String[] args = cli.getArguments();
        assertEquals(0, args.length);
    }

    @Test
    public void shouldNotSetGoalsWhenEmpty() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();
        request.setGoals(Arrays.asList());

        Commandline cli = new Commandline();
        builder.setGoals(request, cli);

        String[] args = cli.getArguments();
        assertEquals(0, args.length);
    }

    @Test
    public void shouldSetPropertiesOnCommandLine() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();
        Properties props = new Properties();
        props.setProperty("key1", "value1");
        props.setProperty("key2", "value2");
        request.setProperties(props);

        Commandline cli = new Commandline();
        builder.setProperties(request, cli);

        String[] args = cli.getArguments();
        assertTrue(args.length >= 4); // -D key=value pairs
    }

    @Test
    public void shouldNotSetPropertiesWhenNull() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();

        Commandline cli = new Commandline();
        builder.setProperties(request, cli);

        String[] args = cli.getArguments();
        assertEquals(0, args.length);
    }

    @Test
    public void shouldSetVerboseFlag() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();
        request.setVerbose(true);

        Commandline cli = new Commandline();
        builder.setVerbose(request, cli);

        String[] args = cli.getArguments();
        boolean found = false;
        for (String arg : args) {
            if ("--verbose".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldNotSetVerboseFlagWhenDisabled() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();
        request.setVerbose(false);

        Commandline cli = new Commandline();
        builder.setVerbose(request, cli);

        String[] args = cli.getArguments();
        for (String arg : args) {
            assertNotEquals("--verbose", arg);
        }
    }

    @Test
    public void shouldCheckRequiredStateSuccessfully() throws IOException {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        builder.setLogger(new SystemOutLogger());
        // Should not throw
        builder.checkRequiredState();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowWhenLoggerIsNull() throws IOException {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        builder.setLogger(null);
        builder.checkRequiredState();
    }

    @Test
    public void shouldSetShellEnvironmentWhenInherited() throws CommandLineConfigurationException {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();
        request.setShellEnvironmentInherited(true);

        Commandline cli = new Commandline();
        builder.setShellEnvironment(request, cli);

        // Should not throw
    }

    @Test
    public void shouldSetShellEnvironmentWithCalibreHome() throws CommandLineConfigurationException {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();
        request.setShellEnvironmentInherited(false);
        request.setCalibreHome(new File("/opt/calibre"));

        Commandline cli = new Commandline();
        builder.setShellEnvironment(request, cli);

        // Should not throw
    }

    @Test
    public void shouldSetShellEnvironmentWithCustomVars() throws CommandLineConfigurationException {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();
        request.setShellEnvironmentInherited(false);
        request.addShellEnvironment("CUSTOM_VAR", "custom_value");

        Commandline cli = new Commandline();
        builder.setShellEnvironment(request, cli);

        // Should not throw
    }

    @Test
    public void shouldHaveDefaultLogger() {
        TestCommandLineBuilder builder = new TestCommandLineBuilder();
        assertNotNull(builder.getLogger());
    }
}
