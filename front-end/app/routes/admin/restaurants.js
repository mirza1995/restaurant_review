import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params, transition){
    let user = this.get("ajaxService").currentUser();
    let restaurants = this.get("ajaxService").getRestaurantsForAdmin();
    return Ember.RSVP.hash({user,restaurants});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In" || model.user.account == "user"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
    }
    controller.set("restaurants",model.restaurants[1]);
    controller.set("numberOfPages",Math.ceil(parseInt(model.restaurants[0].numberOfRestaurants)/12))
  }
});
