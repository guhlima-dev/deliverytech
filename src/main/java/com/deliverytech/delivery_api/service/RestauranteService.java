package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.requests.RestauranteDTO;
import com.deliverytech.delivery_api.dto.responses.RestauranteResponseDTO;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RestauranteService {
    private final RestauranteRepository repository;
    private final ModelMapper mapper;

    public RestauranteService (RestauranteRepository restaurante, ModelMapper mapper){
        this.repository = restaurante;
        this.mapper = mapper;
    }

    // Service Cadastrar Restaurante
    public RestauranteResponseDTO cadastrar(RestauranteDTO dto){
        if (repository.existsByNome(dto.getNome())){
            throw new BusinessException("Restaurante com esse nome já castrado");
        }
        Restaurante restaurante = mapper.map(dto, Restaurante.class);
        restaurante.setAtivo(true);
        restaurante.setAvaliacao(BigDecimal.ZERO);

        Restaurante salvo = repository.save(restaurante);

        return mapper.map(salvo, RestauranteResponseDTO.class);
    }

    // Service Buscar Restaurante por Id
    public RestauranteResponseDTO buscarPorId(Long id){
        Restaurante restaurante = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Restaurante não encontrado"));
        return mapper.map(restaurante, RestauranteResponseDTO.class);
    }

    public Page<RestauranteResponseDTO> listarAtivos(Pageable pageable){
        return repository.findByAtivoTrue(pageable)
                .map(restaurantes -> mapper.map(restaurantes, RestauranteResponseDTO.class));
    }

    // Service Buscar Restaurante por Categoria
    public Page<RestauranteResponseDTO> buscarPorCategoria(String categoria, Pageable pageable){
        return repository.findByCategoria(categoria)
                .map(restaurantes -> mapper.map(restaurantes, RestauranteResponseDTO.class));
    }
}
