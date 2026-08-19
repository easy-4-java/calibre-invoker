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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Tests for {@link DefaultFetchEbookMetadataInvocationRequest}.
 */
public class DefaultFetchEbookMetadataInvocationRequestTest {

    @Test
    public void shouldImplementFetchEbookMetadataInvocationRequest() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        assertTrue(request instanceof FetchEbookMetadataInvocationRequest);
        assertTrue(request instanceof InvocationRequest);
    }

    @Test
    public void shouldDefaultAllowedPluginToNull() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        assertNull(request.getAllowedPlugin());
    }

    @Test
    public void shouldSetAndGetAllowedPlugin() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        InvocationRequest result = request.setAllowedPlugin("Google");
        assertEquals("Google", request.getAllowedPlugin());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultAuthorsToFalse() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        assertFalse(request.isAuthors());
    }

    @Test
    public void shouldSetAndGetAuthors() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        InvocationRequest result = request.setAuthors("Eric Evans");
        assertEquals("Eric Evans", request.getAuthors());
        assertTrue(request.isAuthors());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultCoverFileToNull() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        assertNull(request.getCoverFile());
    }

    @Test
    public void shouldSetAndGetCoverFile() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        File cover = new File("/tmp/cover.jpg");
        InvocationRequest result = request.setCoverFile(cover);
        assertEquals(cover, request.getCoverFile());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultIsbnToFalse() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        assertFalse(request.isIsbn());
    }

    @Test
    public void shouldSetAndGetIsbn() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        InvocationRequest result = request.setIsbn("9780321125217");
        assertEquals("9780321125217", request.getIsbn());
        assertTrue(request.isIsbn());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultOpfToFalse() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        assertFalse(request.isOpf());
    }

    @Test
    public void shouldSetAndGetOpf() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        InvocationRequest result = request.setOpf(true);
        assertTrue(request.isOpf());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultTimeoutTo30() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        assertEquals(30, request.getTimeout());
    }

    @Test
    public void shouldSetAndGetTimeout() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        InvocationRequest result = request.setTimeout(60);
        assertEquals(60, request.getTimeout());
        assertSame(request, result);
    }

    @Test
    public void shouldDefaultTitleToFalse() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        assertFalse(request.isTitle());
    }

    @Test
    public void shouldSetAndGetTitle() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        InvocationRequest result = request.setTitle("Domain-Driven Design");
        assertEquals("Domain-Driven Design", request.getTitle());
        assertTrue(request.isTitle());
        assertSame(request, result);
    }

    @Test
    public void allowedPluginsAreRepeatableAndDefensive() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        List<String> plugins = new ArrayList<>(Arrays.asList("Google", "Open Library"));

        assertSame(request, request.setAllowedPlugins(plugins));
        plugins.add("Amazon.com");
        assertEquals(Arrays.asList("Google", "Open Library"), request.getAllowedPlugins());
        assertThrows(UnsupportedOperationException.class,
                () -> request.getAllowedPlugins().add("Amazon.com"));
    }

    @Test
    public void legacyFalseSettersClearTypedValuesWithoutInventingBooleanStrings() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();
        request.setAuthors("Eric Evans");
        request.setIsbn("9780321125217");
        request.setTitle("Domain-Driven Design");

        request.setAuthors(false);
        request.setIsbn(false);
        request.setTitle(false);

        assertNull(request.getAuthors());
        assertNull(request.getIsbn());
        assertNull(request.getTitle());
    }

    @Test
    public void legacyTrueSettersDoNotInventTypedValues() {
        DefaultFetchEbookMetadataInvocationRequest request = new DefaultFetchEbookMetadataInvocationRequest();

        request.setAuthors(true);
        request.setIsbn(true);
        request.setTitle(true);

        assertNull(request.getAuthors());
        assertNull(request.getIsbn());
        assertNull(request.getTitle());
        assertTrue(request.isAuthors());
        assertTrue(request.isIsbn());
        assertTrue(request.isTitle());
    }

    @Test
    public void legacyBooleanMetadataMethodsRemainDeprecated() throws Exception {
        assertTrue(FetchEbookMetadataInvocationRequest.class.getMethod("isAuthors")
                .isAnnotationPresent(Deprecated.class));
        assertTrue(FetchEbookMetadataInvocationRequest.class.getMethod("setAuthors", boolean.class)
                .isAnnotationPresent(Deprecated.class));
        assertTrue(FetchEbookMetadataInvocationRequest.class.getMethod("isIsbn")
                .isAnnotationPresent(Deprecated.class));
        assertTrue(FetchEbookMetadataInvocationRequest.class.getMethod("setIsbn", boolean.class)
                .isAnnotationPresent(Deprecated.class));
        assertTrue(FetchEbookMetadataInvocationRequest.class.getMethod("isTitle")
                .isAnnotationPresent(Deprecated.class));
        assertTrue(FetchEbookMetadataInvocationRequest.class.getMethod("setTitle", boolean.class)
                .isAnnotationPresent(Deprecated.class));
    }
}
