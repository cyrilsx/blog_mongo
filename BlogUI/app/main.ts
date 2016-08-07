import { bootstrap }    from '@angular/platform-browser-dynamic';
import { AppComponent } from './app.component.ts';
import { appRouterProviders } from './app.routes';
bootstrap(AppComponent, [
    appRouterProviders
]);