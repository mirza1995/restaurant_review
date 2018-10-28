import Component from '@ember/component';

export default Component.extend({
  classNames:["uploader","dropzone","multiple-upload__box"],
  ajaxService: Ember.inject.service('ajax-service'),
  hovered: false,
  dragEnter: function(event) {
    this.set("hovered",true);
    event.preventDefault();
  },

  dragOver: function(event) {
    this.set("hovered",true);
    event.preventDefault();
  },

  dragLeave: function(event) {
    this.set("hovered",false);
    event.preventDefault();
  },

  drop: function(event) {
    this.send("upload",event,true);
  },

  actions: {
    upload(event,dropEvent=false){
      event.preventDefault();
      this.set("hovered",false);
      var data;
      if(dropEvent){
        data = event.dataTransfer;
      } else {
        data = event.target;
      }
      let imageData = [];
      for(var i = 0; i < data.files.length; i++){
        const reader = new FileReader();
        let file = data.files[i];
        if (file) {
          reader.readAsDataURL(file);
        }
        reader.onload = (event) => {
          imageData.push(reader.result);
          this.sendAction("fileInputChanged",imageData);
        };
      }
    },

    uploadPhotos(){
      $(".multiple-upload__input").trigger("click");
    }
  }
});