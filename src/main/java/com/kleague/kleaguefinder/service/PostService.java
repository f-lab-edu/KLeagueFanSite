package com.kleague.kleaguefinder.service;

import com.kleague.kleaguefinder.domain.Post;
import com.kleague.kleaguefinder.exception.NoValueException;
import com.kleague.kleaguefinder.repository.post.PostRepository;
import com.kleague.kleaguefinder.request.post.PostCreateRequest;
import com.kleague.kleaguefinder.request.post.PostModifyRequest;
import com.kleague.kleaguefinder.request.post.PostSearchRequest;
import com.kleague.kleaguefinder.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.kleague.kleaguefinder.response.PostResponse.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    /**
     * 글 추가
     * 수정
     * 조회
     * 삭제
     */

    private final PostRepository postRepository;

    @Transactional
    public Long write(PostCreateRequest request) {
        Post post = postRepository.save(request.toEntity());
        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostResponse findOne(Long postId) {

        Post post = getPost(postId);

        return createPostResponse(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findBySearch(PostSearchRequest request) {
        return postRepository.findByRequest(request.getTitle(), request.getContent()
                        ,request.getSize(), request.getOffSet()).stream()
                .map(PostResponse::createPostResponse).collect(Collectors.toList());
    }

    @Transactional
    public void modify(Long postId, PostModifyRequest request) {
        Post post = getPost(postId);
        post.modify(request.getTitle(), request.getContent());
    }

    @Transactional
    public void delete(Long postId) {
        Post post = getPost(postId);
        postRepository.delete(post);
    }

    private Post getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoValueException("Post", "id"));
        return post;
    }

}
