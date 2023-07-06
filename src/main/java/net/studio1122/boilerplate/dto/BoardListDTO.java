package net.studio1122.boilerplate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListDTO {

    List<BoardListItemDTO> content;
    private Integer size;
    private Integer pageSize;
    private Integer pageNumber;
    private Long totalElements;
    private Integer totalPage;
}
