/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.kernel.util;

import spock.lang.Specification;

/**
 * @author Sampsa Sohlman
 * @author Manuel de la Peña
 * @author Peter Borkuti
 */
public class MapUtilTest extends Specification {

	def "when you try to create a linked hash Map from an array with invalid parameters, a empty map should be obtained"() {
		when:
		Map<String, Object> map = MapUtil.toLinkedHashMap(stringArray.split());

		then:
		map.isEmpty();

		where:
		stringArray << ["one", "one:two:three:four"]
	}

	def "when you try to create a linked hash Map from a null array, a empty map should be obtained"() {
		when:
		Map<String, Object> map = MapUtil.toLinkedHashMap(null);

		then:
		map.isEmpty();
	}

	def "when you try to create a linked hash Map from an array that contains a parameter of type Object, a empty map should be obtained"() {
		when:
		def stringArray = "one:1:" + Object.class.getName();
		Map<String, Object> map = MapUtil.toLinkedHashMap(stringArray.split());

		then:
		map.isEmpty();
	}

	def "when you try to create a linked hash Map from an array with zero legth, a empty map should be obtained"() {
		when:
		Map<String, Object> map = MapUtil.toLinkedHashMap(new String[0]);

		then:
		map.isEmpty();
	}

	def "when you try to create a linked hash Map from a empty array with a custom delimiter,a empty map should be obtained"() {
		when:
		Map<String, Object> map = MapUtil.toLinkedHashMap(new String[0], ",");

		then:
		map.isEmpty();
	}

	def "when you try to create a linked hash Map from an array using a custom delimiter, a map should be returned separated by the custom delimeter"() {
		when:
		Map<String, Object> map = MapUtil.toLinkedHashMap((String[])stringArray.toArray(), ",");

		then:
		map.size() == mapSize;
		map.containsKey(key);
		map.get(key).equals(value);

		where:
		stringArray | mapSize | key | value
		["one,1"]| 1 | "one" | "1"
		["one,1", "two,2"]| 2 | "two" | "2"
	}

	def "when you try to create a linked hash Map from an array using the default delimiter, a map should be returned separated by the default delimeted"() {
		when:
		Map<String, Object> map = MapUtil.toLinkedHashMap((String[])stringArray.toArray());

		then:
		map.size() == mapSize;
		map.containsKey(key);
		map.get(key).equals(value);

		where:
		stringArray | mapSize | key | value
		["one:1"] | 1 | "one" | "1"
		["one:1", "two:2"] | 2 | "two" | "2"
	}

	def "when you try to create a linked hash Map from an array that contains a Number object type defined incorrectly, a NumberFormatException should be obtained"() {
		when:
		MapUtil.toLinkedHashMap(stringArray);

		then:
		NumberFormatException e = thrown();

		where:
		stringArray << ["one:foo:" + Double.class.getName(), "one:foo:" + Integer.class.getName(), "one:foo:" + Long.class.getName(), "one:foo:" + Short.class.getName()]
	}

	def "when you try to create a linked hash Map from an array that contains a primitive number type defined incorrectly, a NumberFormatException should be obtained"() {
		when:
		MapUtil.toLinkedHashMap(stringArray);

		then:
		NumberFormatException e = thrown();

		where:
		stringArray << ["one:foo:double", "one:foo:int", "one:foo:long", "one:foo:short"]
	}

	def "when you try to create a linked hash Map from an array that contains a primitive boolean type defined incorrectly, a map that contains a false boolean should be returned"(){
		when:
		Map<String, Object> map =  MapUtil.toLinkedHashMap("one:foo:boolean");

		then:
		map.size() == 1
		map.containsKey("one");
		map.get("one") == false;
	}

	def "when you try to create a linked hash Map from an array that contains a primitive boolean type defined as true, a map that contains a true boolean should be returned"(){
		when:
		Map<String, Object> map =  MapUtil.toLinkedHashMap("one:true:boolean");

		then:
		map.size() == 1
		map.containsKey("one");
		map.get("one") == true;
	}

	def "when you try to create a linked hash Map from an array that contains a Boolean type defined incorrectly, a map that contains a false Boolean should be returned"(){
		when:
		Map<String, Object> map =  MapUtil.toLinkedHashMap("one:true:" + Boolean.class.getName());

		then:
		map.size() == 1
		map.containsKey("one");
		map.get("one") == true;
		map.get("one") instanceof Boolean;
	}

	def "when you try to create a linked hash Map from an array that contains a Boolean type defined as true, a map that contains a true Boolean should be returned"(){
		when:
		Map<String, Object> map =  MapUtil.toLinkedHashMap("one:true:" + Boolean.class.getName());

		then:
		map.size() == 1
		map.containsKey("one");
		map.get("one") == true;
		map.get("one") instanceof Boolean;
	}

	def "when you try to create a linked hash Map from an array that contains a primitive number defined, a map that contains the same primitive number should be returned"(){
		when:
		Map<String, Object> map =  MapUtil.toLinkedHashMap(stringArray);

		then:
		map.size() == mapSize
		map.containsKey(key);
		map.get(key) == value;

		where:
		stringArray | mapSize | key | value
		"one:1:int"| 1 | "one" | 1
		"one:1:double"| 1 | "one" | 1.0d
		"one:1:long"| 1 | "one" | 1L
		"one:1:short"| 1 | "one" | 1
	}


	def "when you try to create a linked hash Map from an array that contains a Number defined, a map that contains the same Number should be returned"(){
		when:
		Map<String, Object> map =  MapUtil.toLinkedHashMap(stringArray);

		then:
		map.size() == mapSize
		map.containsKey(key);
		def mapValue = map.get(key);
		mapValue == value;
		mapValue.getClass().equals(clazz);


		where:
		stringArray | mapSize | key | value | clazz
		"one:1:" + Byte.class.getName()| 1 | "one" | 1 | Byte.class
		"one:1:" + Float.class.getName()| 1 | "one" | 1 | Float.class
		"one:1:" + Integer.class.getName()| 1 | "one" | 1 | Integer.class
		"one:1:" + Double.class.getName() | 1 | "one" | 1.0d | Double.class
		"one:1:" + Long.class.getName() | 1 | "one" | 1L | Long.class
		"one:1:" + Short.class.getName() | 1 | "one" | 1 | Short.class
	}

	def "when you try to create a linked hash Map from an array that contains a String defined, a map that contains the same String should be returned"(){
		when:
		Map<String, Object> map =  MapUtil.toLinkedHashMap("one:X:" + String.class.getName());

		then:
		map.size() == 1
		map.containsKey("one");
		def mapValue = map.get("one");
		mapValue == "X";
		mapValue instanceof String
	}

	def "when you try to create a Map from an array of a odd size, a exception should be obtained"(){
		when:
		MapUtil.fromArray((String[])["one", "two", "three"].toArray());

		then:
		IllegalArgumentException e = thrown();
		e.getMessage() == "Array length is not an even number";
	}

	def "when you try to create a Map from an array of zero size,an empty map should be returned"(){
		when:
		Map<String, Object> map = MapUtil.fromArray(new String[0]);

		then:
		map.isEmpty();
	}


	def "should suceed with even leght"(){
		when:
		String[] array = [
			PropsKeys.MESSAGE_BOARDS_EMAIL_FROM_ADDRESS,
			PropsKeys.ADMIN_EMAIL_FROM_ADDRESS,
			PropsKeys.MESSAGE_BOARDS_EMAIL_FROM_NAME,
			PropsKeys.ADMIN_EMAIL_FROM_NAME,
			"allowAnonymousPosting",
			PropsKeys.MESSAGE_BOARDS_ANONYMOUS_POSTING_ENABLED,
			"emailFromAddress",
			PropsKeys.MESSAGE_BOARDS_EMAIL_FROM_ADDRESS,
			"emailFromName", PropsKeys.MESSAGE_BOARDS_EMAIL_FROM_NAME,
			"emailHtmlFormat", PropsKeys.MESSAGE_BOARDS_EMAIL_HTML_FORMAT,
			"emailMessageAddedEnabled",
			PropsKeys.MESSAGE_BOARDS_EMAIL_MESSAGE_ADDED_ENABLED,
			"emailMessageUpdatedEnabled",
			PropsKeys.MESSAGE_BOARDS_EMAIL_MESSAGE_UPDATED_ENABLED,
			"enableFlags", PropsKeys.MESSAGE_BOARDS_FLAGS_ENABLED,
			"enableRatings", PropsKeys.MESSAGE_BOARDS_RATINGS_ENABLED,
			"enableRss", PropsKeys.MESSAGE_BOARDS_RSS_ENABLED,
			"messageFormat",
			PropsKeys.MESSAGE_BOARDS_MESSAGE_FORMATS_DEFAULT, "priorities",
			PropsKeys.MESSAGE_BOARDS_THREAD_PRIORITIES, "ranks",
			PropsKeys.MESSAGE_BOARDS_USER_RANKS, "recentPostsDateOffset",
			PropsKeys.MESSAGE_BOARDS_RECENT_POSTS_DATE_OFFSET, "rssDelta",
			PropsKeys.SEARCH_CONTAINER_PAGE_DEFAULT_DELTA,
			"rssDisplayStyle", PropsKeys.RSS_FEED_DISPLAY_STYLE_DEFAULT,
			"rssFeedType", PropsKeys.RSS_FEED_TYPE_DEFAULT,
			"subscribeByDefault",
			PropsKeys.MESSAGE_BOARDS_SUBSCRIBE_BY_DEFAULT]

		Map<String, Object> map = MapUtil.fromArray(array);

		then:
		map.size() > 0;
		for (int i = 0; i < array.length; i += 2) {
			array[i + 1] == map.get(array[i]);
		}
	}

	def "When a map filter is used to filter the even keys, the map returned should only contains the even keys"(){
		when:
		Map<String, String> inputMap = new HashMap<String, String>();

		inputMap.put("1", "one");
		inputMap.put("2", "two");
		inputMap.put("3", "three");
		inputMap.put("4", "four");
		inputMap.put("5", "five");

		Map<String, String> outputMap = MapUtil.filter(
				inputMap, new HashMap<String, String>(),
				new PredicateFilter<String>() {

					@Override
					public boolean filter(String string) {
						int value = GetterUtil.getInteger(string);

						if ((value % 2) == 0) {
							return true;
						}

						return false;
					}

				});


		then:
		outputMap.size() == 2;
		outputMap.get("2") == "two";
		outputMap.get("4") == "four";

	}

	def "When a map filter is used to filter the keys that not contains x, the map returned should not contains these keys"(){
		when:
		Map<String, String> inputMap = new HashMap<String, String>();

		inputMap.put("x1", "one");
		inputMap.put("2", "two");
		inputMap.put("x3", "three");
		inputMap.put("4", "four");
		inputMap.put("x5", "five");

		Map<String, String> outputMap = MapUtil.filter(
				inputMap, new HashMap<String, String>(),
				new PrefixPredicateFilter("x"));

		then:
		outputMap.size() == 2;
		outputMap.get("2") == "two";
		outputMap.get("4") == "four";
	}

	def "When a map filter is used to filter the keys that contains x, the map returned should contains these keys"(){
		when:
		Map<String, String> inputMap = new HashMap<String, String>();

		inputMap.put("x1", "one");
		inputMap.put("2", "two");
		inputMap.put("x3", "three");
		inputMap.put("4", "four");
		inputMap.put("x5", "five");

		Map<String, String> outputMap = MapUtil.filter(
				inputMap, new HashMap<String, String>(),
				new PrefixPredicateFilter("x", true));

		then:
		outputMap.size() == 3;
		outputMap.get("x1") == "one";
		outputMap.get("x3") == "three";
		outputMap.get("x5") == "five";
	}

}