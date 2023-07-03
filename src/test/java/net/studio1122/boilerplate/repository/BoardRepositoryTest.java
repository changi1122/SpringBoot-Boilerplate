package net.studio1122.boilerplate.repository;

import jakarta.transaction.Transactional;
import net.studio1122.boilerplate.domain.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Test
    void save() {
        // given
        Board board = Board.builder()
                .title("제목")
                .body("본문")
                .isDeleted(false)
                .createdDate(OffsetDateTime.now())
                .build();

        // when
        boardRepository.save(board);

        // then
        Board result = boardRepository.findById(board.getId()).orElseThrow();
        assertThat(board).isEqualTo(result);
    }

    @Test
    void testSaveNullThrowException() {
        // given
        Board board = Board.builder()
                .title(null)
                .body("본문")
                .isDeleted(false)
                .createdDate(OffsetDateTime.now())
                .build();

        // when
        Exception e = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> boardRepository.save(board)
        );

        // then
        Assertions.assertEquals(
                "not-null property references a null or transient value : net.studio1122.boilerplate.domain.Board.title",
                e.getMessage()
        );
    }

    @Test
    void findAll() {
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
        int size = boardRepository.findAll().size();
        boardRepository.save(board);
        boardRepository.save(board2);

        // then
        int newSize = boardRepository.findAll().size();
        assertThat(size + 2).isEqualTo(newSize);
    }
}