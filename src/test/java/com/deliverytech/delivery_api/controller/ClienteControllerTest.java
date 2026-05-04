package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.config.SecurityTestConfig;
import com.deliverytech.delivery_api.dto.requests.ClienteDTO;
import com.deliverytech.delivery_api.dto.responses.ClienteReponseDTO;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.security.JwtAuthenticationFilter;
import com.deliverytech.delivery_api.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ClienteController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@Import(SecurityTestConfig.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ClienteService service;

    private ClienteDTO clienteValido(){
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Gustavo Lima");
        dto.setEmail("gustavo@gmail.com");
        dto.setTelefone("(12)98888-8888");
        dto.setEndereco("Rota 1, Violet City");
        return dto;
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    @DisplayName("Deve retornar 201 quando cadastrar cliente")
    void shouldCadastrarCliente() throws Exception {

        ClienteDTO dto = clienteValido();

        ClienteReponseDTO response = new ClienteReponseDTO();
        response.setNome("Gustavo Lima");

        when(service.cadastrar(any(ClienteDTO.class), eq("teste@gmail.com")))
            .thenReturn(response);

        mockMvc.perform(post("/clientes/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Gustavo Lima"));

    }

    @Test
    @DisplayName("Deve retornar erro 400 quando o DTO for inválido")
    void shouldRetornar400DTOInvalido() throws Exception{

        ClienteDTO dto = new ClienteDTO();

        mockMvc.perform(post("/clientes/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    void shouldRetornarErroAoCadastrarClienteDuplicado() throws Exception {

        ClienteDTO dto = clienteValido();

        when(service.cadastrar(any(ClienteDTO.class),eq("teste@gmail.com")))
                .thenThrow(new BusinessException("Cliente já cadastrado"));

        mockMvc.perform(post("/cliente/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    void shouldDeveListarClientesAtivos() throws Exception{
        ClienteReponseDTO clienteReponseDTO = new ClienteReponseDTO();
        clienteReponseDTO.setNome("Gustavo Oliveira");

        List<ClienteReponseDTO> lista = List.of(clienteReponseDTO);

        Page<ClienteReponseDTO> pageResponse = new PageImpl<>(lista);

        when(service.listarAtivos(any(Pageable.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/clientes")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].nome").value("Gustavo Oliveira"));
    }

    @Test
    @DisplayName("Deve Buscasr Cliente por ID.")
    void shouldBuscarPorId() throws Exception {
        ClienteReponseDTO reponse = new ClienteReponseDTO();
        reponse.setNome("Naruto Uzumaki");

        when(service.buscarPorId(1L)).thenReturn(reponse);

        mockMvc.perform(get("/clientes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("Naruto Uzumaki"));
    }

    @Test
    void shouldRetornar404AoBuscarIdInexistente() throws Exception {
        Long idInexistente = 999L;

        when(service.buscarPorId(idInexistente))
                .thenThrow(new EntityNotFoundException("Cliente não encontrado"));

        mockMvc.perform(get("/clientes/{id}", idInexistente)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

}
