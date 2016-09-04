/*******************************************************************************
 * Copyright (c) 2016, TekstoSense and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tekstosense.opennlp.model;

import static com.tekstosense.opennlp.config.Config.*;

/**
 * The Enum MultiTag.
 */
public enum MultiTag {

	/** The multitag. */
	MULTITAG("MultiTag"), /** The facet. */
 FACET("Facet");

	/** The text. */
	private String text;

	/**
	 * Instantiates a new multi tag.
	 *
	 * @param text the text
	 */
	MultiTag(String text) {
		this.text = text;
	}

	/**
	 * Gets the tag type.
	 *
	 * @return the tag type
	 */
	public static MultiTag getTagType(){
		if(isMultipleTagEnabled()){
			for(MultiTag tag : MultiTag.values()){
				if(tag.text.equals(getConfiguration().getString("MultipleTagType")))
					return tag;
			}
		}
		return null;
	}

	/**
	 * Checks if is multiple tag enabled.
	 *
	 * @return true, if is multiple tag enabled
	 */
	public static boolean isMultipleTagEnabled() {
		
		return Boolean.parseBoolean(getConfiguration().getString("MultipleTag"));
	}
}
