package net.studio1122.boilerplate.controller;

import jakarta.websocket.server.PathParam;
import net.studio1122.boilerplate.domain.Board;
import net.studio1122.boilerplate.dto.BoardDTO;
import net.studio1122.boilerplate.dto.BoardListDTO;
import net.studio1122.boilerplate.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/api/board")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void create(@RequestBody Board board) {
        try {
            boardService.create(board);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    @PutMapping("/api/board/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void update(@PathVariable("id") Long id, @RequestBody Board board) {
        try {
            boardService.update(id, board);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    @DeleteMapping("/api/board/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") Long id) {
        try {
            boardService.delete(id);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    @GetMapping("/api/board/{id}")
    @ResponseBody
    public BoardDTO read(@PathVariable("id") Long id) {
        try {
            return BoardDTO.build(boardService.read(id).orElseThrow());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Not found"
            );
        }
    }

    @GetMapping("/api/boardlist")
    @ResponseBody
    public List<BoardListDTO> listAll() {
        try {
            List<BoardListDTO> list = new ArrayList<>();

            List<Board> found = boardService.listAll();
            found.forEach(board -> list.add(BoardListDTO.build(board)));

            return list;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

}
