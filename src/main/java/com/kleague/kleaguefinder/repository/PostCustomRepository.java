package com.kleague.kleaguefinder.repository;

import com.kleague.kleaguefinder.domain.Post;
import com.kleague.kleaguefinder.request.PostSearchRequest;

import java.util.List;

public interface PostCustomRepository {

    public List<Post> findBySearchRequest(PostSearchRequest postSearch);
}
