package com.github.cidarosa.ms.pedidos.controller;

import com.github.cidarosa.ms.pedidos.dto.PedidoDto;
import com.github.cidarosa.ms.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    //METODOS ->

    //BUSCA TODOS OS PEDIDOS
    @GetMapping
    private ResponseEntity<List<PedidoDto>> getAllPedidos(){
        List<PedidoDto> list = pedidoService.findAllPedidos();

        return ResponseEntity.ok(list);
    }

    //BUSCA TODOS OS PEDIDOS PELO ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> getPedido(@PathVariable Long id){
        PedidoDto pedidoDto = pedidoService.findPedidoById(id);

        return ResponseEntity.ok(pedidoDto);
    }

    //CRIA UM PEDIDO
    @PostMapping
    public ResponseEntity<PedidoDto> createPedido(@RequestBody @Valid PedidoDto pedidoDto){
        pedidoDto = pedidoService.savePedido(pedidoDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(pedidoDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(pedidoDto);
    }

    //ATUALIZA UM PEDIDO
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDto> updatePedido(@PathVariable Long id,
                                                  @Valid @RequestBody PedidoDto pedidoDto){

        pedidoDto = pedidoService.updatePedido(id, pedidoDto);

        return ResponseEntity.ok(pedidoDto);
    }

    //DELETA UM PEDIDO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id){

        pedidoService.deletePedidoById(id);
        
        return ResponseEntity.noContent().build();
    }
}
