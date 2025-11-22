package com.cabbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "driver")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "driver_id" , nullable = false )
    private Long driverId;
    @NotBlank(message = "Username is mandatory")
    @Email(message = "Username must be a valid email")
    @Column(name = "username" ,unique = true, nullable = false)
    private String username;
    @Column(name = "password" , unique = true , nullable = false)
    private String password;
    @Column(name = "display_name" , unique = true , nullable = false)
    @Size(min =  3,max = 20, message = "Display name should be in between 3 to 20" )
    private String displayName;
    @NotBlank(message = "Mobile Number is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile Number must be 10 digits")
    private String mobileNumber;
    @NotNull(message = "Driver status is required")
    @Enumerated(EnumType.STRING)
    private DriverStatus status ;
    @NotBlank(message = "Cab Type is mandatory")
    private String cabType;
    @NotBlank(message = "License Number is mandatory")
    private String licenseNumber;
    @NotBlank(message = "Cab Number is mandatory")
    private String cabNumber;
    @NotNull(message = "City is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity city;
}
