import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params){
    let restaurant = this.get("ajaxService").getRestaurantDetails(params.id);
    let id = params.id;
    let tables = this.get("ajaxService").getRestaurantTables(id);
    let user = this.get("ajaxService").currentUser();
    return Ember.RSVP.hash({restaurant,id,tables,user});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
      controller.set('id', model.id);
      controller.set("restaurant",model.restaurant)
      controller.set("tables",model.tables);
      controller.set("numberOfTables",model.tables.length);
    }
  }
});
