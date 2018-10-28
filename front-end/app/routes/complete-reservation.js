import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params, transition){
    let queryParams = transition.queryParams;
    let reservationInformation=queryParams;
    let user = this.get("ajaxService").currentUser();
    return Ember.RSVP.hash({reservationInformation,user});
  },
  setupController(controller,model){
    if(Object.keys(model.reservationInformation).length===0){
      controller.transitionToRoute("/");
    } else {
      if(model.user == "Not Logged In"){
        controller.set("loggedIn",false);
      } else {
        controller.set("loggedIn",true);
        controller.set("user",model.user);
      }
      controller.set("reservationInformation",model.reservationInformation);
    }
  }
});
