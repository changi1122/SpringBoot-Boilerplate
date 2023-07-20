package net.studio1122.boilerplate.controller;

import net.studio1122.boilerplate.domain.Board;
import net.studio1122.boilerplate.domain.User;
import net.studio1122.boilerplate.dto.BoardDTO;
import net.studio1122.boilerplate.dto.BoardListDTO;
import net.studio1122.boilerplate.dto.BoardListItemDTO;
import net.studio1122.boilerplate.service.BoardService;
import net.studio1122.boilerplate.service.UserService;
import net.studio1122.boilerplate.utility.Security;
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
    private final UserService userService;

    @Autowired
    public BoardController(BoardService boardService, UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    @PostMapping("/api/board")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void create(@RequestBody Board board) {
        try {
            User user = (User) userService.loadUserByUsername(Security.getCurrentUsername());
            boardService.create(board, user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/api/board/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void update(@PathVariable("id") Long id, @RequestBody Board board) {
        try {
            User user = (User) userService.loadUserByUsername(Security.getCurrentUsername());
            boardService.update(id, board, user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/api/board/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") Long id) {
        try {
            User user = (User) userService.loadUserByUsername(Security.getCurrentUsername());
            boardService.delete(id, user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/api/board/{id}")
    @ResponseBody
    public BoardDTO read(@PathVariable("id") Long id) {
        //try {
            return BoardDTO.build(boardService.read(id).orElseThrow());
        //} catch (Exception e) {
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        //}
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
