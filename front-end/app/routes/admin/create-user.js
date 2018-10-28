import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params){
    let user = this.get("ajaxService").getUser(params.userId);
    let currentUser = this.get("ajaxService").currentUser();
    let id = params.userId;
    return Ember.RSVP.hash({user,id,currentUser});
  },
  setupController(controller,model){
    if(model.currentUser == "Not Logged In" || model.currentUser.account == "user"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
      controller.set('userId', model.id);
      if(model.user == "New User"){
        controller.set("firstName","");
        controller.set("lastName","");
        controller.set("email","");
        controller.set("phoneNumber","");
        controller.set("user",model.user);
        controller.set("updateUser",false);
        controller.set("adminAccount",false);
      } else {
        let user = model.user;
        controller.set("updateUser",true);
        controller.set("firstName",user.firstName);
        controller.set("lastName",user.lastName);
        controller.set("email",user.email);
        controller.set("phoneNumber",user.phoneNumber);
        controller.set("country",user.country);
        controller.set("city",user.city);
        controller.send("onCountrySelect",user.country);
        controller.send("onCitySelect",user.city);
        controller.set("user",model.user);
        controller.set("adminAccount",user.account == "admin");
      }
    }
  }
});
