import Controller from '@ember/controller';

export default Controller.extend({
  ajaxService: Ember.inject.service('ajax-service'),
  menuCreated:false,
  menuType: 1,
  menuName: "Breakfast",
  numberOfMenuItems: 0,
  breakfast: [],
  lunch: [],
  dinner: [],
  menu: [],
  actions: {
    changeMenu(menuType){
      this.set("menuType",menuType);
      if(menuType == 1){
        this.set("menuName","Breakfast");
      } else if(menuType == 2){
        this.set("menuName","Lunch");
      } else if(menuType == 3){
        this.set("menuName","Dinner");
      }
      let menuName = this.get("menuName").toLowerCase();
      this.set("numberOfMenuItems",this.get(menuName).length);
      this.set("menu",this.get(menuName));
    },

    addItem(){
      this.set("numberOfMenuItems",this.get("numberOfMenuItems")+1);
      let menuName = this.get("menuName");
      let mealItem = {
        id: null,
        restaurant: null,
        name: null,
        type: menuName,
        description: "",
        largePrice: null,
        smallPrice: null,
        mealType: null
      }
      let menuArray = this.get(menuName.toLowerCase());
      menuArray.push(mealItem);
    },

    menuNameChanged(value, index){
      let menuName = this.get("menuName").toLowerCase();
      let menuArray = this.get(menuName);
      menuArray[index].name = value;
    },

    menuLargePriceChanged(value, index){
      let menuName = this.get("menuName").toLowerCase();
      let menuArray = this.get(menuName);
      menuArray[index].largePrice = value;
    },

    menuSmallPriceChanged(value, index){
      let menuName = this.get("menuName").toLowerCase();
      let menuArray = this.get(menuName);
      menuArray[index].smallPrice = value;
    },

    menuDescriptionChanged(value,index){
      let menuName = this.get("menuName").toLowerCase();
      let menuArray = this.get(menuName);
      menuArray[index].description = value;
    },

    menuTypeChanged(value, index){
      let menuName = this.get("menuName").toLowerCase();
      let menuArray = this.get(menuName);
      menuArray[index].mealType = value;
    },

    deleteMenuItem(index){
      let menuName = this.get("menuName").toLowerCase();
      let menuArray = this.get(menuName);
      let id = this.get("id");
      if(id != 0){
        let menuId = menuArray[index].id;
        this.get("ajaxService").deleteMeal(menuId).then(value => {
        },()=>{
          console.log("There was a problem with deleting table");
        });
      }
      menuArray.splice(index,1)
      this.set(menuName,menuArray);
      this.set("menu",menuArray);
      this.set("numberOfMenuItems",this.get("numberOfMenuItems")-1);
    },

    saveMenu(){
      let menu = this.get("breakfast").concat(this.get("lunch")).concat(this.get("dinner"));
      for(let i = 0; i < menu.length; i++){
        menu[i].restaurant = this.get("restaurant");
      }
      let information = {
        objects: menu
      }
      this.get("ajaxService").addMenus(information).then(value => {
        this.set("menuCreated",true);
      },value=>{
        console.log(value)
      });
    },

    validateMenuData(){
      let breakfast = this.get("breakfast");
      let lunch = this.get("lunch");
      let dinner = this.get("dinner");
      let menus = [breakfast, lunch, dinner];
      let menuNames = ["Breakfast", "Lunch", "Dinner"];
      let errorMessages = [];
      let errorFound = false;

      for(var i = 0; i < 3; i++){
        let menu = menus[i];
        this.set("menuElementsError","");
        this.send("checkMenuInputs",menu);
        if(menu.length == 0) {
          errorMessages.push(menuNames[i] + " menu is empty.");
          errorFound = true;
        } else if(this.get("menuElementsError") != ""){
          errorMessages.push(this.get("menuElementsError"));
          errorFound = true;
        }
      }

      if(errorFound){
        this.set("menuErrorMessage",errorMessages);
      } 
      this.set("menuError",errorFound);
    }, 

    checkMenuInputs(menu){
      for(var i = 0; i < menu.length; i++){
        let menuName = menu[i].type;
        if(menu[i].name == null || menu[i].description == null || menu[i].largePrice == null || menu[i].smallPrice == null || menu[i].mealType == null){
          this.set("menuElementsError",menuName + " menu has empty fields.");
        } else if ((/^\d+\.\d+$/.test(menu[i].largePrice) == false && /^\d+$/.test(menu[i].largePrice) == false) || (/^\d+\.\d+$/.test(menu[i].smallPrice) == false && /^\d+$/.test(menu[i].smallPrice) == false)) {
          this.set("menuElementsError",menuName + " menu price(s) are invalid.");
        } else {
          this.set("menuElementsError","");
        }
      }
    },

    cancel(){
      this.transitionToRoute("/admin/restaurants");
    },

    addRestaurant(){
      this.send("validateMenuData");
      if(!this.get("menuError") && !this.get("menuElementsError")){
        this.send("saveMenu");
      }
    },
  }
});
