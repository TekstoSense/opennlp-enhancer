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

import opennlp.tools.ml.maxent.io.GISModelReader;
import opennlp.tools.ml.maxent.io.QNModelReader;
import opennlp.tools.ml.model.AbstractModel;
import opennlp.tools.ml.model.AbstractModelReader;
import opennlp.tools.ml.model.DataReader;
import opennlp.tools.ml.model.GenericModelReader;
import opennlp.tools.ml.perceptron.PerceptronModelReader;

import java.io.File;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/** A subclass of {@link GenericModelReader} that
 *  conserves memory by using delegate readers that call
 *  <code>intern()</code> on the strings they read from their models.
 *  <p>
 *  The assumption here is that there is enough duplication in the strings in
 *  multiple models being loaded that we will benefit from maintaining a single
 *  copy of each string.
 */
public class PooledGenericModelReader extends GenericModelReader {

  /** The delegate model reader. */
  private AbstractModelReader delegateModelReader;

  /**
   * Instantiates a new pooled generic model reader.
   *
   * @param f the f
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public PooledGenericModelReader(File f) throws IOException {
    super(f);
  }

  /**
   * Instantiates a new pooled generic model reader.
   *
   * @param dataReader the data reader
   */
  public PooledGenericModelReader(DataReader dataReader) {
    super(dataReader);
  }

  /* (non-Javadoc)
   * @see opennlp.tools.ml.model.GenericModelReader#checkModelType()
   */
  @Override
  public void checkModelType() throws IOException {
    String modelType = readUTF();
    if (modelType.equals("Perceptron")) {
      delegateModelReader = new LocalPooledPerceptronModelReader(this.dataReader);
    }
    else if (modelType.equals("GIS")) {
      delegateModelReader = new LocalPooledGISModelReader(this.dataReader);
    }
    else if(modelType.equals("QN")){
    	delegateModelReader = new QNModelReader(this.dataReader);
    }
    else {
      throw new IOException("Unknown model format: "+modelType);
    }
  }

  /* (non-Javadoc)
   * @see opennlp.tools.ml.model.GenericModelReader#constructModel()
   */
  @Override
  public AbstractModel constructModel() throws IOException {
    return delegateModelReader.constructModel();
  }

  /** Subclass of {@link GISModelReader} that conserves
   *  memory by calling <code>intern()</code> on the strings it reads from the
   *  model it loads.
   */
  static class LocalPooledGISModelReader extends GISModelReader {
    
    /**
     * Instantiates a new local pooled GIS model reader.
     *
     * @param reader the reader
     */
    public LocalPooledGISModelReader(DataReader reader) {
      super(reader);
    }

    /* (non-Javadoc)
     * @see opennlp.tools.ml.model.AbstractModelReader#readUTF()
     */
    @Override
    public String readUTF() throws IOException {
      return super.readUTF().intern();
    }
  }
  
  /** Subclass of {@link GISModelReader} that conserves
   *  memory by calling <code>intern()</code> on the strings it reads from the
   *  model it loads.
   */
  static class LocalPooledQNModelReader extends QNModelReader {
    
    /**
     * Instantiates a new local pooled QN model reader.
     *
     * @param reader the reader
     */
    public LocalPooledQNModelReader(DataReader reader) {
      super(reader);
    }

    /* (non-Javadoc)
     * @see opennlp.tools.ml.model.AbstractModelReader#readUTF()
     */
    @Override
    public String readUTF() throws IOException {
      return super.readUTF().intern();
    }
  }

  /** Subclass of {@link PerceptronModelReader} that conserves
   *  memory by calling <code>intern()</code> on the strings it reads from the
   *  model it loads.
   */
  static class LocalPooledPerceptronModelReader extends PerceptronModelReader {
    
    /**
     * Instantiates a new local pooled perceptron model reader.
     *
     * @param reader the reader
     */
    public LocalPooledPerceptronModelReader(DataReader reader) {
      super(reader);
    }

    /* (non-Javadoc)
     * @see opennlp.tools.ml.model.AbstractModelReader#readUTF()
     */
    @Override
    public String readUTF() throws IOException {
      return super.readUTF().intern();
    }
  }
}
