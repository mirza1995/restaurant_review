import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  geolocation: Ember.inject.service(),
  loggedIn:false,
  currentLatitude:43.84864,
  currentLongitude:18.35644,
  init(){
    var that = this;
    this.get('geolocation').getLocation().then(geoObject => {
      this.get('geolocation').get('currentLocation');
      let currentLocation = this.get('geolocation').get('currentLocation');
      let location = {
        latitude: currentLocation[0],
        longitude: currentLocation[1]
      }
      this.set("currentLatitude",currentLocation[0]);
      this.set("currentLongitude",currentLocation[1]);

      this.get("ajaxService").getClosestRestaurants(location).then(value => {
        this.set("popularRestaurants",value);
      });
    });
  },
  actions: {
    logout(){
      this.get("ajaxService").logout().then(value => {
        this.set("loggedIn",false)
        this.transitionToRoute("/");
      },value=>{
        console.log("There was an error in the logout.");
      });
    },
    
    searchTable(searchName,hours,date,people){
      this.transitionToRoute("/findTable?searchName="+searchName+"&hours="+hours+"&date="+date+"&persons="+people);
    },

    goToMap(){
      this.transitionToRoute("/map?lat="+this.get("currentLatitude")+"&lng="+this.get("currentLongitude"));
    }
  }
});
