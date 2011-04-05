package com.adamvduke.appengine.common.json;

import com.adamvduke.appengine.common.json.adapter.DatastoreKeyAdapter;
import com.google.appengine.api.datastore.Key;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AppEngineGsonFactory {

	private boolean prettyPrinting;

	public Gson getGson() {

		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		configurePrettyPrinting( builder );
		registerTypeAdapters( builder );
		return builder.create();
	}

	private void configurePrettyPrinting( GsonBuilder builder ) {

		if ( prettyPrinting ) {
			builder.setPrettyPrinting();
		}
	}

	private void registerTypeAdapters( GsonBuilder builder ) {

		builder.registerTypeAdapter( Key.class, new DatastoreKeyAdapter() );
	}

	public void setPrettyPrinting( boolean prettyPrinting ) {

		this.prettyPrinting = prettyPrinting;
	}
}
