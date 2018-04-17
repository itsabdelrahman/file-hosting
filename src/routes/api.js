import { Router } from 'express';
import multer from 'multer';
import { accessFile, generateFileId } from '../utilities';

export default () => {
  const api = Router();
  const upload = multer({
    storage: multer.diskStorage({
      destination: (req, file, cb) => cb(null, process.env.HOST_PORTAL_DIR),
      filename: (req, file, cb) => cb(null, generateFileId(file.mimetype)),
    }),
  });

  api.get('/file/:id', async (req, res) => {
    const filePath = `${process.env.HOST_PORTAL_DIR}/${req.params.id}`;
    const doesFileExist = await accessFile(filePath);

    if (!doesFileExist) {
      return res.status(404).json({
        error: {
          message: 'Not Found',
        },
        data: null,
      });
    }

    return res.sendFile(filePath);
  });

  api.post('/file', upload.single('file'), (req, res) => {
    if (!req.file) {
      return res.status(400).json({
        error: {
          message: 'Bad Request',
        },
        data: null,
      });
    }

    return res.status(201).json({
      error: null,
      data: {
        id: req.file.filename,
        name: req.file.originalname,
        type: req.file.mimetype,
      },
    });
  });

  return api;
};
