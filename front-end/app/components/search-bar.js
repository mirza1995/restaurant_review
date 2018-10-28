import Component from '@ember/component';

export default Component.extend({
  classNames: ["search-bar__buttons"],
  datePicked: "",
  selectedPeople:"2",
  peopleDropdown:false,
  hoursDropdown:false,
  selectedHours:"0:00 AM",
  time:0,
  init(){
    this.send("setTodaysDateTime");
    this._super(...arguments);
  },

  actions: {
    setTodaysDateTime(){
      let date=new Date();
      let dateArray=date.toString().split(" ");
      let timeArray = dateArray[4].split(":");
      let time = parseInt(timeArray[0]) + 1;
      if(time > 12){
        this.set("selectedHours",(time-12)+":00 PM");
      } else {
        this.set("selectedHours",time+":00 AM");
      }
      this.set("time",time);
      let todaysDate = dateArray[1]+" "+dateArray[2]+","+dateArray[3];
      this.set("datePicked",todaysDate);
      this.set("todaysDate",todaysDate);
    },

    toggleDatePicker() {
      this.toggleProperty('showDatePicker');
      this.set('peopleDropdown',false);
      this.set("hoursDropdown",false);
    },

    changePickedDate(date){
      let months=['Jan','Feb','Mart','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
      this.toggleProperty('showDatePicker');
      //Validation for a correct date
      let dateTime=Date.parse(date.toLocaleDateString());
      let todaysDate=Date.parse(this.get("todaysDate"));
      if(todaysDate <= dateTime){
        let dateArray=date.toLocaleDateString().split("/");
        let day;
        if(parseInt(dateArray[1])<10){
          day = "0"+dateArray[1];
        } else {
          day = dateArray[1];
        }
        let datePicked=months[dateArray[0]-1]+" "+day+","+dateArray[2];
        this.set("datePicked",datePicked);
        if(this.get("todaysDate") == datePicked){
          this.send("setTodaysDateTime");
        } else {
          this.set("time",0);
          this.set("selectedHours","0:00 AM");
        }
      }
    },

    onPeopleSelect(peopleSelected){
      this.set("selectedPeople",peopleSelected);
      this.set('peopleDropdown',false);
    },

    showPeopleDropdown(){
      this.toggleProperty('peopleDropdown');
      this.set('hoursDropdown',false);
      this.set("showDatePicker",false);
    },

    showHoursDropdown(){
      this.toggleProperty('hoursDropdown');
      this.set('peopleDropdown',false);
      this.set("showDatePicker",false);
    },

    onHoursSelect(hours){
      this.set("selectedHours",hours);
      this.set('hoursDropdown',false);
    },
    
    findTableSearchBar(){
      this.sendAction('findTable',this.get("selectedHours"),this.get("datePicked"),this.get("selectedPeople"));
    }
  },
});
