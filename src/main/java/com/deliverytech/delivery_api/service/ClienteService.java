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

    public Cliente buscarPorId(Long id){
        return repository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Cliente não encontrado"));
    }

    public void inativar(Long id){
        Cliente cliente = buscarPorId(id);
        cliente.setAtivo(false);
        repository.save(cliente);
    }

    public Cliente atualizar(long id, Cliente dados){
        Cliente cliente = buscarPorId(id);
        cliente.setNome(dados.getNome());
        cliente.setEndereco(dados.getEndereco());
        cliente.setTelefone(dados.getTelefone());
        cliente.setEmail(dados.getEmail());
        return repository.save(cliente);
    }

}
