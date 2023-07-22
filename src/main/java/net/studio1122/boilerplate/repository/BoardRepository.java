package net.studio1122.boilerplate.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import net.studio1122.boilerplate.domain.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepository {

    @PersistenceContext
    private EntityManager em;

    public Board save(Board board) {
        em.persist(board);
        return board;
    }

    public Optional<Board> findById(Long id) {
        Board board = em.find(Board.class, id);
        return Optional.ofNullable(board);
    }

    public List<Board> findAll(Pageable pageable) {
        return em.createQuery("select b from Board b where b.isDeleted = false order by b.createdDate DESC", Board.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    public List<Board> findAllOrderByView(Pageable pageable) {
        return em.createQuery("select b from Board b where b.isDeleted = false order by b.view DESC", Board.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults((pageable.getPageSize()))
                .getResultList();
    }

    public List<Board> findAllOrderByRandom(Pageable pageable) {
        return em.createQuery("select b from Board b where b.isDeleted = false order by rand()", Board.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults((pageable.getPageSize()))
                .getResultList();
    }

    public List<Board> findAll() {
        return em.createQuery("select b from Board b where b.isDeleted = false", Board.class)
                .getResultList();
    }

    public Long count() {
        return em.createQuery("select count(b) from Board b where b.isDeleted = false", Long.class)
                .getSingleResult();
    }

    public void delete(Long id) {
        Board board = em.find(Board.class, id);
        em.remove(board);
    }

    @Transactional
    public void countViewById(Long id) {
        em.createQuery("update Board b set b.view = b.view + 1 where b.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
