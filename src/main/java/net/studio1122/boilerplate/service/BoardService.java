package net.studio1122.boilerplate.service;

import jakarta.transaction.Transactional;
import net.studio1122.boilerplate.domain.Board;
import net.studio1122.boilerplate.domain.User;
import net.studio1122.boilerplate.repository.BoardRepository;
import net.studio1122.boilerplate.utility.Sanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final Sanitizer sanitizer;

    @Autowired
    public BoardService(BoardRepository boardRepository, Sanitizer sanitizer) {
        this.boardRepository = boardRepository;
        this.sanitizer = sanitizer;
    }

    @Transactional
    public void create(Board board, User user) {
        board.setBody(sanitizer.sanitize(board.getBody()));
        board.setCreatedDate(OffsetDateTime.now());
        board.setIsDeleted(false);
        board.setView(0L);
        board.setUser(user);
        boardRepository.save(board);
    }

    @Transactional
    public void update(Long id, Board updated, User user) throws Exception {
        Optional<Board> opBoard = boardRepository.findById(id);
        if (opBoard.isPresent()) {
            Board board = opBoard.get();
            if (!Objects.equals(board.getUser().getId(), user.getId())) {
                throw new Exception("User not matched");
            }

            if (updated.getTitle() != null) {
                board.setTitle(updated.getTitle());
            }
            if (updated.getBody() != null) {
                board.setBody(sanitizer.sanitize(updated.getBody()));
            }
            board.setEditedDate(OffsetDateTime.now());
        } else {
            throw new Exception("Not found");
        }
    }

    @Transactional
    public void delete(Long id, User user) throws Exception {
        Optional<Board> opBoard = boardRepository.findById(id);
        if (opBoard.isPresent()) {
            Board board = opBoard.get();
            if (!Objects.equals(board.getUser().getId(), user.getId())) {
                throw new Exception("User not matched");
            }

            board.setIsDeleted(true);
            board.setDeletedDate(OffsetDateTime.now());
        } else {
            throw new Exception("Not found");
        }
    }

    public Optional<Board> read(Long id) {
        boardRepository.countViewById(id);
        return boardRepository.findById(id);
    }

    public List<Board> list(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public List<Board> listOrderByView(Pageable pageable) {
        return boardRepository.findAllOrderByView(pageable);
    }

    public List<Board> listOrderByRandom(Pageable pageable) {
        return boardRepository.findAllOrderByRandom(pageable);
    }

    public List<Board> listAll() {
        return boardRepository.findAll();
    }

    public Long count() {
        return boardRepository.count();
    }
}
