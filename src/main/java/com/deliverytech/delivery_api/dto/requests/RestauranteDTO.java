package com.deliverytech.delivery_api.dto.requests;

import com.deliverytech.delivery_api.validation.ValidCategoria;
import com.deliverytech.delivery_api.validation.ValidTelefone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteDTO {

    @NotBlank(message = "Nome do restaurante é obrigatório")
    @Size(min = 1 ,max = 100)
    private String nome;

    @NotBlank(message = "Categoria é obrigatória")
    @ValidCategoria
    private String categoria;

    @Size(min = 5, max = 255, message = "Endereço deve ter entre 5 a 255 caracteres")
    private String endereco;

    @NotBlank(message = "Telefone é obrigatório")
    @ValidTelefone
    private String telefone;

    @NotNull(message = "A taxa de entrega é obrigatória")
    private BigDecimal taxaEntrega;
}
