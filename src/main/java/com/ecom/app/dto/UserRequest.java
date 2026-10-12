package com.ecom.app.dto;

import com.ecom.app.model.Address;
import com.ecom.app.model.UserRole;
import lombok.Data;

@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private UserRole role;
    private AddressDTO address;


}
