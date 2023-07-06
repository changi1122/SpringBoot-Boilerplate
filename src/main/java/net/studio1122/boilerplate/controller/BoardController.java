package net.studio1122.boilerplate.controller;

import net.studio1122.boilerplate.domain.Board;
import net.studio1122.boilerplate.dto.BoardDTO;
import net.studio1122.boilerplate.dto.BoardListDTO;
import net.studio1122.boilerplate.dto.BoardListItemDTO;
import net.studio1122.boilerplate.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/api/board/list")
    @ResponseBody
    public BoardListDTO list(Pageable pageable) {
        try {
            List<BoardListItemDTO> list = new ArrayList<>();

            List<Board> found = boardService.list(pageable);
            for (Board board : found) {
                list.add(BoardListItemDTO.build(board));
            }

            Long totalElements = boardService.count();

            return BoardListDTO.builder()
                    .content(list)
                    .size(list.size())
                    .pageSize(pageable.getPageSize())
                    .pageNumber(pageable.getPageNumber())
                    .totalElements(totalElements)
                    .totalPage((int) Math.ceil((double) totalElements / pageable.getPageSize()))
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    @GetMapping("/api/board")
    @ResponseBody
    public BoardListDTO listAll() {
        try {
            List<BoardListItemDTO> list = new ArrayList<>();

            List<Board> found = boardService.listAll();
            for (Board board : found) {
                list.add(BoardListItemDTO.build(board));
            }

            return BoardListDTO.builder()
                    .content(list)
                    .size(list.size())
                    .pageSize(list.size())
                    .pageNumber(0)
                    .totalElements((long)list.size())
                    .totalPage(0)
                    .build();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

}
