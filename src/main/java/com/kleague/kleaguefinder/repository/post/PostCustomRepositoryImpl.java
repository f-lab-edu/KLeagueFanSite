package com.kleague.kleaguefinder.repository.post;

import com.kleague.kleaguefinder.domain.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;
import static com.kleague.kleaguefinder.domain.QPost.*;


@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findByRequest(String title, String content, int size, int offset) {
        return jpaQueryFactory.selectFrom(post)
                .where(post.title.contains(title)
                        .and(post.content.contains(content)))
                .limit(size)
                .offset(offset)
                .orderBy(post.id.desc())
                .fetch();
    }
}
