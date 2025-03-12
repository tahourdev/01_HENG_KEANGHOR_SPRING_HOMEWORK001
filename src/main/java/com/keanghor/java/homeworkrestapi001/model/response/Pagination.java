package com.keanghor.java.homeworkrestapi001.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pagination metadata")
public class Pagination {
    @Schema(description = "Total number of elements", example = "0")
    private long totalElements;

    @Schema(description = "Current page number (0-based)", example = "0")
    private int currentPage;

    @Schema(description = "Number of elements per page", example = "0")
    private int pageSize;

    @Schema(description = "Total number of pages", example = "0")
    private int totalPages;
}
