import { Component, OnInit } from '@angular/core';
import { PostDetailComponent } from './post-detail.component';
import { Post } from './post';
import { PostService} from './post.service';

@Component({
    directives: [PostDetailComponent],
    selector: 'my-posts',
    templateUrl: 'app/post.component.html',
})
export class PostComponent implements OnInit {
    constructor(private postService: PostService) { }
    title = 'FamilyBoom';
    posts = [];
    selectedPost: Post;
    onSelect(post: Post) { this.selectedPost = post; }

    getPosts() {
        this.postService.getPosts().then(posts => this.posts = posts);
    }

    ngOnInit() {
        this.getPosts();
    }

}
