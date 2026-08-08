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

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.DefaultLrs2lrfInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;

/**
 * Tests for {@link Lrs2lrfCommandLineBuilder}.
 */
public class Lrs2lrfCommandLineBuilderTest {

    @Test
    public void shouldExtendAbstractCommandLineBuilder() {
        Lrs2lrfCommandLineBuilder builder = new Lrs2lrfCommandLineBuilder();
        assertTrue(builder instanceof AbstractCommandLineBuilder);
    }

    @Test
    public void shouldSetLrs() {
        Lrs2lrfCommandLineBuilder builder = new Lrs2lrfCommandLineBuilder();
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();
        request.setLrs(true);

        Commandline cli = new Commandline();
        builder.setLrs(request, cli);

        boolean found = false;
        for (String arg : cli.getArguments()) {
            if ("--lrs".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldNotSetLrsWhenFalse() {
        Lrs2lrfCommandLineBuilder builder = new Lrs2lrfCommandLineBuilder();
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();

        Commandline cli = new Commandline();
        builder.setLrs(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetOutputDirectory() {
        Lrs2lrfCommandLineBuilder builder = new Lrs2lrfCommandLineBuilder();
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();
        request.setOutputDirectory(new File("/tmp/output"));

        Commandline cli = new Commandline();
        builder.setOutputDirectory(request, cli);

        boolean foundO = false;
        for (int i = 0; i < cli.getArguments().length; i++) {
            if ("-o".equals(cli.getArguments()[i])) {
                foundO = true;
                break;
            }
        }
        assertTrue(foundO);
    }

    @Test
    public void shouldNotSetOutputDirectoryWhenNull() {
        Lrs2lrfCommandLineBuilder builder = new Lrs2lrfCommandLineBuilder();
        DefaultLrs2lrfInvocationRequest request = new DefaultLrs2lrfInvocationRequest();

        Commandline cli = new Commandline();
        builder.setOutputDirectory(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldNotDoCommandInternalForWrongRequestType() throws CommandLineConfigurationException {
        Lrs2lrfCommandLineBuilder builder = new Lrs2lrfCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();

        Commandline cli = new Commandline();
        builder.doCommandInternal(request, cli);

        // Should not throw, just no-op
    }
}
