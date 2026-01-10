Ficha Prática 4, AMSI - TeSP PSI - 2025/2026 1
Departamento de Engenharia Informática
TeSP em Programação de Sistemas de Informação
Acesso Móvel a Sistemas de Informação
2º Ano – 1º Semestre
2025/2026
Docentes: Sónia Luz, sonia.luz@ipleiria.pt
David Safadinho, david.safadinho@ipleiria.pt
Cátia Ledesma, catia.ledesma@ipleiria.pt
Guilherme Pereira, guilherme.pereira@ipleiria.pt
Ficha Prática 4
Books – Navegação e Fragmentos
Objetivos da Ficha
• Alterar o projeto da aula anterior “Books” de forma a ter um menu de navegação
• Inserir e manipular componentes visuais: NavigationView e AppBar
• Criar fragmentos
Introdução
Nesta ficha vamos criar uma aplicação que permita ao utilizador navegar pelos vários itens de um
menu lateral. A janela principal da aplicação “Books” terá um aspeto semelhante ao apresentado na
figura seguinte:
Ficha Prática 4, AMSI - TeSP PSI - 2025/2026 2
Continuação do projeto Books
1. Dando continuidade ao projeto da aula anterior - Books, crie nova atividade do tipo “Empty Views
Activity”, denominada “MenuMainActivity”, que irá substituir futuramente a “MainActivity”.
2. A próxima etapa será definir os itens do menu de navegação, para isso é necessário criar uma pasta que
irá conter os itens existentes no menu da aplicação. Ao selecionar a pasta res, faça
New→Android Resource Directory, escolha um recurso do tipo menu e posteriormente crie um ficheiro,
New→ Menu Resource File, com nome menu_main.
Dentro do menu crie um grupo, ao qual vão pertencer os
2 primeiros itens. Quando os itens fazem parte de um
grupo, podem partilhar estado e visibilidade, mas não
devem ser clicáveis em simultâneo, para isso é
necessário colocar:
android:checkableBehavior ="single"
Para cada um dos itens vamos atribuir o id: navEstatico,
navDinamico, navEmail, que posteriormente serão
utilizados para identificar a opção selecionada.
2.1. Ao analisar o menu anterior vai verificar que não aparece posicionado corretamente, o objetivo é
colocá-lo no navigationView. Para que isso aconteça, altere a tag do menu principal (assinalado na
imagem a amarelo):
<menu xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools"
tools:showIn="navigation_view">
2.2. O menu anterior ainda não tem os ícones atrás de cada item, para isso deve criar as 3 imagens
através da opção: New→Image Asset, e escolher um ícone do tipo “Action Bar and Tab Icons”.
Posteriormente selecione uma imagem do clipart para associar ao item.
3. Para colocar a barra de ferramentas no layout da atividade “MenuMainActivity” é necessário um novo
widget - AppBarLayout que pode ser incorporado na hierarquia do conteúdo principal.
3.1. Crie um novo layout denominado app_bar_main, que deve ser composto por um AppBarLayout e
um FrameLayout com o id contentFragment, onde será apresentado cada fragmento, de acordo
com a seleção do menu.
3.2. Para que o FrameLayout não fique sobreposto pelo AppBar é necessário adicionar a propriedade:
app:layout_behavior="@string/appbar_scrolling_view_behavior"
Ficha Prática 4, AMSI - TeSP PSI - 2025/2026 3
3.3. No AppBarLayout iremos usar o tema ThemeOverlay.AppCompat.Dark.ActionBar, que
corresponde ao fundo escuro com letra branca (imagem esquerda), e no Toolbar o tema a aplicar
é ThemeOverlay.AppCompat.Light para que o menu suspenso fique com o fundo claro.
3.4. De forma a impedir que a aplicação use o ActionBar definido no themes (que fornece a barra de
ferramentas), deve criar novo tema. Para isso, aceda à pasta res e no ficheiro themes.xml adicionar
o tema NoActionBar, que herda de Theme.Books
<style name="Theme.Books.NoActionBar">
<item name="windowActionBar">false</item>
<item name="windowNoTitle">true</item>
</style>
3.5. Na MainMenuActivity declarada no AndroidManifest, defina como tema o NoActionBar, criado
no ponto anterior.
<activity
android:name=".MainMenuActivity"
android:label="@string/app_name"
android:launchMode="singleTop"
android:screenOrientation="portrait"
android:theme="@style/Theme.Books.NoActionBar" />
4. O NavigationView que irá ser criado posteriormente, é composto pelo menu (ponto 2) e por um
cabeçalho.
4.1. Para construir o cabeçalho deve criar um layout denominado nav_header_main, com aspeto
similar ao da figura abaixo:
Ficha Prática 4, AMSI - TeSP PSI - 2025/2026 4
4.2. Substitua o código do ficheiro nav_header_main pelo código abaixo, e complete de forma a
adicionar os componentes necessários.
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
android:layout_width="match_parent"
android:layout_height="200dp"
android:background="@color/purple_500"
android:gravity="bottom"
android:orientation="vertical"
android:padding="16dp"
android:theme="@style/ThemeOverlay.AppCompat.Dark">
<!-- TODO: definir componentes em falta: ImageView, TextView(email) -->
</LinearLayout>
5. A etapa final do layout consiste em configurar o Navigation Drawer no ficheiro activity_menu_main.xml.
5.1. O ConstraintLayout deverá ser substituído pelo androidx.drawerlayout.widget.DrawerLayout
com o id drawerLayout.
<androidx.drawerlayout.widget.DrawerLayout
xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
xmlns:tools="http://schemas.android.com/tools"
android:id="@+id/drawerLayout"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:fitsSystemWindows="true"
tools:openDrawer="start">
<!-- TODO:definir os componentes em falta -->
</androidx.drawerlayout.widget.DrawerLayout>
5.2. Na hierarquia será feita a inclusão do layout desenvolvido no ponto 3.1. recorrendo à tag include.
Além disso também irá conter:
- um NavigationView, com o id navView, que é composto pelo header definido no ponto 4.1
app:headerLayout="@layout/nav_header_main"
e o menu definido no ponto 2 app:menu="@menu/menu_main"
Ficha Prática 4, AMSI - TeSP PSI - 2025/2026 5
O NavigationView ao ser clicado irá deslizar sobre o ActionBar.
6. Já temos os layouts necessários para o menu de navegação, falta a codificação java de forma a permitir
que o utilizador interaja com os componentes.
6.1. Na “LoginActivity” altere o código do click do botão Login, para que passe a iniciar a atividade
“MenuMainActivity”.
6.2. Na atividade MenuMainActivity vai ser necessário comentar o código referente ao EdgeToEdge,
inicializar o NavigationView e o DrawerLayout, e carregar o conteúdo do cabeçalho do menu de
navegação, com o email recebido pela atividade anterior.
Coloque no método onCreate o código abaixo e crie o método carregarCabecalho().
private NavigationView navigationView;
private DrawerLayout drawer;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main_menu);
Toolbar toolbar = findViewById(R.id.toolbar);
setSupportActionBar(toolbar);
drawer = findViewById(R.id.drawerLayout);
navigationView = findViewById(R.id.navView);
ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
drawer, toolbar, R.string.ndOpen, R.string.ndClose);
toggle.syncState();
drawer.addDrawerListener(toggle);
carregarCabecalho(); //TODO:criar método
}
6.3. Quando um item do menu é selecionado é invocado o método, onNavigationItemSelected.
Adicione um listener que tem como função “escutar” o que acontece a um objeto e “informar”.
Nesta 1ª fase quando um item é selecionado iremos apresentar uma mensagem na consola e o
DrawerLayout é fechado.
public class MainMenuActivity extends AppCompatActivity implements
NavigationView.OnNavigationItemSelectedListener {
@Override
protected void onCreate(Bundle savedInstanceState) {
...
//acrescentar a linha
navigationView.setNavigationItemSelectedListener(this);
}
@Override
public boolean onNavigationItemSelected(MenuItem menuItem) {
if(menuItem.getItemId()==R.id.navEstatico)
System.out.println("-->Nav Estatico");
Ficha Prática 4, AMSI - TeSP PSI - 2025/2026 6
else if(menuItem.getItemId()== R.id.navDinamico)
System.out.println("-->Nav Dinamico");
else if(menuItem.getItemId()==R.id.navEmail)
System.out.println("-->Nav Email");
drawer.closeDrawer(GravityCompat.START);
return true;
}
6.4. Teste o código para verificar se está a funcionar corretamente.
7. Altere o código anterior, para que após a seleção de um item seja apresentado o ecrã pretendido.
7.1. Para isso vai precisar de criar dois fragmentos: “EstaticoFragment” e “DinamicoFragment”, ambos
do tipo “Fragment (Blank)” e copie o conteúdo dos layouts “activity_detalhes_estatico” e
“activity_detalhes_dinamico”.
7.2. Na classe de cada fragmento implemente o código necessário, podendo basear-se na ficha anterior.
Do código que foi gerado automaticamente, só irá ser necessário o construtor e o método
onCreateView (o restante código pode ser eliminado). Para aceder aos componentes gráficos, no
método onCreateView armazene a vista numa variável.
View view= inflater.inflate(R.layout.fragment_dinamico, container,
false);
tvTitulo = view.findViewById(R.id.tvTitulo);
//TODO: completar
return view;
8. Para que possa ser apresentada a informação de cada fragmento de acordo com a opção selecionada, no
“MenuMainActivity” vai ser necessário definir um objeto FragmentManager para gerir esses fragmentos.
Para o item email não será necessário criar nenhum fragmento pois será usado o intent implícito, elaborado
na ficha anterior.
8.1. O método onNavigationItemSelected terá de ser alterado, para incorporar as novas alterações:
private FragmentManager fragmentManager;
@Override
protected void onCreate(Bundle savedInstanceState) {
//adicionar ao código anterior a seguinte linha
fragmentManager = getSupportFragmentManager();
}
@Override
public boolean onNavigationItemSelected(MenuItem menuItem) {
Fragment fragment = null;
if (menuItem.getItemId()==R.id.navEstatico){
fragment = new EstaticoFragment();
Ficha Prática 4, AMSI - TeSP PSI - 2025/2026 7
setTitle(item.getTitle());
}
// TODO: restantes cases
if (fragment!= null)
fragmentManager.beginTransaction().replace(R.id.contentFragment,
fragment).commit();
// mantém-se o restante .. .
}
8.2. Volte a testar e verifique se ao clicar no item do menu é apresentado o respetivo fragmento.
9. Como deve ter reparado, inicialmente nenhum item está selecionado por isso nenhum fragmento é
apresentado. Assim sendo, é necessário efetuar a alterações para que quando é apresentado o
“MenuMainActivity” a opção “Livro Estático” esteja selecionada.
9.1. Crie o método carregarFragmentoInicial, onde terá de selecionar a opção do menu pretendida e
fazer o carregamento desse fragmento.
9.2. Volte a testar e se estiver tudo a funcionar corretamente apague as atividades antigas:
“MainActivity”, “DetalhesEstaticoActivity” e “DetalhesDinamicoActivity”, assim como todas as
dependências e layouts correspondentes.