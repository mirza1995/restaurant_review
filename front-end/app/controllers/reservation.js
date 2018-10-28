import Controller from '@ember/controller';

export default Controller.extend({
  menu:null,
  cousines:null,
  reservationsNumber:null,
  restaurantId:0,
  numberOfPhotos:0,
  loggedIn:false,
  ajaxService: Ember.inject.service('ajax-service'),
  menuType:2,
  showErrorReservation:false,
  reservationTimesAvaliable:null,
  date:null,
  hours:null,
  persons:null,
  starRating:1,
  actions: {
    logout(){
      this.get("ajaxService").logout().then(value => {
        this.set("loggedIn",false)
      },value=>{
        console.log("There was an error in the logout.");
      });
    },

    getMenu(menuType){
      if(menuType=="Breakfast"){
        this.set("menuType",1);
      } else if(menuType=="Lunch"){
        this.set("menuType",2);
      } else if(menuType=="Dinner"){
        this.set("menuType",3);
      }
      let menuInformation={
        id: this.get("restaurant").id,
        type:menuType,
      }
      let menu = this.get("ajaxService").getMenu(menuInformation).then(value=>{
        this.set("menu",value);
      },value=>{
        console.log("There was an error in the menu request");
      });
    },

    findTable(hours,date,people){
      this.set("date",date);
      this.set("hours",hours);
      this.set("persons",people);
      let reservationCriterium = {
        id:this.get("restaurantId"),
        searchName:"",
        hours:hours,
        date:date,
        persons:people
      }
      
      let restaurants = this.get("ajaxService").findTable(reservationCriterium).then(value=>{
        this.set("reservationFound",true);
        this.set("reservationTimesAvaliable",value[1]);
        this.set("tablesLeft",value[0]);
        let reservationRequest={
          id: this.get("restaurantId"),
          date: date
        }
        this.get("ajaxService").getTodaysReservationNumber(reservationRequest).then(value=>{
          this.set("reservationsNumber",value);
        })
      },value=>{
        this.set("showErrorReservation",true);
      });
    },

    reservation(reservationTime,restaurantId,restaurant_name,image_FileName){
      let id=restaurantId;
      let date=this.get("date");
      let persons= this.get("persons");
      let restaurantName= restaurant_name;
      let imageFileName=image_FileName;
      let time=reservationTime;
      this.transitionToRoute("/completeReservation?id="+id+"&date="+date+"&persons="+persons+"&restaurantName="+restaurantName+"&imageFileName="+imageFileName+"&time="+time);
    },

    getAllPhotos(){
      let menu = this.get("ajaxService").getRestaurantPhotos(this.get("restaurantId")).then(value=>{
        this.set("photos",value);
      },value=>{
        console.log("Error getting all photos")
      });
      return false;
    },

    starRatingClicked(starRating){
      this.set("starRating",starRating);
    },

    saveReview(){
      let reviewInformation={
        restaurant_id: this.get("restaurantId"),
        user_id:this.get("userId"),
        rating: this.get("starRating"),
        description: this.get("reviewDescription")
      }
      this.get("ajaxService").reviewRestaurant(reviewInformation).then(value=>{
        $('#reviewModal').modal('toggle');
        this.get("ajaxService").getRestaurantDetails(this.get("restaurantId")).then(value=>{
          this.set("restaurant",value);
        })
      },value=>{
        console.log("There was a problem with saving review. Please try again later.")
      });
    }
  }
  
});
