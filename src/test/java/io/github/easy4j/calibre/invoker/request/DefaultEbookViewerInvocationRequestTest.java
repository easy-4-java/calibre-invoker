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
package io.github.easy4j.calibre.invoker.request;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for {@link DefaultEbookViewerInvocationRequest}.
 */
public class DefaultEbookViewerInvocationRequestTest {

    @Test
    public void shouldImplementInvocationRequest() {
        DefaultEbookViewerInvocationRequest request = new DefaultEbookViewerInvocationRequest();
        assertTrue(request instanceof InvocationRequest);
    }

    @Test
    public void shouldDefaultContinueReadingToFalse() {
        DefaultEbookViewerInvocationRequest request = new DefaultEbookViewerInvocationRequest();
        assertFalse(request.isContinueReading());
    }

    @Test
    public void shouldSetAndGetContinueReading() {
        DefaultEbookViewerInvocationRequest request = new DefaultEbookViewerInvocationRequest();
        request.setContinueReading(true);
        assertTrue(request.isContinueReading());
    }

    @Test
    public void shouldDefaultDebugJavascriptToFalse() {
        DefaultEbookViewerInvocationRequest request = new DefaultEbookViewerInvocationRequest();
        assertFalse(request.isDebugJavascript());
    }

    @Test
    public void shouldSetAndGetDebugJavascript() {
        DefaultEbookViewerInvocationRequest request = new DefaultEbookViewerInvocationRequest();
        request.setDebugJavascript(true);
        assertTrue(request.isDebugJavascript());
    }

    @Test
    public void shouldDefaultFullscreenToFalse() {
        DefaultEbookViewerInvocationRequest request = new DefaultEbookViewerInvocationRequest();
        assertFalse(request.isFullscreen());
    }

    @Test
    public void shouldSetAndGetFullscreen() {
        DefaultEbookViewerInvocationRequest request = new DefaultEbookViewerInvocationRequest();
        request.setFullscreen(true);
        assertTrue(request.isFullscreen());
    }

    @Test
    public void shouldDefaultRaiseWindowToFalse() {
        DefaultEbookViewerInvocationRequest request = new DefaultEbookViewerInvocationRequest();
        assertFalse(request.isRaiseWindow());
    }

    @Test
    public void shouldSetAndGetRaiseWindow() {
        DefaultEbookViewerInvocationRequest request = new DefaultEbookViewerInvocationRequest();
        request.setRaiseWindow(true);
        assertTrue(request.isRaiseWindow());
    }
}
