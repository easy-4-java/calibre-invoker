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
 * Tests for {@link DefaultEbookMetaInvocationRequest}.
 */
public class DefaultEbookMetaInvocationRequestTest {

    @Test
    public void shouldImplementInvocationRequest() {
        DefaultEbookMetaInvocationRequest request = new DefaultEbookMetaInvocationRequest();
        assertTrue(request instanceof InvocationRequest);
    }

    @Test
    public void shouldDefaultAuthorSortToFalse() {
        DefaultEbookMetaInvocationRequest request = new DefaultEbookMetaInvocationRequest();
        assertFalse(request.isAuthorSort());
    }

    @Test
    public void shouldSetAndGetAuthorSort() {
        DefaultEbookMetaInvocationRequest request = new DefaultEbookMetaInvocationRequest();
        request.setAuthorSort(true);
        assertTrue(request.isAuthorSort());
    }

    @Test
    public void shouldDefaultFromOpfToFalse() {
        DefaultEbookMetaInvocationRequest request = new DefaultEbookMetaInvocationRequest();
        assertFalse(request.isFromOpf());
    }

    @Test
    public void shouldSetAndGetFromOpf() {
        DefaultEbookMetaInvocationRequest request = new DefaultEbookMetaInvocationRequest();
        request.setFromOpf(true);
        assertTrue(request.isFromOpf());
    }

    @Test
    public void shouldDefaultGetCoverToFalse() {
        DefaultEbookMetaInvocationRequest request = new DefaultEbookMetaInvocationRequest();
        assertFalse(request.isGetCover());
    }

    @Test
    public void shouldSetAndGetGetCover() {
        DefaultEbookMetaInvocationRequest request = new DefaultEbookMetaInvocationRequest();
        request.setGetCover(true);
        assertTrue(request.isGetCover());
    }

    @Test
    public void shouldDefaultTitleSortToFalse() {
        DefaultEbookMetaInvocationRequest request = new DefaultEbookMetaInvocationRequest();
        assertFalse(request.isTitleSort());
    }

    @Test
    public void shouldSetAndGetTitleSort() {
        DefaultEbookMetaInvocationRequest request = new DefaultEbookMetaInvocationRequest();
        request.setTitleSort(true);
        assertTrue(request.isTitleSort());
    }
}
