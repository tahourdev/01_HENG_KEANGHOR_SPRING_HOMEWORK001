package com.keanghor.java.homeworkrestapi001.model.entity;

import com.keanghor.java.homeworkrestapi001.model.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Long ticketId = 0L;
    private String passengerName;
    private LocalDate travelDate;
    private String sourceStation;
    private String destinationStation;
    private Double price = 0.0;
    private boolean paymentStatus;
    private TicketStatus ticketStatus;
    private String seatNumber;
}
