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

import org.codehaus.plexus.util.cli.CommandLineException;
import org.junit.Test;

/**
 * Tests for {@link DefaultInvocationResult}.
 */
public class DefaultInvocationResultTest {

    @Test
    public void shouldReturnMinValueExitCodeByDefault() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        assertEquals(Integer.MIN_VALUE, result.getExitCode());
    }

    @Test
    public void shouldReturnNullExecutionExceptionByDefault() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        assertNull(result.getExecutionException());
    }

    @Test
    public void shouldSetAndGetExitCode() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        result.setExitCode(0);
        assertEquals(0, result.getExitCode());
    }

    @Test
    public void shouldSetAndGetNonZeroExitCode() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        result.setExitCode(1);
        assertEquals(1, result.getExitCode());
    }

    @Test
    public void shouldSetAndGetExecutionException() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        CommandLineException exception = new CommandLineException("test error");
        result.setExecutionException(exception);
        assertNotNull(result.getExecutionException());
        assertEquals("test error", result.getExecutionException().getMessage());
    }

    @Test
    public void shouldSetExecutionExceptionToNull() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        result.setExecutionException(new CommandLineException("error"));
        result.setExecutionException(null);
        assertNull(result.getExecutionException());
    }

    @Test
    public void shouldImplementInvocationResultInterface() {
        DefaultInvocationResult result = new DefaultInvocationResult();
        assertTrue(result instanceof InvocationResult);
    }
}
