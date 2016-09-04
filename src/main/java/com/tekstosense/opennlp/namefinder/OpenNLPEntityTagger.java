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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import opennlp.tools.namefind.NameSample;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.tekstosense.opennlp.memory.PooledTokenNameFinderModel;
import com.tekstosense.opennlp.memory.PooledTokenNameFinderModelLoader;
import com.tekstosense.opennlp.model.NamedEntity;
import com.tekstosense.opennlp.model.TokenSpan;

import static com.tekstosense.opennlp.util.SpanUtil.*;


/**
 * The Class OpenNLPEntityTagger.
 * 
 * Wapper Class for common implementation of different NLP Tools.
 * This class can be extended and Implemented as per requirement.
 */

public class OpenNLPEntityTagger {

	/** The Constant LOG. */
	private static final Logger LOG = LogManager.getLogger(OpenNLPEntityTagger.class);

	/** The name finder model. */
	List<PooledTokenNameFinderModel> nameFinderModel;
	
	/** The model loader. */
	PooledTokenNameFinderModelLoader modelLoader;

	/**
	 * Instantiates a new open NLP entity tagger.
	 */
	public OpenNLPEntityTagger() {
		nameFinderModel = new ArrayList<PooledTokenNameFinderModel>();
		modelLoader = new PooledTokenNameFinderModelLoader();
	}

	/**
	 * Load models.
	 *
	 * @param modelPath the model path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void loadModels(String... modelPath) throws IOException {
		List<FileInputStream> inputStreams = new ArrayList<>();
		for (String model : modelPath) {
			LOG.info("Loding Model " + model);
			inputStreams.add(new FileInputStream(model));
		}
		loadModels(inputStreams);
	}

	/**
	 * Load models.
	 *
	 * @param modelStreams the model streams
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void loadModels(List<FileInputStream> modelStreams) throws IOException {
		modelStreams.parallelStream().forEach(is -> {
			try {
				this.nameFinderModel.add(this.modelLoader.loadModel(is));
			} catch (Exception e) {
				LOG.error(e, e);
			}
		});
	}

	/**
	 * Load model.
	 *
	 * @param modelPath the model path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void loadModel(String modelPath) throws IOException {
		loadModel(new FileInputStream(modelPath));
	}

	/**
	 * Load model.
	 *
	 * @param modelStream the model stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void loadModel(InputStream modelStream) throws IOException {
		this.nameFinderModel.add(this.modelLoader.loadModel(modelStream));
	}

	/**
	 * Gets the text with tag.
	 *
	 * @param text the text
	 * @return the text with tag
	 */
	public String getTextWithTag(String text) {
		TokenSpan tokenSpan = getTags(text);
		NameSample nameSample = new NameSample(tokenSpan.getTokens(), tokenSpan.getSpans(), false);
		return nameSample.toString();
	}

	/**
	 * Gets the tagged entity.
	 *
	 * @param text the text
	 * @return the tagged entity
	 */
	public String[] getTaggedEntity(String text) {
		TokenSpan tokenSpan = getTags(text);
		return Span.spansToStrings(tokenSpan.getSpans(), tokenSpan.getTokens());
	}

	/**
	 * Gets the entity with text.
	 *
	 * @param text the text
	 * @return the entity with text
	 */
	public Multimap<String, String> getEntityWithText(String text) {
		NamedEntity[] namedEntities = getNamedEntity(getTags(text));
		Multimap<String, String> namedMap = ArrayListMultimap.create();
		for (int i = 0; i < namedEntities.length; i++) {
			String[] types = namedEntities[i].getType();
			for (int j = 0; j < types.length; j++) {
				namedMap.put(types[j], namedEntities[i].getEntity());
			}
		}
		return namedMap;
	}

	/**
	 * Gets the tagged entity with type.
	 *
	 * @param text the text
	 * @return Array of @NamedEntity containing tagged text and Hierarchical
	 *         Entity Types
	 */
	public NamedEntity[] getTaggedEntityWithType(String text) {
		return getNamedEntity(getTags(text));
	}

	/**
	 * Gets the tags.
	 *
	 * @param text the text
	 * @return the tags
	 */
	private TokenSpan getTags(String text) {

		List<NamedEntityTagger> finders = new ArrayList<NamedEntityTagger>(nameFinderModel.size());
		for (PooledTokenNameFinderModel pooledTokenNameFinderModel : nameFinderModel) {
			finders.add(new NamedEntityTagger(pooledTokenNameFinderModel));
		}

		String[] tokens = WhitespaceTokenizer.INSTANCE.tokenize(text);

		if (tokens.length == 0) {
			for (NamedEntityTagger nameFinder : finders) {
				nameFinder.clearAdaptiveData();
			}
		}

		List<Span> names = new ArrayList<Span>();
		for (NamedEntityTagger nameFinder : finders) {
			Collections.addAll(names, nameFinder.find(tokens));
		}

		Span[] spans = dropOverlappingSpans(names.toArray(new Span[names.size()]));
		return new TokenSpan(tokens, spans);
	}

}
