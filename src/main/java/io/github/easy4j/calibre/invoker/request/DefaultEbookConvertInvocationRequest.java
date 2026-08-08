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

/**
 * 
 * 
 * <p>
 * Convert an e-book from one format to another.
 * </p>
 * 
 * <p>
 * input_file is the input and output_file is the output. Both must be specified
 * as the first two arguments to the command.
 * </p>
 * 
 * <p>
 * The output e-book format is guessed from the file extension of output_file.
 * output_file can also be of the special format .EXT where EXT is the output
 * file extension. In this case, the name of the output file is derived from the
 * name of the input file.
 * </p>
 * <p>
 * Note that the filenames must not start with a hyphen. Finally, if output_file
 * has no extension, then it is treated as a directory and an “open e-book”
 * (OEB) consisting of HTML files is written to that directory. These files are
 * the files that would normally have been passed to the output plugin.
 * </p>
 * <p>
 * After specifying the input and output file you can customize the conversion
 * by specifying various options. The available options depend on the input and
 * output file types. To get help on them specify the input and output file and
 * then use the -h option.
 * </p>
 * https://manual.calibre-ebook.com/generated/en/ebook-convert.html
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class DefaultEbookConvertInvocationRequest  extends AbstractInvocationRequest implements InvocationRequest {

	/*
	 * Look and Feel
	 */
	
	
	
	/**
	 * <p>
	 * Detach from the controlling terminal, if any (Linux only)
	 * </p>
	 * Set the value of the {@code detach} {@code true} if the argument
	 * {@code --detach} was specified, otherwise {@code false}
	 */
	private boolean detach;

	/**
	 * Returns whether the process should detach from the controlling terminal (Linux only).
	 *
	 * @return {@code true} if the process should detach, {@code false} otherwise.
	 */
	public boolean isDetach() {
		return detach;
	}

	/**
	 * Sets whether the process should detach from the controlling terminal (Linux only).
	 *
	 * @param detach {@code true} to detach, {@code false} otherwise.
	 */
	public void setDetach(boolean detach) {
		this.detach = detach;
	}

}
