Ficha Prática 2, AMSI - TeSP PSI - 2025/2026 1
Departamento de Engenharia Informática
TeSP em Programação de Sistemas de Informação
Acesso Móvel a Sistemas de Informação
2º Ano – 1º Semestre
2025/2026
Docentes: Sónia Luz, sonia.luz@ipleiria.pt
David Safadinho, david.safadinho@ipleiria.pt
Cátia Ledesma, catia.ledesma@ipleiria.pt
Guilherme Pereira, guilherme.pereira@ipleiria.pt
Ficha Prática 2
Guess a Number
Objetivos da Ficha
• Criar um novo projeto “Adivinhar um número”
• Inserir e manipular componentes visuais: TextView, EditView e Button
• Entender as restrições dos tipos de layout
Introdução
Nesta ficha vamos criar uma aplicação que permita ao utilizador adivinhar um número gerado
aleatoriamente. O utilizador irá receber indicações que permitem adivinhar o número mais facilmente.
A aplicação “Guess a Number” terá um aspeto semelhante ao apresentado na figura seguinte:
Ficha Prática 2, AMSI - TeSP PSI - 2025/2026 2
Criar um novo projeto (Guess a Number)
1. Iniciar o IDE Android Studio e criar um novo projeto:
1.1. Selecionar a opção “New project”;
1.2. Escolher como atividade uma “Empty Views Activity”
1.3. Configurar o nome “Guess_Number”, o package name “pt.ipleiria.estg.dei.amsi” e a pasta de
destino da aplicação;
Nota: O nome e o caminho para a pasta de destino não podem conter espaços nem caracteres
especiais;
1.4. Selecionar a linguagem de programação Java;
1.5. Escolher o SDK mínimo para a aplicação ser executada, API 24: Android 7.0 (Nougat).
2. Após a aplicação criada podemos ver no IDE a estrutura do projeto e começar a definir a parte visual da
aplicação:
2.1. No layout da atividade activity_main.xml (selecionar a pasta “res” e a subpasta “layout”), altere o
tipo de layout de “Constraint Layout” para “Linear Layout” e elimine a TextView com o texto “Hello
World”. Posteriormente, adicione uma caixa de texto e um botão;
2.2. A caixa de texto apenas deve aceitar números por isso deve adicionar o componente “Edit Text
Number” que deve conter o id “etNumero”, na propriedade “ID”;
2.3. De seguida devemos alterar as propriedades do botão, que deve conter o id “btnAdivinha”, na
propriedade “ID”;
2.4. Para cumprir as regras de arquitetura de desenho de aplicações para Android, deverá incluir um
novo recurso (String Resource) com o texto pretendido. Com o botão selecionado, aceda ao tab de
propriedades e escolha o botão “…” que se encontra à frente da propriedade “Text”. Na janela que
lhe será apresentada deve adicionar um novo recurso utilizando “+” > “String value”. Crie um
recurso com o nome “txt_btn_adivinha” e com o conteúdo “Adivinha”;
2.5. Na propriedade onClick deve adicionar o nome do método “onClickAdivinha”. Após isso deve ir ao
código do activity_main.xml e usando o “Alt+Enter” em cima do nome do método escolher a opção
“Create onClick event handler”;
2.6. O aspeto final do layout da aplicação deverá ter o aspeto da figura seguinte.
Ficha Prática 2, AMSI - TeSP PSI - 2025/2026 3
3. Agora é necessário implementar as funcionalidades que permitem ao utilizador adivinhar o valor que foi
gerado aleatoriamente pela aplicação:
3.1. Na classe MainActivity.java para acedermos ao campo de texto é necessário definir a variável de
acesso ao componente, no método onCreate(). Para o conseguirmos fazer temos que definir a
variável que referencia o elemento gráfico recorrendo ao método findViewById(), que a partir do
identificador único de cada componente (R.id) o identifica;
3.2. De modo a testar a parte inicial e para conseguir ver o valor inserido pelo utilizador, deve utilizar um
componente do Android designado de “Toast”, no método “onClickAdivinha”;
3.3. Na classe MainActivity.java deve criar um método que gere aleatoriamente um valor inteiro entre
dois valores recebidos por parâmetro. Este método será usado para gerar o valor que o utilizador
irá tentar adivinhar;
3.4. Deve criar um atributo que guarde o valor gerado de forma que fique disponível para posteriores
comparações;
3.5. Para verificarmos se o utilizador acertou, ou não, no número gerado aleatoriamente será necessário
alterar o método “onClickAdivinha”.
4. Após termos conseguido apresentar a informação utilizando um componente “Toast”, vamos alterar a
aplicação de modo que essa informação passe a ser apresentada através de um componente “Text
View”. Com esta alteração, quando o utilizador tenta acertar no valor gerado o resultado será
semelhante ao apresentado na figura seguinte (apresenta a informação se o nº está abaixo, acima ou
acertou).
Ficha Prática 2, AMSI - TeSP PSI - 2025/2026 4
5. Para dar mais detalhe à aplicação, vamos limitar o número de tentativas possíveis para um valor
predefinido (por exemplo 5).
Será necessário criar um método para iniciar jogo, invocado cada vez que o número de tentativas é
excedido ou quando o utilizador acerta no número. O resultado deverá ser semelhante ao apresentado
nas figuras seguintes: