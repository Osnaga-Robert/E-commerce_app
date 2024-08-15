import { AppServerModule } from './app/app.module.server';
import 'localstorage-polyfill';

declare const global: any;
global['localStorage'] = localStorage;

export { AppServerModule as default };
