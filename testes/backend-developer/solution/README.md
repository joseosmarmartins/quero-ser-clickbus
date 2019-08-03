Was created two projects.

The project "com.jose.places" is my model project, in this project there is the modeling and the rules of the back of the project, as the database configuration, mongodb in this case, and all the services, in this case a service with the methods of a CRUD, as requested.

The project "com.jose.places.servlet" is the servlet, a project that provides the API to consumption, this API realizes the call on the methods on the model project.

To run this project in the easiest way i just build a jar of the model project and add as a dependency on the servlet, so you just need to download or clone the servlet project and run with a tomcat 8 or higher, remember to check if the jar of the model is in your build path.

As example of endpoints:

-The method save:
    For this method you just need to do a POST request with a JSON body, as example:
        {
          "name": "Rua R",
          "slug": "myaddress.com",
          "city": "Caldas Novas",
          "state": "Goias"
        }
    And as example of the call it will be:
        http://localhost:8080/com.jose.places.servlet/v1/place/save
    And as return it will produces a new JSON:
        {
          "name": "Rua R",
          "slug": "myaddress.com",
          "city": "Caldas Novas",
          "state": "Goias",
          "_id": 1,
          "createdAt": "Aug 2, 2019 12:27:33 AM"
        }

-To edit a place:
    To update is used the same method, if the object have an "_id" it will be updated, as example:
        {
          "name": "Rua R Alterada",
          "slug": "myaddresschanged.com",
          "city": "Caldas Novas",
          "state": "Goias",
          "_id": 1,
          "createdAt": "Aug 2, 2019 12:27:33 AM"
        }
    The same URL of the previous method in POST:
        http://localhost:8080/com.jose.places.servlet/v1/place/save
    And as return:
        {
          "name": "Rua R Alterada",
          "slug": "myaddresschanged.com",
          "city": "Caldas Novas",
          "state": "Goias",
          "_id": 1,
          "createdAt": "Aug 2, 2019 12:27:33 AM",
          "updatedAt": "Aug 2, 2019 12:30:53 AM"
        }

-To get a specific place:
    To get a specific place the call will be a GET, as example:
        http://localhost:8080/com.jose.places.servlet/v1/place/getSpecificPlace?id=1
    And it will return a JSON with the specific place:
        {
          "name": "Rua R Alterada",
          "slug": "myaddresschanged.com",
          "city": "Caldas Novas",
          "state": "Goias",
          "_id": 1,
          "createdAt": "Aug 2, 2019 12:27:33 AM",
          "updatedAt": "Aug 2, 2019 12:30:53 AM"
        }

-And to get a list of places:
    To get a list of places is also a GET, example:
        http://localhost:8080/com.jose.places.servlet/v1/place/listPlacesAndFilterByName
    And to filter it is only required to send a query param with the name or part of the name of the place:
        http://localhost:8080/com.jose.places.servlet/v1/place/listPlacesAndFilterByName?query=rua
    And it will return a JSON with a list of places:
        [
          {
            "name": "Rua R Alterada",
            "slug": "myaddresschanged.com",
            "city": "Caldas Novas",
            "state": "Goias",
            "_id": 4,
            "createdAt": "Aug 3, 2019 12:27:33 AM",
            "updatedAt": "Aug 3, 2019 12:30:53 AM"
          }
        ]

There is a Testing Environment running in: http://138.197.129.20:8080/com.jose.places.servlet/v1/place/
To use it just add the method you want, the correct call (POST, GET, etc) and the necessary parameters, as the given examples.

