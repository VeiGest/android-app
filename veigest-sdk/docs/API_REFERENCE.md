# Referência Rápida da API

Guia de referência rápida para todos os endpoints do VeiGest SDK.

---

## Autenticação

```java
// Login
sdk.auth().login(email, password, callback);

// Registo
sdk.auth().register(nome, email, password, callback);

// Logout
sdk.auth().logout(callback);

// Verificar autenticação
boolean authenticated = sdk.auth().isAuthenticated();

// Obter token
String token = sdk.auth().getAccessToken();

// Refresh token
sdk.auth().refreshToken(callback);
```

---

## Veículos

```java
// Listar todos
sdk.vehicles().list(callback);

// Obter por ID
sdk.vehicles().get(id, callback);

// Criar
sdk.vehicles().create(builder, callback);

// Atualizar
sdk.vehicles().update(id, builder, callback);

// Eliminar
sdk.vehicles().delete(id, callback);

// Estatísticas
sdk.vehicles().getStats(id, callback);

// Manutenções do veículo
sdk.vehicles().getMaintenances(vehicleId, callback);

// Abastecimentos do veículo
sdk.vehicles().getFuelLogs(vehicleId, callback);

// Rotas do veículo
sdk.vehicles().getRoutes(vehicleId, callback);

// Documentos do veículo
sdk.vehicles().getDocuments(vehicleId, callback);

// Alertas do veículo
sdk.vehicles().getAlerts(vehicleId, callback);
```

---

## Utilizadores

```java
// Listar todos
sdk.users().list(callback);

// Obter por ID
sdk.users().get(id, callback);

// Criar
sdk.users().create(builder, callback);

// Atualizar
sdk.users().update(id, builder, callback);

// Eliminar
sdk.users().delete(id, callback);

// Utilizador atual
sdk.users().getCurrentUser(callback);

// Condutores da empresa
sdk.users().getDrivers(callback);

// Rotas do condutor
sdk.users().getRoutes(driverId, callback);

// Veículos atribuídos
sdk.users().getVehicles(driverId, callback);

// Abastecimentos do condutor
sdk.users().getFuelLogs(driverId, callback);
```

---

## Manutenções

```java
// Listar todas
sdk.maintenances().list(callback);

// Obter por ID
sdk.maintenances().get(id, callback);

// Criar
sdk.maintenances().create(builder, callback);

// Atualizar
sdk.maintenances().update(id, builder, callback);

// Eliminar
sdk.maintenances().delete(id, callback);

// Agendar
sdk.maintenances().schedule(id, scheduleBuilder, callback);

// Concluir
sdk.maintenances().complete(id, callback);

// Cancelar
sdk.maintenances().cancel(id, callback);

// Pendentes
sdk.maintenances().getPending(callback);

// Próximas (por data)
sdk.maintenances().getUpcoming(days, callback);
```

---

## Abastecimentos

```java
// Listar todos
sdk.fuelLogs().list(callback);

// Obter por ID
sdk.fuelLogs().get(id, callback);

// Criar
sdk.fuelLogs().create(builder, callback);

// Atualizar
sdk.fuelLogs().update(id, builder, callback);

// Eliminar
sdk.fuelLogs().delete(id, callback);

// Estatísticas
sdk.fuelLogs().getStats(callback);

// Estatísticas por veículo
sdk.fuelLogs().getStatsByVehicle(vehicleId, callback);

// Eficiência de combustível
sdk.fuelLogs().getEfficiency(vehicleId, callback);
```

---

## Rotas

```java
// Listar todas
sdk.routes().list(callback);

// Obter por ID
sdk.routes().get(id, callback);

// Criar
sdk.routes().create(builder, callback);

// Atualizar
sdk.routes().update(id, builder, callback);

// Eliminar
sdk.routes().delete(id, callback);

// Iniciar rota
sdk.routes().start(id, callback);

// Finalizar rota
sdk.routes().finish(id, kmFinal, callback);

// Rotas em andamento
sdk.routes().getActive(callback);

// Histórico
sdk.routes().getHistory(startDate, endDate, callback);

// Adicionar ponto GPS
sdk.routes().addGpsPoint(routeId, latitude, longitude, timestamp, callback);

// Pontos GPS da rota
sdk.routes().getGpsPoints(routeId, callback);
```

---

## Documentos

```java
// Listar todos
sdk.documents().list(callback);

// Obter por ID
sdk.documents().get(id, callback);

// Criar
sdk.documents().create(builder, callback);

// Atualizar
sdk.documents().update(id, builder, callback);

// Eliminar
sdk.documents().delete(id, callback);

// A expirar (próximos dias)
sdk.documents().getExpiring(days, callback);

// Expirados
sdk.documents().getExpired(callback);

// Por veículo
sdk.documents().getByVehicle(vehicleId, callback);

// Por condutor
sdk.documents().getByDriver(driverId, callback);

// Renovar
sdk.documents().renew(id, novaDataValidade, callback);
```

---

## Alertas

```java
// Listar todos
sdk.alerts().list(callback);

// Obter por ID
sdk.alerts().get(id, callback);

// Criar
sdk.alerts().create(builder, callback);

// Marcar como lido
sdk.alerts().markAsRead(id, callback);

// Marcar todos como lidos
sdk.alerts().markAllAsRead(callback);

// Eliminar
sdk.alerts().delete(id, callback);

// Não lidos
sdk.alerts().getUnread(callback);

// Por tipo
sdk.alerts().getByType(type, callback);

// Alertas de combustível
sdk.alerts().getFuelAlerts(callback);
```

---

## Relatórios

```java
// Estatísticas gerais
sdk.reports().getStats(callback);

// Relatório de manutenção
sdk.reports().getMaintenanceReport(startDate, endDate, callback);

// Relatório de combustível
sdk.reports().getFuelReport(startDate, endDate, callback);

// Relatório de eficiência
sdk.reports().getEfficiencyReport(vehicleId, callback);

// Relatório de rotas
sdk.reports().getRoutesReport(startDate, endDate, callback);

// Exportar PDF
sdk.reports().exportPdf(type, startDate, endDate, callback);

// Exportar Excel
sdk.reports().exportExcel(type, startDate, endDate, callback);

// Dashboard
sdk.reports().getDashboard(callback);
```

---

## Empresas

```java
// Listar todas (admin)
sdk.companies().list(callback);

// Obter por ID
sdk.companies().get(id, callback);

// Criar (admin)
sdk.companies().create(builder, callback);

// Atualizar
sdk.companies().update(id, builder, callback);

// Eliminar (admin)
sdk.companies().delete(id, callback);

// Empresa atual
sdk.companies().getCurrent(callback);

// Estatísticas
sdk.companies().getStats(id, callback);

// Utilizadores da empresa
sdk.companies().getUsers(companyId, callback);

// Veículos da empresa
sdk.companies().getVehicles(companyId, callback);
```

---

## Tickets

```java
// Listar todos
sdk.tickets().list(callback);

// Obter por ID
sdk.tickets().get(id, callback);

// Criar
sdk.tickets().create(builder, callback);

// Atualizar
sdk.tickets().update(id, builder, callback);

// Eliminar
sdk.tickets().delete(id, callback);

// Responder
sdk.tickets().reply(id, message, callback);

// Fechar
sdk.tickets().close(id, callback);

// Reabrir
sdk.tickets().reopen(id, callback);

// Por estado
sdk.tickets().getByStatus(status, callback);

// Meus tickets
sdk.tickets().getMyTickets(callback);
```

---

## Ficheiros

```java
// Listar todos
sdk.files().list(callback);

// Obter por ID
sdk.files().get(id, callback);

// Upload
sdk.files().upload(file, callback);

// Download
sdk.files().download(id, callback);

// Eliminar
sdk.files().delete(id, callback);
```

---

## Builders - Referência Rápida

### VehicleBuilder
```java
new VehicleService.VehicleBuilder()
    .matricula("AA-00-BB")      // obrigatório
    .marca("Toyota")            // obrigatório
    .modelo("Hilux")            // obrigatório
    .ano(2023)                  // obrigatório
    .tipoCombustivel("diesel")
    .quilometragem(0)
    .condutorId(5)
    .estado("ativo")
    .companyId(1);
```

### UserBuilder
```java
new UserService.UserBuilder()
    .nome("João Silva")         // obrigatório
    .email("joao@email.com")    // obrigatório
    .password("senha123")       // obrigatório
    .telefone("+351912345678")
    .numeroCarta("AB-123456")
    .validadeCarta("2028-12-31")
    .tipo("condutor")
    .companyId(1);
```

### MaintenanceBuilder
```java
new MaintenanceService.MaintenanceBuilder()
    .vehicleId(10)              // obrigatório
    .tipo("preventiva")         // obrigatório
    .data("2024-12-23")         // obrigatório
    .custo(350.00)              // obrigatório
    .descricao("Troca de óleo")
    .oficina("Oficina ABC")
    .kmRegistro(125000)
    .proximaData("2025-06-23");
```

### FuelLogBuilder
```java
new FuelLogService.FuelLogBuilder()
    .vehicleId(10)              // obrigatório
    .data("2024-12-23")         // obrigatório
    .litros(45.5)               // obrigatório
    .valor(68.25)               // obrigatório
    .kmAtual(125500)            // obrigatório
    .driverId(5)
    .precoPorLitro(1.50)
    .local("Posto Shell");
```

### RouteBuilder
```java
new RouteService.RouteBuilder()
    .vehicleId(10)              // obrigatório
    .driverId(5)                // obrigatório
    .origem("Lisboa")           // obrigatório
    .destino("Porto")           // obrigatório
    .kmInicial(125500)          // obrigatório
    .notas("Entrega urgente");
```

### DocumentBuilder
```java
new DocumentService.DocumentBuilder()
    .tipo("seguro")             // obrigatório
    .dataValidade("2025-12-31") // obrigatório
    .vehicleId(10)              // obrigatório (ou driverId)
    .fileId(25)
    .notas("Seguro Allianz");
```

### TicketBuilder
```java
new TicketService.TicketBuilder()
    .titulo("GPS não funciona") // obrigatório
    .descricao("Descrição...")  // obrigatório
    .tipo("tecnico")            // obrigatório
    .prioridade("alta")
    .vehicleId(10);
```

---

## Códigos de Erro

| ErrorType | Descrição | Ação Sugerida |
|-----------|-----------|---------------|
| `NETWORK_ERROR` | Sem conexão | Verificar internet |
| `UNAUTHORIZED` | Token inválido/expirado | Refresh token ou relogin |
| `FORBIDDEN` | Sem permissão | Verificar permissões do utilizador |
| `NOT_FOUND` | Recurso não encontrado | Verificar ID |
| `VALIDATION_ERROR` | Dados inválidos | Corrigir dados enviados |
| `SERVER_ERROR` | Erro no servidor | Tentar novamente mais tarde |
| `TIMEOUT` | Timeout na requisição | Aumentar timeout ou tentar novamente |
| `UNKNOWN` | Erro desconhecido | Verificar logs |

---

## Próximos Passos

- [Guia Completo de Serviços](SERVICES.md)
- [Documentação de Modelos](MODELS.md)
- [Builders Detalhado](BUILDERS.md)
