package com.kleague.kleaguefinder.controller;

import com.kleague.kleaguefinder.request.PostCreateRequest;
import com.kleague.kleaguefinder.request.PostModifyRequest;
import com.kleague.kleaguefinder.request.PostSearchRequest;
import com.kleague.kleaguefinder.response.PostResponse;
import com.kleague.kleaguefinder.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping("v1/posts/write")
    public Long write(@Valid @RequestBody PostCreateRequest postCreateRequest) {
        return postService.write(postCreateRequest);
    }

    @GetMapping("v1/posts/{postId}")
    public PostResponse findOne(@PathVariable("postId") Long postId) {
        return postService.findOne(postId);
    }

    @PostMapping("v1/posts/search")
    public List<PostResponse> search(@RequestBody PostSearchRequest postSearchRequest) {
        return postService.findBySearch(postSearchRequest);
    }

    @PutMapping("v1/posts/{postId}")
    public void modify(@PathVariable("postId") Long postId,
                       @Valid @RequestBody PostModifyRequest postModifyRequest) {
        postService.modify(postId, postModifyRequest);
    }

    @DeleteMapping("v1/posts/{postId}")
    public void delete(@PathVariable("postId") Long postId) {
        postService.delete(postId);
    }
}
