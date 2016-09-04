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


import opennlp.tools.ml.model.AbstractModel;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.model.ArtifactSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


// TODO: Auto-generated Javadoc
/** A variant of {@link TokenNameFinderModel} that will
 *  use <code>intern()</code> when reading strings from the model files via
 *  {@link PooledGenericModelSerializer}.
 *  <p>
 *  The assumption here is that there is enough duplication in the strings in
 *  multiple models being loaded that we will benefit from maintaining only a
 *  single copy of each string.
 *
 */
public class PooledTokenNameFinderModel extends TokenNameFinderModel {

  /**
   * Instantiates a new pooled token name finder model.
   *
   * @param in the in
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public PooledTokenNameFinderModel(InputStream in) throws IOException {
    super(in);
  }

  /**
   * Instantiates a new pooled token name finder model.
   *
   * @param languageCode the language code
   * @param nameFinderModel the name finder model
   * @param resources the resources
   * @param manifestInfoEntries the manifest info entries
   */
  public PooledTokenNameFinderModel(String languageCode,
                                    AbstractModel nameFinderModel, Map<String,Object> resources,
                                    Map<String,String> manifestInfoEntries) {
    super(languageCode, nameFinderModel, resources, manifestInfoEntries);
  }

  /**
   * Instantiates a new pooled token name finder model.
   *
   * @param languageCode the language code
   * @param nameFinderModel the name finder model
   * @param generatorDescriptor the generator descriptor
   * @param resources the resources
   * @param manifestInfoEntries the manifest info entries
   */
  public PooledTokenNameFinderModel(String languageCode,
      AbstractModel nameFinderModel, byte[] generatorDescriptor,
      Map<String,Object> resources, Map<String,String> manifestInfoEntries) {
    super(languageCode, nameFinderModel, generatorDescriptor, resources,
        manifestInfoEntries);
  }

  /* (non-Javadoc)
   * @see opennlp.tools.namefind.TokenNameFinderModel#createArtifactSerializers(java.util.Map)
   */
  @SuppressWarnings("rawtypes")
  @Override
  protected void createArtifactSerializers(
      Map<String,ArtifactSerializer> serializers) {
    super.createArtifactSerializers(serializers);
    PooledGenericModelSerializer.register(serializers);
  }
}
