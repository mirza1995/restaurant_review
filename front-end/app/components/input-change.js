import Component from '@ember/component';

export default Component.extend({
  currentValue: "",
  didReceiveAttrs(){
    this.send("updateForm");
    this._super(...arguments);
  },
  didRender(){
    this.send("updateForm");
    this._super(...arguments);
  },
  didUpdateAttrs(){
    this.send("updateForm");
    this._super(...arguments);
  },
  willClearRender(){
    this.send("updateForm");
    this._super(...arguments);
  },
  actions: {
    updateForm(){
      let field = this.get("field");
      let value = "";
      let menu = this.get("menu");
      if(menu != null) {
        if(field == "name"){
          value = menu.name;
        } else if(field == "largePrice"){
          value = menu.largePrice;
        } else if(field == "smallPrice"){
          value = menu.smallPrice;
        } else if(field == "description"){
          value = menu.description;
        } else if(field == "type"){
          value = menu.mealType;
        } else if(field == "ammount"){
          value = menu.ammount;
        }
        if(value == null){
          value = "";
        }
      }
      this.set("currentValue",value); 
    },

    inputChanged(value){
      this.sendAction("changed",value,this.get("index"));
    }
  }
});
