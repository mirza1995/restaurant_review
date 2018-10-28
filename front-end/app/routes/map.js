import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params, transition){
    let queryParams = transition.queryParams;
    let user = this.get("ajaxService").currentUser();
    return Ember.RSVP.hash({user, queryParams});
  },

  setupController(controller,model){
    let postRequest;
    if(Object.keys(model.queryParams).length===0){
      postRequest = {
        latitude: 43.84864,
        longitude: 18.35644
      }
    } else {
      postRequest = {
        latitude: model.queryParams.lat,
        longitude: model.queryParams.lng
      }
      controller.set("userLocationFound",true);
    }
    let restaurants = this.get("ajaxService").getClosestRestaurantsForMap(postRequest).then(value => {
      controller.set("restaurants", value);
      controller.set("numberOfRestaurants",value.length)
    });

    if(model.user == "Not Logged In"){
      controller.set("loggedIn", false);
    } else {
      controller.set("loggedIn", true);
      controller.set("user", model.user);
    }
  },
});
