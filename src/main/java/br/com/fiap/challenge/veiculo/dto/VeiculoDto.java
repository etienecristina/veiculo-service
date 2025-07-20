package br.com.fiap.challenge.veiculo.dto;

import br.com.fiap.challenge.veiculo.enums.VeiculoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeiculoDto {
    @NotBlank
    private String marca;
    @NotBlank
    private String modelo;
    @NotNull
    private Integer ano;
    @NotBlank
    private String cor;
    @NotNull
    private Float preco;
    @NotNull
    private VeiculoStatus veiculoStatus;
}