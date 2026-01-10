Ficha Prática 1, AMSI - TeSP PSI - 2025/2026 1
Departamento de Engenharia Informática
TeSP em Programação de Sistemas de Informação
Acesso Móvel a Sistemas de Informação
2º Ano – 1º Semestre
2025/2026
Docentes: Sónia Luz, sonia.luz@ipleiria.pt
David Safadinho, david.safadinho@ipleiria.pt
Cátia Ledesma, catia.ledesma@ipleiria.pt
Guilherme Pereira, guilherme.pereira@ipleiria.pt
Ficha Prática 1
IDE e introdução à programação em Android
Objetivos da Ficha
• Criar a primeira aplicação em Android Studio
• Conhecer o ambiente de desenvolvimento e os seus principais componentes
• Correr e executar uma aplicação em Android
Introdução
O desenvolvimento de aplicações para dispositivos Android pode ser efetuado em diversos IDEs
(Integrated Development Environment), nomeadamente o Eclipse, o IntelliJ IDEA IDE ou o NetBeans IDE.
O IDE oficialmente suportado e que será usado nas aulas é o software Android Studio.
Nesta ficha pretende-se criar e executar uma primeira aplicação “Hello World” com um aspeto
semelhante ao apresentado na figura seguinte:
Ficha Prática 1, AMSI - TeSP PSI - 2025/2026 2
Criar um novo projeto “Hello World”
1. Iniciar o IDE Android Studio
1.1. Se utilizarem os computadores da sala devem iniciar o Android Admin.
2. Selecionar a opção “New project”.
3. Existem diversos tipos de atividades disponíveis que se adequam a diversos tipos de aplicações, no
entanto iremos começar através de uma “Empty Views Activity”, para configuramos de raiz.
4. Configure o nome da aplicação, o Package Name e a pasta de destino da aplicação;
4.1. Escolha a linguagem de programação “Java” e a Minimum API Level “API 24: Android 7.0
(Nougat)”;
4.2. O nome e o caminho para a pasta de destino não podem conter espaços nem caracteres
especiais.
Ficha Prática 1, AMSI - TeSP PSI - 2025/2026 3
5. Após a aplicação criada podemos ver no IDE a estrutura do projeto:
6. Para testar a execução da aplicação deve selecionar a opção “Run” ou “Shift+F10”.
6.1. Caso pretenda adicionar um novo emulador selecione a opção “Create Virtual Device”.
Ficheiros do projeto
Propriedades do componente
Componentes
Componente selecionado
Ficha Prática 1, AMSI - TeSP PSI - 2025/2026 4
6.2. Para executar a aplicação no dispositivo móvel deve selecionar as “Opções de Programador”, no
entanto se essa opção não estiver visível, aceda às “Definições”, selecione o item referente às
informações do dispositivo e clique 5 vezes no “Número de Compilação”. Posteriormente,
quando aceder às “Definições” já terá disponível as “Opções de Programador” e deve ativar a
“Depuração USB”.
7. Modificar o nome da aplicação de “Hello World” para “Ficha1”.
8. Alterar a aplicação anterior, colocando na MainActivity o código necessário para resolver os exercícios
apresentados no Powerpoint: sintaxe de Java para Android.
9. Considerando as seguintes notas (de 0 a 20) de 10 alunos [12, 14, 10, 15, 8, 6, 18, 9, 17, 11], adicione
à aplicação anterior o código necessário de forma a calcular e apresentar:
9.1. a média das notas, a nota mais alta e a nota mais baixa;
9.2. a quantidade e a percentagem de notas positivas.
10. Altere a resolução do exercício anterior, de forma a ter uma classe denominada Estatística, que
contém os seguintes métodos:
• calcMedia(v) → cálculo da média
• calcMaior(v) → cálculo do maior valor
• calcMenor(v) → cálculo do menor valor
• calcPercentagemPositivos(limPos, v) → cálculo da percentagem de positivas (tendo como
parâmetro um valor de referência para comparar)