package net.studio1122.boilerplate.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.studio1122.boilerplate.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Board> findAll() {
        return em.createQuery("SELECT b from Board b", Board.class).getResultList();
    }
}
