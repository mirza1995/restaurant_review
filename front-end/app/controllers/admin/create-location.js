import Controller from '@ember/controller';

export default Controller.extend({
  createLocationAjax: Ember.inject.service('ajax-service'),
  showBackEndValidationError:false,
  showLocationError: false,
  arrayOfCoordinates: [],
  locationLatitude:43.84864,
  locationLongitude:18.35644,
  polygonUpdated:false,
  clockStarted:false,
  timer : Ember.run.later(this, () => {
    console.log("Started")
  }, 1500),

  currentTimeMetronome: function(stop) {
    let timer = Ember.run.later(this, () => {
      Ember.run.begin(timer);
      if(this.get("clockStarted")){
        this.set("clockStarted",false);
        console.log("Stoped");
      } else {
        this.set("clockStarted",true);
        this.currentTimeMetronome(false);
        console.log("Started")
      }
    }, 1500);
    if(stop){
      Ember.run.end(timer);
      console.log("Ember Stopped")
      this.currentTimeMetronome(false);
    }
  },
  actions: {
    cancel(){
      this.transitionToRoute("/admin/locations");
    },

    searchLocation(){
      alert(this.get("searchValue"))
    },

    changeLocation(id){
      this.transitionToRoute("/admin/createLocation/"+id);
    },

    addLocation(){
      let locationName=this.get("locationName");

      this.set("showLocationError",locationName==null || locationName=="");
      if(!this.get("showLocationError")){

        let locationName=this.get("locationName");

        if(this.get("id")=="0"){
          let locationInformation={
            "locationName":locationName,
            "numberOfRestaurants": 0,
            "rectanglePoints": this.get("arrayOfCoordinates")
          };
          this.get('createLocationAjax').createLocation(locationInformation).then(value=>{
            //this.transitionToRoute("/admin/locations")
          },value=>{
            this.set("showBackEndValidationError",true);
          });
        } else {
          let locationInformation={
            "id": this.get("location").id,
            "locationName":locationName,
            "numberOfRestaurants": 0,
            "rectanglePoints": this.get("arrayOfCoordinates")
          };
          this.get('createLocationAjax').updateLocation(locationInformation).then(value=>{
            this.transitionToRoute("/admin/locations");
          },value=>{
            this.set("showBackEndValidationError",true);
          });
        }
      } else {
        this.set("showBackEndValidationError",false);
      }
    },

    onMapClick:function(event) {
      let latitude = event.googleEvent.latLng.lat();
      let longitude = event.googleEvent.latLng.lng();
      let arrayOfCoordinates = this.get("arrayOfCoordinates");
      if(arrayOfCoordinates.length < 5){
        arrayOfCoordinates.push({lat: latitude, lng: longitude});
        this.set("arrayOfCoordinates",arrayOfCoordinates);
        this.send("updateRectangle");
      }
    },

    updateRectangle(){
      this.set("polygonUpdated",false);
      setTimeout(()=>{
        this.set("polygonUpdated",true);
      },0.5);
    },

    deleteRectangle(){
      this.set("arrayOfCoordinates",[]);
      this.send("updateRectangle");
    },

    locationNameChanged(){
      let locationName = this.get("locationName");
      if(locationName.length >= 3){
        $.getJSON("https://maps.googleapis.com/maps/api/geocode/json?address="+encodeURIComponent(locationName), val => {
          if(val.results.length) {
            var location = val.results[0].geometry.location;
            this.set("locationLatitude",location.lat);
            this.set("locationLongitude",location.lng);
            this.set("arrayOfCoordinates",[]);
          }
        })
      }
    }
  }
});
