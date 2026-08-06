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

import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.calibre.invoker.exception.CommandLineConfigurationException;
import io.github.easy4j.calibre.invoker.request.DefaultFetchEbookMetadataInvocationRequest;
import io.github.easy4j.calibre.invoker.request.FetchEbookMetadataInvocationRequest;
import io.github.easy4j.calibre.invoker.request.InvocationRequest;

/**
 * Fetch book metadata from online sources. You must specify at least one of title, authors or ISBN.
 */
public class FetchEbookMetadataCommandLineBuilder extends AbstractCommandLineBuilder {

	@Override
	protected void doCommandInternal(InvocationRequest request, Commandline cli)
			throws CommandLineConfigurationException {
		
		if(request instanceof FetchEbookMetadataInvocationRequest) {

			DefaultFetchEbookMetadataInvocationRequest fetchRequest = ( DefaultFetchEbookMetadataInvocationRequest) request;
			
			setAllowedPlugin(fetchRequest, cli);
			setAuthors(fetchRequest, cli);
			// LRF file path. file.lrf
			cli.createArg().setValue(fetchRequest.getCoverFile().getAbsolutePath());
			
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
				executable = "fetch-ebook-metadata.exe";
			} else {
				executable = "fetch-ebook-metadata";
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
	

	protected void setAllowedPlugin(DefaultFetchEbookMetadataInvocationRequest request, Commandline cli) {
		if(StringUtils.isNotEmpty(request.getAllowedPlugin())) {
			cli.createArg().setValue("--allowed-plugin");
			cli.createArg().setValue(request.getAllowedPlugin());
		}
	}
	
	protected void setAuthors(DefaultFetchEbookMetadataInvocationRequest request, Commandline cli) {
		if(request.isAuthors()) {
			cli.createArg().setValue("--authors");
		}
	}
	
	protected void setCoverFile(DefaultFetchEbookMetadataInvocationRequest request, Commandline cli) {
		
		File coverFile = request.getCoverFile();
		if (coverFile != null) {
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
	
	protected void setIsbn(DefaultFetchEbookMetadataInvocationRequest request, Commandline cli) {
		if(request.isIsbn()) {
			cli.createArg().setValue("--isbn");
		}
	}

	protected void setOpf(DefaultFetchEbookMetadataInvocationRequest request, Commandline cli) {
		if(request.isOpf()) {
			cli.createArg().setValue("--opf");
		}
	}
	
	protected void setTimeout(DefaultFetchEbookMetadataInvocationRequest request, Commandline cli) {
		long timeout = request.getTimeout();
		if (timeout > 0) {
			cli.createArg().setValue("--timeout");
			cli.createArg().setValue(String.valueOf(timeout));
		}
	}
	
	protected void setTitle(DefaultFetchEbookMetadataInvocationRequest request, Commandline cli) {
		if(request.isAuthors()) {
			cli.createArg().setValue("--title");
		}
	}

}
