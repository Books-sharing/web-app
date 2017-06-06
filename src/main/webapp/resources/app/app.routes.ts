import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { SearchComponent } from './search/search.component';
import { NotificationComponent } from './notifications/index';
import { AuthGuard } from './guards/index';

const router: Routes = [
    { path: '', component: NotificationComponent, canActivate: [AuthGuard] },
    { path: 'login', component: LoginComponent },
    { path: 'registration', component: RegistrationComponent },
    { path: 'search', component: SearchComponent },
    { path: '**', redirectTo: '' },
];

export const routes = RouterModule.forRoot(router);
