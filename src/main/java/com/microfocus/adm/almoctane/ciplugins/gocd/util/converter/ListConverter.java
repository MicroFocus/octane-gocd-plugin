/**
 * Copyright 2018-2023 Open Text
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors (“Open Text”) are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
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
 */

package com.microfocus.adm.almoctane.ciplugins.gocd.util.converter;

import com.microfocus.adm.almoctane.ciplugins.gocd.util.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This a generic converter to convert DAOs to TOs. It will be used by the REST services to convert the response objects.
 */
public class ListConverter<Source, Target> implements Converter<Collection<Source>, List<Target>> {

	private final Converter<Source, Target> converter;

	/**
	 * Use this constructor to use a converter of your choice to convert single items.
	 *
	 * @param converter
	 */
	public ListConverter(Converter<Source, Target> converter) {
		this.converter = converter;
	}

	/**
	 * This method will convert the given Source list into a TO list using a converter.
	 *
	 * @param items list of Source items
	 * @return list of corresponding TO items; will return an empty list if an empty list was given; will return null if the given list was null;
	 */
	@Override
	public List<Target> convert(Collection<Source> items) {
		if (items == null) {
			return null;
		}
		List<Target> itemTOs = new ArrayList<>();
		for (Source item : items) {
			itemTOs.add(converter.convert(item));
		}
		return itemTOs;
	}

	/**
	 * This is a convenience method to allow a more compact code in the using classes.
	 *
	 * @param items items to convert
	 * @param converter
	 * @param <Source>
	 * @param <Target>
	 * @return the converter list
	 */
	public static <Source, Target> List<Target> convert(Collection<Source> items, Converter<Source, Target> converter) {
		return new ListConverter<>(converter).convert(items);
	}
}
