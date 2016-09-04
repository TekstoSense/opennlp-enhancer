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
/*
 * 
 */
package com.tekstosense.opennlp.namefinder;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tekstosense.opennlp.config.Config.*;

import com.tekstosense.opennlp.memory.PooledTokenNameFinderModel;

import opennlp.tools.ml.BeamSearch;
import opennlp.tools.ml.EventModelSequenceTrainer;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.ml.SequenceTrainer;
import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.TrainerFactory.TrainerType;
import opennlp.tools.ml.model.Event;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.ml.model.SequenceClassificationModel;
import opennlp.tools.namefind.BioCodec;
import opennlp.tools.namefind.NameFinderEventStream;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleSequenceStream;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.SequenceCodec;
import opennlp.tools.util.SequenceValidator;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.featuregen.AdditionalContextFeatureGenerator;
import opennlp.tools.util.featuregen.WindowFeatureGenerator;

// TODO: Auto-generated Javadoc
/**
 * The Class NamedEntityTagger.
 *
 * @author TekstoSense
 */

public class NamedEntityTagger extends NameFinderME {

	/** The empty. */
	private static String[][] EMPTY = new String[0][0];

	/** The additional context feature generator. */
	private AdditionalContextFeatureGenerator additionalContextFeatureGenerator = new AdditionalContextFeatureGenerator();

	/** The sequence validator. */
	private SequenceValidator<String> sequenceValidator;

	/** The seq codec. */
	private SequenceCodec<String> seqCodec = new BioCodec();

	/** The best sequence. */
	private Sequence bestSequence;

	/**
	 * Instantiates a new named entity tagger.
	 *
	 * @param model
	 *            the model
	 */
	public NamedEntityTagger(PooledTokenNameFinderModel model) {
		super(model);
		TokenNameFinderFactory factory = model.getFactory();

		seqCodec = factory.createSequenceCodec();
		sequenceValidator = seqCodec.createSequenceValidator();
		this.model = model.getNameFinderSequenceModel();
		contextGenerator = factory.createContextGenerator();

		// TODO: We should deprecate this. And come up with a better solution!
		contextGenerator.addFeatureGenerator(new WindowFeatureGenerator(additionalContextFeatureGenerator, 8, 8));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see opennlp.tools.namefind.NameFinderME#find(java.lang.String[])
	 */
	@Override
	public Span[] find(String[] tokens) {
		return find(tokens, EMPTY);
	}

	/**
	 * Generates name tags for the given sequence, typically a sentence,
	 * returning token spans for any identified names.
	 *
	 * @param tokens
	 *            an array of the tokens or words of the sequence, typically a
	 *            sentence.
	 * @param additionalContext
	 *            features which are based on context outside of the sentence
	 *            but which should also be used.
	 *
	 * @return an array of spans for each of the names identified.
	 */
	public Span[] find(String[] tokens, String[][] additionalContext) {

		additionalContextFeatureGenerator.setCurrentContext(additionalContext);

		bestSequence = model.bestSequence(tokens, additionalContext, contextGenerator, sequenceValidator);

		List<String> c = bestSequence.getOutcomes();

		contextGenerator.updateAdaptiveData(tokens, c.toArray(new String[c.size()]));
		Span[] spans = seqCodec.decode(c);
		spans = setProbs(spans);
		return spans;
	}

	/**
	 * sets the probs for the spans.
	 *
	 * @param spans
	 *            the spans
	 * @return the span[]
	 */
	private Span[] setProbs(Span[] spans) {
		double[] probs = probs(spans);
		if (probs != null) {

			for (int i = 0; i < probs.length; i++) {
				double prob = probs[i];
				spans[i] = new Span(spans[i], prob);
			}
		}
		return spans;
	}

	 /**
	   * Returns an array of probabilities for each of the specified spans which is
	   * the arithmetic mean of the probabilities for each of the outcomes which
	   * make up the span.
	   *
	   * @param spans The spans of the names for which probabilities are desired.
	   *
	   * @return an array of probabilities for each of the specified spans.
	   */
	  public double[] probs(Span[] spans) {

	    double[] sprobs = new double[spans.length];
	    double[] probs = bestSequence.getProbs();

	    for (int si = 0; si < spans.length; si++) {

	      double p = 0;

	      for (int oi = spans[si].getStart(); oi < spans[si].getEnd(); oi++) {
	        p += probs[oi];
	      }

	      p /= spans[si].length();

	      sprobs[si] = p;
	    }

	    return sprobs;
	  }
	  
	/**
	 * Train.
	 *
	 * @param languageCode the language code
	 * @param type the type
	 * @param samples the samples
	 * @param trainParams the train params
	 * @param factory the factory
	 * @return the token name finder model
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static TokenNameFinderModel train(String languageCode, String type, ObjectStream<NameSample> samples, TrainingParameters trainParams,
			EntityTaggerFactory factory) throws IOException {
		String beamSizeString = trainParams.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);

		addParamsToTraining(trainParams);

		int beamSize = NameFinderME.DEFAULT_BEAM_SIZE;
		if (beamSizeString != null) {
			beamSize = Integer.parseInt(beamSizeString);
		}

		Map<String, String> manifestInfoEntries = new HashMap<String, String>();

		MaxentModel nameFinderModel = null;

		SequenceClassificationModel<String> seqModel = null;

		TrainerType trainerType = TrainerFactory.getTrainerType(trainParams.getSettings());

		if (TrainerType.EVENT_MODEL_TRAINER.equals(trainerType)) {
			ObjectStream<Event> eventStream = new NameFinderEventStream(samples, type, factory.createContextGenerator(),
					factory.createSequenceCodec());

			EventTrainer trainer = TrainerFactory.getEventTrainer(trainParams.getSettings(), manifestInfoEntries);
			nameFinderModel = trainer.train(eventStream);
		} // TODO: Maybe it is not a good idea, that these two don't use the
			// context generator ?!
			// These also don't use the sequence codec ?!
		else if (TrainerType.EVENT_MODEL_SEQUENCE_TRAINER.equals(trainerType)) {
			NameSampleSequenceStream ss = new NameSampleSequenceStream(samples, factory.createContextGenerator());

			EventModelSequenceTrainer trainer = TrainerFactory.getEventModelSequenceTrainer(trainParams.getSettings(), manifestInfoEntries);
			nameFinderModel = trainer.train(ss);
		} else if (TrainerType.SEQUENCE_TRAINER.equals(trainerType)) {
			SequenceTrainer trainer = TrainerFactory.getSequenceModelTrainer(trainParams.getSettings(), manifestInfoEntries);

			NameSampleSequenceStream ss = new NameSampleSequenceStream(samples, factory.createContextGenerator(), false);
			seqModel = trainer.train(ss);
		} else {
			throw new IllegalStateException("Unexpected trainer type!");
		}

		if (seqModel != null) {
			return new TokenNameFinderModel(languageCode, seqModel, factory.getFeatureGenerator(), factory.getResources(), manifestInfoEntries,
					factory.getSequenceCodec(), factory);
		} else {
			return new TokenNameFinderModel(languageCode, nameFinderModel, beamSize, factory.getFeatureGenerator(), factory.getResources(),
					manifestInfoEntries, factory.getSequenceCodec(), factory);
		}
	}

	/**
	 * Adds the params to training.
	 *
	 * @param trainParams the train params
	 */
	private static void addParamsToTraining(TrainingParameters trainParams) {
		trainParams.put("Threads", getConfiguration().getString("Threads"));
		trainParams.put("Algorithm", getConfiguration().getString("Algorithm"));
	}
}
