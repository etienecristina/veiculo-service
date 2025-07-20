package br.com.fiap.challenge.veiculo.service;

import br.com.fiap.challenge.veiculo.model.VeiculoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface VeiculoService {

    VeiculoModel salvarVeiculo(VeiculoModel veiculoModel);

    Page<VeiculoModel> listarTodosOsVeiculos(Specification<VeiculoModel> spec, Pageable pageable);

    Optional<VeiculoModel> buscarVeiculoPorId(UUID veiculoId);

    void deletarVeiculo(UUID veiculoId);

}
