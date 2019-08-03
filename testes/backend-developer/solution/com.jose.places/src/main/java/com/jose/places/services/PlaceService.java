package com.jose.places.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jose.places.Place;
import com.jose.places.utils.MongoUtils;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlaceService {

    private MongoUtils mongoUtils;

    private Gson gson = new Gson();

    public Place savePlace(Place place) throws Exception {
        Integer placeID = getMongoUtils().save("places", gson.toJson(place));
        place = getSpecificPlace(placeID);

        return place;
    }

    public Place getSpecificPlace(Integer placeID) throws Exception {
        String placeJson = getMongoUtils().getById("places", placeID);

        Place place = gson.fromJson(placeJson, Place.class);

        return place;
    }

    public Collection<Place> listPlacesAndFilterByName(String placeName) throws Exception {
        Type type = new TypeToken<Collection<Place>>() {
        }.getType();

        Map<String, Object> parameters = new HashMap<>();

        if (placeName != null) {
            Document nameCriteria = new Document();
            nameCriteria.append("$regex", ".*" + placeName + ".*");
            nameCriteria.append("$options", "gi");

            parameters.put("name", nameCriteria);
        }

        String placesJson = getMongoUtils().retrieve("places", parameters);
        Collection<Place> places = gson.fromJson(placesJson, type);

        return places;
    }

    private MongoUtils getMongoUtils() throws Exception {
        if (mongoUtils == null) mongoUtils = new MongoUtils("127.0.0.1", "27017", "places");
        return mongoUtils;
    }
}
