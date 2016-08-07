import { Component } from '@angular/core';

import { PostService } from './post.service';
import { PostComponent } from './post.component';
import { ROUTER_DIRECTIVES } from '@angular/router';

@Component({
    selector: 'my-app',
    templateUrl: 'app/app.component.html',
    styleUrls: ['app/app.component.css'],
    directives: [ROUTER_DIRECTIVES],
    providers: [
        PostService
    ]
})
export class AppComponent {
    title = 'Tour of Heroes';
}