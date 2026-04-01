package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private RestauranteService service;

    public RestauranteController(RestauranteService service){
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Restaurante> cadastrar(@RequestBody Restaurante restaurante){
        return ResponseEntity.status(201).body(service.cadastrar(restaurante));
    }

    @GetMapping
    public List<Restaurante> listarAtivos() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public Restaurante buscarPorId(@PathVariable Long id){
        return service.buscarPorId(id);
    }
}
