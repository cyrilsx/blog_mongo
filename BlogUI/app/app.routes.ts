import { provideRouter, RouterConfig }  from '@angular/router';
import { PostComponent } from './post.component';
import { PostDetailComponent } from './post-detail.component';
import { DashboardComponent } from './dashboard.component';

const routes: RouterConfig = [
    {
        path: 'posts',
        component: PostComponent
    },
    {
        path: 'dashboard',
        component: DashboardComponent
    },
    {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full'
    },
    {
        path: 'detail/:id',
        component: PostDetailComponent
    },
];

export const appRouterProviders = [
    provideRouter(routes)
];