import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostService } from './post.service'
import { Post } from './post';

@Component({
    selector: 'my-post-detail',
    styles: ['app/post-detail.component.css'],
    template: `
    <button (click)="goBack()">Back</button>
    <div *ngIf="post">
        <h2>{{post.title}}</h2>

        <div>{{post.content}}</div>
    </div>
    `
})
export class PostDetailComponent implements OnInit, OnDestroy {
    constructor(
        private route: ActivatedRoute,
        private postService: PostService) {}

    @Input()
    post: Post;

    sub: any;

    ngOnInit() {
        this.sub = this.route.params.subscribe(params => {
            let id = +params['id'];
            this.postService.getPost(id)
                .then(post => this.post = post);
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    goBack() {
        window.history.back();
    }
}

