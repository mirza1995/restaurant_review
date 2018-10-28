import Component from '@ember/component';

export default Component.extend({
  tagName: "a",
  classNames: ["dropdown-item"],
  attributeBindings: ['href'],
  href: '#',
  'on-select':null,
  peopleAmount:null,
  click(){
    this.sendAction('on-select',this.get("peopleAmount"));
    return false;
  }
});
