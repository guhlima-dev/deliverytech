package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {
    private ClienteRepository repository;

    public ClienteService(ClienteRepository repository){
        this.repository = repository;
    }

    public Cliente cadastrar(Cliente cliente){
        if(repository.existsByEmail(cliente.getEmail())){
            throw new IllegalArgumentException("Email já cadastrado");
        }
        cliente.setAtivo(true);
        return repository.save(cliente);
    }

    public List<Cliente> listarAtivos(){
        return repository.findByAtivoTrue();
    }

}
