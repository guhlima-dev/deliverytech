package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.requests.ClienteDTO;
import com.deliverytech.delivery_api.dto.responses.ClienteReponseDTO;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.ClienteRepository;

import com.deliverytech.delivery_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper mapper;

    public ClienteService(ClienteRepository repository, ModelMapper mapper, UsuarioRepository usuarioRepository){
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ClienteReponseDTO cadastrar(ClienteDTO dto, String email){
        if(email != null){
            throw new BusinessException("E-mail já cadastrado como Cliente.");
        }

        Usuario usuarioLogado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário autenticado não encontrado no BD"));

        if (usuarioLogado.getRole() != Role.CLIENTE && usuarioLogado.getRole() != Role.ADMIN){
            throw new BusinessException("Apenas CLIENTE ou ADMIN podem cadastrar um perfil de cliente");
        }

        Cliente cliente = mapper.map(dto, Cliente.class);
        cliente.setAtivo(true);
        cliente.setEmail(usuarioLogado.getEmail());
        Cliente salvo = repository.save(cliente);

        return mapper.map(salvo, ClienteReponseDTO.class);
    }

    public Page<ClienteReponseDTO> listarAtivos(Pageable pageable){
        return repository.findByAtivoTrue(pageable)
                .map(clientes -> mapper.map(clientes, ClienteReponseDTO.class));
    }

    public ClienteReponseDTO buscarPorId(Long id){
         Cliente cliente = repository.findById(id)
                 .orElseThrow(()-> new EntityNotFoundException("Cliente não encontrado"));

         return mapper.map(cliente, ClienteReponseDTO.class);
    }

    public ClienteReponseDTO inativar(Long id){
        Cliente cliente = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Cliente não encontrado"));
        cliente.setAtivo(!cliente.isAtivo());
        Cliente salvo = repository.save(cliente);

        return mapper.map(salvo, ClienteReponseDTO.class);
    }

    public ClienteReponseDTO atualizar(long id, ClienteDTO dto){
        Cliente cliente = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Cliente não encontrado"));

        cliente.setNome(dto.getNome());
        cliente.setEndereco(dto.getEndereco());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());

        Cliente salvo = repository.save(cliente);

        return mapper.map(salvo, ClienteReponseDTO.class);
    }

}
