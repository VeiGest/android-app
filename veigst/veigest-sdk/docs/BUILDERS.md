# Padrões Builder

Este documento descreve os padrões Builder disponíveis no VeiGest SDK para criação de objetos de forma fluente e type-safe.

---

## Visão Geral

Builders permitem criar objetos de forma clara e flexível:

```java
// Sem Builder - muitos parâmetros
sdk.vehicles().create("AA-00-BB", "Toyota", "Hilux", 2023, "diesel", callback);

// Com Builder - mais legível e flexível
VehicleService.VehicleBuilder builder = new VehicleService.VehicleBuilder()
    .matricula("AA-00-BB")
    .marca("Toyota")
    .modelo("Hilux")
    .ano(2023)
    .tipoCombustivel("diesel")
    .quilometragem(0)
    .estado("ativo");

sdk.vehicles().create(builder, callback);
```

---

## VehicleBuilder

Criar e configurar veículos.

### Métodos Disponíveis

| Método | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| `matricula(String)` | String | Sim | Matrícula do veículo |
| `marca(String)` | String | Sim | Marca |
| `modelo(String)` | String | Sim | Modelo |
| `ano(int)` | int | Sim | Ano de fabrico |
| `tipoCombustivel(String)` | String | Não | Tipo de combustível |
| `quilometragem(int)` | int | Não | Quilometragem inicial |
| `condutorId(int)` | int | Não | ID do condutor |
| `estado(String)` | String | Não | Estado inicial |
| `companyId(int)` | int | Não | ID da empresa |

### Exemplo Completo

```java
VehicleService.VehicleBuilder builder = new VehicleService.VehicleBuilder()
    // Campos obrigatórios
    .matricula("AA-00-BB")
    .marca("Toyota")
    .modelo("Hilux")
    .ano(2023)
    // Campos opcionais
    .tipoCombustivel("diesel")
    .quilometragem(15000)
    .condutorId(5)
    .estado("ativo")
    .companyId(1);

sdk.vehicles().create(builder, new VeiGestCallback<Vehicle>() {
    @Override
    public void onSuccess(@NonNull Vehicle vehicle) {
        Log.d("Vehicle", "Criado: " + vehicle.getMatricula());
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        Log.e("Error", error.getMessage());
    }
});
```

---

## UserBuilder

Criar e configurar utilizadores.

### Métodos Disponíveis

| Método | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| `nome(String)` | String | Sim | Nome completo |
| `email(String)` | String | Sim | Email |
| `password(String)` | String | Sim | Password |
| `telefone(String)` | String | Não | Telefone |
| `numeroCarta(String)` | String | Não | Número da carta de condução |
| `validadeCarta(String)` | String | Não | Data de validade da carta |
| `tipo(String)` | String | Não | Tipo: "admin", "gestor", "condutor" |
| `companyId(int)` | int | Não | ID da empresa |

### Exemplo: Criar Condutor

```java
UserService.UserBuilder builder = new UserService.UserBuilder()
    .nome("João Silva")
    .email("joao.silva@empresa.com")
    .password("senhaSegura123")
    .telefone("+351912345678")
    .numeroCarta("AB-123456")
    .validadeCarta("2028-12-31")
    .tipo("condutor")
    .companyId(1);

sdk.users().create(builder, callback);
```

### Exemplo: Criar Gestor

```java
UserService.UserBuilder builder = new UserService.UserBuilder()
    .nome("Maria Santos")
    .email("maria.santos@empresa.com")
    .password("senhaAdmin456")
    .telefone("+351923456789")
    .tipo("gestor")
    .companyId(1);

sdk.users().create(builder, callback);
```

---

## MaintenanceBuilder

Criar registos de manutenção.

### Métodos Disponíveis

| Método | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| `vehicleId(int)` | int | Sim | ID do veículo |
| `tipo(String)` | String | Sim | Tipo: "preventiva", "corretiva", "revisao" |
| `data(String)` | String | Sim | Data da manutenção (YYYY-MM-DD) |
| `custo(double)` | double | Sim | Custo da manutenção |
| `descricao(String)` | String | Não | Descrição detalhada |
| `oficina(String)` | String | Não | Nome da oficina |
| `kmRegistro(int)` | int | Não | Quilometragem no momento |
| `proximaData(String)` | String | Não | Próxima manutenção |
| `companyId(int)` | int | Não | ID da empresa |

### Exemplo: Manutenção Preventiva

```java
MaintenanceService.MaintenanceBuilder builder = new MaintenanceService.MaintenanceBuilder()
    .vehicleId(10)
    .tipo("preventiva")
    .data("2024-12-23")
    .custo(350.00)
    .descricao("Troca de óleo, filtro de óleo e filtro de ar")
    .oficina("Oficina ABC")
    .kmRegistro(125000)
    .proximaData("2025-06-23");

sdk.maintenances().create(builder, callback);
```

### Exemplo: Manutenção Corretiva

```java
MaintenanceService.MaintenanceBuilder builder = new MaintenanceService.MaintenanceBuilder()
    .vehicleId(10)
    .tipo("corretiva")
    .data("2024-12-23")
    .custo(850.00)
    .descricao("Substituição de pastilhas e discos de travão")
    .oficina("AutoCenter")
    .kmRegistro(125000);

sdk.maintenances().create(builder, callback);
```

---

## ScheduleBuilder

Agendar manutenções.

### Métodos Disponíveis

| Método | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| `scheduledDate(String)` | String | Sim | Data agendada (YYYY-MM-DD) |
| `priority(String)` | String | Não | Prioridade: "alta", "media", "baixa" |
| `assignedTechnician(String)` | String | Não | Técnico responsável |

### Exemplo

```java
MaintenanceService.ScheduleBuilder schedule = new MaintenanceService.ScheduleBuilder()
    .scheduledDate("2024-12-30")
    .priority("alta")
    .assignedTechnician("Carlos Pereira");

sdk.maintenances().schedule(maintenanceId, schedule, callback);
```

---

## FuelLogBuilder

Criar registos de abastecimento.

### Métodos Disponíveis

| Método | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| `vehicleId(int)` | int | Sim | ID do veículo |
| `data(String)` | String | Sim | Data (YYYY-MM-DD) |
| `litros(double)` | double | Sim | Quantidade em litros |
| `valor(double)` | double | Sim | Valor total |
| `kmAtual(int)` | int | Sim | Quilometragem atual |
| `driverId(int)` | int | Não | ID do condutor |
| `notas(String)` | String | Não | Observações |
| `local(String)` | String | Não | Local do abastecimento |
| `precoPorLitro(double)` | double | Não | Preço por litro |
| `companyId(int)` | int | Não | ID da empresa |

### Exemplo Completo

```java
FuelLogService.FuelLogBuilder builder = new FuelLogService.FuelLogBuilder()
    .vehicleId(10)
    .driverId(5)
    .data("2024-12-23")
    .litros(45.5)
    .valor(68.25)
    .kmAtual(125500)
    .precoPorLitro(1.50)
    .local("Posto Shell - Av. Principal")
    .notas("Tanque cheio");

sdk.fuelLogs().create(builder, callback);
```

### Exemplo Simples

```java
FuelLogService.FuelLogBuilder builder = new FuelLogService.FuelLogBuilder()
    .vehicleId(10)
    .data("2024-12-23")
    .litros(45.5)
    .valor(68.25)
    .kmAtual(125500);

sdk.fuelLogs().create(builder, callback);
```

---

## RouteBuilder

Criar rotas/viagens.

### Métodos Disponíveis

| Método | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| `vehicleId(int)` | int | Sim | ID do veículo |
| `driverId(int)` | int | Sim | ID do condutor |
| `origem(String)` | String | Sim | Local de origem |
| `destino(String)` | String | Sim | Local de destino |
| `kmInicial(int)` | int | Sim | Quilometragem inicial |
| `notas(String)` | String | Não | Observações |
| `companyId(int)` | int | Não | ID da empresa |

### Exemplo

```java
RouteService.RouteBuilder builder = new RouteService.RouteBuilder()
    .vehicleId(10)
    .driverId(5)
    .origem("Lisboa - Sede")
    .destino("Porto - Cliente ABC")
    .kmInicial(125500)
    .notas("Entrega urgente");

sdk.routes().create(builder, callback);
```

---

## DocumentBuilder

Criar documentos.

### Métodos Disponíveis

| Método | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| `tipo(String)` | String | Sim | Tipo: "seguro", "inspecao", "licenca", etc. |
| `dataValidade(String)` | String | Sim | Data de validade (YYYY-MM-DD) |
| `vehicleId(int)` | int | Não* | ID do veículo |
| `driverId(int)` | int | Não* | ID do condutor |
| `fileId(int)` | int | Não | ID do ficheiro associado |
| `notas(String)` | String | Não | Observações |
| `companyId(int)` | int | Não | ID da empresa |

> *Pelo menos `vehicleId` ou `driverId` deve ser fornecido.

### Exemplo: Documento de Veículo

```java
DocumentService.DocumentBuilder builder = new DocumentService.DocumentBuilder()
    .tipo("seguro")
    .dataValidade("2025-12-31")
    .vehicleId(10)
    .fileId(25)
    .notas("Seguro contra todos os riscos - Allianz");

sdk.documents().create(builder, callback);
```

### Exemplo: Documento de Condutor

```java
DocumentService.DocumentBuilder builder = new DocumentService.DocumentBuilder()
    .tipo("carta_conducao")
    .dataValidade("2028-06-15")
    .driverId(5)
    .notas("Categoria B");

sdk.documents().create(builder, callback);
```

---

## TicketBuilder

Criar tickets de suporte.

### Métodos Disponíveis

| Método | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| `titulo(String)` | String | Sim | Título do ticket |
| `descricao(String)` | String | Sim | Descrição detalhada |
| `tipo(String)` | String | Sim | Tipo: "tecnico", "suporte", "reclamacao" |
| `prioridade(String)` | String | Não | Prioridade: "alta", "media", "baixa" |
| `vehicleId(int)` | int | Não | ID do veículo relacionado |
| `driverId(int)` | int | Não | ID do condutor relacionado |
| `routeId(int)` | int | Não | ID da rota relacionada |
| `observacoes(String)` | String | Não | Observações adicionais |

### Exemplo: Ticket Técnico

```java
TicketService.TicketBuilder builder = new TicketService.TicketBuilder()
    .titulo("GPS não funciona")
    .descricao("O dispositivo GPS do veículo AA-00-BB parou de enviar localização há 2 dias.")
    .tipo("tecnico")
    .prioridade("alta")
    .vehicleId(10);

sdk.tickets().create(builder, callback);
```

### Exemplo: Reclamação

```java
TicketService.TicketBuilder builder = new TicketService.TicketBuilder()
    .titulo("Problema com manutenção anterior")
    .descricao("O problema reportado na última manutenção não foi resolvido corretamente.")
    .tipo("reclamacao")
    .prioridade("media")
    .vehicleId(10)
    .observacoes("Referente à manutenção de 15/12/2024");

sdk.tickets().create(builder, callback);
```

---

## AlertBuilder

Criar alertas personalizados.

### Métodos Disponíveis

| Método | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| `tipo(String)` | String | Sim | Tipo de alerta |
| `titulo(String)` | String | Sim | Título |
| `descricao(String)` | String | Não | Descrição |
| `prioridade(String)` | String | Não | Prioridade |
| `vehicleId(int)` | int | Não | ID do veículo |
| `driverId(int)` | int | Não | ID do condutor |
| `detalhes(Map)` | Map | Não | Detalhes adicionais |

### Exemplo

```java
AlertService.AlertBuilder builder = new AlertService.AlertBuilder()
    .tipo("manutencao")
    .titulo("Manutenção programada próxima")
    .descricao("O veículo AA-00-BB necessita de revisão em 5 dias")
    .prioridade("media")
    .vehicleId(10);

sdk.alerts().create(builder, callback);
```

---

## CompanyBuilder

Criar empresas (apenas para administradores).

### Métodos Disponíveis

| Método | Tipo | Obrigatório | Descrição |
|--------|------|-------------|-----------|
| `nome(String)` | String | Sim | Nome da empresa |
| `nif(String)` | String | Sim | NIF/NIPC |
| `email(String)` | String | Sim | Email de contacto |
| `telefone(String)` | String | Não | Telefone |
| `morada(String)` | String | Não | Morada |
| `plano(String)` | String | Não | Plano contratado |

### Exemplo

```java
CompanyService.CompanyBuilder builder = new CompanyService.CompanyBuilder()
    .nome("Transportes ABC Lda")
    .nif("123456789")
    .email("geral@transportesabc.pt")
    .telefone("+351212345678")
    .morada("Rua Principal, 123, Lisboa")
    .plano("premium");

sdk.companies().create(builder, callback);
```

---

## Boas Práticas com Builders

### 1. Reutilização

```java
// Criar builder base
VehicleService.VehicleBuilder baseBuilder = new VehicleService.VehicleBuilder()
    .marca("Toyota")
    .ano(2023)
    .tipoCombustivel("diesel")
    .estado("ativo")
    .companyId(1);

// Não reutilize diretamente! Builders são mutáveis
// Crie novos builders para cada veículo
```

### 2. Validação Prévia

```java
// Validar antes de chamar a API
String matricula = matriculaInput.getText().toString();
if (matricula.isEmpty()) {
    matriculaInput.setError("Matrícula obrigatória");
    return;
}

VehicleService.VehicleBuilder builder = new VehicleService.VehicleBuilder()
    .matricula(matricula)
    // ...
```

### 3. Encadeamento Fluente

```java
// Bom - encadeamento claro
sdk.vehicles().create(
    new VehicleService.VehicleBuilder()
        .matricula(matricula)
        .marca(marca)
        .modelo(modelo)
        .ano(ano),
    callback
);
```

### 4. Separação de Lógica

```java
// Criar método helper para construir o builder
private VehicleService.VehicleBuilder buildVehicleFromForm() {
    return new VehicleService.VehicleBuilder()
        .matricula(matriculaInput.getText().toString())
        .marca(marcaInput.getText().toString())
        .modelo(modeloInput.getText().toString())
        .ano(Integer.parseInt(anoInput.getText().toString()))
        .tipoCombustivel(getSelectedFuelType())
        .quilometragem(getKmFromInput());
}

// Uso
sdk.vehicles().create(buildVehicleFromForm(), callback);
```

---

## Próximos Passos

- [Referência de Serviços](SERVICES.md)
- [Referência de Modelos](MODELS.md)
- [Boas Práticas](BEST_PRACTICES.md)
