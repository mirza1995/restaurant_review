import Component from '@ember/component';

export default Component.extend({
  classNames: ["alert","alert-danger","mt-3"],
  attributeBindings: ['role'],
  role: 'alert',
});
