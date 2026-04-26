Projeto pronto para abrir no NetBeans.

Nome do projeto:
- portaria-notifier-server

Tecnologias:
- Spring Boot 3
- Java 17
- WebSocket
- REST

Porta:
- 8081

Endpoints:
- GET  /api/notifier/health
- GET  /api/notifier/status
- POST /api/notifier/alerta

WebSocket:
- ws://HOST:8081/ws-notifier

Como rodar:
1. Abra o NetBeans
2. Open Project
3. Selecione esta pasta
4. Rode a classe:
   br.com.upa.notifier.PortariaNotifierServerApplication

Exemplo de POST:
POST http://localhost:8081/api/notifier/alerta

Body JSON:
{
  "tipo": "ALERTA_SEM_ALTA",
  "idEmpresa": 1,
  "cdAtendimento": 123456,
  "paciente": "123456, JOAO DA SILVA",
  "mensagem": "Paciente saiu pela portaria sem alta médica.",
  "destinos": ["CLINICO", "PEDIATRA"],
  "prioridade": "ALTA",
  "dataHora": "2026-03-31T10:15:00"
}
