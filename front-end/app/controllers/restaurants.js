import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  restaurants:null,
  numberOfPages: 1,
  page:1,
  restaurantsLoaded:false,
  loggedIn:false,
  activeCousine:[],
  restaurantsFound:0,
  searchUpdate:false,
  restaurantName:"",
  priceRating:0,
  starRating:0,
  actions: {
    previousPage(){
      let page=this.get("page");
      if(page > 1){
        this.set("page",page-1);
        this.send('search');
      }
    },

    nextPage(){
      let page=this.get("page");
      if(page<this.get("numberOfPages")){
        this.set("page",page+1);
        this.send('search');
      }
    },

    setPage(page){
      this.set("page",page);
      this.send('search');
    },

    updateNumberOfPages(numberOfRestaurants){
      this.set("numberOfPages",Math.ceil(numberOfRestaurants/6));
      this.set("restaurantsLoaded",true);
    },

    updateRestaurants(value){
      this.set("restaurants",value);
    },

    logout(){
      this.get("ajaxService").logout().then(value => {
        this.set("loggedIn",false)
        this.transitionToRoute("/restaurants");
      },value=>{
        console.log("There was an error in the logout.");
      });
    },

    changeCousineToSearch(cousine){
      let cousineController = this.get("activeCousine");
      let index = cousineController.indexOf(cousine);
      if (index > -1) {
        cousineController.splice(index, 1);
      } else {
        this.set(cousineController.push(cousine));
      }
    },

    updateSearchInformation(restaurantName,priceRating,starRating){
      this.set("priceRating",priceRating);
      this.set("restaurantName",restaurantName);
      this.set("starRating",starRating);
      this.send("search");
    },

    search(){
      let searchInformation={
        page:this.get("page"),
        restaurantCriterium:{
          "restaurantName":this.get("restaurantName"),
          "priceRange":this.get("priceRating"),
          "mark":this.get("starRating"),
        },
        cousines: this.get("activeCousine").toString()
      }
      this.get("ajaxService").search(searchInformation).then(value => {
        this.set("restaurants",value[1]);
        this.set("searchUpdate",true);
        this.set("restaurantsFound",value[0].numberOfRestaurants);
        this.send("updateNumberOfPages",value[0].numberOfRestaurants);
      },value=>{
        this.set("searchUpdate",true);
        this.set("restaurantsFound",0);
        this.set("restaurants",[]);
      });
    }
  }
});
