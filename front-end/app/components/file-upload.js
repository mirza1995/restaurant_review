import Component from '@ember/component';

export default Component.extend({
  classNames:["uploader","dropzone","file-upload__box"],
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
    event.preventDefault();
    this.set("hovered",false);
    var file = event.dataTransfer.files[0];
    const reader = new FileReader();
    let imageData;
    reader.onload = (event) => {
      imageData = reader.result;
      this.sendAction('fileInputChanged', imageData);
    };
    if (file) {
      reader.readAsDataURL(file);
    }
  }
});