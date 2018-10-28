import Component from '@ember/component';

export default Component.extend({
  classNames: ["food-menu__list"],
  menu:null,
  mealTypes:null,
  smallPriceExists:null,
  menuInitiliazed:false,
  
  init(){
    this.send("updateMenu");
    this.set("menuInitiliazed",true);
    this._super(...arguments);
  },

  didReceiveAttrs(){
    if(this.get("menuInitiliazed")){
      this.send("updateMenu")
    }
  },

  actions: {
    updateMenu(){
      let menu=this.get("menu");
      let mealTypes=new Array();
      let smallPriceExists=new Array();
      for(var i=0; i<menu.length;i++){
        if(mealTypes.indexOf(menu[i].mealType)==-1){
          mealTypes.push(menu[i].mealType);
          //Check to know if there are small prices for this meal type
          smallPriceExists[menu[i].mealType]=parseFloat(menu[i].smallPrice)>0;
        } else if(parseFloat(menu[i].smallPrice)>0){
          smallPriceExists[menu[i].mealType]=true;
        }
        menu[i].largePrice=parseFloat(menu[i].largePrice).toFixed(2);
        menu[i].smallPrice=parseFloat(menu[i].smallPrice).toFixed(2);
      }
      this.set("smallPriceExists",smallPriceExists);
      this.set("mealTypes",mealTypes);
    }
  }
});
