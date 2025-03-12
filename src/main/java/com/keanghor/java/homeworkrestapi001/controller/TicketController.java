package com.keanghor.java.homeworkrestapi001.controller;

import com.keanghor.java.homeworkrestapi001.model.entity.Ticket;
import com.keanghor.java.homeworkrestapi001.model.enums.TicketStatus;
import com.keanghor.java.homeworkrestapi001.model.request.TicketRequest;
import com.keanghor.java.homeworkrestapi001.model.request.TicketUpdateRequest;
import com.keanghor.java.homeworkrestapi001.model.response.PaginatedResponse;
import com.keanghor.java.homeworkrestapi001.model.response.Pagination;
import com.keanghor.java.homeworkrestapi001.model.response.TicketResponse;
import com.keanghor.java.homeworkrestapi001.response.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private final List<Ticket> TICKETS = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(5L);

    private TicketResponse toTicketResponse(Ticket ticket) {
        return new TicketResponse(ticket.getTicketId(), ticket.getPassengerName(),
                ticket.getTravelDate(), ticket.getSourceStation(), ticket.getDestinationStation(),
                ticket.getPrice(), ticket.isPaymentStatus(), ticket.getTicketStatus(),
                ticket.getSeatNumber());
    }

    public TicketController() {
        TICKETS.add(new Ticket(1L, "Sokha Meas", LocalDate.parse("2025-04-10"),
                "Phnom Penh", "Siem Reap", 15.00, true, TicketStatus.COMPLETED, "A12"));
        TICKETS.add(new Ticket(2L, "Dara Chan", LocalDate.parse("2025-04-12"),
                "Battambang", "Phnom Penh", 12.50, false, TicketStatus.COMPLETED, "B05"));
        TICKETS.add(new Ticket(3L, "Srey Leak Kim", LocalDate.parse("2025-04-15"),
                "Kampot", "Sihanoukville", 8.00, true, TicketStatus.COMPLETED, "C08"));
        TICKETS.add(new Ticket(4L, "Vuthy Heng", LocalDate.parse("2025-04-18"),
                "Poipet", "Siem Reap", 10.00, false, TicketStatus.CANCELLED, "D14"));
        TICKETS.add(new Ticket(5L, "Bopha Sok", LocalDate.parse("2025-04-20"),
                "Takeo", "Phnom Penh", 7.00, true, TicketStatus.CANCELLED, "E03"));
    }

    @Operation(summary = "Get all tickets")
    @GetMapping
    public ResponseEntity<APIResponse<PaginatedResponse>> getTicketsPaginated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        int adjustedPage = page - 1;
        int start = adjustedPage * size;
        int end = Math.min(start + size, TICKETS.size());
        List<TicketResponse> items = start < TICKETS.size() && start >= 0
                ? TICKETS.subList(start, end).stream().map(this::toTicketResponse).collect(Collectors.toList())
                : Collections.emptyList();

        long totalElements = TICKETS.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        Pagination pagination = new Pagination(totalElements, page, size, totalPages);

        PaginatedResponse payload = new PaginatedResponse(items, pagination);
        APIResponse<PaginatedResponse> response = new APIResponse<>(true, "All tickets retrieved successfully",
                HttpStatus.OK, payload);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Bulk update payment status for multiple tickets")
    @PutMapping
    public ResponseEntity<APIResponse<List<TicketResponse>>> updateMultipleTickets(
            @RequestBody TicketUpdateRequest request) {
        List<TicketResponse> updatedTickets = new ArrayList<>();
        List<Long> notFoundIds = new ArrayList<>();

        for (Long ticketId : request.getTicketIds()) {
            Ticket ticket = TICKETS.stream()
                    .filter(t -> t.getTicketId().equals(ticketId))
                    .findFirst()
                    .orElse(null);
            if (ticket == null) {
                notFoundIds.add(ticketId);
                continue;
            }
            ticket.setPaymentStatus(request.isPaymentStatus());
            updatedTickets.add(toTicketResponse(ticket));
        }

        if (!notFoundIds.isEmpty()) {
            String message = "Some tickets not found: " + notFoundIds;
            APIResponse<List<TicketResponse>> response = new APIResponse<>(false, message,
                    HttpStatus.NOT_FOUND, updatedTickets);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        APIResponse<List<TicketResponse>> response = new APIResponse<>(true, "Multiple tickets payment status updated successfully",
                HttpStatus.OK, updatedTickets);
        return ResponseEntity.ok(response);
    }

    //    @GetMapping
//    public ResponseEntity<APIResponse<List<TicketResponse>>> getAllTickets() {
//        List<TicketResponse> responseData = TICKETS.stream()
//                .map(this::toTicketResponse)
//                .collect(Collectors.toList());
//        APIResponse<List<TicketResponse>> response = new APIResponse<>(true, "Tickets retrieved successfully",
//                HttpStatus.OK, responseData);
//        return ResponseEntity.ok(response);
//    }


    @Operation(summary = "Create a new ticket")
    @PostMapping
    public ResponseEntity<APIResponse<TicketResponse>> createTicket(@RequestBody TicketRequest request) {
        Ticket ticket = new Ticket(null, request.getPassengerName(), request.getTravelDate(),
                request.getSourceStation(), request.getDestinationStation(), request.getPrice(),
                request.isPaymentStatus(), request.getTicketStatus(), request.getSeatNumber());
        ticket.setTicketId(idGenerator.getAndIncrement());
        TICKETS.add(ticket);
        APIResponse<TicketResponse> response = new APIResponse<>(true, "Ticket created successfully",
                HttpStatus.CREATED, toTicketResponse(ticket));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get a ticket by ID")
    @GetMapping("/{ticket-id}")
    public ResponseEntity<APIResponse<TicketResponse>> getTicketById(@PathVariable("ticket-id") Long ticketId) {
        Ticket ticket = TICKETS.stream()
                .filter(t -> t.getTicketId().equals(ticketId))
                .findFirst()
                .orElse(null);
        if (ticket == null) {
            APIResponse<TicketResponse> response = new APIResponse<>(false, "Ticket not found",
                    HttpStatus.NOT_FOUND, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        APIResponse<TicketResponse> response = new APIResponse<>(true, "Ticket retrieved successfully",
                HttpStatus.OK, toTicketResponse(ticket));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update an existing ticket by ID")
    @PutMapping("/{ticket-id}")
    public ResponseEntity<APIResponse<TicketResponse>> updateTicket(@PathVariable("ticket-id") Long ticketId,
                                                                    @RequestBody TicketRequest request) {
        Ticket ticket = TICKETS.stream()
                .filter(t -> t.getTicketId().equals(ticketId))
                .findFirst()
                .orElse(null);
        if (ticket == null) {
            APIResponse<TicketResponse> response = new APIResponse<>(false, "Ticket not found",
                    HttpStatus.NOT_FOUND, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ticket.setPassengerName(request.getPassengerName());
        ticket.setTravelDate(request.getTravelDate());
        ticket.setSourceStation(request.getSourceStation());
        ticket.setDestinationStation(request.getDestinationStation());
        ticket.setPrice(request.getPrice());
        ticket.setPaymentStatus(request.isPaymentStatus());
        ticket.setTicketStatus(request.getTicketStatus());
        ticket.setSeatNumber(request.getSeatNumber());
        APIResponse<TicketResponse> response = new APIResponse<>(true, "Ticket updated successfully",
                HttpStatus.OK, toTicketResponse(ticket));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a ticket by ID")
    @DeleteMapping("/{ticket-id}")
    public ResponseEntity<APIResponse<Void>> deleteTicket(@PathVariable("ticket-id") Long ticketId) {
        boolean removed = TICKETS.removeIf(t -> t.getTicketId().equals(ticketId));
        if (!removed) {
            APIResponse<Void> response = new APIResponse<>(false, "Ticket not found",
                    HttpStatus.NOT_FOUND, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        APIResponse<Void> response = new APIResponse<>(true, "Ticket deleted successfully",
                HttpStatus.OK, null);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Bulk create tickets")
    @PostMapping("/bulk")
    public ResponseEntity<APIResponse<List<TicketResponse>>> createMultipleTickets(
            @RequestBody List<TicketRequest> requests) {
        List<TicketResponse> newTickets = new ArrayList<>();
        for (TicketRequest req : requests) {
            Ticket ticket = new Ticket(null, req.getPassengerName(), req.getTravelDate(),
                    req.getSourceStation(), req.getDestinationStation(), req.getPrice(),
                    req.isPaymentStatus(), req.getTicketStatus(), req.getSeatNumber());
            ticket.setTicketId(idGenerator.getAndIncrement());
            TICKETS.add(ticket);
            newTickets.add(toTicketResponse(ticket));
        }
        APIResponse<List<TicketResponse>> response = new APIResponse<>(true, "Multiple tickets created successfully",
                HttpStatus.CREATED, newTickets);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Search tickets by passenger name")
    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<TicketResponse>>> searchByName(@RequestParam String name) {
        List<TicketResponse> responseData = TICKETS.stream()
                .filter(t -> t.getPassengerName().equalsIgnoreCase(name))
                .map(this::toTicketResponse)
                .collect(Collectors.toList());
        APIResponse<List<TicketResponse>> response = new APIResponse<>(true, "Tickets retrieved successfully",
                HttpStatus.OK, responseData);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter tickets by status and travel date")
    @GetMapping("/filter")
    public ResponseEntity<APIResponse<List<TicketResponse>>> filterTickets(
            @Parameter(required = true)
            @RequestParam TicketStatus status,
            @RequestParam() String travelDate) {
        List<TicketResponse> responseData = TICKETS.stream()
                .filter(t -> t.getTicketStatus() == status &&
                        (travelDate == null || t.getTravelDate().equals(LocalDate.parse(travelDate))))
                .map(this::toTicketResponse)
                .collect(Collectors.toList());
        APIResponse<List<TicketResponse>> response = new APIResponse<>(true, "Tickets filtered successfully.",
                HttpStatus.OK, responseData);
        return ResponseEntity.ok(response);
    }

//    @PatchMapping("/payment")
//    public ResponseEntity<APIResponse<Void>> updatePaymentStatus(
//            @RequestParam List<Long> ids, @RequestParam boolean status) {
//        TICKETS.stream()
//                .filter(t -> ids.contains(t.getTicketId()))
//                .forEach(t -> t.setPaymentStatus(status));
//        APIResponse<Void> response = new APIResponse<>(true, "Payment status updated successfully",
//                HttpStatus.OK, null);
//        return ResponseEntity.ok(response);
//    }
}
