import Component from '@ember/component';

export default Component.extend({
  classNames: ["restaurants-search-bar__popup-text__cousine"],
  classNameBindings: ['isPicked:restaurants-search-bar__popup-text__cousine--picked'],
  isPicked: false,
  click(){
    this.toggleProperty("isPicked")
    this.sendAction("changeCousineToSearch",this.get("cousineName"));
  },
  
  didReceiveAttrs(){
    if(this.get("activeCousine").indexOf(this.get("cousineName"))>=0){
      this.set("isPicked",true);
    }
  }
});
