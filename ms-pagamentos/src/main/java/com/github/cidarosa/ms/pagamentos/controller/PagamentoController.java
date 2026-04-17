package com.github.cidarosa.ms.pagamentos.controller;


import com.github.cidarosa.ms.pagamentos.dto.PagamentoDTO;
import com.github.cidarosa.ms.pagamentos.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    //METODOS ->

    //BUSCA TODOS OS PAGAMENTOS
    @GetMapping
    public ResponseEntity<List<PagamentoDTO>> getAll(){
        List<PagamentoDTO>pagamentoDTOS = pagamentoService.findAllPagamentos();

        return ResponseEntity.ok(pagamentoDTOS);
    }

    //BUSCA TODOS OS PAGAMENTOS PELO ID
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> getById(@PathVariable Long id){
        PagamentoDTO pagamentoDTO = pagamentoService.findPagamentoId(id);

        return ResponseEntity.ok(pagamentoDTO);

    }

    //CRIA UM PAGAMENTO
    @PostMapping
    private ResponseEntity<PagamentoDTO> createPagamento(@RequestBody @Valid PagamentoDTO pagamentoDTO){
        pagamentoDTO = pagamentoService.savePagamento(pagamentoDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(pagamentoDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(pagamentoDTO);
    }

    //ATUALIZA UM PAGAMENTO
    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> updatePagamento(@PathVariable Long id, @Valid @RequestBody PagamentoDTO pagamentoDTO){
        pagamentoDTO = pagamentoService.updatePagamento(id, pagamentoDTO);

        return ResponseEntity.ok(pagamentoDTO);
    }

    //DELETA UM PAGAMENTO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePagamento(@PathVariable Long id){
        pagamentoService.deletePagamentoById(id);

        return ResponseEntity.noContent().build();
    }
}
