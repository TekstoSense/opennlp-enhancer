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

package com.tekstosense.opennlp.config;

import static com.tekstosense.opennlp.config.Config.*;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class ModelLoaderConfig.
 * @author TekstoSense
 */
public class ModelLoaderConfig {

	
	private static final Logger LOG = LogManager.getLogger(ModelLoaderConfig.class);
	private static final String MODELS = "Models";
	private static final String MODELPATH = "ModelPath";
	
	/**
	 * Gets the model names.
	 *
	 * @return the model names
	 */
	// private static final
	private static String[] getModelNames() {
		return getConfiguration().getString(MODELS).split("\\|");
	}

	/**
	 * Gets the models.
	 *
	 * @return the models
	 */
	public static String[] getModels() {
		File dir = new File(Config.getConfiguration().getString(MODELPATH));

		LOG.info("Loading Models from... " + dir.getAbsolutePath());
		List<String> models = new ArrayList<>();
		String[] modelNames = getModelNames();

		List<String> wildCardPath = Arrays.stream(modelNames).map(model -> {
			return "en-ner-" + model + "*.bin";
		}).collect(Collectors.toList());

		FileFilter fileFilter = new WildcardFileFilter(wildCardPath,
				IOCase.INSENSITIVE);
		List<String> filePath = Arrays.asList(dir.listFiles(fileFilter))
				.stream().map(file -> file.getAbsolutePath())
				.collect(Collectors.toList());
		return filePath.toArray(new String[filePath.size()]);
	}

	public static String[] getModels(String[] modelNames) {
		File dir = new File(Config.getConfiguration().getString(MODELPATH));
		LOG.info("Loading Models from... " + dir.getAbsolutePath());

		List<String> wildCardPath = Arrays.stream(modelNames).map(model -> {
			return "en-ner-" + model + "*.bin";
		}).collect(Collectors.toList());

		FileFilter fileFilter = new WildcardFileFilter(wildCardPath,
				IOCase.INSENSITIVE);
		List<String> filePath = Arrays.asList(dir.listFiles(fileFilter))
				.stream().map(file -> file.getAbsolutePath())
				.collect(Collectors.toList());
		return filePath.toArray(new String[filePath.size()]);
	}
	
	/**
	 * Gets the models. This method is used if ModelPath is passed as parameter.
	 * 
	 *
	 * @return the models
	 */
	public static String[] getModels(String modelDirectory) {
		File dir = new File(modelDirectory);

		LOG.info("Loading Models from... " + dir.getAbsolutePath());
		List<String> models = new ArrayList<>();
		String[] modelNames = getModelNames();

		List<String> wildCardPath = Arrays.stream(modelNames).map(model -> {
			return "en-ner-" + model + "*.bin";
		}).collect(Collectors.toList());

		FileFilter fileFilter = new WildcardFileFilter(wildCardPath,
				IOCase.INSENSITIVE);
		List<String> filePath = Arrays.asList(dir.listFiles(fileFilter))
				.stream().map(file -> file.getAbsolutePath())
				.collect(Collectors.toList());
		return filePath.toArray(new String[filePath.size()]);
	}
}