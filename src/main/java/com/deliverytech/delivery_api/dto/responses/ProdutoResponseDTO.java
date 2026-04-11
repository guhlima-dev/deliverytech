package com.deliverytech.delivery_api.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoResponseDTO {

    private Long id;
    private String nome;

    private String descricao;

    private String categoria;

    private BigDecimal preco;

    private boolean disponivel;

    private Long restauranteId;
}
