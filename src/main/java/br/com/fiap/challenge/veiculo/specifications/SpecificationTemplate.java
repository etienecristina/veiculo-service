package br.com.fiap.challenge.veiculo.specifications;

import br.com.fiap.challenge.veiculo.model.VeiculoModel;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {

    @And({
            @Spec(path = "marca", spec = EqualIgnoreCase.class),
            @Spec(path = "modelo", spec = EqualIgnoreCase.class),
            @Spec(path = "ano", spec = EqualIgnoreCase.class),
            @Spec(path = "cor", spec = EqualIgnoreCase.class),
            @Spec(path = "veiculoStatus", spec = EqualIgnoreCase.class)
    })
    public interface VeiculoSpec extends Specification<VeiculoModel> {
    }
}