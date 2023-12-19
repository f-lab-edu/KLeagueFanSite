package com.kleague.kleaguefinder.controller;

import com.kleague.kleaguefinder.request.post.PostCreateRequest;
import com.kleague.kleaguefinder.request.post.PostModifyRequest;
import com.kleague.kleaguefinder.request.post.PostSearchRequest;
import com.kleague.kleaguefinder.response.PostResponse;
import com.kleague.kleaguefinder.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping("api/v1/posts/write")
    public Long write(@RequestBody PostCreateRequest postCreateRequest) {
        return postService.write(postCreateRequest);
    }

    @GetMapping("api/v1/posts/{id}")
    public PostResponse findOne(@PathVariable("id") Long id) {
        return postService.findOne(id);
    }

    @PostMapping("api/v1/posts/search")
    public List<PostResponse> search(@RequestBody PostSearchRequest postSearchRequest) {
        return postService.findBySearch(postSearchRequest);
    }

    @PutMapping("api/v1/posts/{id}")
    public void modify(@PathVariable("id") Long id,
                       @RequestBody PostModifyRequest postModifyRequest) {
        postService.modify(id, postModifyRequest);
    }

    @DeleteMapping("api/v1/posts/{id}")
    public void delete(@PathVariable("id") Long id) {
        postService.delete(id);
    }
}
