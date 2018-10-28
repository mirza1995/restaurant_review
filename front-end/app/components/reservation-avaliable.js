import Component from '@ember/component';

export default Component.extend({
  classNames: ["mt-3"],
  actions: {
    reservation(reservationTime){
      this.sendAction("reservation",reservationTime,this.get("restaurantId"),this.get("restaurantName"),this.get("imageFileName"));
    }
  }
});
