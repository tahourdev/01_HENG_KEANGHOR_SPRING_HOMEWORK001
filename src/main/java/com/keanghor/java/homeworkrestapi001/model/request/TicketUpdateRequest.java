package com.keanghor.java.homeworkrestapi001.model.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketUpdateRequest {

    @Schema(description = "List of ticket IDs to update", example = "[0]", type = "integer", format = "int64")
    private List<Long> ticketIds;
    private boolean paymentStatus;
}
