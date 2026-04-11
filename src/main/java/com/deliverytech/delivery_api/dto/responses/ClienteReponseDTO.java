package com.deliverytech.delivery_api.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteReponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private Boolean ativo;

    public Boolean isAtivo(){
        return ativo;
    }
}
