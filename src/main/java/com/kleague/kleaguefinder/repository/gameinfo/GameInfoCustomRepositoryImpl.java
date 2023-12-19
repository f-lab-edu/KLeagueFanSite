package com.kleague.kleaguefinder.repository.gameinfo;

import com.kleague.kleaguefinder.domain.GameInfo;
import com.kleague.kleaguefinder.request.gameinfo.GameInfoSearchRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.kleague.kleaguefinder.domain.QGameInfo.*;

@RequiredArgsConstructor
public class GameInfoCustomRepositoryImpl implements GameInfoCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GameInfo> findByRequest(String name, String date, String location,
                                        int size, int offset) {
        return jpaQueryFactory.selectFrom(gameInfo)
                .where(gameInfo.date.contains(date)
                        .and(gameInfo.name.contains(name))
                        .and(gameInfo.location.contains(location)))
                .limit(size)
                .offset(offset)
                .orderBy(gameInfo.id.desc())
                .fetch();
    }

    @Override
    public List<GameInfo> findByNameAndDate(String name, String date) {
        return jpaQueryFactory.selectFrom(gameInfo)
                .where(gameInfo.name.equalsIgnoreCase(name)
                        .and(gameInfo.date.equalsIgnoreCase(date)))
                .fetch();
    }
}
