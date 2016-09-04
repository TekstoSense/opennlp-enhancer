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
package com.tekstosense.opennlp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.tekstosense.opennlp.model.MultiTag;
import com.tekstosense.opennlp.model.NamedEntity;
import com.tekstosense.opennlp.model.TokenSpan;

import static com.tekstosense.opennlp.model.MultiTag.*;
import opennlp.tools.util.Span;

// TODO: Auto-generated Javadoc
/**
 * The Class SpanUtil.
 */
public class SpanUtil {

	/**
	 * Drop overlapping spans.
	 *
	 * @param spans the spans
	 * @return the span[]
	 */
	public static Span[] dropOverlappingSpans(Span spans[]) {

		List<Span> sortedSpans = new ArrayList<Span>(spans.length);
		Collections.addAll(sortedSpans, spans);
		Collections.sort(sortedSpans);
		Iterator<Span> it = sortedSpans.iterator();
		Span lastSpan = null;
		while (it.hasNext()) {
			Span span = it.next();
			if (span.equals(lastSpan))
				it.remove();

			lastSpan = span;
		}
		return getTaggedSpans(sortedSpans);

	}

	/**
	 * Gets the tagged spans.
	 *
	 * @param array the array
	 * @return the tagged spans
	 */
	private static Span[] getTaggedSpans(List<Span> array) {
		MultiTag tagType = getTagType();
		if (tagType != null) {
			if (tagType == MultiTag.FACET)
				return facetedTags(array);
			else if (tagType == MultiTag.MULTITAG)
				return multipleTaging(array);
		}
		return singleTagging(array);
	}

	/**
	 * Single tagging.
	 *
	 * @param spans the spans
	 * @return the span[]
	 */
	private static Span[] singleTagging(List<Span> spans) {

		Iterator<Span> it = spans.iterator();
		Span lastSpan = null;
		while (it.hasNext()) {
			Span span = it.next();
			if (lastSpan != null) {
				if (lastSpan.intersects(span)) {
					it.remove();
					span = lastSpan;
				}
			}
			lastSpan = span;
		}
		return spans.toArray(new Span[spans.size()]);
	}

	/**
	 * Multiple taging.
	 *
	 * @param spans the spans
	 * @return the span[]
	 */
	private static Span[] multipleTaging(List<Span> spans) {
		return getOrderedTagging(spans);
	}

	/**
	 * Gets the ordered tagging.
	 *
	 * @param spans the spans
	 * @return the ordered tagging
	 */
	private static Span[] getOrderedTagging(List<Span> spans) {
		return spans.toArray(new Span[spans.size()]);
	}

	/**
	 * Faceted tags.
	 *
	 * @param spans the spans
	 * @return the span[]
	 */
	private static Span[] facetedTags(List<Span> spans) {
		return getFacetedOrderedTagging(spans);
	}

	/**
	 * Gets the faceted ordered tagging.
	 *
	 * @param spans the spans
	 * @return the faceted ordered tagging
	 */
	private static Span[] getFacetedOrderedTagging(List<Span> spans) {
		Iterator<Span> it = spans.iterator();
		List<Span> spans2 = new ArrayList<Span>();
		Span lastSpan = null;
		String type = "";
		int s = 0;
		int e = 0;
		while (it.hasNext()) {
			Span span = it.next();
			if (lastSpan != null) {
				if (!lastSpan.intersects(span)) {
					spans2.add(new Span(s, e, type));
					type = span.getType();
					s = span.getStart();
					e = span.getEnd();
				} else {
					type += "|" + span.getType();
				}
			} else {
				type = span.getType();
				s = span.getStart();
				e = span.getEnd();
			}
			lastSpan = span;
		}
		if (!type.isEmpty())
			spans2.add(new Span(s, e, type));
		return spans2.toArray(new Span[spans2.size()]);
	}

	/**
	 * Gets the named entity.
	 *
	 * @param tokenSpan the token span
	 * @return the named entity
	 */
	public static NamedEntity[] getNamedEntity(TokenSpan tokenSpan) {
		Span[] spans = tokenSpan.getSpans();
		String[] tokens = tokenSpan.getTokens();

		String[] spanText = Span.spansToStrings(spans, tokens);
		NamedEntity[] namedEntities = new NamedEntity[spans.length];

		for (int i = 0; i < spans.length; i++) {
			NamedEntity entity = new NamedEntity();
			entity.setEntity(spanText[i]);
			entity.setType(spans[i].getType().split("\\|"));
			namedEntities[i] = entity;
		}

		return namedEntities;
	}
}
