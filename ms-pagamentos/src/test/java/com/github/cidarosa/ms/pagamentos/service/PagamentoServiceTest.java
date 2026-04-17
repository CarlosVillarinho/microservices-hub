package com.github.cidarosa.ms.pagamentos.service;

import com.github.cidarosa.ms.pagamentos.dto.PagamentoDTO;
import com.github.cidarosa.ms.pagamentos.entities.Pagamento;
import com.github.cidarosa.ms.pagamentos.exceptions.ResourceNotFoundException;
import com.github.cidarosa.ms.pagamentos.repository.PagamentoRepository;
import com.github.cidarosa.ms.pagamentos.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    //ATRIBUTOS
    @Mock
    private PagamentoRepository pagamentoRepository;
    @InjectMocks
    private PagamentoService pagamentoService;

    private Long existingId;
    private Long noExistingId;
    private Pagamento pagamento;

    //METODOS -->
    @BeforeEach
    void setUp(){
        existingId = 1L;
        noExistingId = Long.MAX_VALUE;

        pagamento = Factory.createPagamento();
    }

    //deletes...
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

    //finds...
    @Test
    void findPagamentoByIdShouldReturnPagamentoDTOWhenIdExists(){
        //arrange
        Mockito.when(pagamentoRepository.findById(existingId))
                .thenReturn(Optional.of(pagamento));

        //act
        PagamentoDTO result = pagamentoService.findPagamentoId(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());

        Mockito.verify(pagamentoRepository).findById(existingId);
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    void findPagamentoByIdShouldTrrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Mockito.when(pagamentoRepository.findById(noExistingId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pagamentoService.findPagamentoId(noExistingId));

        Mockito.verify(pagamentoRepository.findById(noExistingId));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    @DisplayName("Dado parâmetros válidos e Id nulo" +
                 "quando chamar Salvar Pagamento" +
                 "então deve gerar Id e persistir um Pagamento")
    void givenValidParamsAndIdIsNull_whenSave_thenShouldPersistPagamento(){
        //arrange
        Mockito.when(pagamentoRepository.save(any(Pagamento.class)))
                .thenReturn(pagamento);
        pagamento.setId(null);
        PagamentoDTO inputDTO = new PagamentoDTO(pagamento);
        //act
        PagamentoDTO result = pagamentoService.savePagamento(inputDTO);
        //assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());
        //verify
        Mockito.verify(pagamentoRepository).save(any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    //updates...
    @Test
    void updatePagamentoShoudReturnPagamentoDTOWhenIdExists(){
       //arrange
       Long id = pagamento.getId();
       Mockito.when(pagamentoRepository.getReferenceById(id)).thenReturn(pagamento);
       Mockito.when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);
       //act
        PagamentoDTO result = pagamentoService.updatePagamento(id, new PagamentoDTO(pagamento));
        //assert e Verify
        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());
        Mockito.verify(pagamentoRepository).getReferenceById(id);
        Mockito.verify(pagamentoRepository).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

     @Test
    void updatePagamentoShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Mockito.when(pagamentoRepository.getReferenceById(noExistingId))
                .thenThrow(EntityNotFoundException.class);
        PagamentoDTO inputDto = new PagamentoDTO(pagamento);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pagamentoService.updatePagamento(noExistingId, inputDto));

        Mockito.verify(pagamentoRepository.getReferenceById(noExistingId));
        Mockito.verify(pagamentoRepository, Mockito.never()).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
     }
}

