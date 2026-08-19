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
package io.github.easy4j.calibre.invoker.support;

import java.io.File;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;

import io.github.easy4j.calibre.invoker.InvocationOutputHandler;
import io.github.easy4j.calibre.invoker.ProcessExecutor;

/**
 * Test process executor that records the requested executable without launching a process.
 */
public final class RecordingProcessExecutor implements ProcessExecutor {

    private final int exitCode;

    private String executableName;

    public RecordingProcessExecutor(int exitCode) {
        this.exitCode = exitCode;
    }

    @Override
    public int execute(Commandline commandline, InvocationOutputHandler outputHandler,
            InvocationOutputHandler errorHandler) throws CommandLineException {
        executableName = new File(commandline.getCommandline()[0]).getName();
        return exitCode;
    }

    public String getExecutableName() {
        return executableName;
    }
}
