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
package io.github.easy4j.calibre.invoker;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Executes a configured Calibre command line.
 */
public interface ProcessExecutor {

    /**
     * Executes the command line using the supplied standard-output and error handlers.
     *
     * @param commandline command line to execute
     * @param outputHandler handler for standard output
     * @param errorHandler handler for standard error
     * @return process exit code
     * @throws CommandLineException if process execution fails
     */
    int execute(Commandline commandline, InvocationOutputHandler outputHandler,
            InvocationOutputHandler errorHandler) throws CommandLineException;
}
