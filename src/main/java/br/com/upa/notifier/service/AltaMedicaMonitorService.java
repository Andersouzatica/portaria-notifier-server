package br.com.upa.notifier.service;

import br.com.upa.notifier.dto.AltaMedicaEvent;
import br.com.upa.notifier.ws.NotifierWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AltaMedicaMonitorService {

    @Value("${portaria-server.base-url}")
    private String portariaServerBaseUrl;

    @Value("${portaria-server.internal-token}")
    private String internalToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private final NotifierWebSocketHandler handler;
    private final ObjectMapper mapper;

    private final Map<Integer, LocalDateTime> enviados = new ConcurrentHashMap<>();

    public AltaMedicaMonitorService(NotifierWebSocketHandler handler, ObjectMapper mapper) {
        this.handler = handler;
        this.mapper = mapper;
    }

    @Scheduled(fixedDelay = 5000)
    public void verificarAltas() {

        try {
            String url = portariaServerBaseUrl + "/altas-recentes?minutos=1";

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Internal-Token", internalToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<AltaMedicaEvent[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    AltaMedicaEvent[].class
            );

            AltaMedicaEvent[] eventos = response.getBody();

            if (eventos == null || eventos.length == 0) {
                limparEnviadosAntigos();
                return;
            }

            for (AltaMedicaEvent evento : Arrays.asList(eventos)) {

                if (evento.getCdAtendimento() == null) {
                    continue;
                }

                if (enviados.containsKey(evento.getCdAtendimento())) {
                    continue;
                }

                enviados.put(evento.getCdAtendimento(), LocalDateTime.now());

                String json = mapper.writeValueAsString(evento);

                System.out.println(">>> Enviando alta médica para agentes: "
                        + evento.getCdAtendimento());

                handler.enviarParaTodos(json);
            }

            limparEnviadosAntigos();

        } catch (Exception e) {
            System.out.println("Erro ao consultar altas no portaria-server:");
            e.printStackTrace();
        }
    }

    private void limparEnviadosAntigos() {
        LocalDateTime limite = LocalDateTime.now().minusHours(1);

        enviados.entrySet().removeIf(e
                -> e.getValue() != null && e.getValue().isBefore(limite)
        );
    }
}

//@Service
//public class AltaMedicaMonitorService {
//
//    @Value("${portaria-server.base-url}")
//    private String portariaServerBaseUrl;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final NotifierWebSocketHandler handler;
//    private final ObjectMapper mapper;
//
//    private final Map<Integer, LocalDateTime> enviados = new ConcurrentHashMap<>();
//
//    public AltaMedicaMonitorService(NotifierWebSocketHandler handler, ObjectMapper mapper) {
//        this.handler = handler;
//        this.mapper = mapper;
//    }
//
//    @Scheduled(fixedDelay = 5000)
//    public void verificarAltas() {
//        try {
//            String url = portariaServerBaseUrl + "/notifier/altas-recentes?minutos=1";
//
//            AltaMedicaEvent[] eventos = restTemplate.getForObject(url, AltaMedicaEvent[].class);
//
//            if (eventos == null || eventos.length == 0) {
//                limparEnviadosAntigos();
//                return;
//            }
//
//            for (AltaMedicaEvent evento : Arrays.asList(eventos)) {
//                if (evento.getCdAtendimento() == null) {
//                    continue;
//                }
//
//                if (enviados.containsKey(evento.getCdAtendimento())) {
//                    continue;
//                }
//
//                enviados.put(evento.getCdAtendimento(), LocalDateTime.now());
//
//                String json = mapper.writeValueAsString(evento);
//
//                System.out.println(">>> Enviando alta médica para agentes: " + evento.getCdAtendimento());
//
//                handler.enviarParaTodos(json);
//            }
//
//            limparEnviadosAntigos();
//
//        } catch (Exception e) {
//            System.out.println("Erro ao consultar altas no portaria-server:");
//            e.printStackTrace();
//        }
//    }
//
//    private void limparEnviadosAntigos() {
//        LocalDateTime limite = LocalDateTime.now().minusHours(1);
//
//        enviados.entrySet().removeIf(e
//                -> e.getValue() != null && e.getValue().isBefore(limite)
//        );
//    }
//}
//package br.com.upa.notifier.service;
//
//import br.com.upa.notifier.dto.AltaMedicaEvent;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//public class AltaMedicaMonitorService {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    // guarda o que já foi enviado (evita duplicidade)
//    private final Map<Integer, LocalDateTime> enviados = new ConcurrentHashMap<>();
//
//    @Scheduled(fixedDelay = 5000)
//    public void verificarAltas() {
//
//        String sql = """
//            SELECT cd_atendimento, cd_multi_empresa
//            FROM dbamv.sacr_tempo_processo
//            WHERE cd_tipo_tempo_processo = 90
//              AND dh_processo >= SYSDATE - (1/1440) -- últimos 1 minuto
//        """;
//
//        List<Object[]> rows = em.createNativeQuery(sql).getResultList();
//
//        for (Object[] r : rows) {
//
//            Integer cdAtendimento = ((Number) r[0]).intValue();
//            Long idEmpresa = ((Number) r[1]).longValue();
//
//            if (jaEnviados.contains(cdAtendimento)) {
//                continue;
//            }
//
//            jaEnviados.add(cdAtendimento);
//
//            var evento = AltaMedicaEvent.builder()
//                    .tipo("ALTA_MEDICA_REALIZADA")
//                    .idEmpresa(idEmpresa)
//                    .cdAtendimento(cdAtendimento)
//                    .dataHora(LocalDateTime.now().toString())
//                    .build();
//
//            System.out.println(">>> Enviando alta médica: " + cdAtendimento);
//
//            messagingTemplate.convertAndSend("/topic/notifier", evento);
//        }
//    }
//}
