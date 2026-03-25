package com.github.cidarosa.ms.pedidos.controller;

import com.github.cidarosa.ms.pedidos.dto.PedidoDto;
import com.github.cidarosa.ms.pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    private ResponseEntity<List<PedidoDto>> getAllPedidos(){

        List<PedidoDto> list = pedidoService.findAllPedidos();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> getPedido(@PathVariable Long id){

        PedidoDto pedidoDto = pedidoService.findPedidoById(id);

        return ResponseEntity.ok(pedidoDto);
    }



}
