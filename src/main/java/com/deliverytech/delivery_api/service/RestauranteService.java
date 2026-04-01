package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository repository;

    public Restaurante cadastrar(Restaurante restaurante){
        if (repository.existsByNome(restaurante.getNome())){
            throw new IllegalArgumentException("Restaurante já cadastrado");
        }
        restaurante.setAtivo(true);
        return repository.save(restaurante);
    }

    public List<Restaurante> listarAtivos(){
        return repository.findByAtivoTrue();
    }

    public Restaurante buscarPorId(Long id){
        return repository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Restaurante não existe"));
    }

    public void inativar(Long id){
        Restaurante restaurante = buscarPorId(id);
        restaurante.setAtivo(false);
        repository.save(restaurante);
    }

    public void ativar(Long id){
        Restaurante restaurante = buscarPorId(id);
        restaurante.setAtivo(true);
        repository.save(restaurante);
    }

    public Restaurante atualizar(long id, Restaurante dados){
        Restaurante restaurante = buscarPorId(id);
        restaurante.setNome(dados.getNome());
        restaurante.setCategoria(dados.getCategoria());
        restaurante.setEndereco(dados.getEndereco());
        restaurante.setTelefone(dados.getTelefone());
        return repository.save(restaurante);
    }
}
