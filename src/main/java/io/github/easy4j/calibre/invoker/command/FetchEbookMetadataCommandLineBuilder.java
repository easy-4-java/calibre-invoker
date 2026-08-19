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

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;

/**
 * Command-line builder for the Calibre {@code fetch-ebook-metadata} tool. Constructs a command
 * line to fetch book metadata from online sources. At least one of title, authors, or ISBN
 * must be specified.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractCommandLineBuilder
 * @see io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest
 */
public class FetchEbookMetadataCommandLineBuilder extends AbstractCommandLineBuilder {

	@Override
	protected void doCommandInternal(InvocationRequest request, Commandline cli)
			throws CommandLineConfigurationException {
		
		if (!(request instanceof FetchEbookMetadataInvocationRequest)) {
			throw new CommandLineConfigurationException(
					"Request must implement FetchEbookMetadataInvocationRequest.");
		}

		FetchEbookMetadataInvocationRequest fetchRequest =
				(FetchEbookMetadataInvocationRequest) request;
		validate(fetchRequest);
		setAllowedPlugins(fetchRequest, cli);
		setAuthors(fetchRequest, cli);
		setCoverFile(fetchRequest, cli);
		setIsbn(fetchRequest, cli);
		setOpf(fetchRequest, cli);
		setTimeout(fetchRequest, cli);
		setTitle(fetchRequest, cli);
	}

	@Override
	protected File findCalibreExecutable() throws CommandLineConfigurationException, IOException {
		if (Objects.nonNull(calibreExecutable) && calibreExecutable.isAbsolute()) {
			return calibreExecutable;
		}
		return resolveCalibreExecutable("fetch-ebook-metadata", null);
	}

	private void validate(FetchEbookMetadataInvocationRequest request)
			throws CommandLineConfigurationException {
		if (request.isAuthors() && StringUtils.isBlank(request.getAuthors())) {
			throw new CommandLineConfigurationException(
					"Fetch metadata authors requires a string value.");
		}
		if (request.isIsbn() && StringUtils.isBlank(request.getIsbn())) {
			throw new CommandLineConfigurationException(
					"Fetch metadata ISBN requires a string value.");
		}
		if (request.isTitle() && StringUtils.isBlank(request.getTitle())) {
			throw new CommandLineConfigurationException(
					"Fetch metadata title requires a string value.");
		}
		if (StringUtils.isBlank(request.getAuthors())
				&& StringUtils.isBlank(request.getIsbn())
				&& StringUtils.isBlank(request.getTitle())) {
			throw new CommandLineConfigurationException(
					"Fetch metadata requires at least one of title, authors or ISBN.");
		}
		if (request.getTimeout() <= 0) {
			throw new CommandLineConfigurationException(
					"Fetch metadata timeout must be positive.");
		}
	}

	protected void setAllowedPlugins(FetchEbookMetadataInvocationRequest request,
			Commandline cli) {
		for (String allowedPlugin : request.getAllowedPlugins()) {
			if (StringUtils.isNotBlank(allowedPlugin)) {
				cli.createArg().setValue("--allowed-plugin");
				cli.createArg().setValue(allowedPlugin);
			}
		}
	}

	protected void setAllowedPlugin(FetchEbookMetadataInvocationRequest request,
			Commandline cli) {
		setAllowedPlugins(request, cli);
	}

	protected void setAuthors(FetchEbookMetadataInvocationRequest request, Commandline cli) {
		if (StringUtils.isNotBlank(request.getAuthors())) {
			cli.createArg().setValue("--authors");
			cli.createArg().setValue(request.getAuthors());
		}
	}

	protected void setCoverFile(FetchEbookMetadataInvocationRequest request, Commandline cli) {
		
		File coverFile = request.getCoverFile();
		if (Objects.nonNull(coverFile)) {
			try {
				File canSet = coverFile.getCanonicalFile();
				coverFile = canSet;
			} catch (IOException e) {
				logger.debug("Failed to canonicalize coverFile path: " + coverFile.getAbsolutePath()
						+ ".", e);
			}

			cli.createArg().setValue("--cover");
			cli.createArg().setValue(coverFile.getPath());
		}
		
	}
	
	protected void setIsbn(FetchEbookMetadataInvocationRequest request, Commandline cli) {
		if (StringUtils.isNotBlank(request.getIsbn())) {
			cli.createArg().setValue("--isbn");
			cli.createArg().setValue(request.getIsbn());
		}
	}

	protected void setOpf(FetchEbookMetadataInvocationRequest request, Commandline cli) {
		if(request.isOpf()) {
			cli.createArg().setValue("--opf");
		}
	}
	
	protected void setTimeout(FetchEbookMetadataInvocationRequest request, Commandline cli) {
		long timeout = request.getTimeout();
		if (timeout > 0) {
			cli.createArg().setValue("--timeout");
			cli.createArg().setValue(String.valueOf(timeout));
		}
	}
	
	protected void setTitle(FetchEbookMetadataInvocationRequest request, Commandline cli) {
		if (StringUtils.isNotBlank(request.getTitle())) {
			cli.createArg().setValue("--title");
			cli.createArg().setValue(request.getTitle());
		}
	}

}
