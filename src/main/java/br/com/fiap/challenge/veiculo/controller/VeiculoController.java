package br.com.fiap.challenge.veiculo.controller;

import br.com.fiap.challenge.veiculo.dto.VeiculoDto;
import br.com.fiap.challenge.veiculo.enums.VeiculoStatus;
import br.com.fiap.challenge.veiculo.model.VeiculoModel;
import br.com.fiap.challenge.veiculo.service.VeiculoService;
import br.com.fiap.challenge.veiculo.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/veiculos")
@CrossOrigin(origins = "*", maxAge = 360)
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping
    public ResponseEntity<Void> salvarVeiculo(@RequestBody @Valid VeiculoDto veiculoDto) {
        var veiculoModel = new VeiculoModel();

        BeanUtils.copyProperties(veiculoDto, veiculoModel);
        veiculoModel.setDataDeInclusao(LocalDateTime.now(ZoneId.of("UTC")));

        var result = veiculoService.salvarVeiculo(veiculoModel);

        return ResponseEntity.created(URI.create("/veiculos/" + result.getVeiculoId() )).build();
    }

    @GetMapping()
    public ResponseEntity<Page<VeiculoModel>> listarTodosOsVeiculos(SpecificationTemplate.VeiculoSpec spec,
                                                                    @PageableDefault(sort = "preco", direction = Sort.Direction.ASC)
                                                                    Pageable pageable) {
        return (ResponseEntity.status(HttpStatus.OK).body(veiculoService.listarTodosOsVeiculos(spec, pageable)));
    }

    @GetMapping("/{veiculoId}")
    public ResponseEntity<Object> buscarVeiculoPorId(@PathVariable("veiculoId") UUID veiculoId) {

        Optional<VeiculoModel> veiculoOptional = veiculoService.buscarVeiculoPorId(veiculoId);

        return veiculoOptional.<ResponseEntity<Object>>map(veiculoModel -> ResponseEntity.status(HttpStatus.OK)
                        .body(veiculoModel)).orElseGet(
                                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não cadastrado"));

    }

    @PutMapping("/{veiculoId}")
    public ResponseEntity<Object> editarVeiculo(@PathVariable("veiculoId") UUID veiculoId,
                                                @RequestBody @Valid VeiculoDto veiculoDto) {

        Optional<VeiculoModel> veiculoOptional = veiculoService.buscarVeiculoPorId(veiculoId);

        if (veiculoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não cadastrado");
        }

        var veiculoModel = veiculoOptional.get();
        veiculoModel.setMarca(veiculoDto.getMarca());
        veiculoModel.setModelo(veiculoDto.getModelo());
        veiculoModel.setAno(veiculoDto.getAno());
        veiculoModel.setCor(veiculoDto.getCor());
        veiculoModel.setPreco(veiculoDto.getPreco());
        veiculoModel.setVeiculoStatus(veiculoDto.getVeiculoStatus());
        veiculoModel.setDataDeAtualizacao(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(veiculoService.salvarVeiculo(veiculoModel));
    }

    @PatchMapping("/{veiculoId}")
    public ResponseEntity<Object> comprarVeiculo(@PathVariable("veiculoId") UUID veiculoId){
        var veiculoOptional = veiculoService.buscarVeiculoPorId(veiculoId);

        if(veiculoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não cadastrado");
        }

        if(veiculoOptional.get().getVeiculoStatus() != VeiculoStatus.DISPONIVEL){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Veículo já vendido.");
        }
        var veiculoModel = veiculoOptional.get();

        veiculoModel.setVeiculoStatus(VeiculoStatus.VENDIDO);
        veiculoModel.setDataDeAtualizacao(LocalDateTime.now(ZoneId.of("UTC")));

        veiculoService.salvarVeiculo(veiculoModel);

        return ResponseEntity.status(HttpStatus.OK).body("Veículo vendido com sucesso.");
    }

    @DeleteMapping("/{veiculoId}")
    public ResponseEntity<Object> deletarVeiculo(@PathVariable("veiculoId") UUID veiculoId) {
        var veiculoOptional = veiculoService.buscarVeiculoPorId(veiculoId);

        if (veiculoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado");
        }

        veiculoService.deletarVeiculo(veiculoOptional.get().getVeiculoId());

        return ResponseEntity.status(HttpStatus.OK).body("Veículo deletado.");
    }
}