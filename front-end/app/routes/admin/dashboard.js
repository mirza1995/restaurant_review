import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params, transition){
    let user = this.get("ajaxService").currentUser();
    let numberOfRestaurants = this.get("ajaxService").getNumberOfRestaurants();
    let numberOfLocations = this.get("ajaxService").getNumberOfLocations();
    let numberOfUsers = this.get("ajaxService").getNumberOfUsers();
    return Ember.RSVP.hash({user,numberOfRestaurants,numberOfLocations,numberOfUsers});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In" || model.user.account == "user"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
    }
    controller.set("numberOfRestaurants",model.numberOfRestaurants);
    controller.set("numberOfLocations",model.numberOfLocations);
    controller.set("numberOfUsers",model.numberOfUsers);
  }
});
