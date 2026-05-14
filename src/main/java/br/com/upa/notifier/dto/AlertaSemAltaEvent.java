package br.com.upa.notifier.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaSemAltaEvent {

    @NotBlank
    private String tipo;

    @NotNull
    private Long idEmpresa;

    @NotNull
    private Integer cdAtendimento;

    @NotBlank
    private String paciente;

    @NotBlank
    private String mensagem;

    private List<String> destinos;

    private String prioridade;

    private String dataHora;

    private Integer corReferencia;

    private String nomeMedico;

    private String especialidade;
    private String snRaioX;
    private String snLab;
    private String snMed;
    private String snPrioridadeClassificacao;
    private String tpAtendimento;
    private Integer idade;
}
