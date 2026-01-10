DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
CAP 6
# MENUS E SETTINGS
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026
TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA 1
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
● São componentes comuns da UI em diversos tipos de aplicações;
● Servem para apresentar ações do utilizador e outras opções em cada atividade;
● Existem 3 tipos principais:
● Option Menu (menu de opções)
● Context Menu (menu flutuante)
● Popup Menu
2DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
OPTIONS MENU
● Options Menu
● É o tipo de menu mais comum em Android;
● Nas versões 2.3 e anteriores do Android, o menu é acionado pressionando o botão físico do menu:
● Aparece na parte inferior e pode conter até 6 itens;
● Se esse número for ultrapassado a sexta opção terá o texto “Mais”, que depois apresenta as restantes opções;
● Na versão 3.0 e posteriores surgiu o conceito de Action Bar
● Na Action Bar os menus aparecem no canto superior direito;
● Podem ser apresentados como um menu drop-down, ou serem forçados a aparecer na barra superior;
3DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
CONTEXT MENU
● Context Menu
● É um menu flutuante que aparece quando selecionamos e seguramos (click longo) um determinado
componente visual do Android, como um botão;
● Fornece ações que afetam o conteúdo selecionado ou a estrutura do contexto;
● Assim, será apresentado no ecrã uma lista de opções
● que correspondem ao menu de contexto;
4DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
POPUP MENU
● Popup Menu
● É a implementação de menu mais atual
● Foi incluído na versão 3.0 do Android;
● É um menu modal ancorado a uma View
● Aparece sob a view que serve de âncora se tiver espaço, ou sobre a view;
5DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
DEFINIÇÃO DE MENU EM XML
● Em vez de criar um menu no código java da atividade;
● O Android fornece um formato xml para definir os itens de menu
● Porque é mais fácil visualizar a estrutura do menu em XML;
● Separa o conteúdo do menu do código da atividade;
● Permite criar configurações alternativas
● Por diferentes versões de plataforma, tamanho do ecrã, etc.;
● Deve definir um menu e respetivos itens através de um recurso de menu
● Para depois poder inflar (apresentar) o respetivo recurso de menu
● Ou seja, carregá-lo como um objeto Menu na atividade ou fragmento;
6DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
DEFINIÇÃO DE MENU EM XML
● Criar um ficheiro XML na pasta res/menu/ do projeto;
● Criar o menu com os seguintes elementos:
● <menu> - É o contentor para os itens de menu. Pode conter um ou mais elementos <item> e <group>;
● <item> - Representa um único item de um menu. Pode conter um <menu> aninhando para criar um submenu;
● <group> - É um contentor invisível e opcional para os elementos <item>. Permite categorizar itens de menu para
partilharem propriedades como estado ativo e visibilidade.
7DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
DEFINIÇÃO DE MENU EM XML
● Elemento <item> é compatível com vários atributos usados para definir aparência
ou comportamento;
● Os principais atributos a definir são:
● android:id - Id único do recurso, para que seja reconhecido quando o utilizador o seleciona;
● android:icon - Referência a um drawable (desenhável) usado como ícone do item;
● android:title - Referência a uma string usada como título do item;
● app:showAsAction - Permite especificar como e quando esse item deve aparecer como item de ação na
Action Bar; Ex: never, ifRoom, always, …;
8DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
USAR O MENU NA ATIVIDADE
● Para usar o menu na atividade pretendida
● É necessário inflar o recurso do menu
● Ou seja, converter o recurso XML num objeto programável;
● Através de MenuInflater.inflate();
● Cuja aplicação depende do tipo de menu a criar;
9DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
CRIAR UM OPTIONS MENU
● Definir o ficheiro menu_opcoes.xml correspondente ao menu;
10
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
xmlns:android="http://schemas.android.com/apk/res/android">
<item
android:id="@+id/menu_1"
android:icon="@android:drawable/ic_menu_sort_alphabetically"
android:title="@string/txtItemMenu1"
app:showAsAction="always" />
<item
android:id="@+id/menu_2"
android:icon="@android:drawable/ic_input_add"
android:title="@string/txtItemMenu2"
app:showAsAction="always" />
<item
android:id="@+id/menu_3"
android:icon="@android:drawable/ic_search_category_default"
android:title="@string/txtItemMenu3"
app:showAsAction="always" />
</menu>
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
CRIAR UM OPTIONS MENU
● Para especificar o menu de opções para uma atividade, é necessário modificar o
método onCreateOptionsMenu();
● Inflar o recurso de menu definido no XML no Menu fornecido pelo método callback;
11
@Override
public boolean onCreateOptionsMenu(Menu menu) {
//inflar o menu criado
MenuInflater inflater = getMenuInflater();
inflater.inflate(R.menu.menu_opcoes, menu);
return super.onCreateOptionsMenu(menu);
}
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
CRIAR UM OPTIONS MENU
● Para que a Action Bar seja apresentada
● Devem modificar o ficheiro themes.xml e selecionar um tema que seja ActionBar
● E modificar a propriedade do layout principal de cada atividade para não haver sobreposição
12
<resources xmlns:tools="http://schemas.android.com/tools">
<style name="Base.Theme.GestaoContactos" parent="Theme.Material3.DayNight">
<item name="colorPrimary">@color/purple_500</item>
</style>
<style name="Theme.GestaoContactos" parent="Base.Theme.GestaoContactos" />
</resources>
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
android:layout_marginTop="?attr/actionBarSize"
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
CRIAR UM OPTIONS MENU
● Processar os eventos de seleção
● Quando o utilizador seleciona um item do menu de opções
● Incluindo os itens de ação da Action Bar;
● O sistema invoca o método onOptionsItemSelected() da atividade;
● Este método recebe o MenuItem selecionado;
● É possível identificar o item através do método getItemId() que devolve o Id único definido;
● Quando se processa um item de menu com sucesso devemos devolver true;
● Se nenhum item for processado devemos chamar a implementação da superclasse que devolve false;
13DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
CRIAR UM OPTIONS MENU
● Processar os eventos de seleção
14
@Override
public boolean onOptionsItemSelected(MenuItem item) {
if (item.getItemId() == R.id.menu_1){
Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
return true;
}else if (item.getItemId() == R.id.menu_2){
Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
return true;
} else if (item.getItemId() == R.id.menu_3){
Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
return true;
}
return super.onOptionsItemSelected(item);
}
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
CRIAR UM CONTEXT MENU
● Para especificar um menu de contexto para uma atividade:
● Registar a view ao qual o menu deve estar associado através do método registerForContextMenu();
● Implementar o método onCreateContextMenu() na atividade, onde deve inflar o menu de contexto;
● Para tratar o item selecionado deve implementar o método onContextItemSelected();
15DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
CRIAR UM POPUP MENU
● Para definir um menu de Popup, para uma atividade:
● Deve implementar um método para o onClick da View que servirá de ancora;
● Onde deve instanciar um PopupMenu, indicando o contexto e a view a que está ancorado;
● Inflar o menu através do MenuInflater;
● Apresentar o menu através do PopupMenu.show();
● Para processar os eventos de seleção sobre um item:
● A atividade deve implementar a interface PopupMenu.OnMenuItemClickListener;
● Registá-la com o PopupMenu através do método setOnMenuItemclickListener();
● Quando o utilizador seleciona um item, o sistema chama o método onMenuItemclick();
16DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
UP ACTION
ACÇÃO VOLTAR ATRÁS
● Cada aplicação deve ter uma forma fácil de encontrar o caminho até à atividade
principal;
● Uma forma simples é adicionar um botão Up (ação voltar atrás)
● Na Action Bar;
● Para todas as atividades menos para a principal;
● Assim, quando o utilizador selecionar o botão Up
● A aplicação irá navegar para a atividade pai;
17DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
UP ACTION
ACÇÃO VOLTAR ATRÁS
● Para suportar esta funcionalidade:
● É necessário cada aplicação declarar no manifest a sua atividade Pai;
● Ativar o botão Up da Action Bar
(passou a ser opcional);
18DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
UP ACTION
ACÇÃO VOLTAR ATRÁS
● Declarar uma atividade pai no ficheiro AndroidManifest.xml
● Em versões mais antigas é necessário definir um <meta-data> com um par nome-valor
● O nome é o "android.support.PARENT_ACTIVITY"
● E o valor é nome da atividade pai
● A partir da versão 4.1
● Foi introduzido o atributo parentActivityName que deve ser definido na declaração da atividade
19
<activity android:name=".ListViewContactosActivity"
android:parentActivityName=".MainActivity"/>
<activity android:name=".ListViewContactosActivity" >
<meta-data
android:name="android.support.PARENT_ACTIVITY"
android:value="dei.amsi.gestaocontactos.MainActivity"/>
</activity>
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
UP ACTION
ACÇÃO VOLTAR ATRÁS
● Ativar o botão Up da Action Bar da atividade (opcional)
● Para aceder à action bar da atividade deve usar o método getSupportActionBar()
● E usar o método setDisplayHomeAsUpEnabled() para efetivamente ativar o botão;
20
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
...
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
}
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
UP ACTION
ACÇÃO VOLTAR ATRÁS
● Após ativar o botão Up da Action Bar da atividade
21DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
PERSISTÊNCIA DE DADOS SIMPLES
SHAREDPREFERENCES
● Necessidade:
● Persistir dados durante o uso da aplicação:
● Para que possam ser utilizados posteriormente;
● Como preferências de utilizador;
● Dados simples que não justificam o uso de bases de dados ou outro tipo de armazenamento mais
complexo;
● Solução:
● O Android disponibiliza uma forma de armazenamento;
● Designada de SharedPreferences, ou preferências de utilizador.
22DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
PERSISTÊNCIA DE DADOS SIMPLES
SHAREDPREFERENCES
● Usadas para dados simples, como por exemplo:
● Flag que indica se é a primeira vez que o utilizador entra na aplicação;
● Dados de login;
● Preferências do utilizador: “Exibição de temperatura em que tipo de graus”;
● Mas NUNCA guardar passwords ou dados pessoais como telefone, cartão crédito, etc;
23DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
PERSISTÊNCIA DE DADOS SIMPLES
SHAREDPREFERENCES
● Consiste numa interface que permite aceder e modificar dados de preferência de
utilizador;
● O valor armazenado apresenta-se sob o formato key-value (chave-valor);
● Ou seja, cada preferência armazenada possui uma identificação ou chave
● E associada a esta chave está um valor;
● Permite armazenar diversos tipos de valor:
● int, float, booleans, Strings e conjuntos de Strings;
24DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
PERSISTÊNCIA DE DADOS SIMPLES
SHAREDPREFERENCES
● Existem duas formas de trabalhar com a SharedPreferences:
● getPreferences() – Quando há poucas preferências a armazenar;
● Guardar dados dentro da mesma chave, como preferências de configuração da aplicação;
● getSharedPreferences() - Quando há muitas preferências a armazenar;
● Criar uma sharedPreference para cada categoria de dados a armazenar;
● Aceder a sharedPreference com apenas uma chave-valor:
● Deve criar um atributo do tipo SharedPreference o preenchê-lo através do método getPreference(),
indicando o modo de acesso:
● PRIVATE - Preferência apenas pode ser acedida pela aplicação;
● APPEND - Aplicado à criação de ficheiros;
● …;
25DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
PERSISTÊNCIA DE DADOS SIMPLES
SHAREDPREFERENCES
● Para alterar uma preferência utiliza-se um atributo do tipo SharedPreference.Editor
● Que irá permitir guardar o valor da sharedPreference alterado, ou atribuído;
● Para guardar a preferência desejada é preciso utilizar o método edit() do Editor
criado anteriormente;
26DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
PERSISTÊNCIA DE DADOS SIMPLES
SHAREDPREFERENCES
● Após isso, atribui-se o valor consoante o tipo de dados a guardar;
● Finalmente é necessário definir o método a usar para que a alteração seja efetivada:
● commit() – Disponível desde a primeira versão da API, e é executado de forma síncrona;
● apply() – Disponível a partir da versão 9, e é executado de forma assíncrona;
27DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
PERSISTÊNCIA DE DADOS SIMPLES
SHAREDPREFERENCES
28
public class MainActivity extends AppCompatActivity {
private String nome;
SharedPreferences sharedPreferences;
SharedPreferences.Editor editor;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
//aceder à sharedPreference e definir o modo de acesso
sharedPreferences = getPreferences(Context.MODE_PRIVATE);
//definir o Editor para permitir guardar / alterar o valor
editor = sharedPreferences.edit();
nome = sharedPreferences.getString("meuNome", ""); //ir buscar o seu valor a partir da chave
if( nome == ""){
Toast.makeText(this, "Nome não estava guardado", Toast.LENGTH_SHORT).show();
editor.putString("meuNome", "Sónia");
editor.apply();
}else{
Toast.makeText(this, "Nome: " + nome, Toast.LENGTH_SHORT).show();
}
}
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
MENUS
DESAFIO
● Criar um menu de opções na MainActivity da aplicação de gestão de contactos:
1. Definir um menu.xml com 3 itens;
2. Associar o menu à atividade principal;
3. Implementar o método para processar o evento de seleção;
4. Verificar quais as principais diferenças aos mudar o valor do atributo showAsAction;
29DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
VIEW BINDING, MENUS E SETTINGS
FONTES E MAIS INFORMAÇÃO
 View Binding
 https://developer.android.com/topic/libraries/view-binding
 Menus
 https://developer.android.com/develop/ui/views/components/menus
 Action Bar
 https://developer.android.com/training/appbar/index.html
 Adicionar Acção Up (Voltar Atrás)
 https://developer.android.com/training/appbar/up-action.html#declare-parent
 SharedPreferences
 https://developer.android.com/training/data-storage/shared-preferences
30DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 6 – #MENUS E SETTINGS
PRÓXIMO TEMA:
Personalização de Aplicações com Estilos e Temas
 Estilos e Temas
 https://developer.android.com/develop/ui/views/theming/themes
 Estilos do Android
 https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/res/res/values/styles.
xml
 Temas do Android
 https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/res/res/values/theme
s.xml
 FloatingAction Button
 https://developer.android.com/develop/ui/views/components/floating-action-button
31DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
BOM TRABALHO !
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026
TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA 32