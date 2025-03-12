package com.keanghor.java.homeworkrestapi001.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated response containing tickets and pagination details")
public class PaginatedResponse {
    @Schema(description = "List of ticket items")
    private List<TicketResponse> items;

    @Schema(description = "Pagination metadata")
    private Pagination pagination;
}
