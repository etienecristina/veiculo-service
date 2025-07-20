package br.com.fiap.challenge.veiculo.repository;

import br.com.fiap.challenge.veiculo.model.VeiculoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VeiculoRepository extends JpaRepository<VeiculoModel, UUID>, JpaSpecificationExecutor<VeiculoModel> {
}