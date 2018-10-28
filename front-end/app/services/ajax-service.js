import BaseHttpService from 'front-end/services/ajax';

export default BaseHttpService.extend({
  getRestaurantDetails: function (id) {
    return this.ajax('GET', `/getRestaurantDetails/`+id);
  },

  getPopularRestaurants: function () {
    return this.ajax('GET', `/allRestaurantsSortReservationsToday`);
  },

  getClosestRestaurants: function(location){
    return this.ajax('POST', `/getClosestRestaurants`,location);
  },

  getClosestRestaurantsForMap: function(location){
    return this.ajax('POST', `/getClosestRestaurantsForMap`,location);
  },

  getAllRestaurants: function () {
    return this.ajax('GET', `/getAllRestaurants`);
  },

  getNumberOfRestaurants: function(){
    return this.ajax('GET', `/numberOfRestaurants`);
  },

  getRestaurantsPage: function(page){
    return this.ajax('GET',`getRestaurantsPage/`+page);
  },

  register: function(userInformation){
  	return this.ajax('POST','/register',userInformation);
  },

  addUser: function(userInformation){
    return this.ajax('POST','/addUser',userInformation);
  },

  updateUserWithoutLogin: function(userInformation){
    return this.ajax('POST','/updateUserWithoutLogin',userInformation);
  },

  login: function(userInformation){
  	return this.ajax('POST','/login',userInformation);
  },

  logout: function(){
    return this.ajax('GET','/logout');
  },

  currentUser: function(){
    return this.ajax('GET','/currentUser');
  },

  getPopularLocations: function(){
  	return this.ajax('GET','/getPopularLocations');
  },

  getCousinesCarousel: function(){
    return this.ajax('GET','/getCousinesCarousel');
  },

  getRestaurantPhotos(id){
    return this.ajax('GET', `/getRestaurantPhotos/`+id);
  },

  getGallery(id){
    return this.ajax('GET', `/getGallery/`+id);
  },

  getNumberOfPhotos(id){
    return this.ajax('GET', `/getNumberOfPhotos/`+id);
  },

  getCousines(){
    return this.ajax('GET',`/getCousines`);
  },

  search(searchInformation){
    return this.ajax("POST",`/search`,searchInformation);
  },

  getMenu(menuInformation){
    return this.ajax("POST",`/menu`,menuInformation);
  },

  findTables(searchInformation){
    return this.ajax("POST",`/findTables`,searchInformation);
  },

  findTable(searchInformation){
    return this.ajax("POST",`/findTable`,searchInformation);
  },

  reservation(reservationInformation){
    return this.ajax("POST",`/reservation`,reservationInformation);
  },

  getTodaysReservationNumber(reservationInformation){
    return this.ajax("POST",`/getTodaysReservations`,reservationInformation);
  },
  
  reviewRestaurant(reviewInformation){
    return this.ajax("POST",`/reviewRestaurant`,reviewInformation);
  },

  getNumberOfRestaurants(){
    return this.ajax("GET",`/numberOfRestaurants`);
  },

  getNumberOfLocations(){
    return this.ajax("GET",`/numberOfLocations`);
  },

  getNumberOfUsers(){
    return this.ajax("GET",`/numberOfUsers`);
  },

  getAllUsers(){
    return this.ajax("GET",`/allUsers`);
  },

  getUser(id){
    return this.ajax("GET",`/getUser/`+id);
  },

  deleteUser(id){
    return this.ajax("GET",`/deleteUser/`+id);
  },

  updateUser(userInformation){
    return this.ajax("POST",'/updateUser',userInformation);
  },

  searchUser(searchCriteria){
    return this.ajax("POST",'/searchUser',searchCriteria);
  },

  getUsersPage(searchInformation){
    return this.ajax("POST",'/getUsersPage',searchInformation);
  },

  getAllCousines(){
    return this.ajax("GET","/getAllCousines");
  },

  getCousinesPage(searchInformation){
    return this.ajax("POST",'/getCousinesPage',searchInformation);
  },

  searchCousine(searchCriteria){
    return this.ajax("POST",'/searchCousine',searchCriteria);
  },

  getAllLocations(){
    return this.ajax("GET","/getAllLocations");
  },

  getLocationsPage(searchInformation){
    return this.ajax("POST",'/getLocationsPage',searchInformation);
  },

  searchLocation(searchCriteria){
    return this.ajax("POST",'/searchLocation',searchCriteria);
  },

  getRestaurantsForAdmin(){
    return this.ajax("GET","/getRestaurantsForAdmin");
  },

  getRestaurantsPageAdmin(searchInformation){
    return this.ajax("POST",'/getRestaurantsPageAdmin',searchInformation);
  },

  searchRestaurantAdmin(searchCriteria){
    return this.ajax("POST",'/searchRestaurantAdmin',searchCriteria);
  },

  getLocation(id){
    return this.ajax("GET",'/getLocation/'+id);
  },

  getLocationByName(name){
    return this.ajax("GET",'/getLocationByName/'+name);
  },

  deleteLocation(id){
    return this.ajax("GET",'/deleteLocation/'+id);
  },

  updateLocation(information){
    return this.ajax("POST",'/updateLocation',information);
  },

  createLocation(information){
    return this.ajax("POST",'/createLocation',information);
  },

  getCousine(id){
    return this.ajax("GET",'/getCousine/'+id);
  },

  deleteCousine(id){
    return this.ajax("GET",'/deleteCousine/'+id);
  },

  updateCousine(information){
    return this.ajax("POST",'/updateCousine',information);
  },

  createCousine(information){
    return this.ajax("POST",'/createCousine',information);
  },

  addTables(information){
    return this.ajax("POST",'/addTables',information);
  },

  getRestaurantMenu(id){
    return this.ajax("GET",'/getRestaurantMenu/'+id);
  },

  getRestaurantTables(id){
    return this.ajax("GET",'/getRestaurantTables/'+id);
  },

  deleteTable(id){
    return this.ajax("GET",'/deleteTable/'+id);
  },

  deleteMeal(id){
    return this.ajax("GET",'/deleteMeal/'+id);
  },

  addMenus(information){
    return this.ajax("POST",'/addMenus',information);
  },

  addRestaurant(information){
    return this.ajax("POST",'/addRestaurant',information);
  },

  updateRestaurant(information){
    return this.ajax("POST",'/updateRestaurant',information);
  },

  deleteRestaurant(id){
    return this.ajax("GET",'/deleteRestaurant/'+id);
  },

  uploadImage(imageData){
    return this.ajax("POST",'/uploadImage',imageData);
  },

  uploadGallery(gallery){
    return this.ajax("POST",'/uploadGallery',gallery);
  }
  
})
