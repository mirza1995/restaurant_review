import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params){
    let cousine = this.get("ajaxService").getCousine(params.id);
    let id = params.id;
    let user = this.get("ajaxService").currentUser(id);
    return Ember.RSVP.hash({cousine,id,user});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In" || model.user.account == "user"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
      controller.set('id', model.id);
      controller.set("cousinePhoto",model.cousine.photo);
      if(model.cousine == "New Cousine"){
        controller.set("cousineName","");
        controller.set("updateCousine",false);
      } else {
        controller.set("updateCousine",true);
        controller.set("cousineName",model.cousine.name);
        controller.set("cousine",model.cousine);
      }
    }
  }
});
