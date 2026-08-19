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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;
import io.github.easy4j.calibre.invoker.request.Lrf2lrsInvocationRequest;
import io.github.easy4j.calibre.invoker.request.LrfviewerInvocationRequest;
import io.github.easy4j.calibre.invoker.request.Lrs2lrfInvocationRequest;
import io.github.easy4j.calibre.invoker.request.Web2diskInvocationRequest;

/**
 * Maps invocation request types to factories for their command-line builders.
 * Each lookup creates a fresh builder instance.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 */
public class CommandLineBuilderRegistry {

	private final Map<Class<? extends InvocationRequest>, Supplier<? extends AbstractCommandLineBuilder>> factories
			= new LinkedHashMap<>();

	/**
	 * Creates the registry used by {@link io.github.easy4j.calibre.invoker.DefaultInvoker}.
	 *
	 * @return A registry containing all command builders currently supported by the facade.
	 */
	public static CommandLineBuilderRegistry defaultRegistry() {
		CommandLineBuilderRegistry registry = new CommandLineBuilderRegistry();
		registry.factories.put(Web2diskInvocationRequest.class, Web2diskCommandLineBuilder::new);
		registry.factories.put(FetchEbookMetadataInvocationRequest.class,
				FetchEbookMetadataCommandLineBuilder::new);
		registry.factories.put(Lrf2lrsInvocationRequest.class, Lrf2lrsCommandLineBuilder::new);
		registry.factories.put(Lrs2lrfInvocationRequest.class, Lrs2lrfCommandLineBuilder::new);
		registry.factories.put(LrfviewerInvocationRequest.class, LrfviewerCommandLineBuilder::new);
		return registry;
	}

	/**
	 * Registers a builder factory for one request type.
	 *
	 * @param requestType The request type matched by the factory, must not be {@code null}.
	 * @param factory The builder factory, must not be {@code null}.
	 * @return This registry.
	 * @throws CommandLineConfigurationException If either argument is {@code null} or the request type
	 *         is already registered.
	 */
	public CommandLineBuilderRegistry register(Class<? extends InvocationRequest> requestType,
			Supplier<? extends AbstractCommandLineBuilder> factory) throws CommandLineConfigurationException {
		if (Objects.isNull(requestType)) {
			throw new CommandLineConfigurationException("Invocation request type must not be null.");
		}
		if (Objects.isNull(factory)) {
			throw new CommandLineConfigurationException(
					"Command-line builder factory must not be null for: " + requestType.getName());
		}
		if (factories.containsKey(requestType)) {
			throw new CommandLineConfigurationException(
					"Command-line builder already registered for: " + requestType.getName());
		}
		factories.put(requestType, factory);
		return this;
	}

	/**
	 * Creates the builder registered for the supplied request. Exact class registrations take
	 * precedence over assignable interface or superclass registrations.
	 *
	 * @param request The invocation request, must not be {@code null}.
	 * @return A fresh command-line builder.
	 * @throws CommandLineConfigurationException If the request is {@code null}, unsupported, or its
	 *         registered factory returns {@code null}.
	 */
	public AbstractCommandLineBuilder create(InvocationRequest request)
			throws CommandLineConfigurationException {
		if (Objects.isNull(request)) {
			throw new CommandLineConfigurationException("Invocation request must not be null.");
		}

		Supplier<? extends AbstractCommandLineBuilder> factory = factories.get(request.getClass());
		if (Objects.isNull(factory)) {
			for (Map.Entry<Class<? extends InvocationRequest>,
					Supplier<? extends AbstractCommandLineBuilder>> entry : factories.entrySet()) {
				if (entry.getKey().isInstance(request)) {
					factory = entry.getValue();
					break;
				}
			}
		}

		if (Objects.isNull(factory)) {
			throw new CommandLineConfigurationException(
					"Unsupported invocation request: " + request.getClass().getName());
		}

		AbstractCommandLineBuilder builder = factory.get();
		if (Objects.isNull(builder)) {
			throw new CommandLineConfigurationException(
					"Command-line builder factory returned null for: " + request.getClass().getName());
		}
		return builder;
	}
}
