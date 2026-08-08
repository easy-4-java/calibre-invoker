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
 * Tests for {@link PrintStreamHandler}.
 */
public class PrintStreamHandlerTest {

    @Test
    public void shouldWriteLineToOutputStream() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStreamHandler handler = new PrintStreamHandler(ps, false);

        handler.consumeLine("hello world");

        assertEquals("hello world" + System.lineSeparator(), baos.toString());
    }

    @Test
    public void shouldWriteEmptyLineForNullInput() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStreamHandler handler = new PrintStreamHandler(ps, false);

        handler.consumeLine(null);

        assertEquals(System.lineSeparator(), baos.toString());
    }

    @Test
    public void shouldFlushWhenAlwaysFlushEnabled() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream spied = new PrintStream(baos);
        PrintStreamHandler handler = new PrintStreamHandler(spied, true);

        handler.consumeLine("test");

        assertTrue(baos.toString().contains("test"));
    }

    @Test
    public void shouldDefaultToSystemOut() {
        PrintStreamHandler handler = new PrintStreamHandler();
        assertNotNull(handler);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullPrintStream() {
        new PrintStreamHandler(null, false);
    }

    @Test
    public void shouldImplementInvocationOutputHandler() {
        PrintStreamHandler handler = new PrintStreamHandler();
        assertTrue(handler instanceof InvocationOutputHandler);
    }

    @Test
    public void shouldWriteMultipleLines() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStreamHandler handler = new PrintStreamHandler(ps, false);

        handler.consumeLine("line1");
        handler.consumeLine("line2");
        handler.consumeLine("line3");

        String output = baos.toString();
        assertTrue(output.contains("line1"));
        assertTrue(output.contains("line2"));
        assertTrue(output.contains("line3"));
    }
}
