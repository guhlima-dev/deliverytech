package com.deliverytech.delivery_api.dto.requests;

import com.deliverytech.delivery_api.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    private Role role;
}
