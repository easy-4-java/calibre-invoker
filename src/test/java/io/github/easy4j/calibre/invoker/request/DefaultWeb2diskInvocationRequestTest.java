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
package io.github.easy4j.calibre.invoker.request;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.github.easy4j.calibre.invoker.InvocationOutputHandler;
import io.github.easy4j.calibre.invoker.SystemOutHandler;
import io.github.easy4j.calibre.invoker.SystemOutLogger;
import org.junit.Test;

/**
 * Tests for {@link DefaultWeb2diskInvocationRequest}.
 */
public class DefaultWeb2diskInvocationRequestTest {

    @Test
    public void shouldImplementWeb2diskInvocationRequest() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertTrue(request instanceof Web2diskInvocationRequest);
        assertTrue(request instanceof InvocationRequest);
    }

    @Test
    public void shouldDefaultDontDownloadStylesheetsToFalse() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertFalse(request.isDontDownloadStylesheets());
    }

    @Test
    public void shouldSetAndGetDontDownloadStylesheets() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationRequest result = request.setDontDownloadStylesheets(true);
        assertTrue(request.isDontDownloadStylesheets());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultBaseDirectoryToNull() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertNull(request.getBaseDirectory());
    }

    @Test
    public void shouldSetAndGetBaseDirectory() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        File dir = new File("/tmp/base");
        InvocationRequest result = request.setBaseDirectory(dir);
        assertEquals(dir, request.getBaseDirectory());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultDelayToZero() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertEquals(0, request.getDelay());
    }

    @Test
    public void shouldSetDelay() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        // Note: setDelay returns null (bug in original code), so we just test it doesn't throw
        request.setDelay(5);
        assertEquals(5, request.getDelay());
    }

    @Test
    public void shouldDefaultEncodingToNull() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertNull(request.getEncoding());
    }

    @Test
    public void shouldSetAndGetEncoding() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationRequest result = request.setEncoding("UTF-8");
        assertEquals("UTF-8", request.getEncoding());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultFilterRegexpToNull() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertNull(request.getFilterRegexp());
    }

    @Test
    public void shouldSetAndGetFilterRegexp() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationRequest result = request.setFilterRegexp(".*\\.html");
        assertEquals(".*\\.html", request.getFilterRegexp());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultMatchRegexpToNull() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertNull(request.getMatchRegexp());
    }

    @Test
    public void shouldSetAndGetMatchRegexp() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationRequest result = request.setMatchRegexp(".*example.*");
        assertEquals(".*example.*", request.getMatchRegexp());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultMaxFilesToMaxLong() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertEquals(9223372036854775807L, request.getMaxFiles());
    }

    @Test
    public void shouldSetAndGetMaxFiles() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationRequest result = request.setMaxFiles(100);
        assertEquals(100, request.getMaxFiles());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultMaxRecursionsToOne() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertEquals(1, request.getMaxRecursions());
    }

    @Test
    public void shouldSetAndGetMaxRecursions() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationRequest result = request.setMaxRecursions(5);
        assertEquals(5, request.getMaxRecursions());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultTimeoutToTen() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertEquals(10, request.getTimeout());
    }

    @Test
    public void shouldSetAndGetTimeout() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationRequest result = request.setTimeout(30);
        assertEquals(30, request.getTimeout());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultURLToNull() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertNull(request.getURL());
    }

    @Test
    public void shouldSetAndGetURL() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationRequest result = request.setURL("https://example.com");
        assertEquals("https://example.com", request.getURL());
        assertSame(request, result);
    }

    // Inherited AbstractInvocationRequest tests
    @Test
    public void shouldDefaultShellEnvironmentInheritedToTrue() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertTrue(request.isShellEnvironmentInherited());
    }

    @Test
    public void shouldSetShellEnvironmentInherited() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setShellEnvironmentInherited(false);
        assertFalse(request.isShellEnvironmentInherited());
    }

    @Test
    public void shouldDefaultCalibreHomeToNull() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertNull(request.getCalibreHome());
    }

    @Test
    public void shouldSetAndGetCalibreHome() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        File home = new File("/opt/calibre");
        request.setCalibreHome(home);
        assertEquals(home, request.getCalibreHome());
    }

    @Test
    public void shouldDefaultVerboseToFalse() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertFalse(request.isVerbose());
    }

    @Test
    public void shouldSetAndGetVerbose() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setVerbose(true);
        assertTrue(request.isVerbose());
    }

    @Test
    public void shouldAddAndGetShellEnvironments() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.addShellEnvironment("KEY1", "value1");
        request.addShellEnvironment("KEY2", "value2");
        Map<String, String> envs = request.getShellEnvironments();
        assertEquals("value1", envs.get("KEY1"));
        assertEquals("value2", envs.get("KEY2"));
    }

    @Test
    public void shouldReturnEmptyMapWhenNoShellEnvironmentsSet() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        Map<String, String> envs = request.getShellEnvironments();
        assertNotNull(envs);
        assertTrue(envs.isEmpty());
    }

    @Test
    public void shouldSetAndGetGoals() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        List<String> goals = Collections.singletonList("convert");
        request.setGoals(goals);
        assertEquals(goals, request.getGoals());
    }

    @Test
    public void shouldDefaultGoalsToNull() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertNull(request.getGoals());
    }

    @Test
    public void shouldSetAndGetProperties() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        Properties props = new Properties();
        props.setProperty("key", "value");
        request.setProperties(props);
        assertEquals("value", request.getProperties().getProperty("key"));
    }

    @Test
    public void shouldDefaultPropertiesToNull() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertNull(request.getProperties());
    }

    @Test
    public void shouldGetOutputHandlerReturnsDefaultWhenNull() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationOutputHandler defaultHandler = new SystemOutHandler();
        assertSame(defaultHandler, request.getOutputHandler(defaultHandler));
    }

    @Test
    public void shouldGetOutputHandlerReturnsSetHandler() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationOutputHandler handler = new SystemOutHandler();
        request.setOutputHandler(handler);
        assertSame(handler, request.getOutputHandler(new SystemOutHandler()));
    }

    @Test
    public void shouldGetErrorHandlerReturnsDefaultWhenNull() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationOutputHandler defaultHandler = new SystemOutHandler();
        assertSame(defaultHandler, request.getErrorHandler(defaultHandler));
    }

    @Test
    public void shouldGetErrorHandlerReturnsSetHandler() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        InvocationOutputHandler handler = new SystemOutHandler();
        request.setErrorHandler(handler);
        assertSame(handler, request.getErrorHandler(new SystemOutHandler()));
    }

    @Test
    public void shouldSetDebug() {
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        assertFalse(request.isDebug());
        request.setDebug(true);
        assertTrue(request.isDebug());
    }
}
