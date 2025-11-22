package com.cabbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    @Email(message = "Username should be email format")
    @Column(name = "username" , unique = true,nullable = false)
    private String username;
    @Column(name = "password" , nullable = false, length = 255)
    private String password;
    @Column(name = "display_name", nullable = false , length = 30)
    private String displayName;
    @Column(name = "mobile_number", nullable = false,length = 15 , unique = true)
    private String mobileNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "status" , nullable = false)
    private  AdminStatus status = AdminStatus.ACTIVE;
}
