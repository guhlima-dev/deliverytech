package com.deliverytech.delivery_api.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados para cadastro/atulização de clientes.")
public class ClienteDTO {

    @Schema(description = "Nome do clliente", example = "Gustavo Lima")
    @NotBlank(message = "Campo nome é obriátorio")
    private String nome;

    @Schema(description = "E-mail do clliente", example = "gustavo@email.com")
    @Email
    @NotBlank(message = "Campo de email é obrigátorio")
    private String email;

    @Schema(description = "Telefone/celular do clliente", example = "(xx)xxxxx-xxxx")
    @Pattern(regexp = "^\\(\\d{2}\\)\\d{4,5}-\\d{4}$",
            message = "Formato de telefone invalido. Use (xx)xxxxx-xxxx"
    )
    @NotBlank(message = "Campo de telefone é obrigátorio")
    private String telefone;

    @Schema(description = "Endereço do clliente", example = "Rua Santos, 123")
    @Size(min = 5, message = "Endereço deve ter no minimo 5 caracteres")
    private String endereco;

}
