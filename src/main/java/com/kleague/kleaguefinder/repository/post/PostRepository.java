package com.kleague.kleaguefinder.repository.post;

import com.kleague.kleaguefinder.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {


}
