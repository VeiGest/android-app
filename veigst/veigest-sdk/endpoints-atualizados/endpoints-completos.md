# Documentação Completa dos Endpoints da API VeiGest

**Versão:** 1.0.0  
**Base URL:** `http://localhost:8080/api/v1`  
**Data:** 6 de Janeiro de 2026

---

## Índice

1. [Autenticação](#1-autenticação)
2. [Empresas](#2-empresas)
3. [Veículos](#3-veículos)
4. [Manutenções](#4-manutenções)
5. [Abastecimentos](#5-abastecimentos)
6. [Alertas](#6-alertas)
7. [Documentos](#7-documentos)
8. [Arquivos](#8-arquivos)
9. [Rotas](#9-rotas)
10. [Usuários](#10-usuários)
11. [Logs de Atividade](#11-logs-de-atividade)

---

## Autenticação

Todos os endpoints (exceto login) requerem autenticação via Bearer Token.

### Header de Autenticação

```
Authorization: Bearer {token_base64}
```

O token é um JSON codificado em Base64 contendo:
- `user_id`: ID do usuário
- `company_id`: ID da empresa
- `expires_at`: Timestamp de expiração

---

## 1. Autenticação

### POST /auth/login

Realiza login do usuário e retorna token de acesso.

**Autenticação:** Não requerida

**Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Resposta (200):**
```json
{
  "success": true,
  "token": "base64_encoded_token",
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "role": "admin",
    "company_id": 1
  },
  "expires_at": 1736200000
}
```

---

### GET /auth/me

Retorna informações do usuário autenticado.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "role": "admin",
  "company_id": 1,
  "status": "active"
}
```

---

### POST /auth/refresh

Renova o token de acesso.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "success": true,
  "token": "new_base64_encoded_token",
  "expires_at": 1736200000
}
```

---

### POST /auth/logout

Realiza logout do usuário.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "success": true,
  "message": "Logout realizado com sucesso"
}
```

---

### GET /auth/info

Retorna informações da API.

**Autenticação:** Não requerida

**Resposta (200):**
```json
{
  "name": "VeiGest API",
  "version": "1.0.0",
  "description": "API de gestão de frotas"
}
```

---

## 2. Empresas

### GET /companies

Lista todas as empresas (apenas admin).

**Autenticação:** Requerida (permissão: company.view)

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| status | string | Filtrar por status (active, inactive) |
| search | string | Buscar por nome, email ou NIF |
| page | int | Número da página |
| per-page | int | Itens por página |

**Resposta (200):**
```json
{
  "items": [
    {
      "id": 1,
      "code": "EMP001",
      "nome": "Empresa Exemplo",
      "email": "contato@empresa.com",
      "telefone": "+351 123 456 789",
      "nif": "123456789",
      "morada": "Rua Principal, 123",
      "cidade": "Lisboa",
      "codigo_postal": "1000-001",
      "pais": "Portugal",
      "estado": "active",
      "created_at": "2025-01-01 10:00:00",
      "updated_at": "2025-01-01 10:00:00"
    }
  ],
  "_meta": {
    "totalCount": 10,
    "pageCount": 1,
    "currentPage": 1,
    "perPage": 20
  }
}
```

---

### GET /companies/{id}

Visualiza uma empresa específica.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID da empresa |

**Resposta (200):**
```json
{
  "id": 1,
  "code": "EMP001",
  "nome": "Empresa Exemplo",
  "email": "contato@empresa.com",
  "telefone": "+351 123 456 789",
  "nif": "123456789",
  "morada": "Rua Principal, 123",
  "cidade": "Lisboa",
  "codigo_postal": "1000-001",
  "pais": "Portugal",
  "estado": "active"
}
```

---

### POST /companies

Cria uma nova empresa.

**Autenticação:** Requerida (permissão: company.create)

**Body:**
```json
{
  "nome": "Nova Empresa",
  "email": "nova@empresa.com",
  "telefone": "+351 987 654 321",
  "nif": "987654321",
  "morada": "Rua Nova, 456",
  "cidade": "Porto",
  "codigo_postal": "4000-001",
  "pais": "Portugal"
}
```

**Resposta (201):**
```json
{
  "id": 2,
  "code": "EMP002",
  "nome": "Nova Empresa",
  "email": "nova@empresa.com",
  "estado": "active"
}
```

---

### PUT /companies/{id}

Atualiza uma empresa existente.

**Autenticação:** Requerida (permissão: company.update)

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID da empresa |

**Body:**
```json
{
  "nome": "Empresa Atualizada",
  "telefone": "+351 111 222 333"
}
```

---

### GET /companies/{id}/vehicles

Lista veículos de uma empresa.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID da empresa |

---

### GET /companies/{id}/users

Lista usuários de uma empresa.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID da empresa |

---

### GET /companies/{id}/stats

Retorna estatísticas de uma empresa.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID da empresa |

**Resposta (200):**
```json
{
  "vehicles_count": 25,
  "active_vehicles": 20,
  "users_count": 15,
  "maintenances_pending": 5,
  "alerts_active": 3
}
```

---

## 3. Veículos

### GET /vehicles

Lista todos os veículos da empresa do usuário.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| status | string | Filtrar por status (active, inactive, maintenance) |
| fuel_type | string | Filtrar por tipo de combustível |
| search | string | Buscar por matrícula, marca ou modelo |
| page | int | Número da página |
| per-page | int | Itens por página |
| expand | string | Campos expandidos (company, driver, maintenances, fuelLogs) |

**Resposta (200):**
```json
{
  "items": [
    {
      "id": 1,
      "company_id": 1,
      "license_plate": "AA-00-AA",
      "brand": "Toyota",
      "model": "Corolla",
      "year": 2022,
      "fuel_type": "diesel",
      "fuel_type_label": "Diesel",
      "mileage": 50000,
      "status": "active",
      "status_label": "Ativo",
      "driver_id": 5,
      "driver_name": "João Silva",
      "photo": "/uploads/vehicles/car1.jpg",
      "created_at": "2025-01-01 10:00:00",
      "updated_at": "2025-06-15 14:30:00"
    }
  ],
  "_meta": {
    "totalCount": 25,
    "pageCount": 2,
    "currentPage": 1,
    "perPage": 20
  }
}
```

---

### GET /vehicles/{id}

Visualiza um veículo específico.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID do veículo |

---

### POST /vehicles

Cria um novo veículo.

**Autenticação:** Requerida

**Body:**
```json
{
  "license_plate": "BB-11-BB",
  "brand": "Ford",
  "model": "Focus",
  "year": 2023,
  "fuel_type": "gasoline",
  "mileage": 0,
  "status": "active",
  "driver_id": 3
}
```

**Resposta (201):**
```json
{
  "id": 26,
  "company_id": 1,
  "license_plate": "BB-11-BB",
  "brand": "Ford",
  "model": "Focus",
  "year": 2023,
  "fuel_type": "gasoline",
  "status": "active"
}
```

---

### PUT /vehicles/{id}

Atualiza um veículo existente.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID do veículo |

**Body:**
```json
{
  "mileage": 55000,
  "status": "maintenance"
}
```

---

### DELETE /vehicles/{id}

Remove um veículo.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID do veículo |

---

### GET /vehicles/{id}/maintenances

Lista manutenções de um veículo.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID do veículo |

---

### GET /vehicles/{id}/fuel-logs

Lista abastecimentos de um veículo.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID do veículo |

---

### GET /vehicles/{id}/stats

Retorna estatísticas de um veículo.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID do veículo |

**Resposta (200):**
```json
{
  "total_maintenances": 12,
  "total_fuel_logs": 45,
  "total_maintenance_cost": 2500.00,
  "total_fuel_cost": 3200.00,
  "average_consumption": 6.5
}
```

---

### GET /vehicles/by-status/{status}

Lista veículos por status.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| status | string | Status do veículo (active, inactive, maintenance) |

---

## 4. Manutenções

### GET /maintenances

Lista todas as manutenções da empresa.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| vehicle_id | int | Filtrar por veículo |
| status | string | Filtrar por status (scheduled, completed, cancelled) |
| type | string | Filtrar por tipo |
| date_from | date | Data inicial |
| date_to | date | Data final |
| page | int | Número da página |
| per-page | int | Itens por página |
| expand | string | Campos expandidos (vehicle) |

**Resposta (200):**
```json
{
  "items": [
    {
      "id": 1,
      "company_id": 1,
      "vehicle_id": 5,
      "type": "preventive",
      "type_label": "Preventiva",
      "description": "Troca de óleo e filtros",
      "cost": 150.00,
      "date": "2025-12-15",
      "mileage_record": 48000,
      "next_date": "2026-06-15",
      "workshop": "Oficina Central",
      "status": "completed",
      "status_label": "Concluída",
      "created_at": "2025-12-10 09:00:00",
      "updated_at": "2025-12-15 16:00:00"
    }
  ],
  "_meta": {
    "totalCount": 50,
    "pageCount": 3,
    "currentPage": 1,
    "perPage": 20
  }
}
```

---

### GET /maintenances/{id}

Visualiza uma manutenção específica.

**Autenticação:** Requerida

---

### POST /maintenances

Cria uma nova manutenção.

**Autenticação:** Requerida

**Body:**
```json
{
  "vehicle_id": 5,
  "type": "corrective",
  "description": "Substituição de pastilhas de freio",
  "cost": 250.00,
  "date": "2026-01-10",
  "mileage_record": 52000,
  "next_date": null,
  "workshop": "Oficina Express",
  "status": "scheduled"
}
```

---

### PUT /maintenances/{id}

Atualiza uma manutenção existente.

**Autenticação:** Requerida

---

### DELETE /maintenances/{id}

Remove uma manutenção.

**Autenticação:** Requerida

---

### GET /maintenances/by-vehicle/{vehicle_id}

Lista manutenções de um veículo específico.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| vehicle_id | int | ID do veículo |

---

### GET /maintenances/by-status/{estado}

Lista manutenções por status.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| estado | string | Status da manutenção |

---

### POST /maintenances/{id}/schedule

Agenda uma manutenção.

**Autenticação:** Requerida

**Body:**
```json
{
  "date": "2026-02-01",
  "workshop": "Oficina Autorizada"
}
```

---

### GET /maintenances/reports/monthly

Relatório mensal de manutenções.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| year | int | Ano do relatório |
| month | int | Mês do relatório |

**Resposta (200):**
```json
{
  "period": "2025-12",
  "total_maintenances": 15,
  "total_cost": 3500.00,
  "by_type": {
    "preventive": 10,
    "corrective": 5
  },
  "by_vehicle": [
    {
      "vehicle_id": 5,
      "license_plate": "AA-00-AA",
      "count": 3,
      "cost": 450.00
    }
  ]
}
```

---

### GET /maintenances/reports/costs

Relatório de custos de manutenção.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| date_from | date | Data inicial |
| date_to | date | Data final |

---

### GET /maintenances/stats

Estatísticas gerais de manutenções.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "total": 120,
  "scheduled": 8,
  "completed": 100,
  "cancelled": 12,
  "total_cost": 25000.00,
  "average_cost": 208.33,
  "by_type": {
    "preventive": 80,
    "corrective": 35,
    "inspection": 5
  }
}
```

---

## 5. Abastecimentos

### GET /fuel-logs

Lista todos os registros de abastecimento.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| vehicle_id | int | Filtrar por veículo |
| date_from | date | Data inicial |
| date_to | date | Data final |
| page | int | Número da página |
| per-page | int | Itens por página |
| expand | string | Campos expandidos (vehicle) |

**Resposta (200):**
```json
{
  "items": [
    {
      "id": 1,
      "vehicle_id": 5,
      "liters": 45.5,
      "value": 82.00,
      "current_mileage": 52000,
      "date": "2026-01-05",
      "price_per_liter": 1.80,
      "notes": "Posto Shell - IC19",
      "created_at": "2026-01-05 14:30:00",
      "updated_at": "2026-01-05 14:30:00"
    }
  ],
  "_meta": {
    "totalCount": 200,
    "pageCount": 10,
    "currentPage": 1,
    "perPage": 20
  }
}
```

---

### GET /fuel-logs/{id}

Visualiza um registro de abastecimento.

**Autenticação:** Requerida

---

### POST /fuel-logs

Cria um novo registro de abastecimento.

**Autenticação:** Requerida

**Body:**
```json
{
  "vehicle_id": 5,
  "liters": 50.0,
  "value": 90.00,
  "current_mileage": 52500,
  "date": "2026-01-06",
  "notes": "Abastecimento completo"
}
```

---

### PUT /fuel-logs/{id}

Atualiza um registro de abastecimento.

**Autenticação:** Requerida

---

### DELETE /fuel-logs/{id}

Remove um registro de abastecimento.

**Autenticação:** Requerida

---

### GET /fuel-logs/by-vehicle/{vehicle_id}

Lista abastecimentos de um veículo.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| vehicle_id | int | ID do veículo |

---

### GET /fuel-logs/stats

Estatísticas de abastecimentos.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "total_liters": 5000.00,
  "total_value": 9000.00,
  "average_price_per_liter": 1.80,
  "average_consumption": 6.5,
  "cost_per_km": 0.12,
  "by_vehicle": [
    {
      "vehicle_id": 5,
      "license_plate": "AA-00-AA",
      "total_liters": 500.00,
      "total_value": 900.00,
      "average_consumption": 6.2
    }
  ]
}
```

---

### GET /fuel-logs/alerts

Alertas de consumo de combustível.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "high_consumption": [
    {
      "vehicle_id": 8,
      "license_plate": "CC-22-CC",
      "average_consumption": 12.5,
      "threshold": 10.0
    }
  ],
  "price_anomalies": [
    {
      "fuel_log_id": 150,
      "price_per_liter": 2.50,
      "average_price": 1.80
    }
  ]
}
```

---

### GET /fuel-logs/efficiency-report

Relatório de eficiência de combustível.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| period | string | Período (week, month, quarter, year) |

---

## 6. Alertas

### GET /alerts

Lista todos os alertas da empresa.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| type | string | Filtrar por tipo (maintenance, document, fuel, other) |
| priority | string | Filtrar por prioridade (low, medium, high, critical) |
| status | string | Filtrar por status (active, resolved, ignored) |
| page | int | Número da página |
| per-page | int | Itens por página |

**Resposta (200):**
```json
{
  "items": [
    {
      "id": 1,
      "company_id": 1,
      "type": "maintenance",
      "type_label": "Manutenção",
      "title": "Manutenção preventiva vencida",
      "description": "Veículo AA-00-AA necessita de revisão",
      "priority": "high",
      "priority_label": "Alta",
      "priority_level": 3,
      "status": "active",
      "status_label": "Ativo",
      "details": "{\"vehicle_id\": 5, \"maintenance_type\": \"preventive\"}",
      "age": "2 dias",
      "created_at": "2026-01-04 10:00:00",
      "resolved_at": null
    }
  ]
}
```

---

### GET /alerts/{id}

Visualiza um alerta específico.

**Autenticação:** Requerida

---

### POST /alerts

Cria um novo alerta.

**Autenticação:** Requerida

**Body:**
```json
{
  "type": "document",
  "title": "Documento a expirar",
  "description": "Seguro do veículo BB-11-BB expira em 7 dias",
  "priority": "medium",
  "details": {
    "vehicle_id": 6,
    "document_id": 15,
    "expiry_date": "2026-01-13"
  }
}
```

---

### PUT /alerts/{id}

Atualiza um alerta existente.

**Autenticação:** Requerida

---

### DELETE /alerts/{id}

Remove um alerta.

**Autenticação:** Requerida

---

### POST /alerts/{id}/resolve

Marca um alerta como resolvido.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "id": 1,
  "status": "resolved",
  "resolved_at": "2026-01-06 15:30:00"
}
```

---

### POST /alerts/{id}/ignore

Ignora um alerta.

**Autenticação:** Requerida

---

### GET /alerts/by-type/{type}

Lista alertas por tipo.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| type | string | Tipo do alerta (maintenance, document, fuel, other) |

---

### GET /alerts/by-priority/{priority}

Lista alertas por prioridade.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| priority | string | Prioridade (low, medium, high, critical) |

---

### GET /alerts/count

Contagem de alertas por status.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "total": 25,
  "active": 10,
  "resolved": 12,
  "ignored": 3
}
```

---

### GET /alerts/stats

Estatísticas de alertas.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "total": 150,
  "by_type": {
    "maintenance": 60,
    "document": 45,
    "fuel": 30,
    "other": 15
  },
  "by_priority": {
    "critical": 5,
    "high": 20,
    "medium": 75,
    "low": 50
  },
  "by_status": {
    "active": 25,
    "resolved": 100,
    "ignored": 25
  },
  "average_resolution_time": "2.5 days"
}
```

---

### GET /alerts/types

Lista tipos de alerta disponíveis.

**Autenticação:** Requerida

**Resposta (200):**
```json
["maintenance", "document", "fuel", "other"]
```

---

### GET /alerts/priorities

Lista prioridades disponíveis.

**Autenticação:** Requerida

**Resposta (200):**
```json
["low", "medium", "high", "critical"]
```

---

### POST /alerts/bulk-resolve

Resolve múltiplos alertas de uma vez.

**Autenticação:** Requerida

**Body:**
```json
{
  "ids": [1, 2, 3, 5, 8]
}
```

---

### POST /alerts/{id}/broadcast

Transmite alerta via MQTT.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID do alerta |

---

## 7. Documentos

### GET /documents

Lista todos os documentos da empresa.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| type | string | Filtrar por tipo |
| vehicle_id | int | Filtrar por veículo |
| driver_id | int | Filtrar por condutor |
| status | string | Filtrar por status (valid, expired) |
| page | int | Número da página |
| per-page | int | Itens por página |
| expand | string | Campos expandidos (company, file, vehicle, driver) |

**Resposta (200):**
```json
{
  "items": [
    {
      "id": 1,
      "company_id": 1,
      "file_id": 10,
      "vehicle_id": 5,
      "driver_id": null,
      "type": "insurance",
      "type_label": "Seguro",
      "expiry_date": "2026-06-30",
      "days_to_expiry": 175,
      "status": "valid",
      "notes": "Seguro contra todos os riscos",
      "created_at": "2025-07-01 10:00:00",
      "updated_at": "2025-07-01 10:00:00"
    }
  ]
}
```

---

### GET /documents/{id}

Visualiza um documento específico.

**Autenticação:** Requerida

---

### POST /documents

Cria um novo documento.

**Autenticação:** Requerida

**Body:**
```json
{
  "file_id": 15,
  "vehicle_id": 5,
  "type": "registration",
  "expiry_date": "2027-01-01",
  "notes": "Documento Único Automóvel"
}
```

---

### PUT /documents/{id}

Atualiza um documento existente.

**Autenticação:** Requerida

---

### DELETE /documents/{id}

Remove um documento.

**Autenticação:** Requerida

---

### GET /documents/by-vehicle/{vehicle_id}

Lista documentos de um veículo.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| vehicle_id | int | ID do veículo |

---

### GET /documents/by-driver/{driver_id}

Lista documentos de um condutor.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| driver_id | int | ID do condutor |

---

### GET /documents/expiring

Lista documentos próximos de expirar.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| days | int | Dias até expiração (default: 30) |

---

### GET /documents/expired

Lista documentos expirados.

**Autenticação:** Requerida

---

### GET /documents/stats

Estatísticas de documentos.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "total": 100,
  "valid": 85,
  "expired": 10,
  "expiring_soon": 5,
  "by_type": {
    "registration": 25,
    "insurance": 25,
    "inspection": 25,
    "license": 20,
    "other": 5
  }
}
```

---

### GET /documents/types

Lista tipos de documento disponíveis.

**Autenticação:** Requerida

**Resposta (200):**
```json
[
  {"value": "registration", "label": "Registo"},
  {"value": "insurance", "label": "Seguro"},
  {"value": "inspection", "label": "Inspeção"},
  {"value": "license", "label": "Carta de Condução"},
  {"value": "other", "label": "Outro"}
]
```

---

## 8. Arquivos

### GET /files

Lista todos os arquivos da empresa.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| uploaded_by | int | Filtrar por usuário que fez upload |
| extension | string | Filtrar por extensão |
| search | string | Buscar por nome |
| sort | string | Ordenação (created_at, -created_at, size, -size) |
| page | int | Número da página |
| per-page | int | Itens por página |

**Resposta (200):**
```json
{
  "items": [
    {
      "id": 1,
      "company_id": 1,
      "original_name": "seguro_veiculo.pdf",
      "size": 1024000,
      "size_formatted": "1 MB",
      "path": "/uploads/files/abc123.pdf",
      "extension": "pdf",
      "uploaded_by": 3,
      "created_at": "2025-12-01 10:00:00"
    }
  ]
}
```

---

### GET /files/{id}

Visualiza um arquivo específico.

**Autenticação:** Requerida

---

### POST /files

Faz upload de um novo arquivo.

**Autenticação:** Requerida

**Content-Type:** multipart/form-data

**Body:**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| file | file | Arquivo a ser enviado |

---

### DELETE /files/{id}

Remove um arquivo.

**Autenticação:** Requerida

---

### GET /files/stats

Estatísticas de arquivos.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "total_files": 150,
  "total_size": 524288000,
  "total_size_formatted": "500 MB",
  "by_extension": {
    "pdf": 80,
    "jpg": 40,
    "png": 20,
    "docx": 10
  }
}
```

---

## 9. Rotas

### GET /routes

Lista todas as rotas da empresa.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| vehicle_id | int | Filtrar por veículo |
| driver_id | int | Filtrar por condutor |
| status | string | Filtrar por status (scheduled, in_progress, completed) |
| date_from | date | Data inicial |
| date_to | date | Data final |
| page | int | Número da página |
| per-page | int | Itens por página |
| expand | string | Campos expandidos (company, vehicle, driver) |

**Resposta (200):**
```json
{
  "items": [
    {
      "id": 1,
      "company_id": 1,
      "vehicle_id": 5,
      "driver_id": 3,
      "start_location": "Lisboa",
      "end_location": "Porto",
      "start_time": "2026-01-06 08:00:00",
      "end_time": "2026-01-06 12:00:00",
      "status": "scheduled",
      "status_label": "Agendada",
      "duration": 14400,
      "duration_formatted": "4h 00m",
      "created_at": "2026-01-05 15:00:00",
      "updated_at": "2026-01-05 15:00:00"
    }
  ]
}
```

---

### GET /routes/{id}

Visualiza uma rota específica.

**Autenticação:** Requerida

---

### POST /routes

Cria uma nova rota.

**Autenticação:** Requerida

**Body:**
```json
{
  "vehicle_id": 5,
  "driver_id": 3,
  "start_location": "Lisboa",
  "end_location": "Faro",
  "start_time": "2026-01-07 06:00:00",
  "end_time": "2026-01-07 10:00:00"
}
```

---

### PUT /routes/{id}

Atualiza uma rota existente.

**Autenticação:** Requerida

---

### DELETE /routes/{id}

Remove uma rota.

**Autenticação:** Requerida

---

### POST /routes/{id}/complete

Marca uma rota como concluída.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| id | int | ID da rota |

---

### GET /routes/by-vehicle/{vehicle_id}

Lista rotas de um veículo.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| vehicle_id | int | ID do veículo |

---

### GET /routes/by-driver/{driver_id}

Lista rotas de um condutor.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| driver_id | int | ID do condutor |

---

### GET /routes/active

Lista rotas em andamento.

**Autenticação:** Requerida

---

### GET /routes/scheduled

Lista rotas agendadas.

**Autenticação:** Requerida

---

### GET /routes/stats

Estatísticas de rotas.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "total": 500,
  "completed": 450,
  "in_progress": 10,
  "scheduled": 40,
  "total_distance": 50000,
  "average_duration": 10800,
  "average_duration_formatted": "3h 00m",
  "by_vehicle": [
    {
      "vehicle_id": 5,
      "license_plate": "AA-00-AA",
      "total_routes": 100,
      "total_distance": 10000
    }
  ],
  "by_driver": [
    {
      "driver_id": 3,
      "driver_name": "João Silva",
      "total_routes": 80
    }
  ]
}
```

---

## 10. Usuários

### GET /users

Lista todos os usuários da empresa.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| role | string | Filtrar por papel (admin, manager, driver) |
| status | string | Filtrar por status (active, inactive) |
| search | string | Buscar por nome, username ou email |
| page | int | Número da página |
| per-page | int | Itens por página |

**Resposta (200):**
```json
{
  "items": [
    {
      "id": 1,
      "username": "admin",
      "email": "admin@empresa.com",
      "role": "admin",
      "status": "active",
      "company_id": 1,
      "created_at": "2025-01-01 10:00:00",
      "updated_at": "2025-06-15 14:00:00"
    }
  ]
}
```

---

### GET /users/{id}

Visualiza um usuário específico.

**Autenticação:** Requerida

---

### POST /users

Cria um novo usuário.

**Autenticação:** Requerida (permissão: user.create)

**Body:**
```json
{
  "username": "novousuario",
  "email": "novo@empresa.com",
  "password": "senha123",
  "role": "driver",
  "status": "active"
}
```

---

### PUT /users/{id}

Atualiza um usuário existente.

**Autenticação:** Requerida (permissão: user.update)

**Body:**
```json
{
  "email": "atualizado@empresa.com",
  "role": "manager"
}
```

---

### DELETE /users/{id}

Remove um usuário.

**Autenticação:** Requerida (permissão: user.delete)

---

### GET /users/drivers

Lista apenas condutores.

**Autenticação:** Requerida

---

### GET /users/profile

Retorna perfil do usuário autenticado.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "id": 3,
  "username": "joaosilva",
  "email": "joao@empresa.com",
  "role": "driver",
  "company_id": 1,
  "company_name": "Empresa Exemplo",
  "status": "active",
  "assigned_vehicle": {
    "id": 5,
    "license_plate": "AA-00-AA",
    "brand": "Toyota",
    "model": "Corolla"
  }
}
```

---

### GET /users/by-company/{company_id}

Lista usuários de uma empresa específica.

**Autenticação:** Requerida (permissão: user.view)

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| company_id | int | ID da empresa |

---

### POST /users/{id}/update-photo

Atualiza foto de perfil do usuário.

**Autenticação:** Requerida

**Content-Type:** multipart/form-data

**Body:**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| photo | file | Imagem de perfil |

---

## 11. Logs de Atividade

### GET /activity-logs

Lista logs de atividade da empresa.

**Autenticação:** Requerida

**Parâmetros Query:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| user_id | int | Filtrar por usuário |
| action | string | Filtrar por ação |
| entity | string | Filtrar por entidade |
| date_from | date | Data inicial |
| date_to | date | Data final |
| page | int | Número da página |
| per-page | int | Itens por página |

**Resposta (200):**
```json
{
  "items": [
    {
      "id": 1,
      "company_id": 1,
      "user_id": 3,
      "user_name": "João Silva",
      "action": "create",
      "action_label": "Criou",
      "entity": "vehicle",
      "entity_label": "Veículo",
      "entity_id": 26,
      "details": "{\"license_plate\": \"CC-33-CC\"}",
      "ip": "192.168.1.100",
      "time_ago": "há 2 horas",
      "created_at": "2026-01-06 10:30:00"
    }
  ]
}
```

---

### GET /activity-logs/{id}

Visualiza um log de atividade específico.

**Autenticação:** Requerida

---

### GET /activity-logs/by-user/{user_id}

Lista logs de um usuário específico.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| user_id | int | ID do usuário |

---

### GET /activity-logs/by-entity/{entity}/{entity_id}

Lista logs de uma entidade específica.

**Autenticação:** Requerida

**Parâmetros Path:**
| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| entity | string | Tipo da entidade (vehicle, user, maintenance, etc.) |
| entity_id | int | ID da entidade |

---

### GET /activity-logs/recent

Lista logs recentes (últimas 24h).

**Autenticação:** Requerida

---

### GET /activity-logs/stats

Estatísticas de logs de atividade.

**Autenticação:** Requerida

**Resposta (200):**
```json
{
  "total": 5000,
  "today": 50,
  "this_week": 350,
  "this_month": 1500,
  "by_action": {
    "create": 1500,
    "update": 2000,
    "delete": 300,
    "view": 1000,
    "login": 150,
    "logout": 50
  },
  "by_entity": {
    "vehicle": 1200,
    "maintenance": 800,
    "fuel_log": 1500,
    "document": 500,
    "user": 300,
    "alert": 400,
    "route": 300
  },
  "most_active_users": [
    {
      "user_id": 3,
      "user_name": "João Silva",
      "actions_count": 500
    }
  ]
}
```

---

### GET /activity-logs/actions

Lista ações disponíveis.

**Autenticação:** Requerida

**Resposta (200):**
```json
["create", "update", "delete", "view", "login", "logout", "upload", "download", "export"]
```

---

### GET /activity-logs/entities

Lista entidades disponíveis.

**Autenticação:** Requerida

**Resposta (200):**
```json
["user", "vehicle", "maintenance", "document", "fuel_log", "alert", "file", "route", "company"]
```

---

### POST /activity-logs

Cria um novo log de atividade (uso interno).

**Autenticação:** Requerida

**Body:**
```json
{
  "action": "export",
  "entity": "vehicle",
  "entity_id": null,
  "details": "{\"format\": \"pdf\", \"records\": 25}"
}
```

---

## Códigos de Resposta HTTP

| Código | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 204 | No Content - Requisição bem-sucedida sem conteúdo |
| 400 | Bad Request - Dados inválidos |
| 401 | Unauthorized - Autenticação necessária ou inválida |
| 403 | Forbidden - Acesso negado |
| 404 | Not Found - Recurso não encontrado |
| 422 | Unprocessable Entity - Erro de validação |
| 500 | Internal Server Error - Erro interno do servidor |

---

## Estrutura de Erro

```json
{
  "success": false,
  "message": "Descrição do erro",
  "code": 400,
  "errors": [
    {
      "field": "email",
      "message": "Email é obrigatório"
    }
  ]
}
```

---

## Paginação

Todas as listagens suportam paginação com os seguintes parâmetros:

| Parâmetro | Tipo | Default | Descrição |
|-----------|------|---------|-----------|
| page | int | 1 | Número da página |
| per-page | int | 20 | Itens por página (máx: 100) |

A resposta inclui metadados de paginação:

```json
{
  "items": [...],
  "_meta": {
    "totalCount": 100,
    "pageCount": 5,
    "currentPage": 1,
    "perPage": 20
  },
  "_links": {
    "self": {"href": "/api/v1/vehicles?page=1"},
    "next": {"href": "/api/v1/vehicles?page=2"},
    "last": {"href": "/api/v1/vehicles?page=5"}
  }
}
```

---

## Expansão de Relacionamentos

Use o parâmetro `expand` para incluir dados relacionados:

```
GET /api/v1/vehicles?expand=company,driver,maintenances
```

---

## Ordenação

Use o parâmetro `sort` para ordenar resultados:

- `sort=created_at` - Ordem ascendente
- `sort=-created_at` - Ordem descendente (prefixo `-`)

Múltiplos campos: `sort=-created_at,name`

---

**Documento gerado automaticamente para a API VeiGest v1.0.0**
