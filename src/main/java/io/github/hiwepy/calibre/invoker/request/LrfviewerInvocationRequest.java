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
package io.github.hiwepy.calibre.invoker.request;

import java.io.File;

public interface LrfviewerInvocationRequest extends InvocationRequest {
	
	public boolean isDisableHyphenation();
	
	public boolean isProfile() ;

	public File getLrsFile();
	
	public boolean isVisualDebug();

	public boolean isWhiteBackground() ;
	
	/**
	 * Set the value of the {@code disable-hyphenation} {@code true} if the
	 * argument {@code --disable-hyphenation} was specified, otherwise {@code false}
	 */
	InvocationRequest setDisableHyphenation(boolean disableHyphenation);
	
	/**
	 * Set the value of the {@code profile} {@code true} if the
	 * argument {@code --profile} was specified, otherwise {@code false}
	 */
	InvocationRequest setProfile(boolean profile);
	
	/**
	 * Set the value of the {@code visual-debug} {@code true} if the
	 * argument {@code --visual-debug} was specified, otherwise {@code false}
	 */
	InvocationRequest setVisualDebug(boolean visualDebug);

	/**
	 * Set the value of the {@code white-background} {@code true} if the
	 * argument {@code --white-background} was specified, otherwise {@code false}
	 */
	InvocationRequest setWhiteBackground(boolean whiteBackground);
	 
	/**
	 * LRS file path. file.lrs
	 */
	InvocationRequest setLrsFile(File lrsFile);
	 
}
