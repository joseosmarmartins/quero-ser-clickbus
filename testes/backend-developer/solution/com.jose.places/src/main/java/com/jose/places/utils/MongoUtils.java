package com.jose.places.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

public class MongoUtils {

    private static MongoDatabase database;

    private static MongoClient mongo;

    private Gson gson = new Gson();

    public MongoUtils() throws Exception {
        getDataBase();
    }

    public MongoUtils(String host, String port, String databaseName) throws Exception {
        getDataBase(host, port, databaseName);
    }

    public static MongoDatabase getDataBase(String host, String port, String databaseName) throws Exception {
        if (database != null) return database;
        mongo = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        database = mongo.getDatabase(databaseName);
        return database;
    }

    public static MongoDatabase getDataBase() throws Exception {
        return getDataBase("127.0.0.1", "27017", "places");
    }

    public Integer save(String collectionName, String json) throws Exception {
        return save(collectionName, gson.fromJson(json, Document.class));
    }

    public Integer save(String collectionName, Document document) throws Exception {
        if (document.get("_id") != null) {
            Document filter = new Document();
            int _id = Double.valueOf(document.get("_id").toString()).intValue();
            filter.put("_id", _id);
            document.put("_id", _id);
            document.put("updatedAt", new Date());
            getDataBase().getCollection(collectionName).replaceOne(filter, document);
        } else {
            document.put("createdAt", new Date());
            document.put("_id", nextId(collectionName));
            getDataBase().getCollection(collectionName).insertOne(document);
        }
        return Integer.valueOf(document.get("_id").toString());
    }

    public String retrieve(String collectionName, Map<String, Object> parameters)
            throws Exception {
        FindIterable<Document> find = retrieveIterable(collectionName, parameters);
        Collection<Map<String, Object>> result = new ArrayList<>();
        for (Document document : find) {
            result.add(document);
        }
        return gson.toJson(result);
    }

    public FindIterable<Document> retrieveIterable(String collectionName, Map<String, Object> parameters) throws Exception {
        Document document = criteria(parameters);
        FindIterable<Document> find =
                getDataBase().getCollection(collectionName).find(document);
        return find;
    }

    private Document criteria(Map<String, Object> parameters) {
        Document document = new Document();
        if (parameters == null) parameters = new HashMap<>();
        for (Entry<String, Object> entry : parameters.entrySet()) {
            document.put(entry.getKey(), entry.getValue());
        }
        return document;
    }

    public String getById(String collectionName, Integer _id) throws Exception {
        Document document = getByIdDocument(collectionName, _id);
        return gson.toJson(document);
    }

    public Document getByIdDocument(String collectionName, Integer _id) throws Exception {
        Document document = new Document();
        document.put("_id", _id);
        FindIterable<Document> find = getDataBase().getCollection(collectionName).find(document);
        if (find == null || find.first() == null) return null;
        return find.first();
    }

    public Integer lastId(String collectionName) throws Exception {
        Document document = new Document();
        document.put("_id", -1);
        FindIterable<Document> find = getDataBase().getCollection(collectionName).find().sort(document).limit(1);
        if (find == null || find.first() == null) return null;
        return Integer.valueOf(find.first().get("_id").toString());
    }

    public Integer nextId(String collectionName) throws Exception {
        Integer lastId = lastId(collectionName);
        if (lastId == null) return 1;
        return lastId + 1;
    }
}
