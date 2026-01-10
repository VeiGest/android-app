Ficha Prática 5, AMSI - TeSP PSI - 2025/2026 1
Departamento de Engenharia Informática
TeSP em Programação de Sistemas de Informação
Acesso Móvel a Sistemas de Informação
2º Ano – 1º Semestre
2025/2026
Docentes: Sónia Luz, sonia.luz@ipleiria.pt
David Safadinho, david.safadinho@ipleiria.pt
Cátia Ledesma, catia.ledesma@ipleiria.pt
Guilherme Pereira, guilherme.pereira@ipleiria.pt
Ficha Prática 5
Books – ListViews
Objetivos da Ficha
• Alterar o projeto “Books” da aula anterior para apresentar os livros numa lista
• Utilizar o componente ListView
• Utilizar Adapters
Introdução
Nesta ficha vamos continuar com a aplicação da ficha anterior, caso pretenda guardar o projeto anterior
crie uma cópia.
A janela inicial de login mantém-se, mas o menu principal irá sofrer umas ligeiras alterações ficando com
um aspeto similar ao apresentado na figura seguinte.
Ficha Prática 5, AMSI - TeSP PSI - 2025/2026 2
Abrir projeto Books
1. Apague os fragmentos EstaticoFragment e DinamicoFragment assim como o fragment_dinamico.xml
(para já manter apenas o layout fragment_estatico.xml).
2. Altere os títulos do menu_main.xml para que o menu esteja de acordo com o layout da imagem
seguinte. Caso tenha extraído as strings para o ficheiro strings.xml, deve efetuar aí a alteração.
3. Criar um novo fragmento do tipo “Fragment (Blank)”, com o nome “ListaLivrosFragment”.
3.1. Criar o layout para o fragmento que deve ser similar ao apresentado na imagem abaixo.
Este layout é obtido através do componente ListView, ao qual deve atribuir o id lvLivros.
Ficha Prática 5, AMSI - TeSP PSI - 2025/2026 3
3.2. Altere o nome do layout fragment_estatico.xml para item_lista_livro.xml, que irá representar
cada item da ListView, e verifique se o atributo altura foi definido como
android:layout_height="wrap_content".
3.3. No SingletonGestorLivros adicione os seguintes livros no método gerarDadosDinamicos(),
que vai ser invocado no construtor:
livros.add(new Livro(1, R.drawable.programarandroid2, 2024,
"Programar em Android AMSI - 1", "2ª Temporada", "AMSI TEAM"));
livros.add(new Livro(2, R.drawable.programarandroid1, 2024,
"Programar em Android AMSI - 2", "2ª Temporada", "AMSI TEAM"));
livros.add(new Livro(3, R.drawable.logoipl, 2024, "Programar em
Android AMSI - 3", "2ª Temporada", "AMSI TEAM"));
livros.add(new Livro(4, R.drawable.programarandroid2, 2024,
"Programar em Android AMSI - 4", "2ª Temporada", "AMSI TEAM"));
livros.add(new Livro(5, R.drawable.programarandroid1, 2024,
"Programar em Android AMSI - 5", "2ª Temporada", "AMSI TEAM"));
livros.add(new Livro(6, R.drawable.logoipl, 2024, "Programar em
Android AMSI - 6", "2ª Temporada", "AMSI TEAM"));
livros.add(new Livro(7, R.drawable.programarandroid2, 2024,
"Programar em Android AMSI - 7", "2ª Temporada", "AMSI TEAM"));
livros.add(new Livro(8, R.drawable.programarandroid1, 2024,
"Programar em Android AMSI - 8", "2ª Temporada", "AMSI TEAM"));
livros.add(new Livro(9, R.drawable.logoipl, 2024, "Programar em
Android AMSI - 9", "2ª Temporada", "AMSI TEAM"));
livros.add(new Livro(10, R.drawable.programarandroid2, 2024,
"Programar em Android AMSI - 10", "2ª Temporada", "AMSI TEAM"));
4. Considerando que para manipular e apresentar os itens de uma ListView vai necessitar de um
adaptador, deve criar um novo package denominado de adaptadores.
4.1. Neste package deve adicionar uma nova classe ListaLivrosAdaptador, que vai estender do
BaseAdapter - extends BaseAdapter.
4.2. Agora irá observar que vão surgir erros, isto acontece porque tem de implementar os 4 métodos
exigidos.
4.3. Nesta classe deve ter os seguintes atributos:
• um do tipo Context que é necessário para o adaptador,
Ficha Prática 5, AMSI - TeSP PSI - 2025/2026 4
• um do tipo LayoutInflater para ter acesso ao layout específico para cada item
(item_lista_livro.xml),
• um do tipo ArrayList onde vai guardar a lista de livros.
4.4. O construtor deve receber como parâmetros um context e a lista de livros.
4.5. Para otimizar o acesso aos recursos visuais através do findViewById aconselha-se a utilização de
um componente ViewHolder.
4.5.1. Na classe ListaLivrosAdaptador deve criar uma classe interna designada de
ViewHolderLista, para acesso aos componentes visuais.
4.5.2. No método getView, o parâmetro View servirá para reutilizar a view para apresentar o
layout de cada item, em vez de estar sempre a criar uma nova.
5. Deve voltar à classe “ListaLivrosFragment”, para criar um atributo do tipo ListView:
5.1. Ao atributo deve atribuir-lhe o adaptador criado no tópico anterior.
5.2. Depois tem de utilizar um listener para saber qual o item que foi selecionado, ou seja, na classe
ListaLivrosFragment tem de implementar o método setOnItemClickListener() da listView.
5.3. Ao clicar num item da lista deve aparecer um Toast com o título do livro clicado.
6. Altere o método onNavigationItemSelected no MenuMainActivity de forma a iniciar o fragmento
ListaLivrosFragment quando se clica nos “Livros (Lista)” (1º item do menu). Teste o código e verifique
se é apresentado o fragmento coma lista de livros.
7. Altere o ponto 5 para que quando se clica num item da lista, seja apresentada uma nova atividade com
os detalhes do livro selecionado.
7.1. Crie uma nova atividade, denominada “DetalhesLivroActivity”, que deverá ter um layout
semelhante ao apresentado na figura abaixo. De referir que os campos devem ser editáveis, por
isso deve utilizar EditText, no caso do campo ano deverá ser do tipo Number.
Ficha Prática 5, AMSI - TeSP PSI - 2025/2026 5
7.2. Altere novamente o método setOnItemClickListener existente na “ListaLivrosFragment”, de forma
a iniciar a atividade “DetalhesLivroActivity” com a informação do livro selecionado.
7.2.1. Para aceder de forma correta ao livro selecionado, deve implementar, na classe
SingletonGestorLivros, o método getLivro(int idLivro).
7.2.2. O ID do livro selecionado é passado para a nova Activity através de uma chave definida no
bundle Extra do Intent.
7.2.3. No DetalhesLivroActivity, o ID do livro recebido no Intent deve ser utilizado para carregar
as informações do livro selecionado, atualizando o título da atividade para “Detalhes:
[título do livro]”.