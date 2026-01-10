DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
CAP 11
#SERVIÇOS E TAREFAS ASSÍNCRONAS
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026
TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
PROCESSOS EM ANDROID
● Processo Linux com uma thread
● Contém até 4 componentes:
● Atividades;
● Serviços;
● Fornecedores de conteúdo (content providers);
● Recetores Broadcast (broadcast receivers);
2
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
PROCESSOS EM ANDROID
3
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
MAIN-THREAD
● Thread inicial da aplicação;
● Gere mensagens entre componentes (eventos);
● Responsável por “desenhar” a UI;
4
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
CÓDIGO NA MAIN-THREAD
● Iniciar outras threads;
● Usar serviços para pedidos muito longos;
● Tem de ser rápido;
● Não pode bloquear (acesso a ficheiros, BD, rede);
● Atrasos resultam em:
● APP lenta a responder (UI bloqueada);
● APP terminada pelo sistema;
5
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
CÓDIGO ASSÍNCRONO
● Código assíncrono pode ser complicado de gerir;
● Execução não segue a ordem de código síncrono;
● Respostas podem vir por ordens diferentes da ordem dos pedidos;
● É preciso garantir que só tentamos apresentar os dados após serem obtidos da fonte (BD, API,
etc.);
● A UI só pode ser alterada na main-thread
6
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
THREADS
● Mesmo sistema que em Java tradicional:
new Thread(new Runnable() {
public void run() {
//código da thread
view.post(new Runnable() {
public void run() {
//atualizar UI
}
});
}
}).start();
7
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS
● Service é um componente da aplicação
● Permite realizar operações longas;
● Não fornece uma interface com o utilizador (UI);
● Tarefas que não requerem interação com o utilizador;
● Por exemplo, um serviço pode:
● Lidar com transações de rede;
● Reproduzir música;
● Executar E/S de ficheiros;
● Interagir com um provedor de conteúdo,
● Tudo a partir do segundo plano.
8
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS
● Service – Pode ter 2 formas:
● Iniciado (started)
● Quando um componente da aplicação o inicia através do método startService();
● Pode correr em segundo plano mesmo após o componente que o iniciou ser destruído;
● Vinculado (bound)
● Quando um componente da aplicação usa o método bindService() para o vincular;
● Nesta forma oferece uma interface cliente-servidor;
● É executado apenas enquanto o outro componente estiver ligado a si;
9
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS
● Um serviço não cria a sua própria thread
● É executado na thread principal do seu processo;
● Assim devemos criar uma nova thread que inclua o serviço:
● Sempre que representar trabalho intensivo;
● Ou operação de bloqueio;
● O uso de uma thread separada, reduz o risco de erros de “Application Not Responding (ANR)”
● E a thread principal da aplicação pode permanecer dedicada às interações com o utilizador;
10
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS
● Há dois modos de criar um serviço iniciado:
● Com thread própria - através da classe Service;
● Na thread principal - através da classe IntentService (subclasse de Service);
● É necessário implementar o método onHandleIntent(), que recebe o intent para cada
solicitação de início;
● Para que possa ser realizado o trabalho de segundo plano;
● Todos os serviços devem ser declarados no ficheiro AndroidManifest.xml da
aplicação;
● Para declarar o serviço, adicione um elemento <service>
como filho do elemento <application>
11
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS
● Ciclo de vida
● Análogo ao de uma Activity, mas mais
simples;
● O diagrama seguinte ilustra as chamadas
de métodos para ambos os tipos;
● Iniciado;
● Vinculado;
12
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
ANDROID VOLLEY – COMO FUNCIONA?
● Biblioteca Volley utiliza o poder do Android para aceder à rede da
melhor maneira possível;
● Vantagens do Volley:
● Não é necessário escrever código para aceder à rede;
● É tudo gerido pelo Volley;
● Sempre que uma nova solicitação de rede é criada, uma thread interna é gerada para
processamento de rede;
● Cujo ciclo de vida é mantido pelo Volley;
● Na implementação padrão de Volley, é possível processar 4 threads de
rede simultaneamente;
13
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
ANDROID VOLLEY – COMO FUNCIONA?
● Com o Volley é possível aceder a mais características / recursos “out of the box”:
● Retry Mechanism
● Voltar a executar pedido quando não obtém resposta;
● Caching
● Armazenar em cache os pedidos efetuados;
● Tipos de pedidos múltiplos:
● JsonObjectRequest;
● JsonArrayRequest;
● StringRequest;
● ImageRequest;
14
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
O QUE USAR?
● Serviços só para tarefas longas
● IntentService oferece classe base e simplifica utilização de serviços;
● Android Volley faz a gestão de threads internamente
● De forma transparente para o programador;
● E sempre de forma assíncrona;
● Projetado para fins de acesso à rede;
● Permite fazer várias solicitações simultaneamente sem a sobrecarga do gerenciamento de threads;
● Mecanismo de repetição;
● Fornece vários tipos de solicitação, através dos quais pedidos complexos podem ser realizados com
facilidade;
15
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
GLIDE - O QUE É?
● O Glide é uma biblioteca open-source desenvolvida pela BumpTech
● Rápido e eficiente na gestão de imagens no Android
● Gestão automática da memória
● Cache integrada
● Suporte nativo para componentes ListView e RecyclerView
● Carregamento de imagens através de:
● URL’s
● Recursos locais ou Assets
16
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
GLIDE – COMO CONFIGURAR?
● Configurar o Glide:
● Adicionar como dependência no ficheiro build.gradle.kts
● Adicionar no ficheiro libs.versions.toml
17
dependencies {
...
implementation (libs.glide)
}
[versions]
...
glide = “5.0.5”
[libraries]
...
glide={module = “com.github.bumptech.glide:glide”, version.ref=“glide”}
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
REQUISITOS DE ACESSO COM APLICAÇÕES ANDROID
GLIDE – COMO UTILIZAR
● No ficheiro .java que se pretende mostrar a imagem (por exemplo no
adaptador)
18
Glide.with(context)
.load(contacto.getFoto())
.placeholder(R.drawable.imagemBase)
.diskCacheStrategy(DiskCacheStrategy.ALL)
.into(imgFoto);
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
SERVIÇOS E TAREFAS ASSÍNCRONAS
FONTES E MAIS INFORMAÇÃO
 Processos e Threads
 https://developer.android.com/guide/components/processes-and-threads.html
 Serviços
 https://developer.android.com/guide/components/services.html
 Android Volley
 https://developer.android.com/training/volley/index.html
 Glide
 https://bumptech.github.io/glide/
19
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 11 – #SERVIÇOS E TAREFAS ASSÍNCRONAS
PRÓXIMO TEMA:
Material Design
 Material Design para Android
 https://developer.android.com/design
 Material Design Guidelines
 https://material.io/guidelines/
 Criação de Aplicações com Material Design
 https://developer.android.com/guide/topics/ui/look-and-feel/
20
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
BOM TRABALHO !
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026
TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO
21