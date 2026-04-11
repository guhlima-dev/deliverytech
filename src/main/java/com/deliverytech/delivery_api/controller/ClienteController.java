package com.deliverytech.delivery_api.controller;

import java.util.concurrent.TimeUnit;

import com.deliverytech.delivery_api.dto.requests.ClienteDTO;
import com.deliverytech.delivery_api.dto.responses.ClienteReponseDTO;
import com.deliverytech.delivery_api.dto.responses.PagedResponse;
import com.deliverytech.delivery_api.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Endpoints para gerencimentos de clientes.")
public class ClienteController {
    private ClienteService service;

    public ClienteController ( ClienteService service){
        this.service = service;
    }

    @Operation(summary = "Cadastrar novos clientes.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Cliente castrado com suscesso"),
                    @ApiResponse(responseCode = "404", description = "Erro de validação")
            }
    )
    @PostMapping("/cadastrar")
    public ResponseEntity<ClienteReponseDTO> cadastrar(@Valid @RequestBody ClienteDTO dto ){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @Operation(summary = "Listar clientes ativos.")
    @GetMapping
    public ResponseEntity <PagedResponse<ClienteReponseDTO>>listarAtivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        var pageResult = service.listarAtivos(pageable);
        var pageResponse = new PagedResponse<>(pageResult);

        return ResponseEntity.ok().header("Content-Type", "application/json")
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                .body(pageResponse);
    }

    @Operation(summary = "Buscar cliente pro Id.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes ativos retornado"),
                    @ApiResponse(responseCode = "404", description = "Cliente não foi encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<ClienteReponseDTO>> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok().header("Content-Type", "application/json")
                .body(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.buscarPorId(id)));
    }

    @Operation(summary = "Ativar ou desativar cliente.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Status do cliente modificado"),
                    @ApiResponse(responseCode = "404", description = "Cliente não foi encontrado pelo id")
            }
    )
    @PutMapping("/{id}/inativar-cliente")
    public  ClienteReponseDTO inativar(@PathVariable Long id){
        return service.inativar(id);
    }

    @PutMapping("/{id}/atualizar-dados-clientes")
    public ClienteReponseDTO atualizar(@PathVariable Long id, ClienteDTO dto){
        return service.atualizar(id, dto);
    }

}