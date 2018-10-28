import Controller from '@ember/controller';
import {validatePhoneNumber,validateEmail} from 'front-end/utils/constants';
import {countriesWithCities,countries} from 'front-end/utils/countriesWithCities';

export default Controller.extend({
  ajax: Ember.inject.service('ajax-service'),
  showBackEndValidationError:false,
  showEmailInUseError:false,
  showFirstNameError: false,
  showLastNameError: false,
  showPhoneNumberError:false,
  showEmailError: false,
  showInvalidEmailError: false,
  showPasswordError: false,
  showPasswordDontMatchError: false,
  showPasswordTooShort:false,
  showInvalidPhoneNumberError:false,
  countries:countries,
  countriesWithCities:countriesWithCities,
  country:"Afghanistan",
  cities: countriesWithCities["Afghanistan"],
  city: "Herat",
  loggedIn:false,
  timeLeft:180,
  currentTime:"3:00",
  currentTimeMetronome: function() {  
    var interval = 1000;
    let timer = Ember.run.later(this, function() {
      this.notifyPropertyChange('currentTimePulse');
      let timeLeft = this.get("timeLeft");
      if(timeLeft == 0){
        this.transitionToRoute("/");
      }
      this.currentTimeMetronome();
      this.set("timeLeft",timeLeft-1);
      let minutes = parseInt(timeLeft/60);
      let seconds = timeLeft-minutes*60;
      if(seconds < 10){
        this.set("currentTime", minutes+":0"+seconds);
      }else {
        this.set("currentTime", minutes+":"+seconds);
      }
    }, interval);
  }.on('init'),
  actions: {
    saveReservation(reservationInformation){
      this.get('ajax').reservation(reservationInformation).then(value=>{
        this.transitionToRoute("/restaurants");
      },value=>{
        console.log("There was an error in the restaurant reservation.");
      });
    },

    completeReservation(){
      let reservationInformation = {
          restaurant:parseInt(this.get("reservationInformation").id),
          persons: this.get("reservationInformation").persons,
          date: this.get("reservationInformation").date,
          hour: this.get("reservationInformation").time
      };
      if(!this.get("loggedIn")){
        let firstName = this.get("firstName");
        let lastName = this.get("lastName");
        let phoneNumber = this.get("phoneNumber");
        let email = this.get("email");
        let password = this.get("password");
        let confirmPassword = this.get("confirmPassword");

        this.set("showFirstNameError",firstName == null || firstName == "");
        this.set("showLastNameError",lastName == null || lastName == "");
        this.set("showPhoneNumberError",phoneNumber == null || phoneNumber == "");
        if(!this.get("showPhoneNumberError")){
          this.set("showInvalidPhoneNumberError",!validatePhoneNumber.test(String(phoneNumber).toLowerCase()));
        }
        this.set("showEmailError", email == null || email == "");
        if(!this.get("showEmailError")){
          this.set("showInvalidEmailError",!validateEmail.test(String(email).toLowerCase()));
        }
        this.set("showPasswordError", password == null || password == "");
        this.set("showPasswordError", password == null || password == "");
        this.set("showPasswordTooShort", (password != null && password != "") && password.length < 4);
        if(!this.get("showFirstNameError") && !this.get("showLastError") && !this.get("showPhoneNumberError") && !this.get("showInvalidPhoneNumberError") && !this.get("showEmailError") && !this.get("showInvalidEmailError") && !this.get("showPasswordError") && !this.get("showPasswordDontMatchError")){
          let firstName=this.get("firstName");
          let lastName=this.get("lastName");
          let phoneNumber=this.get("phoneNumber");
          let email=this.get("email");
          let password=this.get("password");
          let confirmPassword=this.get("confirmPassword");
          let userInformation={
            "firstName":firstName,
            "lastName":lastName,
            "email":email,
            "phoneNumber":phoneNumber,
            "country":this.get("country"),
            "city":this.get("city"),
            "password":password
          };
          this.get('ajax').register(userInformation).then(value=>{
            this.send("saveReservation",reservationInformation);
          },value=>{
            if(value.statusText=="Not Acceptable"){
              this.set("showEmailInUseError", true);
              this.set("showBackEndValidationError", false);
            } else {
              this.set("showEmailInUseError", false);
              this.set("showBackEndValidationError", true);
            }
          });
        } else {
          this.set("showEmailInUseError", false);
          this.set("showBackEndValidationError", false);
        }
      } else {
        this.send("saveReservation",reservationInformation);
      }
    },

    onCountrySelect(value){
      this.set("cities", countriesWithCities[value])
      this.set("country", value);
    },

    onCitySelect(value){
      this.set("city", value);
    },

    logout(){
      this.get("ajaxService").logout().then(value => {
        this.set("loggedIn",false)
        this.transitionToRoute("/");
      },value=>{
        console.log("There was an error in the logout.");
      });
    }
  }
});
