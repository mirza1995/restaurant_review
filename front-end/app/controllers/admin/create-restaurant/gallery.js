import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  addPhotosPage: false,
  uploadedGallery: null,
  galleryError:false,
  noPhotosError:false,
  actions: {
    saveGallery(){
      let photos = this.get("uploadedGallery");
      let uploadInformation = [];
      if(photos != null){
        for(var i = 0; i < photos.length; i++){
          let base64 = photos[i].split(";base64,");
          let imageType = base64[0].split("/")[1];
          let photoInformation = {
            restaurant: this.get("currentRestaurant"),
            imageType: imageType,
            photo: base64[1]
          }
          uploadInformation.push(photoInformation);
        }
        this.get("ajaxService").uploadGallery(uploadInformation).then(value => {
          this.set("galleryUploaded",true);
          this.set("addPhotosPage",false);
        },()=>{
          console.log("There was a problem with Uploading of Gallery");
        });
      } else {
        this.set("galleryError",true);
      }
    },

    photosUploaded(photos){
      this.set("uploadedGallery",photos);
    },

    addPhotos(){
      this.set("addPhotosPage",true);
    },

    showPhotos(){
      this.set("addPhotosPage",false);
    }
  }
});
