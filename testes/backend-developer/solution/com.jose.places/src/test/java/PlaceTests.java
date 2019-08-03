import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jose.places.Place;
import com.jose.places.utils.MongoUtils;
import junit.framework.TestCase;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlaceTests extends TestCase {

    private Gson gson = new Gson();

    public void testCreatePlace() throws Exception {
        Place place = mountPlaceObject();

        MongoUtils mongoUtils = new MongoUtils("127.0.0.1", "27017", "places_test");

        Integer placeID = mongoUtils.save("places", gson.toJson(place));

        assertFalse(placeID == null || placeID == 0);
    }

    public void testEditPlace() throws Exception {
        Place place = mountPlaceObject();

        MongoUtils mongoUtils = new MongoUtils("127.0.0.1", "27017", "places_test");

        Integer lastId = mongoUtils.save("places", gson.toJson(place));

        String placeJson = mongoUtils.getById("places", lastId);

        place = gson.fromJson(placeJson, Place.class);

        String oldSlug = place.getSlug() + "";

        place.setSlug("mychangedAddress.com");

        lastId = mongoUtils.save("places", gson.toJson(place));

        placeJson = mongoUtils.getById("places", lastId);
        place = gson.fromJson(placeJson, Place.class);

        String newSlug = place.getSlug();

        assertFalse(oldSlug.equals(newSlug));
    }

    public void testGetSpecificPlace() throws Exception {
        Place place = mountPlaceObject();

        MongoUtils mongoUtils = new MongoUtils("127.0.0.1", "27017", "places_test");

        Integer placeID = mongoUtils.save("places", gson.toJson(place));
        String placeJson = mongoUtils.getById("places", placeID);

        place = gson.fromJson(placeJson, Place.class);

        assertNotNull(place);
    }

    public void testListPlacesAndFilterByName() throws Exception {
        Type type = new TypeToken<Collection<Place>>() {
        }.getType();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "Rua R");

        MongoUtils mongoUtils = new MongoUtils("127.0.0.1", "27017", "places_test");
        String placesJson = mongoUtils.retrieve("places", parameters);
        Collection<Place> places = gson.fromJson(placesJson, type);

        assertFalse(places == null || places.isEmpty());
    }

    private Place mountPlaceObject() {
        Place place = new Place();
        place.setName("Rua R");
        place.setSlug("myaddress.com");
        place.setCity("Caldas Novas");
        place.setState("Goias");

        return place;
    }
}
