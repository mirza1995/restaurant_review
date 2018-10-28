import Controller from '@ember/controller';
import {countriesWithCities,countries} from 'front-end/utils/countriesWithCities';
import {validatePhoneNumber,validateEmail} from 'front-end/utils/constants';

export default Controller.extend({
  signupAjax: Ember.inject.service('ajax-service'),
  showBackEndValidationError:false,
  showFirstNameError: false,
  showLastNameError: false,
  showPhoneNumberError:false,
  showEmailError: false,
  showInvalidEmailError: false,
  showPasswordError: false,
  showPasswordDontMatchError: false,
  showPasswordTooShort:false,
  showInvalidPhoneNumberError:false,
  showEmailInUseError:false,
  countries:countries,
  countriesWithCities:countriesWithCities,
  countrySelected:"Afghanistan",
  cities: countriesWithCities["Afghanistan"],
  citySelected: "Herat",
  adminAccount: false,
  actions: {
    onCountrySelect(value){
      this.set("cities",countriesWithCities[value])
      this.set("countrySelected",value);
    },
    
    onCitySelect(value){
      this.set("citySelected",value);
    },

    cancel(){
      this.transitionToRoute("/admin/users");
    },

    changeUser(userId){
      this.transitionToRoute('/admin/createUser/'+userId)
    },

    searchUser(){
      alert(this.get("searchValue"))
    },

    addUser(){
      let firstName=this.get("firstName");
      let lastName=this.get("lastName");
      let phoneNumber=this.get("phoneNumber");
      let email=this.get("email");
      let password=this.get("password");
      let confirmPassword=this.get("confirmPassword");

      this.set("showFirstNameError",firstName==null || firstName=="");
      this.set("showLastNameError",lastName==null || lastName=="");
      this.set("showPhoneNumberError",phoneNumber==null || phoneNumber=="");
      if(!this.get("showPhoneNumberError")){
        this.set("showInvalidPhoneNumberError",!validatePhoneNumber.test(String(phoneNumber).toLowerCase()));
      }
      this.set("showEmailError",email==null || email=="");
      if(!this.get("showEmailError")){
        this.set("showInvalidEmailError",!validateEmail.test(String(email).toLowerCase()));
      }
      this.set("showPasswordError", password == null || password == "");
      this.set("showPasswordTooShort", (password != null && password != "") && password.length < 4);
      this.set("showPasswordDontMatchError", confirmPassword!=password);
      if(!this.get("showFirstNameError") && !this.get("showLastError") && !this.get("showPhoneNumberError") && !this.get("showInvalidPhoneNumberError") && !this.get("showEmailError") && !this.get("showInvalidEmailError") && !this.get("showPasswordError") && !this.get("showPasswordDontMatchError")){
        //Save session
        let firstName=this.get("firstName");
        let lastName=this.get("lastName");
        let phoneNumber=this.get("phoneNumber");
        let email=this.get("email");
        let password=this.get("password");
        let confirmPassword=this.get("confirmPassword");
        let account = "user";
        if(this.get("adminAccount")){
          account = "admin";
        }
        if(this.get("userId")=="0"){
          let userInformation={
            "firstName":firstName,
            "lastName":lastName,
            "email":email,
            "phoneNumber":phoneNumber,
            "country":this.get("countrySelected"),
            "city":this.get("citySelected"),
            "password":password,
            "account": account
          };
          this.get('signupAjax').addUser(userInformation).then(value=>{
            this.transitionToRoute("/admin/users")
          },value=>{
            if(value.statusText=="Not Acceptable"){
              this.set("showEmailInUseError",true);
              this.set("showBackEndValidationError",false);
            } else {
              this.set("showEmailInUseError",false);
              this.set("showBackEndValidationError",true);
            }
          });
        } else {
          let userInformation={
            "id": this.get("user").id,
            "firstName":firstName,
            "lastName":lastName,
            "email":email,
            "phoneNumber":phoneNumber,
            "country":this.get("countrySelected"),
            "city":this.get("citySelected"),
            "password":password,
            "account":account
          };
          this.get('signupAjax').updateUserWithoutLogin(userInformation).then(value=>{
            this.transitionToRoute("/admin/users");
          },value=>{
            if(value.statusText=="Not Acceptable"){
              this.set("showEmailInUseError",true);
              this.set("showBackEndValidationError",false);
            } else {
              this.set("showEmailInUseError",false);
              this.set("showBackEndValidationError",true);
            }
          });
        }
        
      } else {
        this.set("showEmailInUseError",false);
        this.set("showBackEndValidationError",false);
      }
    }
  }
});
