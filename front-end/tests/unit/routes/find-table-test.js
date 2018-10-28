import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | findTable', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:find-table');
    assert.ok(route);
  });
});
