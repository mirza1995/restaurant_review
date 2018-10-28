import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  users:null,
  numberOfPages: 1,
  page:1,
  usersLoaded:false,
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
      this.get("ajaxService").getUsersPage(searchInformation).then(value => {
        this.set("users",value[1]);
        this.set("searchUpdate",true);
        this.send("updateNumberOfPages",value[0].numberOfUsers);
      },value=>{
        this.set("searchUpdate",true);
        this.set("users",[]);
      });
    },

    updateNumberOfPages(numberOfUsers){
      this.set("numberOfPages",Math.ceil(numberOfUsers/6));
      this.set("usersLoaded",true);
    },

    changeUser(userId){
      this.transitionToRoute('/admin/createUser/'+userId)
    },

    deleteUser(userId){
      this.get("ajaxService").deleteUser(userId).then(value=>{
        this.set("users",value[1]);
        this.send("updateNumberOfPages",value[0].numberOfUsers);
      });
    },

    searchUser(){
      let searchValue = this.get("searchValue"); 
      if(searchValue == "" || searchValue == null){
        this.get("ajaxService").getAllUsers().then(value=>{
          this.set("users",value[1]);
          this.send("updateNumberOfPages",value[0].numberOfUsers);
        })
      } else if(searchValue.length > 3) {
        let searchCriteria = {
          search: searchValue
        }
        this.get("ajaxService").searchUser(searchCriteria).then(value=>{
          this.set("users",value[1]);
          this.send("updateNumberOfPages",value[0].numberOfUsers);
        }, value=>{
          console.log("There was an error in the user search");
        });
      }
    }
  }
});
