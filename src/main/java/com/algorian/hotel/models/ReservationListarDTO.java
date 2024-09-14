package com.algorian.hotel.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationListarDTO {

    @NotNull
    private Long id;

    private LocalDate dateReservation;

    @NotNull
    private LocalDate dateStart;

    @NotNull
    private LocalDate dateEnd;

    private String userName;

    private String serviceDescription;

}
