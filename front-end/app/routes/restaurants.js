import Route from '@ember/routing/route';
import Ember from 'ember';
export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params, transition){
    let searchInformation={
      page:1,
      restaurantCriterium:{
        "restaurantName":"",
        "priceRange":0,
        "mark":0,
      },
      cousines: [].toString()
    }
    let restaurants = this.get("ajaxService").search(searchInformation);
    let popularLocations = this.get("ajaxService").getPopularLocations();
    let user = this.get("ajaxService").currentUser();
    let cousines = this.get("ajaxService").getCousines();
    return Ember.RSVP.hash({restaurants,popularLocations,user,cousines});
  },
  setupController(controller,model){
    if(model.restaurants.length > 0){
      controller.set("restaurants",model.restaurants[1]);
      controller.send('updateNumberOfPages',model.restaurants[0].numberOfRestaurants);
    }else {
      controller.set("restaurants",[]);
      controller.send('updateNumberOfPages',0);
    }
    controller.set("popularLocations",model.popularLocations);
    if(model.user == "Not Logged In"){
      controller.set("loggedIn",false);
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
    }
    controller.set("cousines",model.cousines);
  }
});
