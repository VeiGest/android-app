DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
CAP 2
DESENHO DE INTERFACES GRÁFICAS | LAYOUTS
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026
TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 2
COMPOSIÇÃO DE UM PROJETO EM ANDROID
Ficheiros do
Projeto
Palete de
Componentes
Interface Associada
a cada activity
Propriedades do
componente
selecionado
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 3
COMPOSIÇÃO DE UM PROJETO EM ANDROID
MANIFEST
 AndroidManifest.xml
 Deve obrigatoriamente acompanhar o projeto
 Descreve as características da aplicação
 Identifica os componentes que a compõem
 Activity, Service, ….
 Permite a definição de permissões de acesso
 Funcionalidade protegidas do dispositivo
 Interagir com outras aplicações
 Aceder a outras aplicações
 (<permission>, <uses-permission>, ….)
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 4
COMPOSIÇÃO DE UM PROJETO EM ANDROID
DIRETORIA JAVA
 Diretoria que contém o código da aplicação
 Ficheiros com extensão .java
 Inclui todas as atividades definidas para a aplicação
 Uma atividade definida como principal e que é executada no lançamento da aplicação
 Inclui o código dos testes automatizados pelo Android
 Package de testes unitários: test
 Package de testes instrumentados: androidTest
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 5
COMPOSIÇÃO DE UM PROJETO EM ANDROID
DIRETORIA RES
 Contém os recursos da aplicação que não são código
 Drawable
 Contém as imagens usadas na aplicação
 É possível separar as imagens em pastas, considerando o tamanho e resolução de cada dispositivo
 Layout
 Contém ficheiros .xml que definem o UI (ecrãs) da aplicação - Layouts
 Mipmap
 Contém os icons da aplicação
 Values
 Contém outros ficheiros .xml que definem recursos do tipo string e color
 Alguns podem ser utilizados para a internacionalização da aplicação
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 6
DESENHO DE INTERFACES GRÁFICAS
VIEWGROUP | VIEW
 Interface de utilizador (UI) de uma APP Android
 Contém uma hierarquia de objetos do tipo
 ViewGroup (Layout)
 Não são visíveis
 Contêm outros objetos do tipo ViewGroup ou View
 Apenas definem a disposição (horizontal, vertical, …) pelo
qual os objetos view se encontram dispostos
 View (Widgets)
 Componentes que já incluem algumas funcionalidades
 Permitem interação com o utilizador
 Ex: botões, campos de texto, listas, …
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 7
LAYOUTS
DEFINIÇÃO
 É uma arquitetura para organizar a UI no Android
 Pode ser contruído de duas formas:
 Forma Visual
 Utilizando os recursos de drag and drop de views (Widgets)
 Forma Declarativa
 Utilizando o ficheiro XML que representa o layout
 Declaração em ficheiros XML permite melhor separação entre a apresentação e o código que
implementa os comportamentos
 Views e ViewGroups podem ser construídos em tempo de execução
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 8
LAYOUTS
LOCALIZAÇÃO
 Todos os ficheiros têm extensão XML
 Localizados na pasta res/layout do
projeto
 Registados na classe R
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 9
LAYOUTS
ATRIBUIÇÃO DO LAYOUT À ATIVIDADE – CLASSE R
 Atribuir o GUI específico para a interface
 Através do método setContentView()
 Indicando a referência única definida na classe R (R.java)
 Invocado dentro do método onCreate() da atividade
 Não existe uma relação direta entre o nome do ficheiro e o nome da atividade
@Override
protected void onCreate(Bundle savedInstanceState)
{
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
}
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 10
LAYOUTS
ATRIBUIÇÃO DO LAYOUT À ATIVIDADE – CLASSE R
 Exemplo de acesso aos componentes através da classe R e do findViewbyId()
public class AboutActivity extends AppCompatActivity {
private TextView textViewNome;
private EditText editTextEmail;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_about);
textViewNome = findViewById(R.id.textViewNome);
editTextEmail = findViewById(R.id.etEmail);
textViewNome.setText("Sónia Luz");
editTextEmail.setText("sonia.luz@ipleiria.pt");
}
}
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 11
LAYOUTS
ATRIBUIÇÃO DO LAYOUT À ATIVIDADE – VINCULAÇÃO DE VISUALIZAÇÃO
 View Binding: Recurso que facilita a programação de código que interage com
visualizações
 disponível a partir da versão 3.6 do Android Studio
 efetua o Binding automático
 através de uma classe de vinculação gerada para cada ficheiro xml de layout
 substitui o findViewById()
 permite evitar erros de acesso incorreto
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 12
LAYOUTS
ATRIBUIÇÃO DO LAYOUT À ATIVIDADE - VINCULAÇÃO DE VISUALIZAÇÃO
 Configurar o Binding automático
 Ativar o viewBinding no ficheiro
build.gradle.kts (module)
 Sincronizar o projeto: Sync Now
android {
...
buildFeatures {
viewBinding = true
}
}
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 13
LAYOUTS
ATRIBUIÇÃO DO LAYOUT À ATIVIDADE - VINCULAÇÃO DE VISUALIZAÇÃO
 Exemplo com classe AboutActivity
 Definir um atributo binding para a classe de vinculação ActivityAboutBinding
 Efetuar o inflate do layout da atividade
 através do método inflate() da classe de vinculação
 Atribuir a referência do layout ao setContentView da atividade
 através do método getRoot()
 Aceder diretamente aos componentes view do layout
 através do atributo binding
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 14
LAYOUTS
ATRIBUIÇÃO DO LAYOUT À ATIVIDADE - VINCULAÇÃO DE VISUALIZAÇÃO
 Exemplo com classe AboutActivity
public class AboutActivity extends AppCompatActivity {
private ActivityAboutBinding binding;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
// setContentView(R.layout.activity_about);
binding = ActivityAboutBinding.inflate(getLayoutInflater());
setContentView(binding.getRoot());
binding.textViewNome.setText("Sónia Luz");
binding.editTextEmail.setText("sonia.luz@ipleiria.pt");
}
}
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 15
LAYOUTS
PARÂMETROS DE UM LAYOUT
 Atributos designados layout_something
 Definem parâmetros para a View de acordo com o layout onde estão inseridos
 Cada ViewGroup
 Define os parâmetros de layout para as Child Views
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 16
LAYOUTS
PARÂMETROS DE UM LAYOUT
 Entre estes atributos estão os que permitem a definição de largura (layout_width) e altura
(layout_height)
 Normalmente usam-se as constantes:
 wrap_content – ajusta o tamanho de acordo com o conteúdo
 match_parent – preenche o tamanho máximo permitido pelo layout
 Valor exato é possível (mas não é recomendado)
 Unidades de medida disponíveis:
 px (pixels), dp (density-independente pixels), sp (scaled pixels), in (inches), mm (millimiters)
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 17
LAYOUTS
TIPOS DE LAYOUTS – 3 TIPOS/GRUPOS MAIS COMUNS
 Layout Linear
 Organiza os childs numa única linha
horizontal ou vertical
 Se o comprimento da janela exceder o
tamanho do ecrã, é criada uma scrollbar
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 18
LAYOUTS
TIPOS DE LAYOUTS – 3 TIPOS/GRUPOS MAIS COMUNS
 Layout Relativo
 Permite especificar a localização de
objetos child
 Relativos entre si
 Ex: child A à esquerda de child B
 Relativos aos parents
 Alinhados na parte superior do parent
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 19
LAYOUTS
TIPOS DE LAYOUTS – 3 TIPOS/GRUPOS MAIS COMUNS
 Visualização Web (Web View)
 Interpreta e representa páginas da Web
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 20
LAYOUTS
TIPOS DE LAYOUTS – 3 TIPOS/GRUPOS MAIS COMUNS
 Tipos de layouts específicos, com diferentes características
 LinearLayout
 RelativeLayout
 ConstraintLayout
 TableLayout
 GridLayout
 CoordinatorLayout
 …
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 21
LAYOUTS
LINEAR LAYOUT
 É um ViewGroup que dispõe as child views seguidas umas às outras em sequência vertical
ou horizontal
 Atributo: android:layout_orientation
 Respeita as margens entre as childs views e alinhamento (ao centro, à esquerda, à direita)
 Atributo: android:layout_gravity
 Permite atribuir pesos, individualmente, para que estas views possam ocupar o restante
espaço do layout
 Atributo: android:layout_weight
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 22
LAYOUTS
RELATIVE LAYOUT
 Permite definir a posição dos componentes
 Relativa a outros componentes presentes no layout
 Relações definidas automaticamente
 Com base na ordem de adição e local (no Android Studio)
 Propriedades principais para definição em xml
 Atributo: android:layout_below
 Posiciona este componente debaixo de outro componente
 Atributo: android:layout_toRightOf
 Alinha o início do componente pelo fim de outro componente (prende à esquerda)
 Atributo: android:layout_alignEnd
 Alinha o fim do componente pelo fim de outro componente (prende à direita)
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 23
LAYOUTS
CONSTRAINT LAYOUT
 Permite definir layouts grandes e complexos com uma hierarquia flat (sem necessidade de
sub layouts)
 Similar ao RelativeLayout
 Todas as Views se relacionam entre si e o seu parent
 Utiliza as vantagens das ferramentas visuais do Layout Editor
 Mais fácil de construir o layout por drag-and-drop do que pelo xml
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 24
LAYOUTS
TABLE LAYOUT
 Organiza as child views em linhas e colunas
 Cada child é representado pelo componente TableRow
 Permite que uma ou mais células sejam adicionadas horizontalmente
 Cada célula só pode conter uma view
 Caso os atributos android:layout_width e android:layout_height não sejam declarados:
 A Largura é ajustada para match_parent
 A Altura é ajustada para wrap_content
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 25
LAYOUTS
GRID LAYOUT
 Tem o intuito de juntar dois layouts
 LinearLayout e TableLayout
 Composto por um conjunto de linhas e colunas
 Poderá ter orientação horizontal e vertical
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 26
LAYOUTS
COORDINATOR LAYOUT
 Layout introduzido na versão 24.1 através do package (Android Design Support Library)
 Tem como objetivo e filosofia coordenar as views que contém
 Permite definir diferentes interações entre cada child
 Através de Behaviors (comportamentos)
 Possível a definição de âncoras para as childs
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 27
BIBLIOGRAFIA
 Estrutura de um Projeto Android Studio
 https://developer.android.com/studio/intro
 Interface do Utilizador no Android
 https://developer.android.com/guide/topics/ui/index.html
 ViewGroups (Layouts)
 http://developer.android.com/guide/topics/ui/declaring-layout.html
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO – 2025 / 2026
CAP 2 – DESENHO INTERFACES GRÁFICAS E LAYOUTS 28
PRÓXIMO TEMA
ACTIVITIES E INTENTS
 Atividades
 http://developer.android.com/guide/components/activities.html
 Intents e filtros de Intents
 http://developer.android.com/guide/components/intents-filters.html
 Gerir o ciclo de vida de uma atividade
 https://developer.android.com/guide/components/activities/activity-lifecycle
 Interagir com outras Apps
 http://developer.android.com/training/basics/intents/index.html
 Partilhar dados simples
 http://developer.android.com/training/sharing/index.html
DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA
BOM TRABALHO !
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026
TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO