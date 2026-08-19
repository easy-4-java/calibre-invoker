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
package io.github.easy4j.calibre.invoker.exception;

import io.github.easy4j.calibre.invoker.InvocationResult;

/**
 * Signals an error during the construction or execution of the command line used to invoke Calibre,
 * e.g. illegal invocation arguments or command-line configuration errors. This should not be confused
 * with a failure of the invoked Calibre process itself which will be reported by means of a
 * non-zero exit code via {@link InvocationResult#getExitCode()}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see InvocationResult#getExitCode()
 * @see io.github.easy4j.calibre.invoker.Invoker#execute(io.github.easy4j.calibre.invoker.request.InvocationRequest)
 */
public class CalibreInvocationException
    extends Exception
{

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception using the specified detail message and cause.
     * 
     * @param message The detail message for this exception, may be <code>null</code>.
     * @param cause The nested exception, may be <code>null</code>.
     */
    public CalibreInvocationException( String message, Throwable cause )
    {
        super( message, cause );
    }

    /**
     * Creates a new exception using the specified detail message.
     * 
     * @param message The detail message for this exception, may be <code>null</code>.
     */
    public CalibreInvocationException( String message )
    {
        super( message );
    }

}
