import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | completeReservation', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:complete-reservation');
    assert.ok(route);
  });
});
