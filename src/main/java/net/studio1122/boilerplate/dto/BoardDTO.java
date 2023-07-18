package net.studio1122.boilerplate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.studio1122.boilerplate.domain.Board;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {

    private Long id;
    private String title;
    private String body;
    private OffsetDateTime createdDate;
    private OffsetDateTime editedDate;
    private String author;

    public static BoardDTO build(Board board) {
        return BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .body(board.getBody())
                .createdDate(board.getCreatedDate())
                .editedDate(board.getEditedDate())
                .author(board.getUser().getNickname())
                .build();
    }
}
