import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params, transition){
    let user = this.get("ajaxService").currentUser();
    let numberOfUsers = this.get("ajaxService").getNumberOfUsers();
    let allUsers = this.get("ajaxService").getAllUsers();
    return Ember.RSVP.hash({user,numberOfUsers,allUsers});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In" || model.user.account == "user"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
    }
    controller.set("users",model.allUsers[1]);
    controller.set("numberOfUsers",model.numberOfUsers);
    controller.set("numberOfPages",Math.ceil(parseInt(model.numberOfUsers)/6))
  }
});
