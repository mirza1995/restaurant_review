import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params){
    let location = this.get("ajaxService").getLocation(params.id);
    let id = params.id;
    let user = this.get("ajaxService").currentUser(id);
    return Ember.RSVP.hash({location,id,user});
  },
  setupController(controller,model){
    if(model.user == "Not Logged In" || model.user.account == "user"){
      controller.transitionToRoute("/");
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
      controller.set('id', model.id);
      if(model.location == "New Location"){
        controller.set("locationName","");
        controller.set("updateLocation",false);
      } else {
        controller.set("updateLocation",true);
        controller.set("locationName",model.location.locationName);
        controller.set("location",model.location);
        $.getJSON("https://maps.googleapis.com/maps/api/geocode/json?address="+encodeURIComponent(model.location.locationName), val => {
          if(val.results.length) {
            var location = val.results[0].geometry.location;
            controller.set("locationLatitude",location.lat);
            controller.set("locationLongitude",location.lng);
          }
        })

        let locationPoints = model.location.points;
        let locationRectangle = [];
        for(var i = 0; i < locationPoints.length; i++){
          let locationPoint = locationPoints[i];
          let coordinate = {
            lat: locationPoint.x,
            lng: locationPoint.y,
          }
          locationRectangle.push(coordinate);
        }
        controller.set("arrayOfCoordinates",locationRectangle);
        controller.send("updateRectangle");
      }
    }
  }
});
