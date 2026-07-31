/**
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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
package io.github.easy4j.calibre.invoker;

import java.io.File;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.calibre.invoker.command.AbstractCommandLineBuilder;
import io.github.easy4j.calibre.invoker.command.Web2diskCommandLineBuilder;
import io.github.easy4j.calibre.invoker.exception.CalibreInvocationException;
import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;
import io.github.easy4j.calibre.invoker.request.Web2diskInvocationRequest;

/**
 * Class intended to be used by clients who wish to invoke a forked Calibre
 * process from their applications
 */
public class DefaultInvoker implements Invoker {

	public static final String ROLE_HINT = "default";

	private static final InvokerLogger DEFAULT_LOGGER = new SystemOutLogger();

	private static final InvocationOutputHandler DEFAULT_OUTPUT_HANDLER = new SystemOutHandler();

	private InvokerLogger logger = DEFAULT_LOGGER;

	private File workingDirectory;
	
	private File ebookRepositoryDirectory;

	private File calibreHome;

	private InvocationOutputHandler outputHandler = DEFAULT_OUTPUT_HANDLER;

	private InvocationOutputHandler errorHandler = DEFAULT_OUTPUT_HANDLER;
	
	protected AbstractCommandLineBuilder getCommandLineBuilder(InvocationRequest request) {
		if(request instanceof Web2diskInvocationRequest) {
			return new Web2diskCommandLineBuilder();
		}
		return null;
	}
	
	public InvocationResult execute(InvocationRequest request) throws CalibreInvocationException {
		
		AbstractCommandLineBuilder cliBuilder = getCommandLineBuilder(request);

		InvokerLogger logger = getLogger();
		if (logger != null) {
			cliBuilder.setLogger(getLogger());
		}

		File ebookRepo = getEbookRepositoryDirectory();
		if (ebookRepo != null) {
			cliBuilder.setLocalRepositoryDirectory(getEbookRepositoryDirectory());
		}

		File calibreHome = getCalibreHome();
		if (calibreHome != null) {
			cliBuilder.setCalibreHome(getCalibreHome());
		}
		
		File workingDirectory = getWorkingDirectory();
		if (workingDirectory != null) {
			cliBuilder.setWorkingDirectory(getWorkingDirectory());
		}

		Commandline cli;
		try {
			cli = cliBuilder.build(request);
		} catch (CommandLineConfigurationException e) {
			throw new CalibreInvocationException("Error configuring command-line. Reason: " + e.getMessage(), e);
		}

		DefaultInvocationResult result = new DefaultInvocationResult();

		try {
			
			int exitCode = executeCommandLine(cli, request);

			result.setExitCode(exitCode);
		} catch (CommandLineException e) {
			result.setExecutionException(e);
		}

		return result;
	}

	private int executeCommandLine(Commandline cli, InvocationRequest request) throws CommandLineException {
		int result = Integer.MIN_VALUE;

		InvocationOutputHandler outputHandler = request.getOutputHandler(this.outputHandler);
		InvocationOutputHandler errorHandler = request.getErrorHandler(this.errorHandler);

		if (getLogger().isDebugEnabled()) {
			getLogger().debug("Executing: " + cli);
		}
		result = CommandLineUtils.executeCommandLine(cli, outputHandler, errorHandler);
		return result;
	}

	public InvokerLogger getLogger() {
		return logger;
	}

	public Invoker setLogger(InvokerLogger logger) {
		this.logger = (logger != null) ? logger : DEFAULT_LOGGER;
		return this;
	}
	
	@Override
	public File getEbookRepositoryDirectory() {
		return ebookRepositoryDirectory;
	}

	@Override
	public Invoker setEbookRepositoryDirectory(File ebookRepositoryDirectory) {
		this.ebookRepositoryDirectory = ebookRepositoryDirectory;
		return this;
	}

	public File getWorkingDirectory() {
		return workingDirectory;
	}

	public Invoker setWorkingDirectory(File workingDirectory) {
		this.workingDirectory = workingDirectory;
		return this;
	}

	public File getCalibreHome() {
		return calibreHome;
	}

	public Invoker setCalibreHome(File calibreHome) {
		this.calibreHome = calibreHome;
		return this;
	}

	public Invoker setErrorHandler(InvocationOutputHandler errorHandler) {
		this.errorHandler = errorHandler;
		return this;
	}

	public Invoker setOutputHandler(InvocationOutputHandler outputHandler) {
		this.outputHandler = outputHandler;
		return this;
	}

	

}
