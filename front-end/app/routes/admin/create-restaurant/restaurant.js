import Route from '@ember/routing/route';
import Ember from 'ember';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params){
    let restaurant = this.get("ajaxService").getRestaurantDetails(params.id);
    let cousines = this.get("ajaxService").getCousines();
    let locations = this.get("ajaxService").getAllLocations();
    let id = params.id;
    let user = this.get("ajaxService").currentUser();
    return Ember.RSVP.hash({restaurant,id,cousines,locations,user});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
      controller.set('id', model.id);
      let locationPoints = model.locations[1][0].points;
      let locationRectangle = [];

      controller.set("locationLatitude",locationPoints[0].x);
      controller.set("locationLongitude",locationPoints[0].y);

      for(var i = 0; i < locationPoints.length; i++){
        let locationPoint = locationPoints[i];
        let coordinate = {
          lat: locationPoint.x,
          lng: locationPoint.y,
        }
        locationRectangle.push(coordinate);
      }
      controller.set("locationRectangle",locationRectangle);
      
      if(model.restaurant == "New Restaurant"){
        controller.set("restaurantName","");
        controller.set("updateRestaurant",false);
      } else {
        let restaurant = model.restaurant;
        let restaurantCousines = restaurant.cousines;
        let selectedCousines = [];
        let menus = model.menu;
        let breakfast = [];
        let lunch = [];
        let dinner = [];
        let tables = model.tables;

        for(var i = 0; i < restaurantCousines.length; i++){
          selectedCousines.push(restaurantCousines[i].name);
        }

        controller.set("updateRestaurant",true);
        controller.set("restaurant",restaurant);
        controller.set("restaurantName",restaurant.restaurantName);
        controller.set("selectedCousines",selectedCousines);
        controller.set("restaurantDescription",restaurant.description);
        controller.set("priceRangeSelected",restaurant.priceRange);
        controller.set("locationSelected",restaurant.location.locationName);
        controller.set("latitude",restaurant.latitude);
        controller.set("longitude",restaurant.longitude);
        controller.set("locationLatitude",restaurant.latitude);
        controller.set("locationLongitude",restaurant.longitude);
        controller.set("mapClicked",true);
        controller.set("logoPhoto",restaurant.imageFileName);
        controller.set("coverPhoto",restaurant.coverFileName);
        
      }
      controller.set("locations",model.locations[1]);
      controller.set("cousines",model.cousines);
    }
  }
});