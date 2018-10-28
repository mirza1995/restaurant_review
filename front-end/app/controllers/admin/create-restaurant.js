import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  restaurantPage: 1,
  numberOfTables: 0,
  tablesSize: ["one","two","three","four","five","six","seven","eight","nine","ten"],
  tables: [],
  menuClickedError:false,
  actions: {
    changeRestaurantPage(page){
      if(page == 1){
        this.transitionToRoute("/admin/createRestaurant/"+this.get("id")+"/restaurant/"+this.get("id"));
      } else if(page == 2 && this.get("id") != 0){
        this.transitionToRoute("/admin/createRestaurant/"+this.get("id")+"/menu/"+this.get("id"));
      } else if(page == 3 && this.get("id") != 0){
        this.transitionToRoute("/admin/createRestaurant/"+this.get("id")+"/gallery/"+this.get("id"));
      } else if(page == 4 && this.get("id") != 0){
        this.transitionToRoute("/admin/createRestaurant/"+this.get("id")+"/tables/"+this.get("id"));
      }
      if(this.get("id") != 0) {
        this.set("restaurantPage",page);
        this.set("menuClickedError",false);
      } else {
        this.set("menuClickedError",true);
      }
    },

    changeRestaurant(id){
      this.transitionToRoute("/admin/createRestaurant/"+id+"/restaurant/"+id);
    },
  }
});
