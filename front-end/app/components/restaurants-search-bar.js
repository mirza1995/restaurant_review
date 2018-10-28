import Component from '@ember/component';

export default Component.extend({
  classNames: ["restaurants__container__search","mt-5","mb-5"],
  showFilter:false,
  showSort:false,
  starRating:0,
  priceRating:0,
  restaurantName:"",
  actions: {
    toggleFilter(){
      this.toggleProperty('showFilter');
    },

    starRatingClicked(starRating){
      this.set("starRating",starRating);
    },

    updatePriceRating(value){
      if(this.get("priceRating") == value){
        this.set("priceRating",0);
      } else {
        this.set("priceRating",value);
      }
      
    },

    changeCousineToSearchComponent(cousine){
      this.sendAction("changeCousineToSearch",cousine);
    },
    
    searchClicked(){
      this.sendAction("updateSearchInformation",this.get("restaurantName"),this.get("priceRating"),this.get("starRating"));
    }
  }
});
