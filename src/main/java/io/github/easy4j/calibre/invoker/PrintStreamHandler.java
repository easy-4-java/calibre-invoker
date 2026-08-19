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

import java.io.PrintStream;

/**
 * An {@link InvocationOutputHandler} implementation that writes output lines to a
 * {@link PrintStream}. Optionally flushes the stream after each line to ensure
 * timely output delivery.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 3.0.0
 * @see InvocationOutputHandler
 * @see SystemOutHandler
 */
public class PrintStreamHandler implements InvocationOutputHandler {

	/**
	 * The print stream to write to, never <code>null</code>.
	 */
	private PrintStream out;

	/**
	 * A flag whether the print stream should be flushed after each line.
	 */
	private boolean alwaysFlush;

	/**
	 * Creates a new output handler that writes to {@link System#out}.
	 */
	public PrintStreamHandler() {
		this(System.out, false);
	}

	/**
	 * Creates a new output handler that writes to the specified print stream.
	 * @param out The print stream to write to, must not be <code>null</code>.
	 * @param alwaysFlush A flag whether the print stream should be flushed after each line.
	 */
	public PrintStreamHandler(PrintStream out, boolean alwaysFlush) {
		if (out == null) {
			throw new NullPointerException("missing output stream");
		}
		this.out = out;
		this.alwaysFlush = alwaysFlush;
	}

	/**
	 * Consumes a single line of output. If the line is {@code null}, an empty line is printed.
	 * If {@code alwaysFlush} is enabled, the stream is flushed after each line.
	 *
	 * @param line The output line to consume, may be {@code null}.
	 */
	public void consumeLine(String line) {
		if (line == null) {
			out.println();
		} else {
			out.println(line);
		}

		if (alwaysFlush) {
			out.flush();
		}
	}

}
