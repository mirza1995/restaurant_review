import EmberRouter from '@ember/routing/router';
import config from './config/environment';

const Router = EmberRouter.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function() {
  this.route('restaurants');
  this.route('reservation',{ path: '/reservation/:restaurant_id'});
  this.route('login');
  this.route('signup');
  this.route('findTable');
  this.route('completeReservation');
  this.route('termsOfUse');
  this.route('privacyPolicy');
  this.route('admin', function() {
    this.route('dashboard');
    this.route('users', function() {});
    this.route('createUser',{ path: '/createUser/:userId'});
    this.route('cousines');
    this.route('createCousine',{ path: '/createCousine/:id'});
    this.route('createRestaurant',{ path: '/createRestaurant/:id'}, function() {
      this.route('restaurant', { path: '/restaurant/:id'});
      this.route('menu', { path: '/menu/:id'});
      this.route('tables', { path: '/tables/:id'});
      this.route('gallery', { path: '/gallery/:id'});
    });
    this.route('restaurants');
    this.route('locations');
    this.route('createLocation',{ path: '/createLocation/:id'});
  });
  this.route('map');
});

export default Router;
