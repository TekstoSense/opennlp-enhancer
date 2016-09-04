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
import java.util.Arrays;
import java.util.List;

import opennlp.tools.util.Span;
import static com.tekstosense.opennlp.config.Config.*;

// TODO: Auto-generated Javadoc
/**
 * The Class OrderUtil.
 */
public class OrderUtil {

	/**
	 * Gets the orders.
	 *
	 * @return the orders
	 */
	public static List<String[]> getOrders() {
		List<String[]> orderList = new ArrayList<String[]>();
		String[] orders = getConfiguration().getString("order").split(",");
		for (String order : orders) {
			orderList.add(order.split("\\|"));
		}
		return orderList;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		getOrders().forEach(order -> {
			Arrays.asList(order).forEach(o -> System.out.println(o));
		});
	}
	
	/**
	 * Gets the relevent order.
	 *
	 * @param spans the spans
	 * @return the relevent order
	 */
	public static String[] getReleventOrder(Span[] spans){
		List<String[]> orders = getOrders();
		for (String[] order : orders) {
			if(isCompatable(order,spans)){
				return order;
			}
		}
		return null;
	}

	/**
	 * Checks if is compatable.
	 *
	 * @param order the order
	 * @param spans the spans
	 * @return true, if is compatable
	 */
	private static boolean isCompatable(String[] order, Span[] spans) {
		// TODO Auto-generated method stub
		return false;
	}
}
