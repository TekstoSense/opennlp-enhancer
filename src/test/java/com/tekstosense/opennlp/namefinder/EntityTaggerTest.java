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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.tekstosense.opennlp.config.ModelLoaderConfig;
import com.tekstosense.opennlp.namefinder.OpenNLPEntityTagger;

/**
 * The Class EntityTaggerTest.
 */
public class EntityTaggerTest {

	/** The entity tagger. */
	OpenNLPEntityTagger entityTagger;

	/** The Constant TEXT. */
	private static final String TEXT = "In January 1993 , the US Department of Transportation granted KLM and Northwest Airlines antitrust immunity , which allowed the two airlines to intensify their partnership.";
	
	/** The Constant TAG_TEXT. */
	private static final String TAG_TEXT = "In January 1993 , the US Department of Transportation granted <START:Airline|Organisation> KLM <END> and <START:Airline|Organisation> Northwest Airlines <END> antitrust immunity , which allowed the two airlines to intensify their partnership.";
	
	/** The Constant TAG_ENTITY. */
	private static final String[] TAG_ENTITY = { "KLM", "Northwest Airlines" };
	
	/** The multi map test. */
	Multimap<String, String> multiMapTest;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		
		//String[] models = ModelLoaderConfig.getModels("/home/biginfolabs/nitin/nitin-bckup/nitin/workspace/bil-text-analyser/bil-text-named-entity/src/main/resources/trainingset/");
		String[] models = ModelLoaderConfig.getModels();
		OpenNLPEntityTagger entityTagger = new OpenNLPEntityTagger();
		entityTagger.loadModels(models);
		
		//String result = entityTagger.getTextWithTag(TEXT);
		//System.out.println(result);
		Multimap<String, String> multimap = entityTagger.getEntityWithText(TEXT);
	
		// System.out.println(entityTagger.getEntityWithText(TEXT).get("Organisation"));
		 
		 for(String key:multimap.keySet()){
	         System.out.println("key :"+key);
	         System.out.println("values :"+ multimap.get(key));
	      }

		}

	/**
	 * Sets the up.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Before
	public void setUp() throws IOException {
		String[] models = ModelLoaderConfig.getModels();
		entityTagger = new OpenNLPEntityTagger();
		entityTagger.loadModels(models);
		
	}

	/**
	 * Gets the text with tag test.
	 *
	 * @return the text with tag test
	 */
	@Test
	public void getTextWithTagTest() {
		String result = entityTagger.getTextWithTag(TEXT);
		assertTrue(result.equals(TAG_TEXT));
	}

	/**
	 * Gets the tagged entity test.
	 *
	 * @return the tagged entity test
	 */
	@Test
	public void getTaggedEntityTest() {
		String[] result = entityTagger.getTaggedEntity(TEXT);
		assertArrayEquals(result, TAG_ENTITY);
	}
}
