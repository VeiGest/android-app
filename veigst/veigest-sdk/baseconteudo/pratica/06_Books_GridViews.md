Ficha Prática 6, AMSI - TeSP PSI - 2025/2026 1
Departamento de Engenharia Informática
TeSP em Programação de Sistemas de Informação
Acesso Móvel a Sistemas de Informação
2º Ano – 1º Semestre
2025/2026
Docentes: Sónia Luz, sonia.luz@ipleiria.pt
David Safadinho, david.safadinho@ipleiria.pt
Cátia Ledesma, catia.ledesma@ipleiria.pt
Guilherme Pereira, guilherme.pereira@ipleiria.pt
Ficha Prática 6
Books – GridViews
Objetivos da Ficha
• Alterar o projeto “Books” da aula anterior para apresentar os livros numa grelha
• Utilizar o componente GridView
• Utilizar Adapters
Introdução
Nesta ficha vamos continuar com a aplicação da aula anterior. A janela inicial de login mantém-se, assim
como o MenuMainActivity, mas será necessário criar um fragmento para apresentar os livros em grelha,
de acordo com a 2ª opção do menu. Para mostrar os livros em grelha irá utilizar-se uma GridView
implementada através de adaptadores.
Ficha Prática 6, AMSI - TeSP PSI - 2025/2026 2
Abrir projeto Books
1. Criar um novo fragmento do tipo “Fragment (Blank)”, com o nome “GrelhaLivrosFragment”.
1.1. Criar o layout para o fragmento que deve ser similar ao apresentado na imagem abaixo. Este layout
é obtido através do componente GridView, ao qual deve atribuir o id gvLivros. Na configuração do
layout da GridView do fragment_grelha_livros.xml, deve configurar o atributo numColumns como
autofit caso pretenda que seja adaptativo, ou então definir um nº fixo de colunas.
1.2. De seguida, deve criar um novo layout denominado, item_grelha_livro.xml, que irá representar
cada item da GridView. Neste caso, apenas vai apresentar a capa do livro através de uma
ImageView:
• caso tenha definido um nº fixo de colunas deve definir os dp para a altura da imagem;
• caso tenha defindo o nº de colunas como autofit, deve indicar a altura e largura da
imagem.
2. Para manipular e apresentar os itens de uma GridView vai necessitar de um adaptador personalizado.
2.1. No package, adaptadores deve adicionar uma nova classe GrelhaLivrosAdaptador, que deve
estender da classe BaseAdapter.
2.2. Após isso, obrigatoriamente tem de implementar os 4 métodos exigidos.
2.3. Nesta classe, à semelhança do que acontecia com as listas, deve ter os seguintes atributos:
• um do tipo Context, necessário para o adaptador,
• um do tipo LayoutInflater para ter acesso ao layout específico para cada item
(item_grelha_livro.xml),
• um do tipo ArrayList onde vai armazenar a lista de livros.
Ficha Prática 6, AMSI - TeSP PSI - 2025/2026 3
2.4. De forma a otimizar o acesso aos recursos visuais (através do findViewById) aconselha-se a
utilização de um componente ViewHolder, tal como foi feito para a ListView da ficha anterior.
2.4.1 Na classe GrelhaLivrosAdaptador deve criar uma classe interna designada de
ViewHolderGrelha, para acesso aos componentes visuais.
2.4.2 Na mesma classe, no método getView o parâmetro convertView servirá para reutilizar a
view para apresentar o layout de cada item.
3. Na classe “GrelhaLivrosFragment”, deve criar um atributo do tipo GridView e atribuir-lhe o adaptador
criado no ponto anterior.
4. Para apresentar os detalhes de um livro, de forma, a que quando se clica num item da grelha, seja
apresentada uma nova atividade com os detalhes do livro selecionado, deve usar a atividade
“DetalhesLivroActivity”, criada na ficha anterior, cujo um layout é semelhante ao apresentado na
figura seguinte.
4.1. Para selecionar um item da grelha de livros, tem de utilizar um listener de forma a saber qual o livro
selecionado.
4.1.1 Na classe “GrelhaLivrosFragment”, deve de implementar o método
setOnItemClickListener() da gridView.
4.1.2 Ao selecionar um item da grelha, vai iniciar a atividade “DetalhesLivroActivity”, com o id do
livro selecionado. Para aceder de forma correta ao livro selecionado, utilize o método
getLivro(), já implementado na classe SingletonGestorLivros (ficha anterior).