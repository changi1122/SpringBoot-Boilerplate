package net.studio1122.boilerplate.service;

import jakarta.transaction.Transactional;
import net.studio1122.boilerplate.domain.Board;
import net.studio1122.boilerplate.repository.BoardRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void create(Board board) {
        boardRepository.save(board);
    }

    @Transactional
    public void update(Long id, Board updated) throws Exception {
        Optional<Board> opBoard = boardRepository.findById(id);
        if (opBoard.isPresent()) {
            Board board = opBoard.get();
            if (!board.getTitle().equals(updated.getTitle())) {
                board.setTitle(updated.getTitle());
            }
            if (!board.getBody().equals(updated.getBody())) {
                board.setBody(updated.getBody());
            }
            board.setEditedDate(OffsetDateTime.now());
        } else {
            throw new Exception("Not found");
        }
    }

    public Optional<Board> read(Long id) {
        return boardRepository.findById(id);
    }

    public List<Board> listAll() {
        return boardRepository.findAll();
    }
}
