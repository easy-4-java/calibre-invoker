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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.calibre.invoker.InvokerLogger;
import io.github.easy4j.calibre.invoker.SystemOutLogger;
import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;

/**
 * Abstract base class for building Calibre command-line invocations. Provides common
 * infrastructure for locating the Calibre executable, setting up shell environments,
 * configuring properties, goals, and verbose mode. Subclasses implement
 * tool-specific command-line construction.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see Web2diskCommandLineBuilder
 * @see io.github.easy4j.calibre.invoker.request.InvocationRequest
 */
public abstract class AbstractCommandLineBuilder {
	
	private static final InvokerLogger DEFAULT_LOGGER = new SystemOutLogger();

	protected InvokerLogger logger = DEFAULT_LOGGER;

	protected File workingDirectory;
	
	protected File localRepositoryDirectory;

	protected File calibreHome;

	protected File calibreExecutable;

	protected Properties systemEnvVars;

	/**
	 * Builds a {@link Commandline} for the given invocation request. This method orchestrates
	 * the full command-line construction process including state validation, executable lookup,
	 * shell environment setup, tool-specific configuration, property injection, goal setting,
	 * and verbose flag configuration.
	 *
	 * @param request The invocation request to build a command line for, must not be {@code null}.
	 * @return The constructed command line, never {@code null}.
	 * @throws CommandLineConfigurationException If the command line cannot be constructed due to
	 *         configuration errors or missing executables.
	 */
	public Commandline build(InvocationRequest request) throws CommandLineConfigurationException {
		
		try {
			checkRequiredState();
		} catch (IOException e) {
			throw new CommandLineConfigurationException(e.getMessage(), e);
		}
		
		File executable = null;
		try {
			executable = findCalibreExecutable();
		} catch (IOException e) {
			throw new CommandLineConfigurationException(e.getMessage(), e);
		}
		
		Commandline cli = new Commandline();
		
		cli.setExecutable(executable.getAbsolutePath());

		// handling for OS-level envars
		setShellEnvironment(request, cli);

		this.doCommandInternal(request, cli);
		
		setProperties(request, cli);

		setGoals(request, cli);
		
		setVerbose(request, cli);

		return cli;
	}
	
	/**
	 * Performs tool-specific command-line argument configuration. Subclasses implement this
	 * to add arguments specific to the Calibre tool being invoked.
	 *
	 * @param request The invocation request containing tool-specific parameters.
	 * @param cli The command line to configure.
	 * @throws CommandLineConfigurationException If the command line cannot be configured.
	 */
	protected abstract void doCommandInternal(InvocationRequest request,Commandline cli) throws CommandLineConfigurationException;

	/**
	 * Locates the Calibre executable for this specific tool. Subclasses implement this
	 * to find the correct executable based on the operating system and Calibre home directory.
	 *
	 * @return The resolved executable file, never {@code null}.
	 * @throws CommandLineConfigurationException If the executable cannot be found.
	 * @throws IOException If an I/O error occurs while resolving the executable.
	 */
	protected abstract File findCalibreExecutable() throws CommandLineConfigurationException, IOException;
	
	
	/**
	 * Validates that the builder is in a valid state for command-line construction.
	 *
	 * @throws IOException If an I/O error occurs during validation.
	 * @throws IllegalStateException If a required field (e.g. logger) is not set.
	 */
	protected void checkRequiredState() throws IOException {
		if (logger == null) {
			throw new IllegalStateException("A logger instance is required.");
		}
	}
 
	/**
	 * Configures the shell environment variables for the command line. Inherits system
	 * environment variables if requested, sets the {@code CALIBRE_HOME} variable if specified,
	 * and adds any custom shell environment variables from the request.
	 *
	 * @param request The invocation request containing environment configuration.
	 * @param cli The command line to configure.
	 * @throws CommandLineConfigurationException If environment variables cannot be read.
	 */
	protected void setShellEnvironment(InvocationRequest request, Commandline cli)
			throws CommandLineConfigurationException {
		if (request.isShellEnvironmentInherited()) {
			try {
				cli.addSystemEnvironment();
			} catch (IOException e) {
				throw new CommandLineConfigurationException(
						"Error reading shell environment variables. Reason: " + e.getMessage(), e);
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException) e;
				} else {
					IllegalStateException error = new IllegalStateException(
							"Unknown error retrieving shell environment variables. Reason: " + e.getMessage());
					error.initCause(e);

					throw error;
				}
			}
		}

		if (request.getCalibreHome() != null) {
			cli.addEnvironment("CALIBRE_HOME", request.getCalibreHome().getAbsolutePath());
		}

		for (Map.Entry<String, String> entry : request.getShellEnvironments().entrySet()) {
			cli.addEnvironment(entry.getKey(), entry.getValue());
		}
		
	}

	/**
	 * Adds goal arguments to the command line from the request.
	 *
	 * @param request The invocation request containing goals.
	 * @param cli The command line to configure.
	 */
	protected void setGoals(InvocationRequest request, Commandline cli) {
		List<String> goals = request.getGoals();
		if ((goals != null) && !goals.isEmpty()) {
			cli.createArg().setLine(StringUtils.join(goals.iterator(), " "));
		}
	}

	/**
	 * Adds system properties as {@code -Dkey=value} arguments to the command line.
	 *
	 * @param request The invocation request containing system properties.
	 * @param cli The command line to configure.
	 */
	protected void setProperties(InvocationRequest request, Commandline cli) {
		Properties properties = request.getProperties();

		if (properties != null) {
			for (Iterator<Entry<Object, Object>> it = properties.entrySet().iterator(); it.hasNext();) {
				Entry<Object, Object> entry = it.next();

				String key = (String) entry.getKey();
				String value = (String) entry.getValue();

				cli.createArg().setValue("-D");
				cli.createArg().setValue(key + '=' + value);
			}
		}
	}

	/**
	 * Locates the Calibre home directory by checking the {@code calibre.home} system property
	 * and the {@code CALIBRE_HOME} environment variable, in that order.
	 *
	 * @return The resolved Calibre home directory, or {@code null} if not found.
	 * @throws CommandLineConfigurationException If the configured home is not a valid directory.
	 * @throws IOException If an I/O error occurs while reading environment variables.
	 */
	protected File findCalibreHome() throws CommandLineConfigurationException, IOException {
		if (calibreHome == null) {
			String calibreHomeProperty = System.getProperty("calibre.home");
			if (calibreHomeProperty != null) {
				calibreHome = new File(calibreHomeProperty);
				if (!calibreHome.isDirectory()) {
					throw new IllegalStateException(
							"${calibre.home} is not specified as a directory: \'" + calibreHomeProperty + "\'.");
				}
			}
			
			if ((calibreHome == null) && (getSystemEnvVars().getProperty("CALIBRE_HOME") != null)) {
				calibreHome = new File(getSystemEnvVars().getProperty("CALIBRE_HOME"));
			}
		}
		return calibreHome;
	}
	 
	/**
	 * Adds the {@code --verbose} flag to the command line if verbose mode is enabled in the request.
	 *
	 * @param request The invocation request to check for verbose flag.
	 * @param cli The command line to configure.
	 */
	protected void setVerbose(InvocationRequest request, Commandline cli) {
		if(request.isVerbose()) {
			cli.createArg().setValue("--verbose");
		}
	}

	private Properties getSystemEnvVars() throws IOException {
		if (this.systemEnvVars == null) {
			// with 1.5 replace with System.getenv()
			this.systemEnvVars = CommandLineUtils.getSystemEnvVars();
		}
		return this.systemEnvVars;
	}

	public File getLocalRepositoryDirectory() {
		return localRepositoryDirectory;
	}

	public void setLocalRepositoryDirectory(File localRepositoryDirectory) {
		this.localRepositoryDirectory = localRepositoryDirectory;
	}

	public InvokerLogger getLogger() {
		return logger;
	}

	public void setLogger(InvokerLogger logger) {
		this.logger = logger;
	}

	public File getCalibreHome() {
		return calibreHome;
	}

	public void setCalibreHome(File calibreHome) {
		this.calibreHome = calibreHome;
	}

	public File getCalibreExecutable() {
		return calibreExecutable;
	}

	/**
	 * {@code calibreExecutable} can either be relative to ${calibre.home}/ or absolute
	 * @param calibreExecutable the executable
	 */
	public void setCalibreExecutable(File calibreExecutable) {
		this.calibreExecutable = calibreExecutable;
	}

	public File getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(File workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
 

}
