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

import org.junit.Test;

/**
 * Tests for {@link SystemOutLogger}.
 */
public class SystemOutLoggerTest {

    @Test
    public void shouldCreateWithDefaultConstructor() {
        SystemOutLogger logger = new SystemOutLogger();
        assertNotNull(logger);
    }

    @Test
    public void shouldExtendPrintStreamLogger() {
        SystemOutLogger logger = new SystemOutLogger();
        assertTrue(logger instanceof PrintStreamLogger);
    }

    @Test
    public void shouldImplementInvokerLogger() {
        SystemOutLogger logger = new SystemOutLogger();
        assertTrue(logger instanceof InvokerLogger);
    }

    @Test
    public void shouldHaveInfoThresholdByDefault() {
        SystemOutLogger logger = new SystemOutLogger();
        assertEquals(InvokerLogger.INFO, logger.getThreshold());
    }

    @Test
    public void shouldHaveInfoEnabledByDefault() {
        SystemOutLogger logger = new SystemOutLogger();
        assertTrue(logger.isInfoEnabled());
    }

    @Test
    public void shouldHaveDebugDisabledByDefault() {
        SystemOutLogger logger = new SystemOutLogger();
        assertFalse(logger.isDebugEnabled());
    }

    @Test
    public void shouldHaveWarnEnabledByDefault() {
        SystemOutLogger logger = new SystemOutLogger();
        assertTrue(logger.isWarnEnabled());
    }

    @Test
    public void shouldHaveErrorEnabledByDefault() {
        SystemOutLogger logger = new SystemOutLogger();
        assertTrue(logger.isErrorEnabled());
    }

    @Test
    public void shouldHaveFatalEnabledByDefault() {
        SystemOutLogger logger = new SystemOutLogger();
        assertTrue(logger.isFatalErrorEnabled());
    }

    @Test
    public void shouldLogWithoutError() {
        SystemOutLogger logger = new SystemOutLogger();
        // Should not throw
        logger.info("test message");
        logger.warn("warn message");
        logger.error("error message");
        logger.fatalError("fatal message");
    }
}
