import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  locations:null,
  numberOfPages: 1,
  page:1,
  locationsLoaded:false,
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
      this.get("ajaxService").getLocationsPage(searchInformation).then(value => {
        this.set("locations",value[1]);
        this.set("searchUpdate",true);
        this.send("updateNumberOfPages",value[0].numberOfLocations);
      },value=>{
        this.set("searchUpdate",true);
        this.set("locations",[]);
      });
    },

    updateNumberOfPages(numberOfLocations){
      this.set("numberOfPages",Math.ceil(numberOfLocations/12));
      this.set("locationsLoaded",true);
    },

    changeLocation(id){
      this.transitionToRoute('/admin/createLocation/'+id)
    },

    deleteLocation(id){
      this.get("ajaxService").deleteLocation(id).then(value=>{
        this.set("locations",value[1]);
        this.send("updateNumberOfPages",value[0].numberOfLocations);
      });
    },

    searchLocation(){
      let searchValue = this.get("searchValue"); 
      if(searchValue == "" || searchValue == null){
        this.get("ajaxService").getAllLocations().then(value=>{
          this.set("locations",value[1]);
          this.send("updateNumberOfPages",value[0].numberOfLocations);
        })
      } else if(searchValue.length > 3) {
        let searchCriteria = {
          search: searchValue
        }
        this.get("ajaxService").searchLocation(searchCriteria).then(value=>{
          this.set("locations",value[1]);
          this.send("updateNumberOfPages",value[0].numberOfLocations);
        }, value=>{
          console.log("There was an error in the locations search");
        });
      }
    }
  }
});
