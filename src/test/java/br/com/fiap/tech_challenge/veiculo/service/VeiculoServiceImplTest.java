package br.com.fiap.tech_challenge.veiculo.service;


import br.com.fiap.challenge.veiculo.model.VeiculoModel;
import br.com.fiap.challenge.veiculo.repository.VeiculoRepository;
import br.com.fiap.challenge.veiculo.service.impl.VeiculoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VeiculoServiceImplTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private VeiculoServiceImpl veiculoService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve salvar um veículo com sucesso")
    void salvarVeiculo() {
        VeiculoModel veiculo = new VeiculoModel();
        veiculo.setMarca("Toyota");
        veiculo.setPreco(50000f);

        when(veiculoRepository.save(veiculo)).thenReturn(veiculo);

        VeiculoModel result = veiculoService.salvarVeiculo(veiculo);

        assertNotNull(result);
        assertEquals("Toyota", result.getMarca());
        verify(veiculoRepository, times(1)).save(veiculo);
    }

    @Test
    @DisplayName("Deve listar veículos com paginação e specification")
    void listarTodosOsVeiculos() {
        Pageable pageable = PageRequest.of(0, 10);
        Specification<VeiculoModel> spec = (root, query, criteriaBuilder) -> null;

        List<VeiculoModel> veiculos = List.of(new VeiculoModel(), new VeiculoModel());
        Page<VeiculoModel> page = new PageImpl<>(veiculos, pageable, veiculos.size());

        when(veiculoRepository.findAll(spec, pageable)).thenReturn(page);

        Page<VeiculoModel> result = veiculoService.listarTodosOsVeiculos(spec, pageable);

        assertEquals(2, result.getContent().size());
        verify(veiculoRepository, times(1)).findAll(spec, pageable);
    }

    @Test
    @DisplayName("Deve buscar veículo por ID existente")
    void buscarVeiculoPorId_existente() {
        UUID id = UUID.randomUUID();
        VeiculoModel veiculo = new VeiculoModel();
        veiculo.setVeiculoId(id);

        when(veiculoRepository.findById(id)).thenReturn(Optional.of(veiculo));

        Optional<VeiculoModel> result = veiculoService.buscarVeiculoPorId(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getVeiculoId());
        verify(veiculoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty quando veículo não existir")
    void buscarVeiculoPorId_inexistente() {
        UUID id = UUID.randomUUID();

        when(veiculoRepository.findById(id)).thenReturn(Optional.empty());

        Optional<VeiculoModel> result = veiculoService.buscarVeiculoPorId(id);

        assertTrue(result.isEmpty());
        verify(veiculoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve deletar veículo por ID")
    void deletarVeiculo() {
        UUID id = UUID.randomUUID();

        doNothing().when(veiculoRepository).deleteById(id);

        veiculoService.deletarVeiculo(id);

        verify(veiculoRepository, times(1)).deleteById(id);
    }
}

