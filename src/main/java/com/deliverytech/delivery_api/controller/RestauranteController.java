package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.requests.RestauranteDTO;
import com.deliverytech.delivery_api.dto.responses.PagedResponse;
import com.deliverytech.delivery_api.dto.responses.RestauranteResponseDTO;
import com.deliverytech.delivery_api.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private RestauranteService service;

    public RestauranteController(RestauranteService service){
        this.service = service;
    }

    @Operation(summary = "Cadastrar novos restaurantes.")
    @PostMapping("/cadastrar")
    public ResponseEntity<RestauranteResponseDTO> cadastrar(@Valid @RequestBody RestauranteDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @Operation(summary = "Listar restaurantes por categoria.")
    @GetMapping("/")
    public ResponseEntity<PagedResponse<RestauranteResponseDTO>> buscarPorCategoria(
            @RequestParam String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var pageResult = service.buscarPorCategoria(categoria, pageable);
        var response = new PagedResponse<>(pageResult);

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
    }


}
