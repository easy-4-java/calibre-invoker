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
package io.github.easy4j.calibre.invoker;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.codehaus.plexus.util.Os;
import org.junit.Assume;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import io.github.easy4j.calibre.invoker.exception.CalibreInvocationException;
import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;

/**
 * Tests for {@link DefaultInvoker}.
 */
public class DefaultInvokerTest {

    @Rule
    public TemporaryFolder calibreHome = new TemporaryFolder();

    @Test
    public void shouldCreateWithDefaultValues() {
        DefaultInvoker invoker = new DefaultInvoker();
        assertNotNull(invoker.getLogger());
        assertNull(invoker.getWorkingDirectory());
        assertNull(invoker.getCalibreHome());
        assertNull(invoker.getEbookRepositoryDirectory());
    }

    @Test
    public void shouldHaveDefaultRoleHint() {
        assertEquals("default", DefaultInvoker.ROLE_HINT);
    }

    @Test
    public void shouldSetAndGetLogger() {
        DefaultInvoker invoker = new DefaultInvoker();
        InvokerLogger logger = new PrintStreamLogger();
        Invoker result = invoker.setLogger(logger);
        assertSame(logger, invoker.getLogger());
        assertSame(invoker, result);
    }

    @Test
    public void shouldUseDefaultLoggerWhenNullSet() {
        DefaultInvoker invoker = new DefaultInvoker();
        invoker.setLogger(null);
        assertNotNull(invoker.getLogger());
    }

    @Test
    public void shouldSetAndGetWorkingDirectory() {
        DefaultInvoker invoker = new DefaultInvoker();
        File dir = new File("/tmp/work");
        Invoker result = invoker.setWorkingDirectory(dir);
        assertEquals(dir, invoker.getWorkingDirectory());
        assertSame(invoker, result);
    }

    @Test
    public void shouldSetAndGetCalibreHome() {
        DefaultInvoker invoker = new DefaultInvoker();
        File home = new File("/opt/calibre");
        Invoker result = invoker.setCalibreHome(home);
        assertEquals(home, invoker.getCalibreHome());
        assertSame(invoker, result);
    }

    @Test
    public void shouldSetAndGetEbookRepositoryDirectory() {
        DefaultInvoker invoker = new DefaultInvoker();
        File repo = new File("/tmp/ebooks");
        Invoker result = invoker.setEbookRepositoryDirectory(repo);
        assertEquals(repo, invoker.getEbookRepositoryDirectory());
        assertSame(invoker, result);
    }

    @Test
    public void shouldSetOutputHandler() {
        DefaultInvoker invoker = new DefaultInvoker();
        InvocationOutputHandler handler = new SystemOutHandler();
        Invoker result = invoker.setOutputHandler(handler);
        assertSame(invoker, result);
    }

    @Test
    public void shouldSetErrorHandler() {
        DefaultInvoker invoker = new DefaultInvoker();
        InvocationOutputHandler handler = new SystemOutHandler();
        Invoker result = invoker.setErrorHandler(handler);
        assertSame(invoker, result);
    }

    @Test
    public void shouldImplementInvokerInterface() {
        DefaultInvoker invoker = new DefaultInvoker();
        assertTrue(invoker instanceof Invoker);
    }

    @Test
    public void shouldChainSetters() {
        DefaultInvoker invoker = new DefaultInvoker();
        File home = new File("/opt/calibre");
        File work = new File("/tmp/work");
        File repo = new File("/tmp/ebooks");

        Invoker result = invoker
                .setCalibreHome(home)
                .setWorkingDirectory(work)
                .setEbookRepositoryDirectory(repo)
                .setLogger(new SystemOutLogger())
                .setOutputHandler(new SystemOutHandler())
                .setErrorHandler(new SystemOutHandler());

        assertSame(invoker, result);
        assertEquals(home, invoker.getCalibreHome());
        assertEquals(work, invoker.getWorkingDirectory());
        assertEquals(repo, invoker.getEbookRepositoryDirectory());
    }

    @Test
    public void shouldReturnNullBuilderForUnknownRequestType() {
        DefaultInvoker invoker = new DefaultInvoker();
        // InvocationRequest is an interface, not a Web2diskInvocationRequest
        // so getCommandLineBuilder should return null
        assertNull(invoker.getCommandLineBuilder(new io.github.easy4j.calibre.invoker.request.DefaultEbookConvertInvocationRequest()));
    }

    @Test
    public void shouldReportMissingExecutableAsInvocationError() {
        DefaultInvoker invoker = new DefaultInvoker();
        invoker.setCalibreHome(calibreHome.getRoot());
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setURL("https://example.test");

        try {
            invoker.execute(request);
            fail("Expected a missing Calibre executable to be reported");
        } catch (CalibreInvocationException error) {
            assertTrue(error.getCause() instanceof CommandLineConfigurationException);
            assertTrue(error.getMessage().contains("Calibre executable not found"));
        }
    }

    @Test
    public void shouldReturnTheExitCodeOfAStartedCalibreProcess() throws Exception {
        Assume.assumeFalse(Os.isFamily("windows"));
        File executable = calibreHome.newFile("web2disk");
        Files.write(executable.toPath(), "#!/bin/sh\nexit 7\n".getBytes(StandardCharsets.UTF_8));
        assertTrue(executable.setExecutable(true));

        DefaultInvoker invoker = new DefaultInvoker();
        invoker.setCalibreHome(calibreHome.getRoot());
        invoker.setWorkingDirectory(calibreHome.getRoot());
        invoker.setEbookRepositoryDirectory(calibreHome.getRoot());
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setURL("https://example.test");

        InvocationResult result = invoker.execute(request);

        assertEquals(7, result.getExitCode());
        assertNull(result.getExecutionException());
    }
}
