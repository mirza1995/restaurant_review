import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  numberOfRestaurants:0,
  numberOfLocations:0,
  numberOfUsers:0,
  user:null,
  loggedIn:null,
  actions: {
    logout(){
      this.get("ajaxService").logout().then(value => {
        this.set("loggedIn",false)
        this.transitionToRoute("/");
      },value=>{
        console.log("There was an error in the logout.");
      });
    }
  }
});
