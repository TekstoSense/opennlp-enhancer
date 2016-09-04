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
package com.tekstosense.opennlp.namefinder;

import java.util.Map;

import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.util.SequenceCodec;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating EntityTagger objects.
 */
public class EntityTaggerFactory extends TokenNameFinderFactory {

	/** The feature generator bytes. */
	private byte[] featureGeneratorBytes;
	
	/** The resources. */
	private Map<String, Object> resources;
	
	/** The seq codec. */
	private SequenceCodec<String> seqCodec;

	/**
	 * Instantiates a new entity tagger factory.
	 */
	public EntityTaggerFactory() {
		super();
	}

	/**
	 * Instantiates a new entity tagger factory.
	 *
	 * @param featureGeneratorBytes the feature generator bytes
	 * @param resources the resources
	 * @param seqCodec the seq codec
	 */
	public EntityTaggerFactory(byte[] featureGeneratorBytes, final Map<String, Object> resources, SequenceCodec<String> seqCodec) {
		init(featureGeneratorBytes, resources, seqCodec);
	}

	/**
	 * Inits the.
	 *
	 * @param featureGeneratorBytes the feature generator bytes
	 * @param resources the resources
	 * @param seqCodec the seq codec
	 */
	void init(byte[] featureGeneratorBytes, final Map<String, Object> resources, SequenceCodec<String> seqCodec) {
		this.featureGeneratorBytes = featureGeneratorBytes;
		this.resources = resources;
		this.seqCodec = seqCodec;
	}

	/* (non-Javadoc)
	 * @see opennlp.tools.namefind.TokenNameFinderFactory#getSequenceCodec()
	 */
	protected SequenceCodec<String> getSequenceCodec() {
		return seqCodec;
	}

	/* (non-Javadoc)
	 * @see opennlp.tools.namefind.TokenNameFinderFactory#getResources()
	 */
	protected Map<String, Object> getResources() {
		return resources;
	}

	/* (non-Javadoc)
	 * @see opennlp.tools.namefind.TokenNameFinderFactory#getFeatureGenerator()
	 */
	protected byte[] getFeatureGenerator() {
		return featureGeneratorBytes;
	}
}
