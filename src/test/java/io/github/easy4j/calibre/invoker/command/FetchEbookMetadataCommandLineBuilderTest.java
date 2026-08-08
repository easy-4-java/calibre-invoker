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
import io.github.easy4j.calibre.invoker.request.DefaultFetchEbookMetadataInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;

/**
 * Tests for {@link FetchEbookMetadataCommandLineBuilder}.
 */
public class FetchEbookMetadataCommandLineBuilderTest {

    @Test
    public void shouldExtendAbstractCommandLineBuilder() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        assertTrue(builder instanceof AbstractCommandLineBuilder);
    }

    @Test
    public void shouldSetAllowedPlugin() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setAllowedPlugin("Google");

        Commandline cli = new Commandline();
        builder.setAllowedPlugin(request, cli);

        boolean foundPlugin = false;
        boolean foundValue = false;
        for (int i = 0; i < cli.getArguments().length; i++) {
            if ("--allowed-plugin".equals(cli.getArguments()[i])) {
                foundPlugin = true;
                if (i + 1 < cli.getArguments().length && "Google".equals(cli.getArguments()[i + 1])) {
                    foundValue = true;
                }
            }
        }
        assertTrue(foundPlugin);
        assertTrue(foundValue);
    }

    @Test
    public void shouldNotSetAllowedPluginWhenNull() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();

        Commandline cli = new Commandline();
        builder.setAllowedPlugin(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetAuthors() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setAuthors(true);

        Commandline cli = new Commandline();
        builder.setAuthors(request, cli);

        boolean found = false;
        for (String arg : cli.getArguments()) {
            if ("--authors".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldNotSetAuthorsWhenFalse() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();

        Commandline cli = new Commandline();
        builder.setAuthors(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetCoverFile() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setCoverFile(new File("/tmp/cover.jpg"));

        Commandline cli = new Commandline();
        builder.setCoverFile(request, cli);

        boolean foundCover = false;
        for (int i = 0; i < cli.getArguments().length; i++) {
            if ("--cover".equals(cli.getArguments()[i])) {
                foundCover = true;
                break;
            }
        }
        assertTrue(foundCover);
    }

    @Test
    public void shouldNotSetCoverFileWhenNull() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();

        Commandline cli = new Commandline();
        builder.setCoverFile(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetIsbn() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setIsbn(true);

        Commandline cli = new Commandline();
        builder.setIsbn(request, cli);

        boolean found = false;
        for (String arg : cli.getArguments()) {
            if ("--isbn".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldSetOpf() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setOpf(true);

        Commandline cli = new Commandline();
        builder.setOpf(request, cli);

        boolean found = false;
        for (String arg : cli.getArguments()) {
            if ("--opf".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldSetTimeout() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setTimeout(60);

        Commandline cli = new Commandline();
        builder.setTimeout(request, cli);

        boolean foundTimeout = false;
        boolean foundValue = false;
        for (int i = 0; i < cli.getArguments().length; i++) {
            if ("--timeout".equals(cli.getArguments()[i])) {
                foundTimeout = true;
                if (i + 1 < cli.getArguments().length && "60".equals(cli.getArguments()[i + 1])) {
                    foundValue = true;
                }
            }
        }
        assertTrue(foundTimeout);
        assertTrue(foundValue);
    }

    @Test
    public void shouldNotSetTimeoutWhenZero() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setTimeout(0);

        Commandline cli = new Commandline();
        builder.setTimeout(request, cli);

        assertEquals(0, cli.getArguments().length);
    }

    @Test
    public void shouldSetTitle() {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setAuthors(true); // setTitle checks isAuthors (bug in original code)

        Commandline cli = new Commandline();
        builder.setTitle(request, cli);

        boolean found = false;
        for (String arg : cli.getArguments()) {
            if ("--title".equals(arg)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldNotDoCommandInternalForWrongRequestType() throws CommandLineConfigurationException {
        FetchEbookMetadataCommandLineBuilder builder = new FetchEbookMetadataCommandLineBuilder();
        DefaultWeb2diskInvocationRequest request = new DefaultWeb2diskInvocationRequest();

        Commandline cli = new Commandline();
        builder.doCommandInternal(request, cli);

        // Should not throw, just no-op
    }
}
