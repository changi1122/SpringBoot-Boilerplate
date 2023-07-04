package net.studio1122.boilerplate.service;

import jakarta.transaction.Transactional;
import net.studio1122.boilerplate.domain.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Test
    void 글쓰기() {
        // given
        Board board = Board.builder()
                .title("제목")
                .body("본문")
                .isDeleted(false)
                .createdDate(OffsetDateTime.now())
                .build();

        // when
        boardService.create(board);

        // then
        Board result = boardService.read(board.getId()).orElseThrow();
        assertThat(result).isEqualTo(board);
    }

    @Test
    void 글_수정() {
        // given
        Board board = Board.builder()
                .title("제목")
                .body("본문")
                .isDeleted(false)
                .createdDate(OffsetDateTime.now())
                .build();
        boardService.create(board);

        Board updated = Board.builder()
                .title("수정된 제목")
                .body("수정된 본문")
                .build();

        // when
        try {
            boardService.update(board.getId(), updated);
        } catch (Exception e) {
            fail("Not found 예외");
        }

        // then
        Board result = boardService.read(board.getId()).orElseThrow();
        assertThat(result.getTitle()).isEqualTo(updated.getTitle());
    }

    @Test
    void 글_삭제() {
        // given
        Board board = Board.builder()
                .title("제목")
                .body("본문")
                .isDeleted(false)
                .createdDate(OffsetDateTime.now())
                .build();
        boardService.create(board);

        // when
        try {
            boardService.delete(board.getId());
        } catch (Exception e) {
            fail("Not found 예외");
        }

        // then
        Board result = boardService.read(board.getId()).orElseThrow();
        assertThat(result.getIsDeleted()).isEqualTo(true);

    }

    @Test
    void 없는_글_수정() {
        // given
        Board updated = Board.builder()
                .title("수정된 제목")
                .body("수정된 본문")
                .build();

        // when
        Exception e = Assertions.assertThrows(Exception.class,
                () -> boardService.update(-1L, updated)
        );

        // then
        Assertions.assertEquals(
                "Not found",
                e.getMessage()
        );
    }

    @Test
    void 전체_글_목록() {
        // given
        Board board = Board.builder()
                .title("제목")
                .body("본문")
                .isDeleted(false)
                .createdDate(OffsetDateTime.now())
                .build();
        Board board2 = Board.builder()
                .title("제목2")
                .body("본문2")
                .isDeleted(false)
                .createdDate(OffsetDateTime.now())
                .build();

        // when
        int size = boardService.listAll().size();
        boardService.create(board);
        boardService.create(board2);

        // then
        int newSize = boardService.listAll().size();
        assertThat(newSize).isEqualTo(size + 2);
    }
    
}