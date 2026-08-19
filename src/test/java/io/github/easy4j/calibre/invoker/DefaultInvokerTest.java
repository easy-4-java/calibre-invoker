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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.Os;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import io.github.easy4j.calibre.invoker.command.CommandLineBuilderRegistry;
import io.github.easy4j.calibre.invoker.command.Web2diskCommandLineBuilder;
import io.github.easy4j.calibre.invoker.exception.CalibreInvocationException;
import io.github.easy4j.calibre.invoker.request.DefaultEbookConvertInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;
import io.github.easy4j.calibre.invoker.support.RecordingProcessExecutor;

/**
 * Tests for {@link DefaultInvoker}.
 */
public class DefaultInvokerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

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
    public void unsupportedRequestBecomesCheckedInvocationError() {
        DefaultInvoker invoker = new DefaultInvoker(new RecordingProcessExecutor(0));
        DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();

        CalibreInvocationException exception = assertThrows(CalibreInvocationException.class,
                () -> invoker.execute(request));

        assertTrue(exception.getMessage().contains(request.getClass().getName()));
    }

    @Test
    public void nullRequestBecomesCheckedInvocationError() {
        DefaultInvoker invoker = new DefaultInvoker(new RecordingProcessExecutor(0));

        CalibreInvocationException exception = assertThrows(CalibreInvocationException.class,
                () -> invoker.execute(null));

        assertTrue(exception.getMessage().contains("must not be null"));
    }

    @Test
    public void injectedRegistryControlsBuilderRouting() throws Exception {
        CommandLineBuilderRegistry registry = new CommandLineBuilderRegistry()
                .register(DefaultEbookConvertInvocationRequest.class, Web2diskCommandLineBuilder::new);
        DefaultInvoker invoker = new DefaultInvoker(new RecordingProcessExecutor(0), registry);

        assertTrue(invoker.getCommandLineBuilder(new DefaultEbookConvertInvocationRequest())
                instanceof Web2diskCommandLineBuilder);
    }

    @Test
    public void executeDelegatesToInjectedProcessExecutor() throws Exception {
        RecordingProcessExecutor executor = new RecordingProcessExecutor(7);
        DefaultInvoker invoker = new DefaultInvoker(executor);
        invoker.setCalibreHome(createFakeCalibreHome());
        InvocationResult result = invoker.execute(validWeb2diskRequest());
        assertEquals(7, result.getExitCode());
        assertEquals(web2diskExecutableName(), executor.getExecutableName());
    }

    private File createFakeCalibreHome() throws IOException {
        File calibreHome = temporaryFolder.newFolder("calibre-home");
        assertTrue(new File(calibreHome, web2diskExecutableName()).createNewFile());
        return calibreHome;
    }

    private String web2diskExecutableName() {
        return Os.isFamily("windows") ? "web2disk.exe" : "web2disk";
    }

    private DefaultWeb2diskInvocationRequest validWeb2diskRequest() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setURL("https://example.com");
        return request;
    }
}
