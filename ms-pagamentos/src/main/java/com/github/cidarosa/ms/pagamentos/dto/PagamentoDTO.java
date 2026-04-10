package com.github.cidarosa.ms.pagamentos.dto;

import com.github.cidarosa.ms.pagamentos.entities.Pagamento;
import com.github.cidarosa.ms.pagamentos.entities.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PagamentoDTO {
    //ATRIBUTOS
    private Long id;

    @NotNull(message = "o campo valor é obrigatorio")
    @Positive(message = "o campo valor deve ser um numero positivo")
    private BigDecimal valor;

    @NotBlank(message = "o campo nome é obrigatorio")
    @Size(min = 3, max = 50, message = "o campo nome deve ter entre 3 a 50 caracteres")
    private String nome;

    @NotBlank(message = "o numero do cartao é obrigatorio")
    @Size(min = 16, max = 16, message = "o numero do cartao deve ter 16 caracteres")
    private String numeroCartao;

    @NotBlank(message = "o campo validade é obrigatorio")
    @Size(min = 5, max = 5, message = "Validade do cartao deve ter 5 caracteres")
    private String validade;

    @NotBlank(message = "o codigo de seguranca é obrigatorio")
    @Size(min = 3, max = 3, message = "Codigo de seguranca deve ter 3 caracteres")
    private String codigoSeguranca;

    private Status status;

    @NotNull(message = "o campo ID do pedido é obrigatorio")
    @Positive(message = "o id deve ser um valor possitivo")
    private Long pedidoId;

    public PagamentoDTO(Pagamento pagamento){
        id = pagamento.getId();
        valor = pagamento.getValor();
        nome = pagamento.getNome();
        numeroCartao = pagamento.getNumeroCartao();
        validade = pagamento.getValidade();
        codigoSeguranca = pagamento.getCodigoSeguranca();
        status = pagamento.getStatus();

        pedidoId = pagamento.getPedidoId();
    }

}
