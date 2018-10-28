import Component from '@ember/component';

export default Component.extend({
  classNames: ["transparent-menu"],
  menuShowed: true,
  actions: {
    showMenu(){
      this.toggleProperty("menuShowed")
    }
  }
});
