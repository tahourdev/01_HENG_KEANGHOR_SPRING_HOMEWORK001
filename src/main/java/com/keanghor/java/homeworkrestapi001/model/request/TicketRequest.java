package com.keanghor.java.homeworkrestapi001.model.request;

import com.keanghor.java.homeworkrestapi001.model.enums.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {
    private String passengerName;
    private LocalDate travelDate;
    private String sourceStation;
    private String destinationStation;
    @Schema(description = "Ticket price", example = "0")
    private Double price;
    private boolean paymentStatus;
    private TicketStatus ticketStatus;
    private String seatNumber;
}