package br.com.fiap.tech_challenge.veiculo.model;

import br.com.fiap.challenge.veiculo.enums.VeiculoStatus;
import br.com.fiap.challenge.veiculo.model.VeiculoModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;

class VeiculoModelTest {


    @Test
    @DisplayName("Deve criar e acessar todos os atributos de VeiculoModel")
    void deveCriarEValidarAtributosDeVeiculoModel() {
        // GIVEN
        UUID veiculoId = UUID.randomUUID();
        String marca = "Tesla";
        String modelo = "Model 3";
        Integer ano = 2024;
        String cor = "Branco";
        Float preco = 250000.0f;
        VeiculoStatus status = VeiculoStatus.DISPONIVEL;
        LocalDateTime dataDeInclusao = LocalDateTime.now();
        LocalDateTime dataDeAtualizacao = LocalDateTime.now();

        // WHEN
        VeiculoModel veiculo = new VeiculoModel();
        veiculo.setVeiculoId(veiculoId);
        veiculo.setMarca(marca);
        veiculo.setModelo(modelo);
        veiculo.setAno(ano);
        veiculo.setCor(cor);
        veiculo.setPreco(preco);
        veiculo.setVeiculoStatus(status);
        veiculo.setDataDeInclusao(dataDeInclusao);
        veiculo.setDataDeAtualizacao(dataDeAtualizacao);

        // THEN
        assertThat(veiculo.getVeiculoId()).isEqualTo(veiculoId);
        assertThat(veiculo.getMarca()).isEqualTo(marca);
        assertThat(veiculo.getModelo()).isEqualTo(modelo);
        assertThat(veiculo.getAno()).isEqualTo(ano);
        assertThat(veiculo.getCor()).isEqualTo(cor);
        assertThat(veiculo.getPreco()).isEqualTo(preco);
        assertThat(veiculo.getVeiculoStatus()).isEqualTo(status);
        assertThat(veiculo.getDataDeInclusao()).isEqualTo(dataDeInclusao);
        assertThat(veiculo.getDataDeAtualizacao()).isEqualTo(dataDeAtualizacao);
    }
}