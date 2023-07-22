package net.studio1122.boilerplate.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import net.studio1122.boilerplate.domain.Heart;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class HeartRepository {

    @PersistenceContext
    EntityManager em;

    public Heart save(Heart heart) {
        em.persist(heart);
        return heart;
    }

    public Optional<Heart> findByBoardIdAndByUserId(Long boardId, Long userId) {
        try {
            Heart heart = em.createQuery(
                            "select h from Heart h where h.post.id = :boardId and h.user.id = :userId",
                            Heart.class)
                    .setParameter("boardId", boardId)
                    .setParameter("userId", userId)
                    .getSingleResult();

            return Optional.ofNullable(heart);
        }
        catch (NoResultException e) {
            return Optional.empty();
        }

    }

    public Boolean existsByBoardIdAndByUserId(Long boardId, Long userId) {
        Long cnt = em.createQuery(
                "select count(h) from Heart h where h.post.id = :boardId and h.user.id = :userId",
                        Long.class)
                .setParameter("boardId", boardId)
                .setParameter("userId", userId)
                .getSingleResult();
        return (cnt != 0);
    }
}
