import Component from '@ember/component';

export default Component.extend({
  classNames: ["landing__search"],
  actions: {
    findTable(hours,date,people){
      let searchName = this.get("searchName") || "";
      this.sendAction('searchTable', searchName, hours, date, people);
    }
  }
});
