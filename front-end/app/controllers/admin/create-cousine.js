import Controller from '@ember/controller';

export default Controller.extend({
  createCousineAjax: Ember.inject.service('ajax-service'),
  showBackEndValidationError:false,
  showCousineError: false,
  actions: {
    cousinePhotoUpload(url){
      this.set("cousinePhoto",url);
    },

    cancel(){
      this.transitionToRoute("/admin/cousines");
    },

    searchCousine(){
      alert(this.get("searchValue"))
    },

    changeCousine(id){
      this.transitionToRoute("/admin/createCousine/"+id);
    },

    addCousine(){
      let cousineName=this.get("cousineName");
      this.set("showCousineError",cousineName==null || cousineName=="");
      if(!this.get("showCousineError")){
        //Upload Logo photo
        let base64 = this.get("cousinePhoto").split(";base64,");
        let imageType = base64[0].split("/")[1];
        let uploadInformation = {
          imageType: imageType,
          photo: base64[1]
        }
        if(base64.length > 1){
          this.get("createCousineAjax").uploadImage(uploadInformation).then(value => {
            this.set("cousinePhoto",value);
            this.send("createCousine");
          },()=>{
            console.log("There was a problem with Uploading of Cousine Image");
          });
        } else {
          this.send("createCousine");
        }
      } else {
        this.set("showBackEndValidationError",false);
      }
    },

    createCousine(){
      if(this.get("id")=="0"){
        let cousineInformation={
          name:this.get("cousineName"),
          photo: this.get("cousinePhoto")
        };
        this.get('createCousineAjax').createCousine(cousineInformation).then(value=>{
          this.transitionToRoute("/admin/cousines")
        },value=>{
          this.set("showBackEndValidationError",true);
        });
      } else {
        let cousineInformation={
          id: this.get("cousine").id,
          name:this.get("cousineName"),
          photo: this.get("cousinePhoto")
        };
        this.get('createCousineAjax').updateCousine(cousineInformation).then(value=>{
          this.transitionToRoute("/admin/cousines");
        },value=>{
          this.set("showBackEndValidationError",true);
        });
      }
    }
  }
});
