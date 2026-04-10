package com.github.cidarosa.ms.pagamentos.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_pagamento")
public class Pagamento {
    //ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String nome;            //nome do cartao

    @Column(nullable = false, length = 16)
    private String numeroCartao;    //XXXX XXXX XXXX XXXX

    @Column(nullable = false, length = 5)
    private String validade;        //mm/aa

    @Column(nullable = false, length = 3)
    private String codigoSeguranca; //XXX

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false, length = 16)
    private Long pedidoId;
}
