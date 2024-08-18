package org.dessert.moah.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dessert.moah.common.entity.BaseTime;
import org.dessert.moah.user.type.UserRoleEnum;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class Users extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    @Builder
    public Users(String name, String password, String email, String phoneNumber, UserRoleEnum role, String address) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.address = address;

    }


   public void UpdateUserInfo(String phoneNumber, String address){
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
