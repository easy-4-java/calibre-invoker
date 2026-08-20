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
import io.github.easy4j.calibre.invoker.request.Lrs2lrfInvocationRequest;

/**
 * Command-line builder for the Calibre {@code lrs2lrf} tool. Constructs a command line to
 * compile an LRS file into an LRF file with optional output directory and LRS mode
 * configuration.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractCommandLineBuilder
 * @see Lrs2lrfInvocationRequest
 */
public class Lrs2lrfCommandLineBuilder extends AbstractCommandLineBuilder {

	@Override
	public Commandline build(InvocationRequest request)
			throws CommandLineConfigurationException {
		requireTypedRequest(request);
		return super.build(request);
	}

	@Override
	protected void doCommandInternal(InvocationRequest request, Commandline cli)
			throws CommandLineConfigurationException {
		Lrs2lrfInvocationRequest lrs2lrfRequest = requireTypedRequest(request);
		validateLrsFile(lrs2lrfRequest.getLrsFile());

		setLrs(lrs2lrfRequest, cli);
		setOutputDirectory(lrs2lrfRequest, cli);
		cli.createArg().setValue(lrs2lrfRequest.getLrsFile().getAbsolutePath());
	}

	@Override
	protected File findCalibreExecutable()
			throws CommandLineConfigurationException, IOException {
		return resolveCalibreExecutable("lrs2lrf", null);
	}

	private Lrs2lrfInvocationRequest requireTypedRequest(InvocationRequest request)
			throws CommandLineConfigurationException {
		if (Objects.isNull(request) || !(request instanceof Lrs2lrfInvocationRequest)) {
			throw new CommandLineConfigurationException(
					"Request must implement Lrs2lrfInvocationRequest.");
		}
		return (Lrs2lrfInvocationRequest) request;
	}

	private void validateLrsFile(File lrsFile)
			throws CommandLineConfigurationException {
		if (Objects.isNull(lrsFile)) {
			throw new CommandLineConfigurationException(
					"Lrs2lrf lrsFile must not be null.");
		}
		if (!lrsFile.exists()) {
			throw new CommandLineConfigurationException(
					"Lrs2lrf lrsFile must exist.");
		}
		if (!lrsFile.isFile()) {
			throw new CommandLineConfigurationException(
					"Lrs2lrf lrsFile must be a file.");
		}
	}

	protected void setLrs(Lrs2lrfInvocationRequest request, Commandline cli) {
		if (request.isLrs()) {
			cli.createArg().setValue("--lrs");
		}
	}

	protected void setOutputDirectory(Lrs2lrfInvocationRequest request,
			Commandline cli) throws CommandLineConfigurationException {
		File outputDirectory = request.getOutputDirectory();
		if (Objects.nonNull(outputDirectory)) {
			outputDirectory = validateOutputDirectory(outputDirectory);
			File outputFile = outputFile(outputDirectory, request.getLrsFile(), ".lrf");

			cli.createArg().setValue("-o");
			cli.createArg().setValue(outputFile.getPath());
		}
	}

	private File validateOutputDirectory(File outputDirectory)
			throws CommandLineConfigurationException {
		if (!outputDirectory.exists()) {
			throw new CommandLineConfigurationException(
					"Lrs2lrf outputDirectory must exist.");
		}
		if (!outputDirectory.isDirectory()) {
			throw new CommandLineConfigurationException(
					"Lrs2lrf outputDirectory must be a directory.");
		}
		try {
			return outputDirectory.getCanonicalFile();
		} catch (IOException ignored) {
			throw new CommandLineConfigurationException(
					"Cannot canonicalize Lrs2lrf outputDirectory.");
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
						"Lrs2lrf output target must remain within outputDirectory.");
			}
			return canonicalOutputFile;
		} catch (IOException ignored) {
			throw new CommandLineConfigurationException(
					"Cannot canonicalize Lrs2lrf output file.");
		}
	}
}
