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
public class BoardListDTO {

    private Long id;
    private String title;
    private OffsetDateTime createdDate;
    private OffsetDateTime editedDate;

    public static BoardListDTO build(Board board) {
        return BoardListDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .createdDate(board.getCreatedDate())
                .editedDate(board.getEditedDate())
                .build();
    }
}
