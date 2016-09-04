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
package com.tekstosense.opennlp.model;

// TODO: Auto-generated Javadoc
/**
 * The Class NamedEntity.
 */
public class NamedEntity {

	/** The entity. */
	String entity;
	
	/** The type. */
	String[] type;

	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * Sets the entity.
	 *
	 * @param entity the new entity
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String[] getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String[] type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(this.entity);
		string.append(" - > ");
		String sep = "";
		for (int i = 0; i < this.type.length; i++) {
			string.append(sep).append(this.type[i]);
			sep = ",";
		}
		return string.toString();
	}

}
