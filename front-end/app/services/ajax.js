import Service from '@ember/service';
import Ember from 'ember';

export default Service.extend({
  ajax(type, url, params) {
    return Ember.$.ajax({
      url: url,
      type: type,
      cache: false,
      contentType: 'application/json',
      data: params ? JSON.stringify(params) : null,
      success: function(data){
        console.log(data)
      }
    });
  }
});
