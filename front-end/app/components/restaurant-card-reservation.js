import Component from '@ember/component';

export default Component.extend({
  classNames: ['popular__card', 'card', 'col-md-6', 'col-lg-4', 'mt-3'],
  cousines:null,
  didReceiveAttrs(){
    let cousinesArray=this.get("restaurant").cousines;
    let cousines = "";
    for(var i = 0; i < cousinesArray.length; i++){
      cousines += cousinesArray[i].name;
      if(i < cousinesArray.length-1){
        cousines += " | ";
      }
    }
    this.set("cousines",cousines);
  }
});