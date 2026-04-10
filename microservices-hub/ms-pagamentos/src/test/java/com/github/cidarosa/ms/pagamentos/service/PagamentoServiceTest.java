package com.github.cidarosa.ms.pagamentos.service;

import com.github.cidarosa.ms.pagamentos.exceptions.ResourceNotFoundException;
import com.github.cidarosa.ms.pagamentos.repository.PagamentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.module.ResolutionException;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Long existingId;
    private Long noExistingId;

    @BeforeEach
    void setUp(){
        existingId = 1L;

        noExistingId = Long.MAX_VALUE;
    }

    @Test
    void deleteByIdShouldDeleteWhenIdExists(){
        Mockito.when(pagamentoRepository.existsById(existingId)).thenReturn(true);

        pagamentoService.deletePagamentoById(existingId);

        Mockito.verify(pagamentoRepository).existsById(existingId);

        Mockito.verify(pagamentoRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    @DisplayName("deletePagamentoByID deveria laçar ResourceNotFoundException quando o Id nao exisitir")
    void deletePagamentoByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Mockito.when(pagamentoRepository.existsById(noExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class,
                ()->{
            pagamentoService.deletePagamentoById(noExistingId);
                });

        Mockito.verify(pagamentoRepository).existsById(noExistingId);

        Mockito.verify(pagamentoRepository,Mockito.never()).deleteById(Mockito.anyLong());
        }
    }

