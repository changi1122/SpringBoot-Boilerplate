package net.studio1122.boilerplate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.studio1122.boilerplate.domain.Board;
import net.studio1122.boilerplate.domain.Heart;

import java.time.OffsetDateTime;
import java.util.Optional;

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
    private Long view;
    private Long heart;
    private String author;
    private Boolean isHeartGiven;

    public static BoardDTO build(Board board, Optional<Heart> heart) {
        return BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .body(board.getBody())
                .createdDate(board.getCreatedDate())
                .editedDate(board.getEditedDate())
                .view(board.getView())
                .heart(board.getHeart())
                .author(board.getUser().getNickname())
                .isHeartGiven(heart.isPresent())
                .build();
    }
}
