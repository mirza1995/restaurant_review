import Controller from '@ember/controller';

export default Controller.extend({
  location: {
    lat: 43.84864,
    lng: 18.35644
  },
  markerTooltipOpen: 0,
  userLocationFound:false,
  mapTooltipOpen:true,
  actions: {
    changeTip(id){
      this.set("markerTooltipOpen",id);
    }
  }
});
