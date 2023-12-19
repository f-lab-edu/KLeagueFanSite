package com.kleague.kleaguefinder.repository;

import com.kleague.kleaguefinder.domain.Post;
import com.kleague.kleaguefinder.request.PostSearchRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;
import static com.kleague.kleaguefinder.domain.QPost.*;


@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findBySearchRequest(PostSearchRequest postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .where(post.title.contains(postSearch.getTitle())
                        .and(post.content.contains(postSearch.getContent())))
                .limit(postSearch.getSize())
                .offset(postSearch.getOffSet())
                .orderBy(post.id.desc())
                .fetch();
    }
}
