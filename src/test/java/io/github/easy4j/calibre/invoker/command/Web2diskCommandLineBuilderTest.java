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

import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Test;

import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;

/**
 * Tests for {@link Web2diskCommandLineBuilder}.
 */
public class Web2diskCommandLineBuilderTest {

    @Test
    public void shouldExtendAbstractCommandLineBuilder() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        assertTrue(builder instanceof AbstractCommandLineBuilder);
    }

    @Test
    public void shouldSetBaseDirectory() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setBaseDirectory(new File("/tmp/base"));

        Commandline cli = new Commandline();
        builder.setBaseDirectory(request, cli);

        String[] args = cli.getArguments();
        boolean foundD = false;
        boolean foundPath = false;
        for (int i = 0; i < args.length; i++) {
            if ("-d".equals(args[i])) {
                foundD = true;
                if (i + 1 < args.length) {
                    foundPath = true;
                }
            }
        }
        assertTrue("Should contain -d flag", foundD);
        assertTrue("Should contain base directory path", foundPath);
    }

    @Test
    public void shouldNotSetBaseDirectoryWhenNull() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();

        Commandline cli = new Commandline();
        builder.setBaseDirectory(request, cli);

        String[] args = cli.getArguments();
        assertEquals(0, args.length);
    }

    @Test
    public void shouldSetDelay() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setDelay(5);

        Commandline cli = new Commandline();
        builder.setDelay(request, cli);

        String[] args = cli.getArguments();
        boolean foundDelay = false;
        boolean foundValue = false;
        for (int i = 0; i < args.length; i++) {
            if ("--delay".equals(args[i])) {
                foundDelay = true;
                if (i + 1 < args.length && "5".equals(args[i + 1])) {
                    foundValue = true;
                }
            }
        }
        assertTrue("Should contain --delay flag", foundDelay);
        assertTrue("Should contain delay value", foundValue);
    }

    @Test
    public void shouldSetDontDownloadStylesheets() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setDontDownloadStylesheets(true);

        Commandline cli = new Commandline();
        builder.setDontDownloadStylesheets(request, cli);

        String[] args = cli.getArguments();
        boolean found = false;
        for (String arg : args) {
            if ("--dont-download-stylesheets".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldNotSetDontDownloadStylesheetsWhenFalse() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setDontDownloadStylesheets(false);

        Commandline cli = new Commandline();
        builder.setDontDownloadStylesheets(request, cli);

        String[] args = cli.getArguments();
        for (String arg : args) {
            assertNotEquals("--dont-download-stylesheets", arg);
        }
    }

    @Test
    public void shouldSetEncoding() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setEncoding("UTF-8");

        Commandline cli = new Commandline();
        builder.setEncoding(request, cli);

        String[] args = cli.getArguments();
        boolean foundEncoding = false;
        boolean foundValue = false;
        for (int i = 0; i < args.length; i++) {
            if ("--encoding".equals(args[i])) {
                foundEncoding = true;
                if (i + 1 < args.length && "UTF-8".equals(args[i + 1])) {
                    foundValue = true;
                }
            }
        }
        assertTrue(foundEncoding);
        assertTrue(foundValue);
    }

    @Test
    public void shouldNotSetEncodingWhenNull() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();

        Commandline cli = new Commandline();
        builder.setEncoding(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetFilterRegexp() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setFilterRegexp(".*\\.css");

        Commandline cli = new Commandline();
        builder.setFilterRegexp(request, cli);

        String[] args = cli.getArguments();
        boolean found = false;
        for (int i = 0; i < args.length; i++) {
            if ("--filter-regexp".equals(args[i])) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldNotSetFilterRegexpWhenNull() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();

        Commandline cli = new Commandline();
        builder.setFilterRegexp(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetMatchRegexp() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setMatchRegexp(".*example.*");

        Commandline cli = new Commandline();
        builder.setMatchRegexp(request, cli);

        String[] args = cli.getArguments();
        boolean found = false;
        for (int i = 0; i < args.length; i++) {
            if ("--match-regexp".equals(args[i])) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldNotSetMatchRegexpWhenNull() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();

        Commandline cli = new Commandline();
        builder.setMatchRegexp(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetMaxFiles() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setMaxFiles(100);

        Commandline cli = new Commandline();
        builder.setMaxFiles(request, cli);

        String[] args = cli.getArguments();
        boolean foundN = false;
        boolean foundValue = false;
        for (int i = 0; i < args.length; i++) {
            if ("-n".equals(args[i])) {
                foundN = true;
                if (i + 1 < args.length && "100".equals(args[i + 1])) {
                    foundValue = true;
                }
            }
        }
        assertTrue(foundN);
        assertTrue(foundValue);
    }

    @Test
    public void shouldNotSetMaxFilesWhenZero() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setMaxFiles(0);

        Commandline cli = new Commandline();
        builder.setMaxFiles(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetMaxRecursions() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setMaxRecursions(3);

        Commandline cli = new Commandline();
        builder.setMaxRecursions(request, cli);

        String[] args = cli.getArguments();
        boolean foundR = false;
        boolean foundValue = false;
        for (int i = 0; i < args.length; i++) {
            if ("-r".equals(args[i])) {
                foundR = true;
                if (i + 1 < args.length && "3".equals(args[i + 1])) {
                    foundValue = true;
                }
            }
        }
        assertTrue(foundR);
        assertTrue(foundValue);
    }

    @Test
    public void shouldNotSetMaxRecursionsWhenZero() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setMaxRecursions(0);

        Commandline cli = new Commandline();
        builder.setMaxRecursions(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetTimeout() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setTimeout(30);

        Commandline cli = new Commandline();
        builder.setTimeout(request, cli);

        String[] args = cli.getArguments();
        boolean foundT = false;
        boolean foundValue = false;
        for (int i = 0; i < args.length; i++) {
            if ("-t".equals(args[i])) {
                foundT = true;
                if (i + 1 < args.length && "30".equals(args[i + 1])) {
                    foundValue = true;
                }
            }
        }
        assertTrue(foundT);
        assertTrue(foundValue);
    }

    @Test
    public void shouldNotSetTimeoutWhenZero() {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setTimeout(0);

        Commandline cli = new Commandline();
        builder.setTimeout(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldNotDoCommandInternalForNonWeb2diskRequest() throws io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException {
        Web2diskCommandLineBuilder builder = new Web2diskCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();
        request.setURL("https://example.com");

        Commandline cli = new Commandline();
        // This should work since request IS a Web2diskInvocationRequest
        builder.doCommandInternal(request, cli);
    }
}
