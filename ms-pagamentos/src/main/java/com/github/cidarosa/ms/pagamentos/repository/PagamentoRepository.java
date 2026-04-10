package com.github.cidarosa.ms.pagamentos.repository;

import com.github.cidarosa.ms.pagamentos.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
