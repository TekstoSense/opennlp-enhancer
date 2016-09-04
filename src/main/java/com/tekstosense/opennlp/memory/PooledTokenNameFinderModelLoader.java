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

package com.tekstosense.opennlp.memory;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import opennlp.tools.cmdline.ModelLoader;
import opennlp.tools.util.InvalidFormatException;


/**
 * Extended Version of Token Name Finder Model loader for lesser memory footprint.
 * <p>
 * <b>Note:</b> Do not use this class, internal use only!
 */
final public class PooledTokenNameFinderModelLoader extends ModelLoader<PooledTokenNameFinderModel> {

	
  /**
   * Instantiates a new pooled token name finder model loader.
   */
  public PooledTokenNameFinderModelLoader() {
    super("Token Name Finder");
  }

  /* (non-Javadoc)
   * @see opennlp.tools.cmdline.ModelLoader#loadModel(java.io.InputStream)
   */
  @Override
  public PooledTokenNameFinderModel loadModel(InputStream modelIn)
      throws IOException, InvalidFormatException {
    return new PooledTokenNameFinderModel(modelIn);
  }

}
