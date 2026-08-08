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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

/**
 * Tests for {@link PrintStreamLogger}.
 */
public class PrintStreamLoggerTest {

    @Test
    public void shouldLogDebugMessageWhenThresholdIsDebug() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);

        logger.debug("debug msg");

        assertTrue(baos.toString().contains("[DEBUG]"));
        assertTrue(baos.toString().contains("debug msg"));
    }

    @Test
    public void shouldNotLogDebugMessageWhenThresholdIsInfo() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.INFO);

        logger.debug("debug msg");

        assertEquals("", baos.toString());
    }

    @Test
    public void shouldLogInfoMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.INFO);

        logger.info("info msg");

        assertTrue(baos.toString().contains("[INFO]"));
        assertTrue(baos.toString().contains("info msg"));
    }

    @Test
    public void shouldLogWarnMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.WARN);

        logger.warn("warn msg");

        assertTrue(baos.toString().contains("[WARN]"));
        assertTrue(baos.toString().contains("warn msg"));
    }

    @Test
    public void shouldLogErrorMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.ERROR);

        logger.error("error msg");

        assertTrue(baos.toString().contains("[ERROR]"));
        assertTrue(baos.toString().contains("error msg"));
    }

    @Test
    public void shouldLogFatalMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.FATAL);

        logger.fatalError("fatal msg");

        assertTrue(baos.toString().contains("[FATAL]"));
        assertTrue(baos.toString().contains("fatal msg"));
    }

    @Test
    public void shouldLogMessageWithThrowable() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);

        logger.error("error msg", new RuntimeException("cause"));

        assertTrue(baos.toString().contains("[ERROR]"));
        assertTrue(baos.toString().contains("error msg"));
        assertTrue(baos.toString().contains("Error:"));
        assertTrue(baos.toString().contains("RuntimeException"));
    }

    @Test
    public void shouldNotLogWhenMessageAndThrowableAreNull() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);

        logger.error(null, null);

        assertEquals("", baos.toString());
    }

    @Test
    public void shouldLogThrowableWithoutMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);

        logger.error(null, new RuntimeException("cause"));

        assertTrue(baos.toString().contains("Error:"));
    }

    @Test
    public void shouldLogNullMessageWithThrowable() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);

        logger.debug(null, new RuntimeException("test"));

        assertTrue(baos.toString().contains("[DEBUG]"));
        assertTrue(baos.toString().contains("Error:"));
    }

    @Test
    public void shouldLogInfoWithThrowable() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);

        logger.info("info", new RuntimeException("cause"));

        assertTrue(baos.toString().contains("[INFO]"));
        assertTrue(baos.toString().contains("Error:"));
    }

    @Test
    public void shouldLogWarnWithThrowable() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);

        logger.warn("warn", new RuntimeException("cause"));

        assertTrue(baos.toString().contains("[WARN]"));
        assertTrue(baos.toString().contains("Error:"));
    }

    @Test
    public void shouldLogFatalWithThrowable() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.DEBUG);

        logger.fatalError("fatal", new RuntimeException("cause"));

        assertTrue(baos.toString().contains("[FATAL]"));
        assertTrue(baos.toString().contains("Error:"));
    }

    @Test
    public void shouldReportDebugEnabled() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.DEBUG);
        assertTrue(logger.isDebugEnabled());
    }

    @Test
    public void shouldReportDebugDisabled() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.INFO);
        assertFalse(logger.isDebugEnabled());
    }

    @Test
    public void shouldReportInfoEnabled() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.INFO);
        assertTrue(logger.isInfoEnabled());
    }

    @Test
    public void shouldReportInfoDisabled() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.WARN);
        assertFalse(logger.isInfoEnabled());
    }

    @Test
    public void shouldReportWarnEnabled() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.WARN);
        assertTrue(logger.isWarnEnabled());
    }

    @Test
    public void shouldReportWarnDisabled() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.ERROR);
        assertFalse(logger.isWarnEnabled());
    }

    @Test
    public void shouldReportErrorEnabled() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.ERROR);
        assertTrue(logger.isErrorEnabled());
    }

    @Test
    public void shouldReportErrorDisabled() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.FATAL);
        assertFalse(logger.isErrorEnabled());
    }

    @Test
    public void shouldReportFatalEnabled() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.FATAL);
        assertTrue(logger.isFatalErrorEnabled());
    }

    @Test
    public void shouldReportFatalDisabledAtHigherThreshold() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.DEBUG);
        // FATAL (0) is always enabled when threshold is DEBUG (4)
        assertTrue(logger.isFatalErrorEnabled());
    }

    @Test
    public void shouldSetAndGetThreshold() {
        PrintStreamLogger logger = new PrintStreamLogger(System.out, InvokerLogger.INFO);
        assertEquals(InvokerLogger.INFO, logger.getThreshold());

        logger.setThreshold(InvokerLogger.DEBUG);
        assertEquals(InvokerLogger.DEBUG, logger.getThreshold());
    }

    @Test
    public void shouldDefaultToSystemOutAndInfoThreshold() {
        PrintStreamLogger logger = new PrintStreamLogger();
        assertEquals(InvokerLogger.INFO, logger.getThreshold());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullPrintStream() {
        new PrintStreamLogger(null, InvokerLogger.INFO);
    }

    @Test
    public void shouldImplementInvokerLogger() {
        PrintStreamLogger logger = new PrintStreamLogger();
        assertTrue(logger instanceof InvokerLogger);
    }

    @Test
    public void shouldNotFilterMessagesAtExactThreshold() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.WARN);

        logger.warn("at threshold");

        assertTrue(baos.toString().contains("[WARN]"));
        assertTrue(baos.toString().contains("at threshold"));
    }

    @Test
    public void shouldFilterMessagesAboveThreshold() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStreamLogger logger = new PrintStreamLogger(new PrintStream(baos), InvokerLogger.WARN);

        logger.info("above threshold");

        assertEquals("", baos.toString());
    }
}
