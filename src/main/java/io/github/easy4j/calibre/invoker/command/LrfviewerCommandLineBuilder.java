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
import io.github.easy4j.calibre.invoker.request.LrfviewerInvocationRequest;

/**
 * Command-line builder for the Calibre {@code lrfviewer} tool. Constructs a command line to
 * view LRF e-books with configurable rendering options including hyphenation control,
 * profiling, visual debugging, and background color.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see AbstractCommandLineBuilder
 * @see LrfviewerInvocationRequest
 */
public class LrfviewerCommandLineBuilder extends AbstractCommandLineBuilder {

	@Override
	public Commandline build(InvocationRequest request)
			throws CommandLineConfigurationException {
		requireTypedRequest(request);
		return super.build(request);
	}

	@Override
	protected void doCommandInternal(InvocationRequest request, Commandline cli)
			throws CommandLineConfigurationException {
		LrfviewerInvocationRequest lrfviewerRequest = requireTypedRequest(request);
		validateLrfInput(lrfviewerRequest.getLrsFile());

		setDisableHyphenation(lrfviewerRequest, cli);
		setProfile(lrfviewerRequest, cli);
		setVisualDebug(lrfviewerRequest, cli);
		setWhiteBackground(lrfviewerRequest, cli);
		cli.createArg().setValue(lrfviewerRequest.getLrsFile().getAbsolutePath());
	}

	@Override
	protected File findCalibreExecutable()
			throws CommandLineConfigurationException, IOException {
		return resolveCalibreExecutable("lrfviewer", null);
	}

	private LrfviewerInvocationRequest requireTypedRequest(InvocationRequest request)
			throws CommandLineConfigurationException {
		if (Objects.isNull(request) || !(request instanceof LrfviewerInvocationRequest)) {
			throw new CommandLineConfigurationException(
					"Request must implement LrfviewerInvocationRequest.");
		}
		return (LrfviewerInvocationRequest) request;
	}

	private void validateLrfInput(File lrfInput)
			throws CommandLineConfigurationException {
		if (Objects.isNull(lrfInput)) {
			throw new CommandLineConfigurationException(
					"Lrfviewer LRF input (legacy lrsFile) must not be null.");
		}
		if (!lrfInput.exists()) {
			throw new CommandLineConfigurationException(
					"Lrfviewer LRF input (legacy lrsFile) must exist.");
		}
		if (!lrfInput.isFile()) {
			throw new CommandLineConfigurationException(
					"Lrfviewer LRF input (legacy lrsFile) must be a file.");
		}
	}

	protected void setDisableHyphenation(LrfviewerInvocationRequest request,
			Commandline cli) {
		if (request.isDisableHyphenation()) {
			cli.createArg().setValue("--disable-hyphenation");
		}
	}

	protected void setProfile(LrfviewerInvocationRequest request, Commandline cli) {
		if (request.isProfile()) {
			cli.createArg().setValue("--profile");
		}
	}

	protected void setVisualDebug(LrfviewerInvocationRequest request, Commandline cli) {
		if (request.isVisualDebug()) {
			cli.createArg().setValue("--visual-debug");
		}
	}

	protected void setWhiteBackground(LrfviewerInvocationRequest request,
			Commandline cli) {
		if (request.isWhiteBackground()) {
			cli.createArg().setValue("--white-background");
		}
	}
}
