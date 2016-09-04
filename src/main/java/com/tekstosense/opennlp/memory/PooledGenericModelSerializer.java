/*
 * Copyright 2008-2011 Grant Ingersoll, Thomas Morton and Drew Farris
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * -------------------
 * To purchase or learn more about Taming Text, by Grant Ingersoll, Thomas Morton and Drew Farris, visit
 * http://www.manning.com/ingersoll
 */

package com.tekstosense.opennlp.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import opennlp.tools.ml.model.AbstractModel;
import opennlp.tools.ml.model.BinaryFileDataReader;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.model.ArtifactSerializer;
import opennlp.tools.util.model.GenericModelSerializer;

// TODO: Auto-generated Javadoc
/**
 * A variant of {@link GenericModelSerializer} that conserves memory by
 * interning the strings read as a part of a model by using a
 * {@link PooledGenericModelReader} to read the model.
 */
public class PooledGenericModelSerializer extends GenericModelSerializer {

	/* (non-Javadoc)
	 * @see opennlp.tools.util.model.GenericModelSerializer#create(java.io.InputStream)
	 */
	@Override
	public AbstractModel create(InputStream in) throws IOException, InvalidFormatException {
		return new PooledGenericModelReader(new BinaryFileDataReader(in)).getModel();
	}

	/**
	 * Register.
	 *
	 * @param factories the factories
	 */
	@SuppressWarnings("rawtypes")
	public static void register(Map<String, ArtifactSerializer> factories) {
		factories.put("model", new PooledGenericModelSerializer());
	}
}
