import Route from '@ember/routing/route';
import Ember from 'ember';
export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(){
    let popularLocations = this.get("ajaxService").getPopularLocations();
    let popularRestaurants = this.get("ajaxService").getPopularRestaurants();
    let cousinesCarousel = this.get("ajaxService").getCousinesCarousel();
    let user = this.get("ajaxService").currentUser();
    let numberOfRestaurants = this.get("ajaxService").getNumberOfRestaurants();
    return Ember.RSVP.hash({popularLocations, popularRestaurants, cousinesCarousel, user, numberOfRestaurants});
  },

  setupController(controller,model){
    controller.set('popularLocations', model.popularLocations);
    controller.set("popularRestaurants",model.popularRestaurants);
    controller.set("cousinesCarousel",model.cousinesCarousel);
    if(model.user == "Not Logged In"){
      controller.set("loggedIn",false);
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
    }
    controller.set("numberOfRestaurants",model.numberOfRestaurants);
  },
});
