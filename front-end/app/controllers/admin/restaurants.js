import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  cousines:null,
  numberOfPages: 1,
  page:1,
  restaurantsLoaded:false,
  actions:{
    previousPage(){
      let page=this.get("page");
      if(page > 1){
        this.set("page",page-1);
        this.send('getPage');
      }
    },

    nextPage(){
      let page=this.get("page");
      if(page<this.get("numberOfPages")){
        this.set("page",page+1);
        this.send('getPage');
      }
    },

    setPage(page){
      this.set("page",page);
      this.send('getPage');
    },

    getPage(){
      let searchValue = this.get("searchValue");
      if(searchValue == "" || searchValue == null){
        searchValue = "Empty";
      }
      let searchInformation = {
        page: this.get("page"),
        search: searchValue
      }
      this.get("ajaxService").getRestaurantsPageAdmin(searchInformation).then(value => {
        this.set("restaurants",value[1]);
        this.set("searchUpdate",true);
        this.send("updateNumberOfPages",value[0].numberOfRestaurants);
      },value=>{
        this.set("searchUpdate",true);
        this.set("restaurants",[]);
      });
    },

    updateNumberOfPages(numberOfRestaurants){
      this.set("numberOfPages",Math.ceil(numberOfRestaurants/12));
      this.set("restaurantsLoaded",true);
    },

    changeRestaurant(id){
      this.transitionToRoute('/admin/createRestaurant/'+id)
    },

    deleteRestaurant(id){
      this.get("ajaxService").deleteRestaurant(id).then(value=>{
        this.set("restaurants",value[1]);
        this.send("updateNumberOfPages",value[0].numberOfRestaurants);
        this.set("page",1);
      });
    },

    searchRestaurant(){
      let searchValue = this.get("searchValue");
      if(searchValue == "" || searchValue == null){
        this.get("ajaxService").getRestaurantsForAdmin().then(value=>{
          this.set("restaurants",value[1]);
          this.send("updateNumberOfPages",value[0].numberOfRestaurants);
        })
      } else if(searchValue.length > 3) {
        let searchCriteria = {
          search: searchValue
        }
        this.get("ajaxService").searchRestaurantAdmin(searchCriteria).then(value=>{
          this.set("restaurants",value[1]);
          this.send("updateNumberOfPages",value[0].numberOfRestaurants);
        }, value=>{
          console.log("There was an error in the restaurants search");
        });
      }
    }
  }
});
