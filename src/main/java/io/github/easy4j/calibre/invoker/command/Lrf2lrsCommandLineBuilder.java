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

import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;
import io.github.easy4j.calibre.invoker.request.Lrf2lrsInvocationRequest;

/**
 * Command-line builder for the Calibre {@code lrf2lrs} tool. Constructs a command line to
 * convert an LRF file into an LRS (XML UTF-8 encoded) file with optional resource output
 * control and output directory configuration.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractCommandLineBuilder
 * @see Lrf2lrsInvocationRequest
 */
public class Lrf2lrsCommandLineBuilder extends AbstractCommandLineBuilder {

	@Override
	public Commandline build(InvocationRequest request)
			throws CommandLineConfigurationException {
		requireTypedRequest(request);
		return super.build(request);
	}

	@Override
	protected void doCommandInternal(InvocationRequest request, Commandline cli)
			throws CommandLineConfigurationException {
		Lrf2lrsInvocationRequest lrf2lrsRequest = requireTypedRequest(request);
		validateLrfFile(lrf2lrsRequest.getLrfFile());

		setDontOutputResources(lrf2lrsRequest, cli);
		setOutputDirectory(lrf2lrsRequest, cli);
		cli.createArg().setValue(lrf2lrsRequest.getLrfFile().getAbsolutePath());
	}

	@Override
	protected File findCalibreExecutable()
			throws CommandLineConfigurationException, IOException {
		return resolveCalibreExecutable("lrf2lrs", null);
	}

	private Lrf2lrsInvocationRequest requireTypedRequest(InvocationRequest request)
			throws CommandLineConfigurationException {
		if (Objects.isNull(request) || !(request instanceof Lrf2lrsInvocationRequest)) {
			throw new CommandLineConfigurationException(
					"Request must implement Lrf2lrsInvocationRequest.");
		}
		return (Lrf2lrsInvocationRequest) request;
	}

	private void validateLrfFile(File lrfFile)
			throws CommandLineConfigurationException {
		if (Objects.isNull(lrfFile)) {
			throw new CommandLineConfigurationException(
					"Lrf2lrs lrfFile must not be null.");
		}
		if (!lrfFile.exists()) {
			throw new CommandLineConfigurationException(
					"Lrf2lrs lrfFile must exist.");
		}
		if (!lrfFile.isFile()) {
			throw new CommandLineConfigurationException(
					"Lrf2lrs lrfFile must be a file.");
		}
	}

	protected void setDontOutputResources(Lrf2lrsInvocationRequest request,
			Commandline cli) {
		if (request.isDontOutputResources()) {
			cli.createArg().setValue("--dont-output-resources");
		}
	}

	protected void setOutputDirectory(Lrf2lrsInvocationRequest request,
			Commandline cli) throws CommandLineConfigurationException {
		File outputDirectory = request.getOutputDirectory();
		if (Objects.nonNull(outputDirectory)) {
			outputDirectory = validateOutputDirectory(outputDirectory);
			File outputFile = outputFile(outputDirectory, request.getLrfFile(), ".lrs");

			cli.createArg().setValue("-o");
			cli.createArg().setValue(outputFile.getPath());
		}
	}

	private File validateOutputDirectory(File outputDirectory)
			throws CommandLineConfigurationException {
		if (!outputDirectory.exists()) {
			throw new CommandLineConfigurationException(
					"Lrf2lrs outputDirectory must exist.");
		}
		if (!outputDirectory.isDirectory()) {
			throw new CommandLineConfigurationException(
					"Lrf2lrs outputDirectory must be a directory.");
		}
		try {
			return outputDirectory.getCanonicalFile();
		} catch (IOException ignored) {
			throw new CommandLineConfigurationException(
					"Cannot canonicalize Lrf2lrs outputDirectory.");
		}
	}

	private File outputFile(File outputDirectory, File inputFile, String targetExtension)
			throws CommandLineConfigurationException {
		String inputName = inputFile.getName();
		int extensionIndex = inputName.lastIndexOf('.');
		String baseName = extensionIndex > 0 ? inputName.substring(0, extensionIndex) : inputName;
		try {
			File canonicalOutputFile =
					new File(outputDirectory, baseName + targetExtension).getCanonicalFile();
			if (!canonicalOutputFile.toPath().startsWith(outputDirectory.toPath())) {
				throw new CommandLineConfigurationException(
						"Lrf2lrs output target must remain within outputDirectory.");
			}
			return canonicalOutputFile;
		} catch (IOException ignored) {
			throw new CommandLineConfigurationException(
					"Cannot canonicalize Lrf2lrs output file.");
		}
	}
}
