import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | termsOfUse', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:terms-of-use');
    assert.ok(route);
  });
});
