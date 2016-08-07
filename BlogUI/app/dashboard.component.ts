import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Post } from './post';
import { PostService } from './post.service';


@Component({
    selector: 'my-dashboard',
    templateUrl: 'app/dashboard.component.html',
    styleUrls: ['app/dashboard.component.css']
})
export class DashboardComponent implements OnInit {
    posts: Post[] = [];
    constructor(
        private postService: PostService,
        private router: Router
    ) { }

    ngOnInit() {
        this.postService.getPosts()
            .then(posts => this.posts = posts.slice(1, 5));
    }
    gotoDetail(post: Post) {
        let link = ['/detail', post.id];
        this.router.navigate(link);
    }
}