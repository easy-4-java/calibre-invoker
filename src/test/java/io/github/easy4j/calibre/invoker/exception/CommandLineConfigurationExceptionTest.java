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
package io.github.easy4j.calibre.invoker.exception;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for {@link CommandLineConfigurationException}.
 */
public class CommandLineConfigurationExceptionTest {

    @Test
    public void shouldCreateWithMessage() {
        CommandLineConfigurationException ex = new CommandLineConfigurationException("config error");
        assertEquals("config error", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void shouldCreateWithMessageAndCause() {
        RuntimeException cause = new RuntimeException("root cause");
        CommandLineConfigurationException ex = new CommandLineConfigurationException("config error", cause);
        assertEquals("config error", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void shouldCreateWithNullMessage() {
        CommandLineConfigurationException ex = new CommandLineConfigurationException(null);
        assertNull(ex.getMessage());
    }

    @Test
    public void shouldCreateWithNullMessageAndNullCause() {
        CommandLineConfigurationException ex = new CommandLineConfigurationException(null, null);
        assertNull(ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void shouldExtendException() {
        CommandLineConfigurationException ex = new CommandLineConfigurationException("test");
        assertTrue(ex instanceof Exception);
    }

    @Test
    public void shouldHaveSerialVersionUID() throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = CommandLineConfigurationException.class.getDeclaredField("serialVersionUID");
        field.setAccessible(true);
        assertEquals(1L, field.getLong(null));
    }
}
