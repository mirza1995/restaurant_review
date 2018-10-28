import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  numberOfTables: 0,
  tablesSize: ["one","two","three","four","five","six","seven","eight","nine","ten"],
  tables: [],
  tablesAdded:false,
  actions: {
    addTable(){
      this.set("numberOfTables",this.get("numberOfTables")+1);
      let id = this.get("id");
      if(id == 0) {
        id = null;
      }
      let table = {
        id: null,
        restaurant: null,
        persons: null,
        ammount: null,
        shape: "square",
      }
      let tables = this.get("tables");
      tables.push(table);
    },

    deleteTable(index){
      let tables = this.get("tables");
      let id = this.get("id");
      if(id != 0){
        let tableId = tables[index].id;
        this.get("ajaxService").deleteTable(tableId).then(value => {
          tables.splice(index,1)
          this.set("numberOfTables",this.get("numberOfTables")-1);
        },value=>{
          console.log("There was a problem with deleting table");
        });
      } else {
        tables.splice(index,1)
        this.set("numberOfTables",this.get("numberOfTables")-1);
      }
    },

    onTableSelect(value,index){
      let tables = this.get("tables");
      tables[index].persons = value;
    },

    onShapeSelect(value,index){
      let tables = this.get("tables");
      tables[index].shape = value;
    },

    amountChanged(value, index) {
      let tables = this.get("tables");
      tables[index].ammount = value;
      tables[index].tablesUnreserved = value;
    },

    saveTables(){
      this.send("validateTablesData");
      if(this.get("tablesError") == false){
        let tables = this.get("tables");
        for(let i = 0; i < tables.length; i++){
          tables[i].restaurant = this.get("restaurant");
        }
        let information = {
          objects: tables
        }
        this.get("ajaxService").addTables(information).then(value => {
          this.set("tablesAdded",true);
        },value=>{
          console.log(value)
        });
      }
    },

    validateTablesData(){
      let tables = this.get("tables");
      let errorFound = false;
      if(tables.length == 0){
        errorFound = true;
        this.set("tableErrorMessage","Tables are missing.");
      } else {
        for(var i = 0; i < tables.length; i++){
          let persons = tables[i].persons;
          let ammount = tables[i].ammount;
          if(persons == null || ammount == null){
            this.set("tableErrorMessage","Table field(s) are empty");
            errorFound = true;
            break;
          } else if(/^\d+$/.test(ammount) == false) {
            this.set("tableErrorMessage","Ammount field is invalid");
            errorFound = true;
            break;
          } else {
            this.set("tableErrorMessage","");
          }
        }
      }
      this.set("tablesError", this.get("tableErrorMessage") != "");
    },
  }
});
