package com.cabbooking.dto;

import com.cabbooking.entity.BookingStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long bookingId;
    private UserDTO user;           // ✅ Embed full user info
    private DriverDTO driver;       // ✅ Embed full driver info
    private CityDTO city;           // ✅ Embed city info
    private CityRouteDTO route;     // ✅ Embed route info
    private Double fareAmount;
    private BookingStatus bookingStatus;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String message;
}
