import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  latitude:0,
  longitude:0,
  locationLatitude: 43.84864,
  locationLongitude:18.35644,
  selectedCousines: [],
  locationSelected:"Sarajevo",
  priceRangeSelected: 1,
  restaurantDescription: null,
  restaurantPage: 1,
  tablesSize: ["one","two","three","four","five","six","seven","eight","nine","ten"],
  logoPhoto: null,
  coverPhoto: null,
  restaurantCreated:false,
  locationRectangle:null,
  actions: {

    onCousineSelect(event){
      const selectedCousines = Ember.$(event.target).val();
      this.set('selectedCousines', selectedCousines || []);
      console.log(this.get("selectedCousines"))
    },

    onPriceRangeSelect(value){
      this.set("priceRangeSelected",value);
    },
    
    onLocationSelect(value){
      this.set("locationSelected",value);
      this.get("ajaxService").getLocationByName(value).then(val => {
        let locationPoints = val.points;
        this.set("locationLatitude",locationPoints[0].x);
        this.set("locationLongitude",locationPoints[0].y);
        let locationRectangle = [];
        for(var i = 0; i < locationPoints.length; i++){
          let locationPoint = locationPoints[i];
          let coordinate = {
            lat: locationPoint.x,
            lng: locationPoint.y,
          }
          locationRectangle.push(coordinate);
        }
        this.set("locationRectangle",locationRectangle);
        console.log(this.get("locationRectangle"));
      },()=>{
        console.log("There was a problem with Location");
      });
    },

    addRestaurant(){
      this.send("validateRestaurantData");
      if(!this.get("basicDetailsError")){
        let locations = this.get("locations");
        let location = null;

        for(var i = 0; i < locations.length; i++){
          if(locations[i].locationName == this.get("locationSelected")){
            location = locations[i];
            break;
          }
        }
        this.set("locationOfRestaurant",location);
        var cousines = this.get("cousines");
        let selectedCousines = [];
        for(var i = 0; i < cousines.length; i++){
          if(this.get("selectedCousines").indexOf(cousines[i].name) > -1){
            selectedCousines.push(cousines[i]);
          }
        }
        this.set("cousinesOfRestaurant",selectedCousines);

        //Upload Logo photo
        let base64 = this.get("logoPhoto").split(";base64,");
        let imageType = base64[0].split("/")[1];
        let uploadInformation = {
          imageType: imageType,
          photo: base64[1]
        }
        if(base64.length > 1){
          this.get("ajaxService").uploadImage(uploadInformation).then(value => {
            this.set("logoPhoto",value);
            this.send("uploadCoverPhoto");
          },()=>{
            console.log("There was a problem with Uploading of Logo Image");
          });
        } else {
          this.send("uploadCoverPhoto");
        }
      }
    },

    uploadCoverPhoto(){
      let base64 = this.get("coverPhoto").split(";base64,");
      let imageType = base64[0].split("/")[1];
      let uploadInformation = {
        imageType: imageType,
        photo: base64[1]
      }
      if(base64.length > 1){
        this.get("ajaxService").uploadImage(uploadInformation).then(value => {
          this.set("coverPhoto",value);
          this.send("createRestaurant");
        },()=>{
          console.log("There was a problem with Uploading of Cover Image");
        });
      } else {
        this.send("createRestaurant");
      }
    },

    createRestaurant(){
      if(this.get("id") == 0){
        let restaurantInformation = {
          restaurantName: this.get("restaurantName"),
          description: this.get("restaurantDescription"),
          latitude: this.get("latitude"),
          longitude: this.get("longitude"),
          mark: 0,
          votes: 0,
          priceRange: this.get("priceRangeSelected"),
          imageFileName: this.get("logoPhoto"),
          coverFileName: this.get("coverPhoto"),
          booked: 0,
          location: this.get("locationOfRestaurant"),
          cousines: this.get("cousinesOfRestaurant")
        }
        this.get("ajaxService").addRestaurant(restaurantInformation).then(value => {
          this.set("restaurantCreated",true);
          this.set("id",value.id);
          this.transitionToRoute("/admin/createRestaurant/"+value.id+"/restaurant/"+value.id);
        },value=>{
          console.log("There was a problem with saving restaurant");
        });
      } else {
        let restaurant = this.get("restaurant");
        let restaurantInformation = {
          id: restaurant.id,
          restaurantName: this.get("restaurantName"),
          description: this.get("restaurantDescription"),
          latitude: this.get("latitude"),
          longitude: this.get("longitude"),
          mark: restaurant.mark,
          votes: restaurant.votes,
          priceRange: this.get("priceRangeSelected"),
          imageFileName: this.get("logoPhoto"),
          coverFileName: this.get("coverPhoto"),
          booked: restaurant.booked,
          location: this.get("locationOfRestaurant"),
          cousines: this.get("cousinesOfRestaurant")
        }
        this.get("ajaxService").updateRestaurant(restaurantInformation).then(value => {
          this.set("currentRestaurant",value);
          this.set("restaurantUpdated",true);
        },value=>{
          console.log("There was a problem with saving restaurant");
        });
      }
    },

    validateRestaurantData(){
      let restaurantName = this.get("restaurantName");
      let description = this.get("description");
      let errorMessages = [];
      let errorFound = false;
      if(restaurantName == "" || description == ""){
        errorMessages.push("Restaurant name or description is missing.");
        errorFound = true;
      } 
      if(!this.get("mapClicked")){
        errorMessages.push("Location of restaurant is missing on the map.");
        errorFound = true;
      }
      if(this.get("selectedCousines").length == 0){
        errorMessages.push("Restaurant cousines are empty.");
        errorFound = true;
      }
      if(this.get("logoPhoto") == null){
        errorMessages.push("Logo is not uploaded.");
        errorFound = true;
      }
      if(this.get("coverPhoto") == null){
        errorMessages.push("Cover photo is not uploaded.");
        errorFound = true;
      }
      if(errorFound){
        this.set("basicDetailsErrorMessages",errorMessages);
      } 
      this.set("basicDetailsError",errorFound);
    },

    onMapClick:function(event) {
      let latitude = event.googleEvent.latLng.lat();
      let longitude = event.googleEvent.latLng.lng();
      this.set("latitude",latitude);
      this.set("longitude",longitude);
      this.set("mapClicked",true);
      this.send("calculateArea",latitude,longitude)
    },

    calculateArea(lat, lng){
      let locationRectangle = this.get("locationRectangle");
      console.log(locationRectangle)
      let distance1 = (locationRectangle[0].lng - locationRectangle[2].lng) * (locationRectangle[3].lat - locationRectangle[1].lat);
      let distance2 = (locationRectangle[1].lng - locationRectangle[3].lng) * (locationRectangle[0].lat - locationRectangle[2].lat);
      let rectangleArea = 0.5 * (Math.abs(distance1 + distance2));

      let sumOfTriangleAreas = 0;
      let point1Lat = 0;
      let point2Lat = 10;
      let point3Lat = locationRectangle[3].lat;
      let point1Lng = 10;
      let point2Lng = 15;
      let point3Lng = locationRectangle[3].lng;
      sumOfTriangleAreas += 0.5 *(point1Lat * (point2Lng- point3Lng) + point2Lat * (point3Lng- point1Lng) + point3Lat * (point1Lng - point2Lng));

      point1Lat = -10;
      point2Lat = 10;
      point3Lat = 0;
      point1Lng = 0;
      point2Lng = 15;
      point3Lng = -10;
      sumOfTriangleAreas += 0.5 *(point1Lat * (point2Lng- point3Lng) + point2Lat * (point3Lng- point1Lng) + point3Lat * (point1Lng - point2Lng));
      
      point1Lat = 0;
      point2Lat = 10;
      point3Lat = 10;
      point1Lng = -10;
      point2Lng = 15;
      point3Lng = 0;
      sumOfTriangleAreas += 0.5 *(point1Lat * (point2Lng- point3Lng) + point2Lat * (point3Lng- point1Lng) + point3Lat * (point1Lng - point2Lng));
      
      point1Lat = 10;
      point2Lat = 10;
      point3Lat = 0;
      point1Lng = 15;
      point2Lng = 0;
      point3Lng = 10;
      sumOfTriangleAreas += 0.5 *(point1Lat * (point2Lng- point3Lng) + point2Lat * (point3Lng- point1Lng) + point3Lat * (point1Lng - point2Lng));
      
      for(var i = 0; i < 4; i++){
        let j = i + 1;
        if(i == 3){
          j = 0;
        }
        console.log("i: " + locationRectangle[i].lat + "," + locationRectangle[i].lng)
        console.log("j: " + locationRectangle[j].lat + "," + locationRectangle[j].lng)
        sumOfTriangleAreas += 0.5 *(locationRectangle[i].lat * (locationRectangle[j].lng - lng) + locationRectangle[j].lat * (lng- locationRectangle[i].lng) + lat * (locationRectangle[i].lng - locationRectangle[j].lng));
      }
      console.log(Math.abs(sumOfTriangleAreas) <= rectangleArea);
      console.log(sumOfTriangleAreas)
      
    },

    coverUpload(url){
      this.set("coverPhoto",url);
    },

    logoUpload(url){
      this.set("logoPhoto",url);
    },

    cancel(){
      this.transitionToRoute("/admin/restaurants");
    },
  }
});
