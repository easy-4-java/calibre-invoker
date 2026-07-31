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
package io.github.easy4j.calibre.invoker.command;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.DefaultLrf2lrsInvocationRequest;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;
import io.github.easy4j.calibre.invoker.request.Lrs2lrfInvocationRequest;

/**
 * Convert an LRF file into an LRS (XML UTF-8 encoded) file
 */
public class Lrf2lrsCommandLineBuilder extends AbstractCommandLineBuilder {

	@Override
	protected void doCommandInternal(InvocationRequest request, Commandline cli)
			throws CommandLineConfigurationException {
		
		if(request instanceof Lrs2lrfInvocationRequest) {

			DefaultLrf2lrsInvocationRequest lrf2lrsRequest = ( DefaultLrf2lrsInvocationRequest) request;
			
			setDontOutputResources(lrf2lrsRequest, cli);
			setOutputDirectory(lrf2lrsRequest, cli);
			// LRF file path. file.lrf
			cli.createArg().setValue(lrf2lrsRequest.getLrfFile().getAbsolutePath());
			
		}
		
	}

	@Override
	protected File findCalibreExecutable() throws CommandLineConfigurationException, IOException {
		
		if (calibreHome == null) {
			findCalibreHome();
		}

		logger.debug("Using ${calibre.home} of: \'" + calibreHome + "\'.");

		if (calibreExecutable == null || !calibreExecutable.isAbsolute()) {
			String executable;
			if (calibreExecutable != null) {
				executable = calibreExecutable.getPath();
			} else if (Os.isFamily("windows")) {
				executable = "lrf2lrs.exe";
			} else {
				executable = "lrf2lrs";
			}

			calibreExecutable = new File(calibreHome, executable);

			try {
				File canonicalMvn = calibreExecutable.getCanonicalFile();
				calibreExecutable = canonicalMvn;
			} catch (IOException e) {
				logger.debug("Failed to canonicalize maven executable: " + calibreExecutable + ". Using as-is.", e);
			}

			if (!calibreExecutable.isFile()) {
				throw new CommandLineConfigurationException("Calibre executable not found at: " + calibreExecutable);
			}
		}

		return calibreExecutable;
	}
	
	protected void setDontOutputResources(DefaultLrf2lrsInvocationRequest request, Commandline cli) {
		if(request.isDontOutputResources()) {
			cli.createArg().setValue("--dont-output-resources");
		}
	}

	protected void setOutputDirectory(DefaultLrf2lrsInvocationRequest request, Commandline cli) {
		
		File outputDirectory = request.getOutputDirectory();
		if (outputDirectory != null) {
			try {
				File canSet = outputDirectory.getCanonicalFile();
				outputDirectory = canSet;
			} catch (IOException e) {
				logger.debug("Failed to canonicalize output path: " + outputDirectory.getAbsolutePath() + ".", e);
			}

			cli.createArg().setValue("-o");
			cli.createArg().setValue(outputDirectory.getPath());
		}
 
	}

	
	 
	

}
