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
 * Tests for {@link DefaultEbookEditInvocationRequest}.
 */
public class DefaultEbookEditInvocationRequestTest {

    @Test
    public void shouldImplementInvocationRequest() {
        DefaultEbookEditInvocationRequest request = new DefaultEbookEditInvocationRequest();
        assertTrue(request instanceof InvocationRequest);
    }

    @Test
    public void shouldDefaultDetachToFalse() {
        DefaultEbookEditInvocationRequest request = new DefaultEbookEditInvocationRequest();
        assertFalse(request.isDetach());
    }

    @Test
    public void shouldSetAndGetDetach() {
        DefaultEbookEditInvocationRequest request = new DefaultEbookEditInvocationRequest();
        request.setDetach(true);
        assertTrue(request.isDetach());
    }
}
