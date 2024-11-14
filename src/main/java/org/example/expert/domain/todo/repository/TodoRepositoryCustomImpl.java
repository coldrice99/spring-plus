package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;

import java.util.Optional;

public class TodoRepositoryCustomImpl implements TodoRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public TodoRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        // QueryDSL 에서 자동 생성된 Q 클래스를 사용해 Todo와 User 엔티티에 접근
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo result = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin() // fetchJoin() 을 추가하여 즉시 로딩
                .where(todo.id.eq(todoId))
                .fetchOne(); // 쿼리의 결과를 하나만 가져옴 (하나일 때만 유효)

        // 조회 결과가 있을 경우 Todo 객체를 Optional로 감싸서 반환하고, 조회된 데이터가 없으면 빈 Optional이 반환
        return Optional.ofNullable(result);
    }
}
