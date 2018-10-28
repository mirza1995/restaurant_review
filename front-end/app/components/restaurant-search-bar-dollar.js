import Component from '@ember/component';

export default Component.extend({
  tagName: "span",
  classNames: ["restaurants-search-bar__dollar"],
  classNameBindings:["isHovered:restaurants-search-bar__dollar--picked"],
  isHovered: false,
  click(){
    this.sendAction("updatePriceRating",this.get("value"));
  }
});
