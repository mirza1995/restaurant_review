import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params, transition){
    let queryParams = transition.queryParams;
    let reservationCriterium=queryParams;
    let restaurants=[];
    if(Object.keys(queryParams).length===0){
      reservationCriterium="empty";
    } else {
      restaurants=this.get("ajaxService").findTables(reservationCriterium);
    }
    let user = this.get("ajaxService").currentUser();
    return Ember.RSVP.hash({restaurants,user,reservationCriterium});
  },
  setupController(controller,model){
    if(model.reservationCriterium=="empty"){
      controller.transitionToRoute("/");
    } else {
      controller.set("restaurants",model.restaurants);
      if(model.user == "Not Logged In"){
        controller.set("loggedIn",false);
      } else {
        controller.set("loggedIn",true);
        controller.set("user",model.user);
      }
      controller.set("reservationCriterium",model.reservationCriterium);
    }
  }
});
