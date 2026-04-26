package br.com.upa.notifier.service;

import br.com.upa.notifier.dto.AlertaSemAltaEvent;
import br.com.upa.notifier.ws.NotifierWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NotifierService {

    private final NotifierWebSocketHandler handler;
    private final ObjectMapper mapper;

    public NotifierService(NotifierWebSocketHandler handler, ObjectMapper mapper) {
        this.handler = handler;
        this.mapper = mapper;
    }

    public void enviarAlerta(AlertaSemAltaEvent alerta) {
        try {
            String json = mapper.writeValueAsString(alerta);

            System.out.println("NotifierService recebeu alerta:");
            System.out.println("Atendimento = " + alerta.getCdAtendimento());
            System.out.println("Empresa = " + alerta.getIdEmpresa());
            System.out.println("Destinos = " + alerta.getDestinos());
            System.out.println("Conexões WS = " + handler.totalConexoes());

            handler.enviarParaTodos(json);

            log.info("Alerta enviado. Atendimento={}, Empresa={}, Conexões={}",
                    alerta.getCdAtendimento(),
                    alerta.getIdEmpresa(),
                    handler.totalConexoes());
        } catch (Exception e) {
            log.error("Erro ao serializar/enviar alerta: {}", e.getMessage(), e);
            throw new IllegalStateException("Falha ao enviar alerta.", e);
        }
    }

//    public void enviarAlerta(AlertaSemAltaEvent alerta) {
//        try {
//            validarECompletar(alerta);
//
//            String json = mapper.writeValueAsString(alerta);
//
//            log.info(
//                    "Preparando envio do alerta. Atendimento={}, Empresa={}, Tipo={}, Destinos={}, Conexões={}",
//                    alerta.getCdAtendimento(),
//                    alerta.getIdEmpresa(),
//                    alerta.getTipo(),
//                    alerta.getDestinos(),
//                    handler.totalConexoes()
//            );
//
//            handler.enviarParaTodos(json);
//
//            log.info(
//                    "Alerta enviado com sucesso. Atendimento={}, Empresa={}, Conexões={}",
//                    alerta.getCdAtendimento(),
//                    alerta.getIdEmpresa(),
//                    handler.totalConexoes()
//            );
//
//        } catch (Exception e) {
//            log.error("Erro ao serializar/enviar alerta: {}", e.getMessage(), e);
//            throw new IllegalStateException("Falha ao enviar alerta.", e);
//        }
//    }
    public int totalConexoes() {
        return handler.totalConexoes();
    }

    private void validarECompletar(AlertaSemAltaEvent alerta) {
        if (alerta == null) {
            throw new IllegalArgumentException("Alerta não informado.");
        }

        if (alerta.getTipo() == null || alerta.getTipo().isBlank()) {
            alerta.setTipo("ALERTA_SEM_ALTA");
        }

        if (alerta.getMensagem() == null || alerta.getMensagem().isBlank()) {
            alerta.setMensagem("Paciente saiu pela portaria sem alta médica.");
        }

        if (alerta.getPrioridade() == null || alerta.getPrioridade().isBlank()) {
            alerta.setPrioridade("ALTA");
        }

        if (alerta.getDataHora() == null || alerta.getDataHora().isBlank()) {
            alerta.setDataHora(LocalDateTime.now().toString());
        }

        if (alerta.getDestinos() == null || alerta.getDestinos().isEmpty()) {
            alerta.setDestinos(List.of("CLINICA GERAL"));
        }
    }
}

//package br.com.upa.notifier.service;
//
//import br.com.upa.notifier.dto.AlertaSemAltaEvent;
//import br.com.upa.notifier.ws.NotifierWebSocketHandler;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class NotifierService {
//
//    private final NotifierWebSocketHandler handler;
//    private final ObjectMapper mapper;
//
//    public NotifierService(NotifierWebSocketHandler handler, ObjectMapper mapper) {
//        this.handler = handler;
//        this.mapper = mapper;
//    }
//
//    public void enviarAlerta(AlertaSemAltaEvent alerta) {
//        try {
//            String json = mapper.writeValueAsString(alerta);
//            handler.enviarParaTodos(json);
//            log.info("Alerta enviado. Atendimento={}, Empresa={}, Conexões={}",
//                    alerta.getCdAtendimento(),
//                    alerta.getIdEmpresa(),
//                    handler.totalConexoes());
//        } catch (Exception e) {
//            log.error("Erro ao serializar/enviar alerta: {}", e.getMessage(), e);
//            throw new IllegalStateException("Falha ao enviar alerta.", e);
//        }
//    }
//
//    public int totalConexoes() {
//        return handler.totalConexoes();
//    }
//}
