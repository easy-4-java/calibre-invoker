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

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import io.github.easy4j.calibre.invoker.InvocationOutputHandler;

/**
 * Abstract base implementation of {@link InvocationRequest} that provides default behavior
 * for common invocation parameters. Subclasses extend this to add tool-specific parameters
 * for particular Calibre commands.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see InvocationRequest
 * @see DefaultEbookConvertInvocationRequest
 * @see DefaultWeb2diskInvocationRequest
 */
public abstract class AbstractInvocationRequest implements InvocationRequest {

	public static final String DEFAULT_EXECUTABLE = "calibre";

	private boolean debug;

	private InvocationOutputHandler errorHandler;

	private List<String> goals;

	private InvocationOutputHandler outputHandler;

	private Properties properties;

	private boolean shellEnvironmentInherited = true;

	private File calibreHome;

	private Map<String, String> shellEnvironments;
	/**
	 * Show detailed output information. Useful for debugging
	 */
	private boolean verbose;

	/**
	 * {@inheritDoc}
	 */
	public InvocationOutputHandler getErrorHandler(InvocationOutputHandler defaultHandler) {
		return Objects.isNull(errorHandler) ? defaultHandler : errorHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getGoals() {
		return goals;
	}

	/**
	 * {@inheritDoc}
	 */
	public InvocationOutputHandler getOutputHandler(InvocationOutputHandler defaultHandler) {
		return Objects.isNull(outputHandler) ? defaultHandler : outputHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Returns whether debug mode is enabled.
	 *
	 * @return {@code true} if debug mode is enabled, {@code false} otherwise.
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Enables or disables debug mode for this invocation request.
	 *
	 * @param debug {@code true} to enable debug mode, {@code false} otherwise.
	 * @return This invocation request for method chaining.
	 */
	public InvocationRequest setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public InvocationRequest setErrorHandler(InvocationOutputHandler errorHandler) {
		this.errorHandler = errorHandler;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public InvocationRequest setGoals(List<String> goals) {
		this.goals = goals;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public InvocationRequest setOutputHandler(InvocationOutputHandler outputHandler) {
		this.outputHandler = outputHandler;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public InvocationRequest setProperties(Properties properties) {
		this.properties = properties;
		return this;
	}

	/**
	 * @see CalibreCommandLineBuilder#setShellEnvironment(InvocationRequest,
	 *      org.codehaus.plexus.util.cli.Commandline)
	 */
	/**
	 * {@inheritDoc}
	 */
	public boolean isShellEnvironmentInherited() {
		return shellEnvironmentInherited;
	}

	/**
	 * {@inheritDoc}
	 */
	public InvocationRequest setShellEnvironmentInherited(boolean shellEnvironmentInherited) {
		this.shellEnvironmentInherited = shellEnvironmentInherited;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public File getCalibreHome() {
		return calibreHome;
	}

	/**
	 * {@inheritDoc}
	 */
	public InvocationRequest setCalibreHome(File calibreHome) {
		this.calibreHome = calibreHome;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * {@inheritDoc}
	 */
	public InvocationRequest setVerbose(boolean verbose) {
		this.verbose = verbose;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public InvocationRequest addShellEnvironment(String name, String value) {
		if (Objects.isNull(this.shellEnvironments)) {
			this.shellEnvironments = new HashMap<String, String>();
		}
		this.shellEnvironments.put(name, value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, String> getShellEnvironments() {
		return Objects.isNull(shellEnvironments)
				? Collections.<String, String>emptyMap() : shellEnvironments;
	}

}
