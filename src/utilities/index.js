import uuid from 'uuid';
import mime from 'mime';
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

export const generateFileId = mimetype =>
  `${uuid.v4()}.${mime.getExtension(mimetype)}`;
