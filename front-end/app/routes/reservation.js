import Route from '@ember/routing/route';

export default Route.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  model(params){
    let restaurantDetails = this.get("ajaxService").getRestaurantDetails(params.restaurant_id);
    let photos = this.get("ajaxService").getGallery(params.restaurant_id).then(value=>{
      return value;
    },value=>{
      return "No Photos";
    });
    let numberOfPhotos=this.get("ajaxService").getNumberOfPhotos(params.restaurant_id);
    let user = this.get("ajaxService").currentUser();
    let menuInformation={
      id: params.restaurant_id,
      type:"Lunch",
    }
    let menu = this.get("ajaxService").getMenu(menuInformation);
    //For getting todays reservations
    let id=params.restaurant_id;
    return Ember.RSVP.hash({restaurantDetails,photos,numberOfPhotos,user,menu,id});
  },
  setupController(controller,model){
    controller.set('restaurant', model.restaurantDetails);
    let cousinesArray=model.restaurantDetails.cousines;
    let cousines = "";
    for(var i = 0; i < cousinesArray.length; i++){
      cousines += cousinesArray[i].name;
      if(i < cousinesArray.length-1){
        cousines += " | ";
      }
    }
    controller.set("cousines",cousines);
    
    if(model.photos!= "No Photos"){
      controller.set('photos',model.photos);
    }
    controller.set("numberOfPhotos",model.numberOfPhotos);
    if(model.user == "Not Logged In"){
      controller.set("loggedIn",false);
    } else {
      controller.set("loggedIn",true);
      controller.set("user",model.user);
      controller.set("userId",model.user.id);
    }
    controller.set("menu",model.menu);
    controller.set("restaurantId",model.id);
    let restaurant=model.restaurantDetails;
    controller.set('markers', [
      {
        id: 'unique-marker-id',
        lat: restaurant.latitude,
        lng: restaurant.longitude,
        click(event, marker) {},
        rightclick(event, marker) {},
        dblclick(event, marker) {},
        mouseover(event, marker) {},
        mouseout(event, marker) {},
        mouseup(event, marker) {},
        mousedown(event, marker) {},
        drag(e, marker) {},
        dragstart(e, marker) {},
        dragend(e, marker) {}
      }
    ]),
    Ember.$('.reservation__header').css("background",restaurant.coverFileName);
    controller.set("numberOfMenuItems",model.menu.length);
    controller.set("reservationHeaderBackground",'background:url('+restaurant.coverFileName+');')
  }
});
