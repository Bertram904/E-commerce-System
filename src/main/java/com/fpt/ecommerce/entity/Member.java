package com.fpt.ecommerce.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tblMember")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, length = 50)
    String username;

    @Column(nullable = false)
    String password;

    @Column(length = 255)
    String address;

    @Column(name = "dob", nullable = false)
    LocalDate dob;

    @Column(length = 25)
    String phoneNumber;

    @Column(nullable = false, length = 50)
    String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_member_role",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    Date updatedAt;
}
