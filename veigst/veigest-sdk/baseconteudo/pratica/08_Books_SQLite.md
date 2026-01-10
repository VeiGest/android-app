Ficha Prática 8, AMSI - TeSP PSI - 2025/2026 1
Departamento de Engenharia Informática
TeSP em Programação de Sistemas de Informação
Acesso Móvel a Sistemas de Informação
2º Ano – 1º Semestre
2025/2026
Docentes: Sónia Luz, sonia.luz@ipleiria.pt
David Safadinho, david.safadinho@ipleiria.pt
Cátia Ledesma, catia.ledesma@ipleiria.pt
Guilherme Pereira, guilherme.pereira@ipleiria.pt
Ficha Prática 8
Books – Base de Dados SQLite
Objetivos da Ficha
• Criar e manipular Base de Dados Local SQLite
Introdução
Nesta ficha vamos continuar com a aplicação Books, mas recorrendo ao uso de base de dados locais
SQlite, para permitir a persistência dos dados da aplicação localmente.
Abrir a pasta do projeto da aula anterior
1. De forma a manter persistência dos dados localmente, iremos recorrer à base de dados SQLite. Para
isso, no package modelo, deve criar uma classe denominada LivroBDHelper;
1.1. Esta classe tem de estender de SQLiteOpenHelper, o que obriga a que sejam implementados
os métodos onCreate e onUpgrade;
1.2. Para poder conseguir criar e atualizar a base de dados é necessário criar as constantes
DB_NAME e TABLE_NAME do tipo String e o DB_VERSION do tipo inteiro;
1.3. Para permitir a manipulação dos componentes SQLite é necessária a criação de uma instância
da classe SQLiteDatabase;
1.4. Após isso deve-se implementar o construtor, recebendo apenas como parâmetro o Context.
Para definir permissões de leitura e escrita na base de dados, teremos de utilizar o método
getWritableDatabase();
Ficha Prática 8, AMSI - TeSP PSI - 2025/2026 2
1.5. Como já temos a base de dados criada, vamos proceder à criação da tabela Livro, no método
onCreate();
1.6. Deve também implementar o método onUpgrade() para permitir a atualização da base de
dados;
1.7. De seguida vamos implementar os métodos para realizar as operações de CRUD:
1.7.1. Deve criar, na classe Livro, o método setId(int) para atualizar o id devolvido pela base de
dados;
1.7.2. Na classe Livro deve ainda alterar o construtor, de forma a receber o id como parâmetro,
deixando de ser auto-increment. Com esta alteração irão surgir alguns erros, para
solucionar deve comentar no SingletonGestorLivros o código referente à criação dos
livros e no DetalhesLivroActivity deve atribuir o valor 0 ao id do livro criado.
1.7.3. Implementar os métodos adicionarLivroBD, editarLivroBD, removerLivroBD e
getAllLivrosBD, que permitem realizar as operações CRUD do livro.
2. Na classe SingletonGestorLivros, para manipular a base de dados:
2.1. Deve alterar o construtor e o método getInstance() para passar a receber um parâmetro do
tipo Context, necessário para instanciar a classe da base de dados. Neste caso será necessário
corrigir todas as chamadas ao método getInstance() para que passe a receber o contexto
adequado;
2.2. Deve criar um atributo livrosBD do tipo LivroBDHelper;
2.3. No construtor, deve iniciar a instância do LivroBDHelper, deixando de usar o método
gerarDadosDinamico();
2.4. Após isso, deve alterar os métodos adicionarLivro, removerLivro, editarLivro e getLivros,
renomeando-os para adicionarLivroBD, removerLivroBD, editarLivroBD e getLivrosBD, de
modo a utilizar os métodos da base de dados.