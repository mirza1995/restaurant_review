import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params, transition){
    let user = this.get("ajaxService").currentUser();
    let locations = this.get("ajaxService").getAllLocations();
    return Ember.RSVP.hash({user,locations});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In" || model.user.account == "user"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
    }
    controller.set("locations",model.locations[1]);
    controller.set("numberOfPages",Math.ceil(parseInt(model.locations[0].numberOfLocations)/12))
  }
});
