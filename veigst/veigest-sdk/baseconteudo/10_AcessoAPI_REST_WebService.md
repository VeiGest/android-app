DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
CAP 10
CARACTERÍSTICAS DE UMA API REST E REQUISITOS DE
ACESSO COM APLICAÇÕES ANDROID
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026
TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA 1
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
CARACTERÍSTICAS DE UMA API REST
DEFINIÇÃO DE API
● API – Application Programming Interface
(Interface de Programação de Aplicações)
● Corresponde a um conjunto de rotinas e padrões estabelecidos e
documentados por uma aplicação A;
● Para que outras aplicações consigam utilizar as funcionalidades desta
aplicação A;
● Sem precisar conhecer detalhes da implementação do software;
2DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
CARACTERÍSTICAS DE UMA API REST
DEFINIÇÃO DE API
● As APIs permitem uma interoperabilidade entre aplicações
● Ou seja, a comunicação entre aplicações e entre os utilizadores;
● O que reforça ainda mais a importância de pensarmos em algo padronizado;
● De fácil representação e compreensão por humanos e máquinas;
● Podem fornecem diversas formas de representação:
● XML; JSON; YAML;
3DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
CARACTERÍSTICAS DE UMA API REST
API REST
● REST – Representational State Transfer
(Transferência de Estado Transacional)
● É uma abstração da arquitetura da Web;
● Consiste em princípios/regras/constraints que, quando seguidas,
permitem a criação de um projeto com interfaces bem definidas;
● Desta forma, permitindo, por exemplo, que aplicações comuniquem entre si;
4DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
CARACTERÍSTICAS DE UMA API REST
REST E RESTFUL
● Diferença entre REST e RESTful
● Ambos representam os mesmos princípios;
● Diferença é apenas gramatical;
● Ou seja, sistemas que utilizam os princípios REST são designados de RESTful.
❑ REST: conjunto de princípios de arquitetura;
❑ RESTful: capacidade de determinado sistema aplicar os princípios de REST;
5DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
CARACTERÍSTICAS DE UMA API REST
API REST
● Princípios API REST
● Um protocolo cliente/servidor sem estado:
● Cada mensagem HTTP contém toda a informação necessária para compreender o pedido.
● Como resultado: nem o cliente, nem o servidor necessitam gravar nenhum estado das
comunicações entre mensagens.
● Na prática, muitas aplicações baseadas em HTTP utilizam cookies e outros mecanismos para
manter o estado da sessão (algumas destas práticas, como a reescrita de URLs, não são
permitidas pela regra do REST).
6DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
CARACTERÍSTICAS DE UMA API REST
API REST
● Princípios API REST
● Um conjunto de operações bem
definidas que se aplicam a todos os
recursos de informação:
● Protocolo HTTP (POST, GET, PUT …)
7
Método HTTP Operação
POST Criar
GET Ler
PUT Atualizar
DELETE Apagar
PATCH Atualizar parcialmente
HEADER Mostrar header
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
CARACTERÍSTICAS DE UMA API REST
API REST
● Web Service REST
● O principal num web service RESTful são:
● Os URL’s do sistema;
● Os Resources;
● Um Resource é um recurso, uma entidade, ou seja, um objeto com informação
que será representada em XML;
8DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
CARACTERÍSTICAS DE UMA API REST
API REST
● Web Service REST
● No geral o URL para aceder ao recurso será sempre o mesmo;
● Mas se mudarmos o método HTTP, o resultado do pedido será diferente;
9
Método exemplo.com/recurso Exemplo.com/recurso/1
GET Lista de recursos Detalhe de um recurso
POST Adiciona um recurso -
PUT - Atualiza um recurso
DELETE - Remove um recurso
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
CARACTERÍSTICAS DE UMA API REST
FORMATOS DE DADOS
● Formato de dados: JSON x XML
● São formatos para a notação de dados a serem transmitidos;
● Principal diferença é o espaço que ocupam
● O XML ocupa muito mais espaço que o JSON
● Quando representam o mesmo objeto;
● Porque o XML usa uma tag para identificar o início e o fim de cada nó;
● O JSON é muito recomendado quando falamos de dispositivos móveis:
● Porque consome menos largura de banda da ligação de internet do utilizador;
10DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
CARACTERÍSTICAS DE UMA API REST
JSON
● JSON
● JavaScript Object Notation - é uma estrutura de dados para armazenamento e
transmissão segura de informações no formato texto;
● Apesar de muito simples, tem sido bastante utilizado por aplicações Web e Mobile;
● Devido à sua capacidade de estruturar informações de uma forma compacta e leve,
tornando mais rápida a leitura dessas informações;
● Para cada valor representado, atribui-se um nome que descreve o seu significado.
11DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
BIBLIOTECAS API REST
● Bibliotecas para acesso a API REST
● Spring Android
● ION
● Retrofit
● Android Volley
12DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
VOLLEY
● Biblioteca Volley
● Divulgado no Google I/O 2013;
● Permite programação automática de pedidos de rede;
● Várias ligações de rede simultâneas;
● Armazenamento em cache de resposta de memória e memória transparente
● Com coerência de cache HTTP padrão;
● Suporte para priorização de solicitação;
13DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
VOLLEY
● Biblioteca Volley
● API de solicitação de cancelamento;
● Sendo possível cancelar um único pedido;
● Configurar blocos de solicitações para cancelar;
● Facilidade de personalização;
● Ordenação que facilita o preenchimento correto da UI com dados obtidos de
forma assíncrona da rede.
● Ferramentas de depuração e tracing;
14DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
VOLLEY
● Biblioteca Volley
● Existem diferentes tipos de pedidos de objetos;
● Os mais importantes são:
● StringRequest;
● JsonObjectRequest;
● JsonArrayRequest;
● ImageRequest.
15DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
VOLLEY
● Definir o acesso a uma API REST através do Volley:
● Adicionar a biblioteca Volley ao projeto
● Colocando como dependência no ficheiro build.gradle ou build.gradle.kts
● Adicionar permissões de acesso a internet no AndroidManifest.xml
● Caso o servidor não contenha HTTPS devem adicionar no AndroidManifest.xml
16DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
dependencies {
...
implementation (“com.android.volley:volley:1.2.1”)
}
<uses-permission android:name="android.permission.INTERNET" />
<application android:usesCleartextTraffic=“true” />
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
VOLLEY
● Definir o acesso a uma API REST através do Volley:
● Definir uma RequestQueue através do método Volley.newRequestQueue()
● Que usa valores por omissão e inicia a fila;
● Ou definir uma classe Singleton
● Que faça o encapsulamento da RequestQueue
● E garanta apenas uma única instância da mesma;
17DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
ACESSO À INTERNET
● Verificar se existe ligação de internet?
● Definir permissões no AndroidManifest.xml
● Utilizar a classe ConnectivityManager
● Para consultar a rede ativa;
● Determinar se ela tem conectividade com a internet;
● Utilizar a classe NetworkInfo
● Para guardar a ligação ativa;
● Determinar o tipo de ligação;
18DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
VOLLEY
● Exemplo:
● Aplicação para definir acesso a uma API REST efetuando um pedido
simples:
● Através de JsonObjetRequest;
● Através de JsonArrayRequest;
19DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
DESAFIO
● Criar uma aplicação com ligação de rede para acesso a uma API REST
● Usando o url que contém dados falsos,
● Efetuar pedidos simples através de:
● StringRequest;
● JsonObjectRequest;
● JsonArrayRequest;
20DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
FONTES E MAIS INFORMAÇÃO
 Opções de Armazenamento
 https://developer.android.com/training/data-
storage
 Armazenamento recorrendo a Ligação de
Rede
 https://developer.android.com/guide/topics/connec
tivity
 Introdução ao JSON
 http://www.json.org/json-pt.html
 Classes JSON disponíveis no Android Studio
 https://developer.android.com/reference/org/json/
package-summary.html
21DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
 Transmitting Network Data Using Volley
 https://developer.android.com/training/volle
y/index.html
 Making a Standard Request
 https://developer.android.com/training/volle
y/request.html
 https://google.github.io/volley/request.html
 Determinar Ligação a Internet
 https://developer.android.com/training/moni
toring-device-state/connectivity-status-type
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 10 – CARACTERÍSTICAS DE UMA API REST E REQUISITOS
DE ACESSO COM APPLICAÇÕES ANDROID
PRÓXIMO TEMA:
Serviços e Tarefas Assíncronas
 Processos e Threads
 https://developer.android.com/guide/components/processes-and-threads.html
 Serviços
 https://developer.android.com/guide/components/services.html
 AsyncTask
 https://developer.android.com/reference/android/os/AsyncTask.html
 Android Volley
 https://developer.android.com/training/volley/index.html
 Android Volley vs AsyncTask
 http://www.truiton.com/2015/02/android-volley-vs-asynctask-better-approach/
22DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
BOM TRABALHO !
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026
TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA 23