import { Router } from 'express';
import multer from 'multer';
import { accessFile } from '../utilities';

export default () => {
  const api = Router();
  const upload = multer({ dest: process.env.HOST_PORTAL_DIR });

  api.get('/media/:id', async (req, res) => {
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

  api.post('/media', upload.single('file'), (req, res) => {
    if (!req.file) {
      return res.status(400).json({
        error: {
          message: 'Bad Request',
        },
        data: null,
      });
    }

    return res.json({
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
