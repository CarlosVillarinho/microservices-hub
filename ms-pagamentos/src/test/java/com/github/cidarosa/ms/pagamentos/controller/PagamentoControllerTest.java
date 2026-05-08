package com.github.cidarosa.ms.pagamentos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cidarosa.ms.pagamentos.dto.PagamentoDTO;
import com.github.cidarosa.ms.pagamentos.entities.Pagamento;
import com.github.cidarosa.ms.pagamentos.exceptions.ResourceNotFoundException;
import com.github.cidarosa.ms.pagamentos.service.PagamentoService;
import com.github.cidarosa.ms.pagamentos.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.module.ResolutionException;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagamentoController.class)
public class PagamentoControllerTest {

    //Atributos
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private PagamentoService pagamentoService;
    private Pagamento pagamento;
    private Long existingId, noExistingId;

    @BeforeEach
    void setUp(){
        existingId = 1l;
        noExistingId = Long.MAX_VALUE;
        pagamento = Factory.createPagamento();
    }

    @Test
    void findAllPagamentosShouldReturnListPagamentoDTO() throws Exception{
        PagamentoDTO inputDto = new PagamentoDTO(pagamento);
        List<PagamentoDTO> list = List.of(inputDto);
        Mockito.when(pagamentoService.findAllPagamentos()).thenReturn(list);
        //act + Assert
        ResultActions result = mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON)); //request: Accept
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$[0].id").value(pagamento.getId()));
        result.andExpect(jsonPath("$[0].valor").value(pagamento.getValor().doubleValue()));
        //verify (comportamento)
        Mockito.verify(pagamentoService).findAllPagamentos();
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

    @Test
    void createPagamentoShouldReturn201WhenValid() throws Exception{
        PagamentoDTO requestDTO = new PagamentoDTO(Factory.createPagamentoSemId());
        //Bean objectMapper para converter JAVA para JSON
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);
        PagamentoDTO responseDTO = new PagamentoDTO(pagamento);
        Mockito.when(pagamentoService.savePagamento(any(PagamentoDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/pagamentos")
                .contentType(MediaType.APPLICATION_JSON) //request Content=Type
                .accept(MediaType.APPLICATION_JSON) //request Accept
                .content(jsonRequestBody)) //request body
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) //RESPONSE
                .andExpect(jsonPath("$.id").value(pagamento.getId()))
                .andExpect(jsonPath("$.status").value(pagamento.getStatus().name()))
                .andExpect(jsonPath("$.valor").value(pagamento.getValor().doubleValue()))
                .andExpect(jsonPath("$.pedidoId").value(pagamento.getPedidoId()));

        Mockito.verify(pagamentoService).savePagamento(any(PagamentoDTO.class));
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

    @Test
    void deletePagamentoShouldReturn204WhenIdExists() throws Exception{
        Mockito.doNothing().when(pagamentoService).deletePagamentoById(existingId);

        mockMvc.perform(delete("/pagamentos/{id}", existingId))
                .andExpect(status().isNoContent());

        Mockito.verify(pagamentoService).deletePagamentoById(existingId);
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

    @Test
    void deletePagamentoShouldReturn404WhenIdDoesNotExists() throws Exception{
        Mockito.doThrow(new ResourceNotFoundException("Recurso não encontrado. ID: " + noExistingId))
                .when(pagamentoService).deletePagamentoById(noExistingId);

        mockMvc.perform(delete("/pagamentos/{id}", noExistingId))
                .andExpect(status().isNotFound());

        Mockito.verify(pagamentoService).deletePagamentoById(noExistingId);
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

    @Test
    void findPagamentoByIdShouldReturnPagamentoDTOWhenIdExists() throws Exception{
        PagamentoDTO responseDTO = new PagamentoDTO(pagamento);
        Mockito.when(pagamentoService.findPagamentoId(existingId)).thenReturn(responseDTO);

        mockMvc.perform(get("/pagamentos/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)) //request Accept
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.valor").value(pagamento.getValor().doubleValue()))
                .andExpect(jsonPath("$.status").value(pagamento.getStatus().name()))
                .andExpect(jsonPath("$.pedidoId").value(pagamento.getPedidoId()));

        Mockito.verify(pagamentoService).findPagamentoId(existingId);
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

    @Test
    void findPagamentoByIdShouldReturn404WhenIdDoesNotExists() throws Exception{
        Mockito.when(pagamentoService.findPagamentoId(noExistingId))
                .thenThrow(new ResourceNotFoundException("Recurso não encontrado. ID: " + noExistingId));

        mockMvc.perform(get("/pagamentos/{id}", noExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

        Mockito.verify(pagamentoService).findPagamentoId(noExistingId);
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

    @Test
    void createPagamentoShouldReturn422WhenInvalid() throws Exception{
        Pagamento pagamentoInvalido = Factory.createPagamentoSemId();
        pagamentoInvalido.setValor(BigDecimal.valueOf(0));
        pagamentoInvalido.setNome(null);
        PagamentoDTO requestDTO = new PagamentoDTO(pagamentoInvalido);
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);
        PagamentoDTO responseDTO = new PagamentoDTO(pagamentoInvalido);

        Mockito.when(pagamentoService.savePagamento(any(PagamentoDTO.class))).thenReturn(requestDTO);

        mockMvc.perform(post("/pagamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        Mockito.verifyNoInteractions(pagamentoService);
    }

    @Test
    void updatePagamentoShouldReturn200WhenValid() throws Exception{
        PagamentoDTO requestDTO = new PagamentoDTO(Factory.createPagamento());
        String jsonRepository = objectMapper.writeValueAsString(requestDTO);
        PagamentoDTO responseDTO = new PagamentoDTO(pagamento);
        Mockito.when(pagamentoService.updatePagamento(eq(existingId), any(PagamentoDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/pagamentos/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRepository))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.status").value(pagamento.getStatus().name()))
                .andExpect(jsonPath("$.pedidoId").value(pagamento.getPedidoId()));

        Mockito.verify(pagamentoService).updatePagamento(eq(existingId), any(PagamentoDTO.class));
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

    @Test
    void updatePagamentoShouldReturn422WhenInvalid() throws Exception{
        Pagamento pagamentoInvalido = Factory.createPagamento();
        pagamentoInvalido.setValor(BigDecimal.ZERO);
        pagamentoInvalido.setNome(null);
        PagamentoDTO requestDTO = new PagamentoDTO(pagamentoInvalido);
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(put("/pagamentos/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());

        Mockito.verifyNoMoreInteractions(pagamentoService);
    }

    @Test
    void updatePagamentoShouldReturn404WhenIdDoesNotExists() throws Exception{
        PagamentoDTO requestDTO = new PagamentoDTO(Factory.createPagamentoSemId());
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);
        Mockito.when(pagamentoService.updatePagamento(eq(noExistingId), any(PagamentoDTO.class)))
                        .thenThrow(new ResourceNotFoundException("Recurso não encontrado. ID: " + noExistingId));

        mockMvc.perform(put("/pagamentos/{id}", noExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isNotFound())
                .andDo(print());

        Mockito.verify(pagamentoService).updatePagamento(eq(noExistingId), any(PagamentoDTO.class));
        Mockito.verifyNoMoreInteractions(pagamentoService);
    }
}
