package com.keanghor.java.homeworkrestapi001.model.response;
import com.keanghor.java.homeworkrestapi001.model.enums.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long ticketId;
    private String passengerName;
    private LocalDate travelDate;
    private String sourceStation;
    private String destinationStation;
    private Double price;
    private boolean paymentStatus;
    private TicketStatus ticketStatus;
    private String seatNumber;
}
