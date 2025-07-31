package br.com.fiap.tech_challenge.veiculo.dto;


import br.com.fiap.challenge.veiculo.dto.VeiculoDto;
import br.com.fiap.challenge.veiculo.enums.VeiculoStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class VeiculoDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar um VeiculoDto com sucesso")
    void deveValidarVeiculoDtoComSucesso() {
        // GIVEN
        VeiculoDto veiculoDto = VeiculoDto.builder()
                .marca("Honda")
                .modelo("Civic")
                .ano(2023)
                .cor("Preto")
                .preco(120000.0f)
                .veiculoStatus(VeiculoStatus.DISPONIVEL)
                .build();

        // WHEN
        Set<ConstraintViolation<VeiculoDto>> violations = validator.validate(veiculoDto);

        // THEN
        assertThat(violations).isEmpty();
    }
}