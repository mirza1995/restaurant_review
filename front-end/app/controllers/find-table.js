import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  restaurants:null,
  loggedIn:false,
  actions:{
    reservation(time,restaurantId,restaurantName,imageFileName){
      let id = restaurantId;
      let date = this.get("reservationCriterium").date;
      let persons = this.get("reservationCriterium").persons;
      this.transitionToRoute("/completeReservation?id="+id+"&date="+date+"&persons="+persons+"&restaurantName="+restaurantName+"&imageFileName="+imageFileName+"&time="+time);
    },

    logout(){
      this.get("ajaxService").logout().then(value => {
        this.set("loggedIn",false)
        this.transitionToRoute("/");
      },value=>{
        console.log("There was an error in the logout.");
      });
    },
  }
});
