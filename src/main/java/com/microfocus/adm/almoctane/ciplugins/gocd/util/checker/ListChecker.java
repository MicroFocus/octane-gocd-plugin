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

package com.microfocus.adm.almoctane.ciplugins.gocd.util.checker;

import com.microfocus.adm.almoctane.ciplugins.gocd.util.checker.Checker;

import java.util.Collection;

/**
 * This a generic checker to convert DAOs to TOs. It will be used by the REST services to convert the response objects.
 */
public class ListChecker<Instance> implements Checker<Collection<Instance>> {

	private final Checker<Instance> checker;

	/**
	 * Use this constructor to use a checker of your choice to check all items.
	 * @param checker
	 */
	public ListChecker(Checker<Instance> checker) {
		this.checker = checker;
	}

	/**
	 * This method will check all items in the collection whether they match the given checker.
	 *
	 * @return true if all item in the list did comply with the given checker.
	 */
	@Override
	public boolean check(Collection<Instance> items) {
		if (items == null) {
			return true;
		}
		for (Instance item : items) {
			if (!checker.check(item)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This is a convenience method to allow a more compact code when using this class.
	 * @param items
	 * @param checker
	 * @param <Instance>
	 * @return
	 */
	public static <Instance> boolean check(Collection<Instance> items, Checker<Instance> checker) {
		return new ListChecker<>(checker).check(items);
	}
}
