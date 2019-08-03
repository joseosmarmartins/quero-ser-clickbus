package com.jose.places.servlet;

import com.jose.places.Place;
import com.jose.places.services.PlaceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.Collection;

@Path("/place/")
public class PlaceServlet {

    @POST
    @Path("save")
    public Place save(@Context HttpServletResponse response, @Context HttpServletRequest request, Place place) {
        try {
            PlaceService placeService = new PlaceService();
            place = placeService.savePlace(place);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return place;
    }

    @GET
    @Path("getSpecificPlace")
    public Place getSpecificPlace(@Context HttpServletResponse response, @Context HttpServletRequest request, @QueryParam("id") Integer id) {
        Place place = null;
        try {
            PlaceService placeService = new PlaceService();
            place = placeService.getSpecificPlace(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return place;
    }

    @GET
    @Path("listPlacesAndFilterByName")
    public Collection<Place> listPlacesAndFilterByName(@Context HttpServletResponse response, @Context HttpServletRequest request, @QueryParam("query") String query) {
        Collection<Place> places = new ArrayList<>();
        try {
            PlaceService placeService = new PlaceService();
            places = placeService.listPlacesAndFilterByName(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return places;
    }

    public void corsResponse(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Content-Type, Access-Control-Allow-Headers, Authorization, X-Auth-Token, X-Requested-With");
    }
}
