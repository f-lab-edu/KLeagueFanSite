package com.kleague.kleaguefinder.repository.post;

import com.kleague.kleaguefinder.domain.Post;

import java.util.List;

public interface PostCustomRepository {

    public List<Post> findByRequest(String title, String content, int size, int offset);
}
