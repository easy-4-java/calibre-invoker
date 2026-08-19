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
 * <p>
 * Launch the calibre edit book tool. You can optionally also specify the names 
 * of files inside the book which will be opened for editing automatically.</p>
 * https://manual.calibre-ebook.com/generated/en/ebook-edit.html
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class DefaultEbookEditInvocationRequest extends AbstractInvocationRequest implements InvocationRequest  {
	
	/**
	 * <p>Detach from the controlling terminal, if any (Linux only)</p>
	 * Set the value of the {@code detach} {@code true} if the
	 * argument {@code --detach} was specified, otherwise {@code false}
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
