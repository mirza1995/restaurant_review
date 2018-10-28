import Component from '@ember/component';

export default Component.extend({
  classNames:["form-inline"],
  user:null,
  loggedIn:true,
  admin: false,
  didReceiveAttrs(){
    if(this.get("user") != null && this.get("user").account == "admin"){
      this.set("admin",true);
    }
  },
  actions: {
    logout(){
      this.sendAction("logOut");
    }
  }
});
