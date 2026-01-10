Ficha Prática 7, AMSI - TeSP PSI - 2025/2026 1
Departamento de Engenharia Informática
TeSP em Programação de Sistemas de Informação
Acesso Móvel a Sistemas de Informação
2º Ano – 1º Semestre
2025/2026
Docentes: Sónia Luz, sonia.luz@ipleiria.pt
David Safadinho, david.safadinho@ipleiria.pt
Cátia Ledesma, catia.ledesma@ipleiria.pt
Guilherme Pereira, guilherme.pereira@ipleiria.pt
Ficha Prática 7
Books – Menus e Widgets
Objetivos da Ficha
• Criar e manipular menus
• Criar e manipular FloatingActionButton
• Utilizar widgets para manipulação e apresentação de informação
Introdução
Nesta ficha vamos continuar com o projeto anterior, e pretende-se salientar o uso de Menus e Widgets,
para melhorar a apresentação e utilização da aplicação.
Abrir a pasta do projeto da aula anterior
1. Na atividade “MenuMainActivity”, utilize as sharedPreferences para guardar os dados do email do
utilizador.
2. Para poder criar menus com ícones, deve adicionar um novo Image Asset para cada ícone, de acordo com
a imagem seguinte. Para isso deve clicar o botão direito do rato na pasta drawable e escolher a opção
Image Asset. Esses ícones irão representar as funcionalidades para Pesquisar, Adicionar, Remover e
Guardar um livro.
Ficha Prática 7, AMSI - TeSP PSI - 2025/2026 2
3. Para permitir adicionar um novo livro a partir dos fragmentos “ListaLivrosFragment” e
“GrelhaLivrosFragment”, deve adicionar um botão do tipo FloatingActionButton.
3.1. Adicione um FloatingActionButton, em cada fragmento, com as caraterísticas seguintes:
<com.google.android.material.floatingactionbutton.FloatingActionButton
android:id="@+id/fabLista"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_gravity="bottom|end"
android:layout_margin="16dp"
android:backgroundTint="@color/purple_500"
app:tint="@color/white"
android:focusable="true"
android:src="@drawable/ic_action_adicionar" />
3.2. Para criar a ação do FloatingActionButton, crie no método onCreateView o listener
setOnClickListener e redirecione o utilizador para a atividade DetalhesLivroActivity.
1.
4. De seguida deve criar um menu de opções para pesquisa, denominado menu_pesquisa.xml.
4.1. Para criar o menu deve selecionar, com o botão direito do rato, em cima da pasta res/menu e
escolher a opção “Menu Resource File”, tal como pode ver na imagem seguinte:
Ficha Prática 7, AMSI - TeSP PSI - 2025/2026 3
4.2. Este menu deve conter o item itemPesquisa com as caraterísticas seguintes:
<item
android:id="@+id/itemPesquisa"
android:icon="@drawable/ic_action_pesquisar"
android:title="Pesquisa"
app:actionViewClass="android.widget.SearchView"
app:showAsAction="always"/>
5. No fragmento “ListaLivrosFragment” deve associar o menu de opções.
5.1. Para que o fragmento permita mostrar um menu de opções, é necessário adicionar a seguinte linha
de código no método onCreateView:
setHasOptionsMenu(true);
5.2. Deve reimplementar o método onCreateOptionsMenu() para apresentar o menu
menu_pesquisa.xml;
5.3. Após a implementação do método, crie um atributo do tipo SearchView;
5.4. No método onCreateOptionsMenu() vai-se criar uma pesquisa em runtime, recorrendo ao listener
OnQueryTextListener() através da implementação do método setOnQueryTextListener();
5.5. Agora é necessário implementar código no método onQueryTextChange(), de forma a filtrar os
livros. O resultado esperado será semelhante ao apresentado na figura seguinte.
Ficha Prática 7, AMSI - TeSP PSI - 2025/2026 4
6. Para permitir apagar um livro, deve criar um menu opções denominado menu_remover.xml:
6.1. Este menu irá conter o item itemRemover.
6.2. Além disso, o menu deve estar associado à atividade “DetalhesLivroActivity” para permitir remover
o livro apresentado, sendo necessário reimplementar o método onCreateOptionsMenu().
7. Na atividade “DetalhesLivroActivity” deve adicionar um FloatingActionButton para guardar um livro.
7.1. Deve inserir um novo LinearLayout e arrastar lá para dentro todos os componentes existentes;
7.2. Altere o layout principal para CoordinatorLayout;
7.3. Para finalizar adicione ao CoordinatorLayout um FloatingActionButton.
8. Na classe Livro altere o id, de forma que seja incremental:
8.1. Altere o construtor de modo que o id do livro seja único, incrementado de forma automática e
sequencial. Assim sendo, o construtor deixa de receber um id por parâmetro.
8.2. Após executar esta ação, é necessário alterar o método gerarDadosDinamico, que foi definido no
SingletonGestorLivros.
9. Na classe SingletonGestorLivros deve implementar os métodos: adicionarLivro(Livro livro),
editarLivro(Livro livro) e removerLivro(int idLivro), que adiciona, edita e elimina livro.
Ficha Prática 7, AMSI - TeSP PSI - 2025/2026 5
10. Na atividade “DetalhesLivroActivity”, quando não recebe nenhum id de livro, através do bundle Extra do
intent, deve implementar a funcionalidade para adicionar um novo livro, caso contrário a atividade deve
permitir editar os detalhes do livro apresentado:
10.1. O FloatingActionButton existente deve adequar o seu icon à tarefa de adicionar um livro ou gravar
os dados editados e além disso deve implementar as respetivas funcionalidades. Deve garantir que
todos os campos referentes ao livro estão preenchidos.
10.2. Alterar os Intents existentes em “ListaLivroFragment” e “GrelhaLivrosFragment”, de forma que
quando finalizamos a inserção ou edição de um livro, seja apresentado num Toast a informação sobre
a ação executada, de acordo com a imagem seguinte:
10.3. Para implementar a funcionalidade, deve substituir o startActitivity pelo startActivityForResult
onde vai colocar um código da ação e depois efetuar uma reimplementação do método
onActivityResult().
11. Na atividade DetalhesLivroActivity deve implementar a funcionalidade “remover livro”:
11.1. Quando é recebido um id de livro, através do bundle Extra do intent, o menu menu_remover.xml
deve estar disponível;
11.2 Deve reimplementar o método onOptionsItemSelected(), que permite detetar qual o item do
menu selecionado;
Ficha Prática 7, AMSI - TeSP PSI - 2025/2026 6
11.3. Para a implementação da funcionalidade remover, deve usar o componente AlertDialog, que
permite confirmar junto do utilizador a sua intenção, tal como apresentado na imagem seguinte:
12. (*) Para melhorar a forma de interação com a aplicação deve investigar:
12.1. O uso do widget SwipeRefreshLayout para sincronizar os dados a apresentar na lista/grelha.
12.2. A substituição dos componentes Toast por componentes do tipo Snackbar para apresentação de
mensagens, tal como podem ver na imagem seguinte;