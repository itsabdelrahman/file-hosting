import http from 'http';
import express from 'express';
import bodyParser from 'body-parser';
import cors from 'cors';
import api from './routes/api';
import config from './constants/config';

require('dotenv').config();

const app = express();
app.server = http.createServer(app);

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cors());

app.use('/api/', api());

app.server.listen(process.env.PORT || config.server.port);
// eslint-disable-next-line no-console
console.log(`🚀  Server listening on port ${app.server.address().port}...`);
