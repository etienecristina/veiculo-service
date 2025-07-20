package br.com.fiap.challenge.veiculo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import br.com.fiap.challenge.veiculo.enums.VeiculoStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_VEICULOS")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VeiculoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private UUID veiculoId;
    @Column(nullable = false, length = 100)
    private String marca;
    @Column(nullable = false, length = 100)
    private String modelo;
    @Column(nullable = false)
    private Integer ano;
    @Column(nullable = false)
    private String cor;
    @Column(nullable = false)
    private Float preco;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private VeiculoStatus veiculoStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataDeInclusao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataDeAtualizacao;
}