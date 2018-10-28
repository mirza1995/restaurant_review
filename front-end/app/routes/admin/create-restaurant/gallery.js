import Route from '@ember/routing/route';

export default Route.extend({
	ajaxService: Ember.inject.service('ajax-service'),
  model(params){
    let restaurant = this.get("ajaxService").getRestaurantDetails(params.id);
    let id = params.id;
    let user = this.get("ajaxService").currentUser();
    let photos = this.get("ajaxService").getRestaurantPhotos(id);
    return Ember.RSVP.hash({restaurant,id,user,photos});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In"){
      controller.transitionToRoute("/");
    } else {
      controller.set("photos",model.photos);
      controller.set("loggedIn",true);
      controller.set("user",model.user);
      controller.set('id', model.id);
    }
  }
});
