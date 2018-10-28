import { helper } from '@ember/component/helper';

export function getArrayValue([array,index]) {
  return array[index];
}

export default helper(getArrayValue);
