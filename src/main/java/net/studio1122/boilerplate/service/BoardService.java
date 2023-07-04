package net.studio1122.boilerplate.service;

import jakarta.transaction.Transactional;
import net.studio1122.boilerplate.domain.Board;
import net.studio1122.boilerplate.repository.BoardRepository;
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
        board.setCreatedDate(OffsetDateTime.now());
        board.setIsDeleted(false);
        boardRepository.save(board);
    }

    @Transactional
    public void update(Long id, Board updated) throws Exception {
        Optional<Board> opBoard = boardRepository.findById(id);
        if (opBoard.isPresent()) {
            Board board = opBoard.get();
            if (updated.getTitle() != null) {
                board.setTitle(updated.getTitle());
            }
            if (updated.getBody() != null) {
                board.setBody(updated.getBody());
            }
            board.setEditedDate(OffsetDateTime.now());
        } else {
            throw new Exception("Not found");
        }
    }

    @Transactional
    public void delete(Long id) throws Exception {
        Optional<Board> opBoard = boardRepository.findById(id);
        if (opBoard.isPresent()) {
            Board board = opBoard.get();
            board.setIsDeleted(true);
            board.setDeletedDate(OffsetDateTime.now());
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
