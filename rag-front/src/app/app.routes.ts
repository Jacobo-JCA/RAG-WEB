import { Routes } from '@angular/router';
import { Upload } from './components/upload/upload';
import { Chat } from './components/chat/chat';
import { History } from './components/history/history';

export const routes: Routes = [
    { path: '',        redirectTo: 'upload', pathMatch: 'full' },
    { path: 'upload',  component: Upload },
    { path: 'chat',    component: Chat },
    { path: 'history', component: History },
];
