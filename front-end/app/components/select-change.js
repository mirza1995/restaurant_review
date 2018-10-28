import Component from '@ember/component';

export default Component.extend({
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
    onSelect(value){
      this.sendAction("changed",value,this.get("index"))
    },

    updateForm(){
      let field = this.get("field");
      if(field == "persons"){
        let persons = this.get("tables").persons;
        if(persons != null){
          this.set("selectedValue",this.get("tablesSize")[parseInt(persons)-1]);
        }
      } else if(field == "shape"){
        this.set("selectedValue",this.get("tables").shape);
      }
    }
  }
});
