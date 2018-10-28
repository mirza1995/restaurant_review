import Route from '@ember/routing/route';
import Ember from 'ember';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params){
    let id = params.id;
    let user = this.get("ajaxService").currentUser();
    return Ember.RSVP.hash({id,user});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
      controller.set('id', model.id);
      controller.transitionToRoute("/admin/createRestaurant/"+model.id+"/restaurant/"+model.id);
    }
  }
});