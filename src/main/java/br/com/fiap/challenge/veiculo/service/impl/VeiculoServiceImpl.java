package br.com.fiap.challenge.veiculo.service.impl;

import br.com.fiap.challenge.veiculo.model.VeiculoModel;
import br.com.fiap.challenge.veiculo.repository.VeiculoRepository;
import br.com.fiap.challenge.veiculo.service.VeiculoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public VeiculoServiceImpl(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @Override
    public VeiculoModel salvarVeiculo(VeiculoModel veiculoModel) {
        return veiculoRepository.save(veiculoModel);
    }

    @Override
    public Page<VeiculoModel> listarTodosOsVeiculos(Specification<VeiculoModel> spec, Pageable pageable) {
        return veiculoRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<VeiculoModel> buscarVeiculoPorId(UUID veiculoId) {
        return veiculoRepository.findById(veiculoId);
    }

    @Override
    public void deletarVeiculo(UUID veiculoId) {
        veiculoRepository.deleteById(veiculoId);
    }
}