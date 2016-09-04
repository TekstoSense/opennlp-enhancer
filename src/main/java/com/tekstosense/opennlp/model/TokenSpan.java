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

import opennlp.tools.util.Span;

// TODO: Auto-generated Javadoc
/**
 * The Class TokenSpan.
 */
public class TokenSpan {

	/** The tokens. */
	String[] tokens;
	
	/** The spans. */
	Span[] spans;

	/**
	 * Instantiates a new token span.
	 *
	 * @param tokens the tokens
	 * @param spans the spans
	 */
	public TokenSpan(String[] tokens, Span[] spans) {
		this.tokens = tokens;
		this.spans = spans;
	}

	/**
	 * Gets the tokens.
	 *
	 * @return the tokens
	 */
	public String[] getTokens() {
		return tokens;
	}

	/**
	 * Gets the spans.
	 *
	 * @return the spans
	 */
	public Span[] getSpans() {
		return spans;
	}

}
