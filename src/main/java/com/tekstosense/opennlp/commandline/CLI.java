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
package com.tekstosense.opennlp.commandline;

import java.io.IOException;

import com.tekstosense.opennlp.config.ModelLoaderConfig;
import com.tekstosense.opennlp.namefinder.OpenNLPEntityTagger;

// TODO: Auto-generated Javadoc
/**
 * The Class CLI.
 * @author TekstoSense
 */
public class CLI {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		
		if(args.length < 1){
			System.out.println("Enter Text..");
		}
		
		String text = args[0];
		String[] models = ModelLoaderConfig.getModels();
		OpenNLPEntityTagger entityTagger = new OpenNLPEntityTagger();
		entityTagger.loadModels(models);
		String result = entityTagger.getTextWithTag(text);
		System.out.println(result);
	}
}
