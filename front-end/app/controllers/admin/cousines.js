import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  cousines:null,
  numberOfPages: 1,
  page:1,
  cousinesLoaded:false,
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
      this.get("ajaxService").getCousinesPage(searchInformation).then(value => {
        this.set("cousines",value[1]);
        this.set("searchUpdate",true);
        this.send("updateNumberOfPages",value[0].numberOfCousines);
      },value=>{
        this.set("searchUpdate",true);
        this.set("cousines",[]);
      });
    },

    updateNumberOfPages(numberOfCousines){
      this.set("numberOfPages",Math.ceil(numberOfCousines/12));
      this.set("cousinesLoaded",true);
      this.set("page",1);
    },

    changeCousine(id){
      this.transitionToRoute('/admin/createCousine/'+id)
    },

    deleteCousine(id){
      this.get("ajaxService").deleteCousine(id).then(value=>{
        this.set("cousines",value[1]);
        this.send("updateNumberOfPages",value[0].numberOfCousines);
      });
    },

    searchCousine(){
      let searchValue = this.get("searchValue"); 
      if(searchValue == "" || searchValue == null){
        this.get("ajaxService").getAllCousines().then(value=>{
          this.set("cousines",value[1]);
          this.send("updateNumberOfPages",value[0].numberOfCousines);
        })
      } else if(searchValue.length > 3){
        let searchCriteria = {
          search: searchValue
        }
        this.get("ajaxService").searchCousine(searchCriteria).then(value=>{
          this.set("cousines",value[1]);
          this.send("updateNumberOfPages",value[0].numberOfCousines);
        }, value=>{
          console.log("There was an error in the cousine search");
        });
      }
    }
  }
});
