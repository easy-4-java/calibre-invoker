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

import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Test;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.DefaultLrfviewerInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;

/**
 * Tests for {@link LrfviewerCommandLineBuilder}.
 */
public class LrfviewerCommandLineBuilderTest {

    @Test
    public void shouldExtendAbstractCommandLineBuilder() {
        LrfviewerCommandLineBuilder builder = new LrfviewerCommandLineBuilder();
        assertTrue(builder instanceof AbstractCommandLineBuilder);
    }

    @Test
    public void shouldSetDisableHyphenation() {
        LrfviewerCommandLineBuilder builder = new LrfviewerCommandLineBuilder();
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        request.setDisableHyphenation(true);

        Commandline cli = new Commandline();
        builder.setDisableHyphenation(request, cli);

        String[] args = cli.getArguments();
        boolean found = false;
        for (String arg : args) {
            if ("--disable-hyphenation".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldNotSetDisableHyphenationWhenFalse() {
        LrfviewerCommandLineBuilder builder = new LrfviewerCommandLineBuilder();
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();

        Commandline cli = new Commandline();
        builder.setDisableHyphenation(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetProfile() {
        LrfviewerCommandLineBuilder builder = new LrfviewerCommandLineBuilder();
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        request.setProfile(true);

        Commandline cli = new Commandline();
        builder.setProfile(request, cli);

        boolean found = false;
        for (String arg : cli.getArguments()) {
            if ("--profile".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldSetVisualDebug() {
        LrfviewerCommandLineBuilder builder = new LrfviewerCommandLineBuilder();
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        request.setVisualDebug(true);

        Commandline cli = new Commandline();
        builder.setVisualDebug(request, cli);

        boolean found = false;
        for (String arg : cli.getArguments()) {
            if ("--visual-debug".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldSetWhiteBackground() {
        LrfviewerCommandLineBuilder builder = new LrfviewerCommandLineBuilder();
        DefaultLrfviewerInvocationRequest request = new DefaultLrfviewerInvocationRequest();
        request.setWhiteBackground(true);

        Commandline cli = new Commandline();
        builder.setWhiteBackground(request, cli);

        boolean found = false;
        for (String arg : cli.getArguments()) {
            if ("--white-background".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldNotDoCommandInternalForWrongRequestType() throws CommandLineConfigurationException {
        LrfviewerCommandLineBuilder builder = new LrfviewerCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();

        Commandline cli = new Commandline();
        builder.doCommandInternal(request, cli);

        // Should not throw, just no-op
    }
}
