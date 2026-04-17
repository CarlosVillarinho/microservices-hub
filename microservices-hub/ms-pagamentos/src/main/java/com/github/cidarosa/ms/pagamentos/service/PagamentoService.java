package com.github.cidarosa.ms.pagamentos.service;

import com.github.cidarosa.ms.pagamentos.dto.PagamentoDTO;
import com.github.cidarosa.ms.pagamentos.entities.Pagamento;
import com.github.cidarosa.ms.pagamentos.entities.Status;
import com.github.cidarosa.ms.pagamentos.exceptions.ResourceNotFoundException;
import com.github.cidarosa.ms.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    //METODOS ->
    @Transactional(readOnly = true)
    public List<PagamentoDTO> findAllPagamentos(){
        List<Pagamento> pagamentos = pagamentoRepository.findAll();

        return pagamentos.stream().map(PagamentoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public PagamentoDTO findPagamentoId(Long id){
        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(
                ()-> new ResolutionException("Recurso nao encontrado. ID: " + id));

        return new PagamentoDTO(pagamento);
    }

    @Transactional
    public PagamentoDTO savePagamento(PagamentoDTO pagamentoDTO){
        Pagamento pagamento = new Pagamento();
        mapperDtoToPagamento(pagamentoDTO, pagamento);
        pagamento.setStatus(Status.CRIADO);
        pagamento = pagamentoRepository.save(pagamento);

        return new PagamentoDTO(pagamento);
    }

    private void mapperDtoToPagamento(PagamentoDTO pagamentoDTO, Pagamento pagamento) {
        pagamento.setValor(pagamentoDTO.getValor());
        pagamento.setNome(pagamentoDTO.getNome());
        pagamento.setNumeroCartao(pagamentoDTO.getNumeroCartao());
        pagamento.setValidade(pagamentoDTO.getValidade());
        pagamento.setCodigoSeguranca(pagamentoDTO.getCodigoSeguranca());
        pagamento.setPedidoId(pagamentoDTO.getPedidoId());
    }

    @Transactional
    public PagamentoDTO updatePagamento(Long id, PagamentoDTO pagamentoDTO){
        try{
            Pagamento pagamento = pagamentoRepository.getReferenceById(id);
            mapperDtoToPagamento(pagamentoDTO, pagamento);
            pagamento.setStatus(pagamentoDTO.getStatus());
            pagamento = pagamentoRepository.save(pagamento);
            return new PagamentoDTO(pagamento);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado: " + id);
        }
    }

    @Transactional
    public void deletePagamentoById(Long id){
        if (!pagamentoRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado: " + id);
        }
        pagamentoRepository.deleteById(id);
    }
}
