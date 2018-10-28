import Controller from '@ember/controller';
import {validateEmail} from 'front-end/utils/constants';
export default Controller.extend({
  showEmailError: false,
  showPasswordError: false,
  showLoginError:false,
  userService: Ember.inject.service('ajax-service'),
  actions: {
    submitLogin(){
      let email = this.get("email");
      let password = this.get("password");
      this.set("showEmailError",email == null || email == "");
      if(!this.get("showEmailError")){
        this.set("showInvalidEmailError",!validateEmail.test(String(email).toLowerCase()));
      }
      this.set("showPasswordError", password == null || password == "");
      if(!this.get("showEmailError") && !this.get("showPasswordError") && !this.get("showInvalidEmailError")){
        let userInformation = {
          "email":email,
          "password":password
        };

        this.get("userService").login(userInformation).then(value => {
          this.transitionToRoute("/")
        },value=>{
          console.log(value);
          this.set("showLoginError",true);
        });
      }
     },
   }
});
