import { Injectable } from '@angular/core';
import { POSTS } from './mock-posts';

@Injectable()
export class PostService {
    getPosts() {
        return Promise.resolve(POSTS);
    }

    getPost(id: number) {
        return this.getPosts()
            .then(posts => posts.find(post => post.id === id));
    }
}