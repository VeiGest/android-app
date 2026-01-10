Ficha Prática 3, AMSI - TeSP PSI - 2025/2026 1
Departamento de Engenharia Informática
TeSP em Programação de Sistemas de Informação
Acesso Móvel a Sistemas de Informação
2º Ano – 1º Semestre
2025/2026
Docentes: Sónia Luz, sonia.luz@ipleiria.pt
David Safadinho, david.safadinho@ipleiria.pt
Cátia Ledesma, catia.ledesma@ipleiria.pt
Guilherme Pereira, guilherme.pereira@ipleiria.pt
Ficha Prática 3
Books: atividades e intents
Objetivos da Ficha
• Criar um novo projeto denominado “Books” com uma atividade inicial de Login
• Criar atividades a partir de outras atividades
• Enviar um email
• Utilização de uma classe do tipo Singleton
Introdução
Nesta ficha vamos criar uma aplicação que permita ao utilizador implementar um acesso por login a partir
de um email e password.
A janela inicial da aplicação “Books” deve ter um aspeto semelhante ao apresentado na figura seguinte.
Ficha Prática 3, AMSI - TeSP PSI - 2025/2026 2
Criar o projeto (Books)
1. Deve efetuar o download da pasta recursos, disponibilizada no Moodle e depois, criar um novo projeto
denominado “Books”, que irá ser desenvolvido ao longo do semestre.
1.1. Para a atividade inicial (Launch Activity) crie uma atividade do tipo “Empty Views Activity”.
1.2. Adicionar as imagens disponibilizadas na pasta drawable.
1.3. Mudar o nome da atividade MainActivity para LoginActivity. Deve também mudar o nome do
ficheiro que contém o layout, activity_main para activity_login.
1.4. Para que a Action Bar seja apresentada, deve aceder à pasta res → values→ themes e modificar
o ficheiro themes.xml de acordo com o código apresentado abaixo e depois deve gerar
automaticamente a cor purple_500.
<resources xmlns:tools="http://schemas.android.com/tools">
<style name="Base.Theme.Books"
parent="Theme.MaterialComponents.DayNight.DarkActionBar">
<item name="colorPrimary">@color/purple_500</item>
</style>
<style name="Theme.Books" parent="Base.Theme.Books" />
</resources>
1.5. Para que o Action Bar não fique sobreposto ao layout, deve colocar no layout principal de
cada atividade a propriedade:
android:layout_marginTop="?attr/actionBarSize"
2. No layout “activity_login”, deve:
2.1. Criar uma imagem através de uma ImageView, usando a imagem do IPL disponibilizada;
2.2. Criar duas EditText, uma para email outra para a password e um Button, para efetuar o respetivo
Login;
2.3. Criar um método isEmailValido() para validar o email introduzido (ou seja, para verificar se é um
email válido);
2.4. Criar um método isPasswordValida() para garantir que a password tem no mínimo 4 caracteres;
2.5. Criar o método onClickLogin(), associado ao click do botão, que tem como intuito validar o email
e o login e apresentar o resultado da validação num Toast.
2.6. Como deve ter verificado o atributo android:onClick está deprecated porque traz impacto
negativo no desempenho, por isso pode substituir o método onClickLogin() por um listener na
atividade.
Ficha Prática 3, AMSI - TeSP PSI - 2025/2026 3
3. Criar uma Atividade, do tipo” Empty Views Activity”, com o nome “MainActivity” que irá permitir
navegar na aplicação.
3.1. Criar o layout para esta atividade, composto por 3 botões, que deve ser similar ao apresentado
na imagem seguinte:
3.2. Voltar novamente à “LoginActivity” e efetuar as alterações necessárias para que, em vez de
apresentar um Toast se consiga iniciar a atividade “MainActivity”. Esta alteração deverá ser
efetuada no listener associado ao click do botão Login, recorrendo à criação de um novo Intent.
Através do novo Intent, deve passar o email como parâmetro para a “MainActivity”, de forma a
ser apresentado na segunda TextBox.
3.3. Criar duas atividades, denominadas “DetalhesEstaticoActivity” e “DetalhesDinamicoActivity”
que irão permitir apresentar a informação do livro. No modo Estático os dados do livro são
carregados diretamente nas TextBox no método onCreate, já o modo Dinâmico será efetuado
com recurso ao Singleton, abordado no exercício 6.
Os layouts das atividades devem ser similares aos apresentados nas imagens seguintes:
Ficha Prática 3, AMSI - TeSP PSI - 2025/2026 4
3.4. Voltar novamente à “MainActivity” e efetuar as alterações necessárias, para que ao clicar nos
botões “Livro Estático” e “Livro Dinâmico” se inicie uma outra atividade
(“DetalhesEstaticoActivity” ou “DetalhesDinamicoActivity” consoante a opção clicada).
4. Considerando a atividade “DetalhesEstaticoActivity”, implementar o código necessário de modo a
conseguir manipular os componentes.
4.1. Para adicionar o botão back na ActionBar, deve ir ao ficheiro AndroidManisfest.xml e definir o
parent, que neste caso será a “MainActivity”.
4.2. Teste a aplicação e verifique o que acontece quando voltamos à MainActivity.
4.3. Volte ao AndroidManisfest.xml e defina que a “MainActivity” deve ter o launchMode como
singleTop. Agora teste novamente, e vai verificar que quando fizer back o endereço de email
continua a ser apresentado.
<activity
android:name=".MainActivity"
android:launchMode="singleTop"
android:exported="false" />
<activity android:name=".DetalhesEstaticoActivity"
android:parentActivityName=".MainActivity"/>
Ficha Prática 3, AMSI - TeSP PSI - 2025/2026 5
5. De forma a organizar as classes e a separar conceitos, deve criar um package com o nome modelo.
5.1. No package modelo deve criar a classe Livro com os seguintes atributos: id, titulo, serie, autor,
ano, capa. O id e o ano devem ser armazenados com o tipo de dados inteiro, assim como a capa,
apesar de corresponder a uma imagem.
5.2. Implemente o construtor e respetivos métodos Getters and Setters exceto o setter do id.
5.3. Crie um construtor que recebe todos os atributos da classe como parâmetros de entrada.
6. De seguida, no mesmo package, deve criar a classe do tipo Singleton denominada
SingletonGestorLivros, para conter a simulação dos dados dinâmicos.
6.1. Cria o construtor da classe de forma a garantir que é thread-safe e que contém uma única
instância durante a execução do programa.
6.2. A classe SingletonGestorLivros deve conter uma lista de livros, que será iniciada no respetivo
construtor.
6.3. Deve também criar um método que devolva os respetivos livros da lista.
7. Tendo em conta as classes criadas anteriormente, deve proceder à implementação do código da
atividade “DetalhesDinamicoActivity”.
8. Adicione os métodos de ciclo de vida de uma atividade (mencionados na aula TP) na MainActivity,
incluindo instruções para imprimir textos na consola e execute a aplicação verificando a ordem das
chamadas. Use o debugger para poder parar em cada método e ver as informações de contexto sobre
o estado da aplicação e respetiva atividade.
9. (*) Na “MainActivity” deve implementar a funcionalidade para o botão Enviar Email, recorrendo ao
respetivo Intent implícito.