package br.com.fiap.tech_challenge.veiculo.controller;

import br.com.fiap.challenge.veiculo.controller.VeiculoController;
import br.com.fiap.challenge.veiculo.dto.VeiculoDto;
import br.com.fiap.challenge.veiculo.enums.VeiculoStatus;
import br.com.fiap.challenge.veiculo.model.VeiculoModel;
import br.com.fiap.challenge.veiculo.service.VeiculoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VeiculoControllerTest {

    @Mock
    private VeiculoService veiculoService;

    @InjectMocks
    private VeiculoController veiculoController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(veiculoController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve salvar um veículo com sucesso e retornar status 201 Created")
    void salvarVeiculo_Success() throws Exception {
        // GIVEN
        VeiculoDto veiculoDto = VeiculoDto.builder()
                .marca("Ford")
                .modelo("Fusion")
                .ano(2020)
                .cor("Preto")
                .preco(95000.00f)
                .veiculoStatus(VeiculoStatus.DISPONIVEL)
                .build();

        VeiculoModel veiculoModelSalvo = new VeiculoModel();
        veiculoModelSalvo.setVeiculoId(UUID.randomUUID());
        veiculoModelSalvo.setMarca(veiculoDto.getMarca());
        veiculoModelSalvo.setModelo(veiculoDto.getModelo());
        veiculoModelSalvo.setAno(veiculoDto.getAno());
        veiculoModelSalvo.setCor(veiculoDto.getCor());
        veiculoModelSalvo.setPreco(veiculoDto.getPreco());
        veiculoModelSalvo.setVeiculoStatus(veiculoDto.getVeiculoStatus());
        veiculoModelSalvo.setDataDeInclusao(LocalDateTime.now());

        // WHEN
        when(veiculoService.salvarVeiculo(any(VeiculoModel.class))).thenReturn(veiculoModelSalvo);

        // THEN
        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/veiculos/" + veiculoModelSalvo.getVeiculoId()));

        verify(veiculoService, times(1)).salvarVeiculo(any(VeiculoModel.class));
    }

    @Test
    @DisplayName("Não deve salvar um veículo com dados inválidos e retornar status 400 Bad Request")
    void salvarVeiculo_InvalidData() throws Exception {
        // GIVEN
        VeiculoDto veiculoDtoInvalido = VeiculoDto.builder()
                .marca("") // Marca inválida
                .modelo("Fusion")
                .ano(2020)
                .cor("Preto")
                .preco(95000.00f)
                .veiculoStatus(VeiculoStatus.DISPONIVEL)
                .build();

        // WHEN & THEN
        mockMvc.perform(post("/veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoDtoInvalido)))
                .andExpect(status().isBadRequest());

        verify(veiculoService, never()).salvarVeiculo(any(VeiculoModel.class));
    }

    @Test
    @DisplayName("Deve buscar um veículo por ID com sucesso e retornar status 200 OK")
    void buscarVeiculoPorId_Success() throws Exception {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();
        VeiculoModel veiculoModel = new VeiculoModel();
        veiculoModel.setVeiculoId(veiculoId);
        veiculoModel.setMarca("Honda");
        veiculoModel.setModelo("Civic");
        veiculoModel.setAno(2021);
        veiculoModel.setCor("Azul");
        veiculoModel.setPreco(95000.00f);
        veiculoModel.setVeiculoStatus(VeiculoStatus.DISPONIVEL);

        // WHEN
        when(veiculoService.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.of(veiculoModel));

        // THEN
        mockMvc.perform(get("/veiculos/{veiculoId}", veiculoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.veiculoId").value(veiculoId.toString()))
                .andExpect(jsonPath("$.marca").value("Honda"))
                .andExpect(jsonPath("$.modelo").value("Civic"));

        verify(veiculoService, times(1)).buscarVeiculoPorId(veiculoId);
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found quando o veículo por ID não for encontrado")
    void buscarVeiculoPorId_NotFound() throws Exception {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();

        // WHEN
        when(veiculoService.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.empty());

        // THEN
        mockMvc.perform(get("/veiculos/{veiculoId}", veiculoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Veículo não cadastrado"));

        verify(veiculoService, times(1)).buscarVeiculoPorId(veiculoId);
    }

    @Test
    @DisplayName("Deve editar um veículo com sucesso e retornar status 200 OK")
    void editarVeiculo_Success() throws Exception {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();
        VeiculoDto veiculoDtoAtualizado = VeiculoDto.builder()
                .marca("BMW")
                .modelo("X1")
                .ano(2023)
                .cor("Branco")
                .preco(250000.00f)
                .veiculoStatus(VeiculoStatus.DISPONIVEL)
                .build();

        VeiculoModel veiculoModelExistente = new VeiculoModel();
        veiculoModelExistente.setVeiculoId(veiculoId);
        veiculoModelExistente.setMarca("BMW");
        veiculoModelExistente.setModelo("X1");
        veiculoModelExistente.setAno(2020); // Ano antigo
        veiculoModelExistente.setCor("Preto"); // Cor antiga
        veiculoModelExistente.setPreco(200000.00f); // Preço antigo
        veiculoModelExistente.setVeiculoStatus(VeiculoStatus.DISPONIVEL);
        veiculoModelExistente.setDataDeInclusao(LocalDateTime.now().minusDays(1));

        VeiculoModel veiculoModelAtualizado = new VeiculoModel();
        veiculoModelAtualizado.setVeiculoId(veiculoId);
        veiculoModelAtualizado.setMarca(veiculoDtoAtualizado.getMarca());
        veiculoModelAtualizado.setModelo(veiculoDtoAtualizado.getModelo());
        veiculoModelAtualizado.setAno(veiculoDtoAtualizado.getAno());
        veiculoModelAtualizado.setCor(veiculoDtoAtualizado.getCor());
        veiculoModelAtualizado.setPreco(veiculoDtoAtualizado.getPreco());
        veiculoModelAtualizado.setVeiculoStatus(veiculoDtoAtualizado.getVeiculoStatus());
        veiculoModelAtualizado.setDataDeInclusao(veiculoModelExistente.getDataDeInclusao());
        veiculoModelAtualizado.setDataDeAtualizacao(LocalDateTime.now());


        // WHEN
        when(veiculoService.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.of(veiculoModelExistente));
        when(veiculoService.salvarVeiculo(any(VeiculoModel.class))).thenReturn(veiculoModelAtualizado);

        // THEN
        mockMvc.perform(put("/veiculos/{veiculoId}", veiculoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoDtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("BMW"))
                .andExpect(jsonPath("$.ano").value(2023))
                .andExpect(jsonPath("$.preco").value(250000.00));

        verify(veiculoService, times(1)).buscarVeiculoPorId(veiculoId);
        verify(veiculoService, times(1)).salvarVeiculo(any(VeiculoModel.class));
    }

    @Test
    @DisplayName("Não deve editar um veículo inexistente e retornar status 404 Not Found")
    void editarVeiculo_NotFound() throws Exception {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();
        VeiculoDto veiculoDto = VeiculoDto.builder()
                .marca("BMW")
                .modelo("X1")
                .ano(2023)
                .cor("Branco")
                .preco(250000.00f)
                .veiculoStatus(VeiculoStatus.DISPONIVEL)
                .build();

        // WHEN
        when(veiculoService.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.empty());

        // THEN
        mockMvc.perform(put("/veiculos/{veiculoId}", veiculoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Veículo não cadastrado"));

        verify(veiculoService, times(1)).buscarVeiculoPorId(veiculoId);
        verify(veiculoService, never()).salvarVeiculo(any(VeiculoModel.class));
    }

    @Test
    @DisplayName("Não deve editar um veículo com dados inválidos e retornar status 400 Bad Request")
    void editarVeiculo_InvalidData() throws Exception {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();
        VeiculoDto veiculoDtoInvalido = VeiculoDto.builder()
                .marca("Nissan")
                .modelo("") // Modelo inválido
                .ano(2023)
                .cor("Branco")
                .preco(150000.00f)
                .veiculoStatus(VeiculoStatus.DISPONIVEL)
                .build();

        // WHEN & THEN
        mockMvc.perform(put("/veiculos/{veiculoId}", veiculoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(veiculoDtoInvalido)))
                .andExpect(status().isBadRequest());

        verify(veiculoService, never()).buscarVeiculoPorId(any(UUID.class));
        verify(veiculoService, never()).salvarVeiculo(any(VeiculoModel.class));
    }

    @Test
    @DisplayName("Deve vender um veículo com sucesso e retornar status 200 OK")
    void venderVeiculo_Success() throws Exception {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();
        VeiculoModel veiculoModel = new VeiculoModel();
        veiculoModel.setVeiculoId(veiculoId);
        veiculoModel.setMarca("Hyundai");
        veiculoModel.setModelo("HB20");
        veiculoModel.setAno(2020);
        veiculoModel.setCor("Cinza");
        veiculoModel.setPreco(60000.00f);
        veiculoModel.setVeiculoStatus(VeiculoStatus.DISPONIVEL);

        // WHEN
        when(veiculoService.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.of(veiculoModel));
        when(veiculoService.salvarVeiculo(any(VeiculoModel.class))).thenReturn(veiculoModel); // Não é o foco, mas necessário

        // THEN
        mockMvc.perform(put("/veiculos/{veiculoId}/vender", veiculoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Veículo atualizado para vendido com sucesso."));

        verify(veiculoService, times(1)).buscarVeiculoPorId(veiculoId);
        verify(veiculoService, times(1)).salvarVeiculo(any(VeiculoModel.class));
        verify(veiculoService).salvarVeiculo(argThat(v -> v.getVeiculoStatus() == VeiculoStatus.VENDIDO));
    }

    @Test
    @DisplayName("Não deve vender um veículo inexistente e retornar status 404 Not Found")
    void venderVeiculo_NotFound() throws Exception {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();

        // WHEN
        when(veiculoService.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.empty());

        // THEN
        mockMvc.perform(put("/veiculos/{veiculoId}/vender", veiculoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Veículo não cadastrado"));

        verify(veiculoService, times(1)).buscarVeiculoPorId(veiculoId);
        verify(veiculoService, never()).salvarVeiculo(any(VeiculoModel.class));
    }

    @Test
    @DisplayName("Não deve vender um veículo que não esteja DISPONIVEL e retornar status 409 Conflict")
    void venderVeiculo_NotAvailableConflict() throws Exception {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();
        VeiculoModel veiculoModel = new VeiculoModel();
        veiculoModel.setVeiculoId(veiculoId);
        veiculoModel.setVeiculoStatus(VeiculoStatus.VENDIDO); // Já vendido

        // WHEN
        when(veiculoService.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.of(veiculoModel));

        // THEN
        mockMvc.perform(put("/veiculos/{veiculoId}/vender", veiculoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string("Esse veículo não está disponível."));

        verify(veiculoService, times(1)).buscarVeiculoPorId(veiculoId);
        verify(veiculoService, never()).salvarVeiculo(any(VeiculoModel.class));
    }

    @Test
    @DisplayName("Deve deletar um veículo com sucesso e retornar status 200 OK")
    void deletarVeiculo_Success() throws Exception {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();
        VeiculoModel veiculoModel = new VeiculoModel();
        veiculoModel.setVeiculoId(veiculoId);

        // WHEN
        when(veiculoService.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.of(veiculoModel));
        doNothing().when(veiculoService).deletarVeiculo(veiculoId);

        // THEN
        mockMvc.perform(delete("/veiculos/{veiculoId}", veiculoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Veículo deletado."));

        verify(veiculoService, times(1)).buscarVeiculoPorId(veiculoId);
        verify(veiculoService, times(1)).deletarVeiculo(veiculoId);
    }

    @Test
    @DisplayName("Não deve deletar um veículo inexistente e retornar status 404 Not Found")
    void deletarVeiculo_NotFound() throws Exception {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();

        // WHEN
        when(veiculoService.buscarVeiculoPorId(veiculoId)).thenReturn(Optional.empty());

        // THEN
        mockMvc.perform(delete("/veiculos/{veiculoId}", veiculoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Veículo não encontrado"));

        verify(veiculoService, times(1)).buscarVeiculoPorId(veiculoId);
        verify(veiculoService, never()).deletarVeiculo(any(UUID.class));
    }
}