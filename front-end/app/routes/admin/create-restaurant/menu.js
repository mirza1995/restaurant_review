import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params){
    let id = params.id;
    let menu = this.get("ajaxService").getRestaurantMenu(id);
    let user = this.get("ajaxService").currentUser();
    let restaurant = this.get("ajaxService").getRestaurantDetails(params.id);
    return Ember.RSVP.hash({id,menu,user,restaurant});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
      controller.set('id', model.id);
      controller.set("restaurant",model.restaurant)
      let menus = model.menu;
      let breakfast = [];
      let lunch = [];
      let dinner = [];

      for(var i = 0; i < menus.length; i++){
        if(menus[i].type == "Breakfast"){
          breakfast.push(menus[i]);
        } else if(menus[i].type == "Lunch"){
          lunch.push(menus[i]);
        } else if(menus[i].type == "Dinner"){
          dinner.push(menus[i]);
        }
      }

      controller.set("breakfast",breakfast);
      controller.set("lunch",lunch);
      controller.set("dinner",dinner);
      controller.set("menu",breakfast);
      controller.set("numberOfMenuItems",breakfast.length);
    }
  }
});
