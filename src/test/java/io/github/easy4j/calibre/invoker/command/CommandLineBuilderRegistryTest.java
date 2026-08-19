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

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.DefaultEbookConvertInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultFetchEbookMetadataInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultLrf2lrsInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultLrfviewerInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultLrs2lrfInvocationRequest;
import io.github.easy4j.calibre.invoker.request.DefaultWeb2diskInvocationRequest;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;
import io.github.easy4j.calibre.invoker.request.Web2diskInvocationRequest;

/**
 * Tests for {@link CommandLineBuilderRegistry}.
 */
public class CommandLineBuilderRegistryTest {

	@Test
	public void createRejectsNullRequestWithCheckedError() {
		CommandLineBuilderRegistry registry = new CommandLineBuilderRegistry();

		CommandLineConfigurationException exception = assertThrows(CommandLineConfigurationException.class,
				() -> registry.create(null));

		assertTrue(exception.getMessage().contains("must not be null"));
	}

	@Test
	public void createRejectsUnsupportedRequestWithItsClassName() {
		CommandLineBuilderRegistry registry = new CommandLineBuilderRegistry();
		DefaultEbookConvertInvocationRequest request = new DefaultEbookConvertInvocationRequest();

		CommandLineConfigurationException exception = assertThrows(CommandLineConfigurationException.class,
				() -> registry.create(request));

		assertTrue(exception.getMessage().contains(request.getClass().getName()));
	}

	@Test
	public void duplicateRegistrationIsRejected() throws Exception {
		CommandLineBuilderRegistry registry = new CommandLineBuilderRegistry()
				.register(Web2diskInvocationRequest.class, Web2diskCommandLineBuilder::new);

		CommandLineConfigurationException exception = assertThrows(CommandLineConfigurationException.class,
				() -> registry.register(Web2diskInvocationRequest.class, Web2diskCommandLineBuilder::new));

		assertTrue(exception.getMessage().contains(Web2diskInvocationRequest.class.getName()));
	}

	@Test
	public void interfaceRegistrationMatchesImplementingRequest() throws Exception {
		CommandLineBuilderRegistry registry = new CommandLineBuilderRegistry()
				.register(Web2diskInvocationRequest.class, Web2diskCommandLineBuilder::new);

		AbstractCommandLineBuilder first = registry.create(new DefaultWeb2diskInvocationRequest());
		AbstractCommandLineBuilder second = registry.create(new DefaultWeb2diskInvocationRequest());

		assertTrue(first instanceof Web2diskCommandLineBuilder);
		assertTrue(second instanceof Web2diskCommandLineBuilder);
		assertNotSame(first, second);
	}

	@Test
	public void exactRegistrationWinsOverAssignableRegistration() throws Exception {
		CommandLineBuilderRegistry registry = new CommandLineBuilderRegistry()
				.register(InvocationRequest.class, FetchEbookMetadataCommandLineBuilder::new)
				.register(DefaultWeb2diskInvocationRequest.class, Web2diskCommandLineBuilder::new);

		AbstractCommandLineBuilder builder = registry.create(new DefaultWeb2diskInvocationRequest());

		assertTrue(builder instanceof Web2diskCommandLineBuilder);
	}

	@Test
	public void defaultRegistryContainsTheFiveExistingRoutes() throws Exception {
		CommandLineBuilderRegistry registry = CommandLineBuilderRegistry.defaultRegistry();

		assertTrue(registry.create(new DefaultWeb2diskInvocationRequest()) instanceof Web2diskCommandLineBuilder);
		assertTrue(registry.create(new DefaultFetchEbookMetadataInvocationRequest())
				instanceof FetchEbookMetadataCommandLineBuilder);
		assertTrue(registry.create(new DefaultLrf2lrsInvocationRequest()) instanceof Lrf2lrsCommandLineBuilder);
		assertTrue(registry.create(new DefaultLrs2lrfInvocationRequest()) instanceof Lrs2lrfCommandLineBuilder);
		assertTrue(registry.create(new DefaultLrfviewerInvocationRequest()) instanceof LrfviewerCommandLineBuilder);
	}
}
