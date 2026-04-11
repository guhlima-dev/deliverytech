package com.deliverytech.delivery_api.repository;

import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery_api.model.Restaurante;

import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    Optional<Restaurante> findByNome(String nome);
    Page<Restaurante> findByCategoria(String categoria);
    Page<Restaurante> findByAtivoTrue(Pageable pageable);
    boolean existsByNome(@NotBlank(message = "Nome do restaurante é obrigatório") String nome);
}