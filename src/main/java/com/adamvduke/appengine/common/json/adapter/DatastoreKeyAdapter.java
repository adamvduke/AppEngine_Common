package com.adamvduke.appengine.common.json.adapter;

import java.lang.reflect.Type;

import com.google.appengine.api.datastore.Key;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DatastoreKeyAdapter implements JsonSerializer <Key> {

	@Override
	public JsonElement serialize( Key src, Type typeOfSrc, JsonSerializationContext context ) {

		return new JsonPrimitive( src.getId() );
	}
}
