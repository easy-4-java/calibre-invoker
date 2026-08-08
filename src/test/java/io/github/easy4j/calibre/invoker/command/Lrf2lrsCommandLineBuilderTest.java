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
import io.github.easy4j.calibre.invoker.request.DefaultLrf2lrsInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;

/**
 * Tests for {@link Lrf2lrsCommandLineBuilder}.
 */
public class Lrf2lrsCommandLineBuilderTest {

    @Test
    public void shouldExtendAbstractCommandLineBuilder() {
        Lrf2lrsCommandLineBuilder builder = new Lrf2lrsCommandLineBuilder();
        assertTrue(builder instanceof AbstractCommandLineBuilder);
    }

    @Test
    public void shouldSetDontOutputResources() {
        Lrf2lrsCommandLineBuilder builder = new Lrf2lrsCommandLineBuilder();
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        request.setDontOutputResources(true);

        Commandline cli = new Commandline();
        builder.setDontOutputResources(request, cli);

        String[] args = cli.getArguments();
        boolean found = false;
        for (String arg : args) {
            if ("--dont-output-resources".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldNotSetDontOutputResourcesWhenFalse() {
        Lrf2lrsCommandLineBuilder builder = new Lrf2lrsCommandLineBuilder();
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        request.setDontOutputResources(false);

        Commandline cli = new Commandline();
        builder.setDontOutputResources(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetOutputDirectory() {
        Lrf2lrsCommandLineBuilder builder = new Lrf2lrsCommandLineBuilder();
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();
        request.setOutputDirectory(new File("/tmp/output"));

        Commandline cli = new Commandline();
        builder.setOutputDirectory(request, cli);

        String[] args = cli.getArguments();
        boolean foundO = false;
        for (int i = 0; i < args.length; i++) {
            if ("-o".equals(args[i])) {
                foundO = true;
                break;
            }
        }
        assertTrue(foundO);
    }

    @Test
    public void shouldNotSetOutputDirectoryWhenNull() {
        Lrf2lrsCommandLineBuilder builder = new Lrf2lrsCommandLineBuilder();
        DefaultLrf2lrsInvocationRequest request = new DefaultLrf2lrsInvocationRequest();

        Commandline cli = new Commandline();
        builder.setOutputDirectory(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldNotDoCommandInternalForWrongRequestType() throws CommandLineConfigurationException {
        Lrf2lrsCommandLineBuilder builder = new Lrf2lrsCommandLineBuilder();
        // Pass a non-Lrs2lrfInvocationRequest - should be a no-op
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();

        Commandline cli = new Commandline();
        builder.doCommandInternal(request, cli);

        // Should not throw, just no-op
    }
}
