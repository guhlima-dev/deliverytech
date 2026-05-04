package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.requests.ClienteDTO;
import com.deliverytech.delivery_api.dto.responses.ClienteReponseDTO;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceIT {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteDTO dto;
    private Usuario usuario;
    private Cliente cliente;
    private ClienteReponseDTO clienteReponseDTO;

    @BeforeEach
    void setup() {
        dto = new ClienteDTO("Son Goku", "goku@gmail.com", "(12)99999-9999", "Rua 1, Cell");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("goku@gmail.com");
        usuario.setRole(Role.CLIENTE);
        usuario.setAtivo(true);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setUsuario(usuario);
        cliente.setEmail(usuario.getEmail());
        cliente.setAtivo(true);

        clienteReponseDTO = new ClienteReponseDTO();
        clienteReponseDTO.setId(1L);
        clienteReponseDTO.setEmail("goku@gmail.com");

    }

    @Test
    void shouldCadastrarClienteComSucesso() {
        when(usuarioRepository.findByEmail("goku@gmail.com")).thenReturn(Optional.of(usuario));
        when(clienteRepository.existsByUsuario_Id(1L)).thenReturn(false);
        when(mapper.map(dto, Cliente.class)).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(mapper.map(cliente, ClienteReponseDTO.class)).thenReturn(clienteReponseDTO);

        ClienteReponseDTO resultado = clienteService.cadastrar(dto, "goku@gmail.com");

        assertNotNull(resultado);
        assertEquals("goku@gmail.com", resultado.getEmail());
        verify(clienteRepository).save(any());
    }

    @Test
    void shouldExcecaoQuandoClienteJaExistir() {
        when(usuarioRepository.findByEmail("goku@gmail.com")).thenReturn(Optional.of(usuario));
        when(clienteRepository.existsByUsuario_Id(1L)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            clienteService.cadastrar(dto, "goku@gmail.com");
        });
    }

    @Test
    void shouldListarClientesAtivos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> page = new PageImpl<>(List.of(cliente));

        when(clienteRepository.findByAtivoTrue(pageable)).thenReturn(page);
        when(mapper.map(any(Cliente.class), eq(ClienteReponseDTO.class))).thenReturn(clienteReponseDTO);

        Page<ClienteReponseDTO> resultado = clienteService.listarAtivos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(mapper, atLeastOnce()).map(any(Cliente.class), eq(ClienteReponseDTO.class));
    }

    @Test
    void shouldBuscarClientePorIdComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mapper.map(cliente, ClienteReponseDTO.class)).thenReturn(clienteReponseDTO);

        ClienteReponseDTO resultado = clienteService.buscarPorId(1L);
        assertNotNull(resultado);
    }

    @Test
    void shouldLancarExcecaoQuandoClienteNaoEncontrado(){
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, ()->{
            clienteService.buscarPorId(1L);
        });
    }

    @Test
    void shouldInativarOuAtivarCliente(){
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenReturn(cliente);
        when(mapper.map(cliente, ClienteReponseDTO.class)).thenReturn(clienteReponseDTO);

        ClienteReponseDTO resultado = clienteService.inativar(1L);

        assertNotNull(resultado);
        verify(clienteRepository).save(any());
    }

}