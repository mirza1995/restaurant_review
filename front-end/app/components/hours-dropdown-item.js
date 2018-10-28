import Component from '@ember/component';

export default Component.extend({
  tagName: "a",
  classNames: ["dropdown-item"],
  attributeBindings: ['href'],
  href: '#',
  'on-select':null,
  hoursAmount:null,
  click(){
    let hoursAmount = this.get("hoursAmount");
    let hoursPM="AM";
    let half=":00";
    if(hoursAmount>12){
      hoursAmount=hoursAmount-12;
      hoursPM="PM";
    }
    if(this.get("half")==2){
      half=":30";
    }
    this.sendAction('on-select',hoursAmount+half+" "+hoursPM);
    return false;
  }
});
