import Promise from 'bluebird';

const fs = Promise.promisifyAll(require('fs'));

export const accessFile = async path => {
  try {
    await fs.accessAsync(path);
  } catch (e) {
    return false;
  }

  return true;
};
