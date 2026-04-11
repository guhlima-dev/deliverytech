package com.deliverytech.delivery_api.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteResponseDTO {

    private Long id;
    private String nome;
    private String categoria;
    private String endereco;
    private String telefone;
    private BigDecimal avaliacao;
    private BigDecimal taxaEntrega;
    private boolean ativo;
}
