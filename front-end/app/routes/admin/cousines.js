import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params, transition){
    let user = this.get("ajaxService").currentUser();
    let cousines = this.get("ajaxService").getAllCousines();
    return Ember.RSVP.hash({user,cousines});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In" || model.user.account == "user"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
    }
    controller.set("cousines",model.cousines[1]);
    controller.set("numberOfPages",Math.ceil(parseInt(model.cousines[0].numberOfCousines)/12))
  }
});
