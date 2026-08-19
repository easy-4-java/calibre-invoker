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

/**
 * Default implementation of {@link InvocationResult} that describes the result of a Calibre
 * invocation. Stores both the exit code reported by the Calibre process and any exception
 * that occurred during execution.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see InvocationResult
 * @see DefaultInvoker
 */
public final class DefaultInvocationResult
    implements InvocationResult
{

    /**
     * The exception that prevented to execute the command line, will be <code>null</code> if Calibre could be
     * successfully started.
     */
    private CommandLineException executionException;

    /**
     * The exit code reported by the Calibre invocation.
     */
    private int exitCode = Integer.MIN_VALUE;

    /**
     * Creates a new invocation result
     */
    DefaultInvocationResult()
    {
        // hide constructor
    }

    /**
     * {@inheritDoc}
     */
    public int getExitCode()
    {
        return exitCode;
    }

    /**
     * {@inheritDoc}
     */
    public CommandLineException getExecutionException()
    {
        return executionException;
    }

    /**
     * Sets the exit code reported by the Calibre invocation.
     * 
     * @param exitCode The exit code reported by the Calibre invocation.
     */
    void setExitCode( int exitCode )
    {
        this.exitCode = exitCode;
    }

    /**
     * Sets the exception that prevented to execute the command line.
     * 
     * @param executionException The exception that prevented to execute the command line, may be <code>null</code>.
     */
    void setExecutionException( CommandLineException executionException )
    {
        this.executionException = executionException;
    }

}
