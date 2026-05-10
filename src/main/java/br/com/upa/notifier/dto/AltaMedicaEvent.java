package br.com.upa.notifier.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AltaMedicaEvent {

    private String tipo;
    private Long idEmpresa;
    private Integer cdAtendimento;
    private String dataHora;
}
