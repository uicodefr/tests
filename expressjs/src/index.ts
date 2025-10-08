import express from 'express';
import type { Request, Response } from 'express';
import taskController from './controller/task-controller.js';

const app = express();
const port = 3000;

app.get('/', (_req: Request, res: Response) => {
    res.send('Hello World !');
});

app.use('/tasks', taskController);

app.listen(port, () => {
    console.log(`App listening on port ${port}`);
});
