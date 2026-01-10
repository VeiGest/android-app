# VeiGest - Layouts XML
## Interface Gráfica e Recursos

**ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 / 2026**  
**TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO**

---

## 📖 Índice

1. [Introdução aos Layouts](#introdução-aos-layouts)
2. [Tipos de Layout](#tipos-de-layout)
3. [Layouts do VeiGest](#layouts-do-veigest)
4. [Views Comuns](#views-comuns)
5. [Material Design Components](#material-design-components)
6. [Recursos (Resources)](#recursos-resources)
7. [Temas e Estilos](#temas-e-estilos)
8. [Atributos Customizados](#atributos-customizados)
9. [Boas Práticas](#boas-práticas)

---

## Introdução aos Layouts

Um **Layout** define a estrutura visual de uma interface de utilizador. No Android, os layouts são definidos em ficheiros XML na pasta `res/layout/`.

### Estrutura de um Layout

```xml
<?xml version="1.0" encoding="utf-8"?>
<TipoDeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Views filhas -->
    <View
        android:id="@+id/nome_da_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

</TipoDeLayout>
```

### Namespaces XML

| Namespace | Prefixo | Uso |
|-----------|---------|-----|
| Android | `android:` | Atributos padrão do Android |
| App | `app:` | Atributos de bibliotecas (Material, ConstraintLayout) |
| Tools | `tools:` | Atributos apenas para preview (não aparecem em runtime) |

---

## Tipos de Layout

### LinearLayout

Organiza filhos em linha (horizontal) ou coluna (vertical).

```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Primeiro" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Segundo" />

</LinearLayout>
```

### ConstraintLayout

Layout flexível com constraints (restrições) entre views.

```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/tv_titulo"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:text="Título Centralizado" />

    <Button
        android:id="@+id/btn_acao"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toBottomOf="@id/tv_titulo"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_margin="16dp"
        android:text="Botão" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### FrameLayout

Empilha views umas sobre as outras. Ideal para containers de Fragments.

```xml
<FrameLayout
    android:id="@+id/fragment_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

### ScrollView

Permite scroll vertical do conteúdo.

```xml
<ScrollView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fillViewport="true">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">
        
        <!-- Conteúdo que pode exceder o ecrã -->
        
    </LinearLayout>

</ScrollView>
```

### Comparação de Layouts

| Layout | Uso | Performance |
|--------|-----|-------------|
| `LinearLayout` | Layouts simples lineares | ⭐⭐⭐ |
| `ConstraintLayout` | Layouts complexos | ⭐⭐⭐⭐ |
| `FrameLayout` | Container de fragments | ⭐⭐⭐⭐⭐ |
| `RelativeLayout` | Posicionamento relativo | ⭐⭐ (obsoleto) |
| `ScrollView` | Conteúdo scrollável | ⭐⭐⭐ |

---

## Layouts do VeiGest

### fragment_login.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="?attr/Background"
    android:fillViewport="true">

    <LinearLayout
        android:orientation="vertical"
        android:gravity="center"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="32dp">

        <!-- LOGO -->
        <ImageView
            android:id="@+id/logo"
            android:layout_width="140dp"
            android:layout_height="140dp"
            android:layout_marginBottom="130dp"
            android:src="@drawable/ic_veigest" />

        <!-- USERNAME -->
        <EditText
            android:id="@+id/et_username"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:padding="12dp"
            android:layout_marginBottom="16dp"
            android:background="?attr/TextBox"
            android:textColor="?attr/TextBoxText"
            android:textColorHint="?attr/TextHint"
            android:hint="@string/usernameE" />

        <!-- PASSWORD -->
        <EditText
            android:id="@+id/et_password"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:padding="12dp"
            android:layout_marginBottom="12dp"
            android:background="?attr/TextBox"
            android:inputType="textPassword"
            android:textColor="?attr/TextBoxText"
            android:textColorHint="?attr/TextHint"
            android:hint="@string/passwordE" />

        <!-- CHECKBOX -->
        <CheckBox
            android:id="@+id/cbRemember"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp"
            android:text="@string/remember_meE"
            style="?attr/checkboxStyle"
            android:textColor="?attr/RememberMeText" />

        <!-- PROGRESS BAR -->
        <ProgressBar
            android:id="@+id/progress_bar"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
            android:visibility="gone" />

        <!-- LOGIN BUTTON -->
        <Button
            android:id="@+id/btn_login"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:text="@string/loginE"
            android:textStyle="bold"
            android:textSize="16sp"
            style="?attr/buttonStyle"
            android:backgroundTint="?attr/ButtonBackground"
            android:textColor="?attr/ButtonText"
            android:layout_marginBottom="20dp" />

        <!-- LINKS -->
        <TextView
            android:id="@+id/tvForgotPassword"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/forgot_passwordE"
            android:textColor="?attr/LinkText"
            android:textStyle="bold"
            android:layout_marginBottom="8dp" />

        <TextView
            android:id="@+id/tvNoAccount"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/no_accountE"
            android:textColor="?attr/LinkText"
            android:textStyle="bold" />

    </LinearLayout>

</ScrollView>
```

### fragment_dashboard.xml (Estrutura)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="?attr/Background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <!-- Header com Menu e Logout -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical">

            <ImageView
                android:id="@+id/btn_menu"
                android:layout_width="32dp"
                android:layout_height="32dp"
                android:src="@drawable/ic_menu_hamburger" />

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical"
                android:layout_marginStart="16dp">

                <TextView
                    android:id="@+id/tv_welcome"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/welcome" />

                <TextView
                    android:id="@+id/tv_driver_name"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="18sp"
                    android:textStyle="bold"
                    android:text="@string/driver_name_placeholder" />

            </LinearLayout>

            <ImageView
                android:id="@+id/btn_logout"
                android:layout_width="32dp"
                android:layout_height="32dp"
                android:src="@drawable/ic_menu_logout" />

        </LinearLayout>

        <!-- Cards de Informação -->
        <com.google.android.material.card.MaterialCardView
            android:id="@+id/card_active_route"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            app:cardCornerRadius="12dp"
            app:cardElevation="4dp">

            <!-- Conteúdo do Card -->

        </com.google.android.material.card.MaterialCardView>

        <!-- Mais cards... -->

    </LinearLayout>

</ScrollView>
```

---

## Views Comuns

### TextView

Exibe texto estático ou dinâmico.

```xml
<TextView
    android:id="@+id/tv_titulo"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/titulo"
    android:textSize="18sp"
    android:textStyle="bold"
    android:textColor="@color/black"
    android:gravity="center"
    android:padding="8dp" />
```

### EditText

Campo de entrada de texto.

```xml
<EditText
    android:id="@+id/et_email"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:hint="@string/email_hint"
    android:inputType="textEmailAddress"
    android:imeOptions="actionNext"
    android:padding="12dp"
    android:background="@drawable/bg_edittext" />
```

#### Tipos de Input

| inputType | Descrição |
|-----------|-----------|
| `text` | Texto genérico |
| `textPassword` | Password (oculta caracteres) |
| `textEmailAddress` | Email (mostra @ no teclado) |
| `number` | Apenas números |
| `phone` | Número de telefone |
| `textMultiLine` | Texto com múltiplas linhas |

### Button

```xml
<Button
    android:id="@+id/btn_enviar"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    android:text="@string/enviar"
    android:textColor="@color/white"
    android:backgroundTint="@color/primary"
    android:textStyle="bold"
    android:textAllCaps="false" />
```

### ImageView

```xml
<ImageView
    android:id="@+id/iv_foto"
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:src="@drawable/ic_placeholder"
    android:scaleType="centerCrop"
    android:contentDescription="@string/foto_descricao" />
```

### ProgressBar

```xml
<!-- Circular (indeterminado) -->
<ProgressBar
    android:id="@+id/progress_circular"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:visibility="gone" />

<!-- Horizontal (determinado) -->
<ProgressBar
    android:id="@+id/progress_horizontal"
    style="?android:attr/progressBarStyleHorizontal"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:max="100"
    android:progress="50" />
```

### CheckBox

```xml
<CheckBox
    android:id="@+id/cb_aceitar"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/aceitar_termos"
    android:checked="false" />
```

---

## Material Design Components

### MaterialCardView

```xml
<com.google.android.material.card.MaterialCardView
    android:id="@+id/card_veiculo"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardCornerRadius="12dp"
    app:cardElevation="4dp"
    app:strokeWidth="0dp"
    android:layout_margin="8dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <TextView
            android:id="@+id/tv_placa"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="AA-00-AA"
            android:textSize="20sp"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/tv_modelo"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Toyota Hilux"
            android:textSize="14sp" />

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

### TextInputLayout

```xml
<com.google.android.material.textfield.TextInputLayout
    android:id="@+id/til_email"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="@string/email"
    app:endIconMode="clear_text"
    style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox">

    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/et_email"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:inputType="textEmailAddress" />

</com.google.android.material.textfield.TextInputLayout>
```

### FloatingActionButton

```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fab_adicionar"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom|end"
    android:layout_margin="16dp"
    android:src="@drawable/ic_add"
    app:backgroundTint="@color/primary"
    app:tint="@color/white" />
```

---

## Recursos (Resources)

### Cores (colors.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Cores Principais -->
    <color name="veigest_primary">#11C7A5</color>
    <color name="veigest_primary_dark">#0DA88A</color>
    <color name="veigest_accent">#FF5722</color>
    
    <!-- Cores de Texto -->
    <color name="Black">#FF000000</color>
    <color name="White">#FFFFFFFF</color>
    <color name="Grey">#757575</color>
    
    <!-- Cores de Estado -->
    <color name="success">#4CAF50</color>
    <color name="warning">#FF9800</color>
    <color name="error">#F44336</color>
    
    <!-- Cores de Background -->
    <color name="background_light">#F5F5F5</color>
    <color name="background_dark">#121212</color>
</resources>
```

### Strings (strings.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- App -->
    <string name="app_name">VeiGest</string>
    
    <!-- Login -->
    <string name="usernameE">Email ou Username</string>
    <string name="passwordE">Password</string>
    <string name="loginE">Entrar</string>
    <string name="remember_meE">Lembrar-me</string>
    <string name="forgot_passwordE">Esqueceu a password?</string>
    <string name="no_accountE">Não tem conta? Registe-se</string>
    
    <!-- Menu -->
    <string name="menu_dashboard">Dashboard</string>
    <string name="menu_routes">Rotas</string>
    <string name="menu_vehicles">Veículos</string>
    <string name="menu_documents">Documentos</string>
    <string name="menu_profile">Perfil</string>
    <string name="menu_settings">Definições</string>
    <string name="menu_logout">Terminar Sessão</string>
    
    <!-- Dashboard -->
    <string name="welcome">Bem-vindo</string>
    <string name="driver_name_placeholder">Condutor</string>
    
    <!-- Mensagens -->
    <string name="loading">A carregar...</string>
    <string name="error_connection">Erro de conexão</string>
    <string name="success_saved">Guardado com sucesso</string>
</resources>
```

### Dimensões (dimens.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Margens e Paddings -->
    <dimen name="margin_small">8dp</dimen>
    <dimen name="margin_medium">16dp</dimen>
    <dimen name="margin_large">24dp</dimen>
    
    <!-- Tamanhos de Texto -->
    <dimen name="text_small">12sp</dimen>
    <dimen name="text_medium">14sp</dimen>
    <dimen name="text_large">18sp</dimen>
    <dimen name="text_title">24sp</dimen>
    
    <!-- Componentes -->
    <dimen name="button_height">50dp</dimen>
    <dimen name="card_corner_radius">12dp</dimen>
    <dimen name="card_elevation">4dp</dimen>
</resources>
```

---

## Temas e Estilos

### themes.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <!-- Tema Base (Modo Claro) -->
    <style name="Theme.Veigest" parent="Theme.MaterialComponents.Light.NoActionBar">
        <!-- Cores Principais -->
        <item name="colorPrimary">@color/veigest_primary</item>
        <item name="colorPrimaryVariant">@color/veigest_primary_dark</item>
        <item name="colorOnPrimary">@color/White</item>
        <item name="colorSecondary">@color/veigest_accent</item>
        
        <!-- Background -->
        <item name="android:windowBackground">@color/background_light</item>
        
        <!-- Atributos Customizados -->
        <item name="Background">@color/background_light</item>
        <item name="TextBox">@drawable/bg_edittext_light</item>
        <item name="TextBoxText">@color/Black</item>
        <item name="TextHint">@color/Grey</item>
        <item name="ButtonBackground">@color/veigest_primary</item>
        <item name="ButtonText">@color/White</item>
        <item name="LinkText">@color/veigest_primary</item>
    </style>

</resources>
```

### themes.xml (night)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <!-- Tema Base (Modo Escuro) -->
    <style name="Theme.Veigest" parent="Theme.MaterialComponents.Light.NoActionBar">
        <!-- Cores Principais -->
        <item name="colorPrimary">@color/veigest_primary</item>
        <item name="colorPrimaryVariant">@color/veigest_primary_dark</item>
        <item name="colorOnPrimary">@color/White</item>
        
        <!-- Background -->
        <item name="android:windowBackground">@color/background_dark</item>
        
        <!-- Atributos Customizados -->
        <item name="Background">@color/background_dark</item>
        <item name="TextBox">@drawable/bg_edittext_dark</item>
        <item name="TextBoxText">@color/White</item>
        <item name="TextHint">@color/Grey</item>
        <item name="ButtonBackground">@color/veigest_primary</item>
        <item name="ButtonText">@color/White</item>
        <item name="LinkText">@color/veigest_primary</item>
    </style>

</resources>
```

---

## Atributos Customizados

### attrs.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Atributos customizados para temas -->
    <attr name="Background" format="reference|color" />
    <attr name="TextBox" format="reference" />
    <attr name="TextBoxText" format="reference|color" />
    <attr name="TextHint" format="reference|color" />
    <attr name="ButtonBackground" format="reference|color" />
    <attr name="ButtonText" format="reference|color" />
    <attr name="LinkText" format="reference|color" />
    <attr name="RememberMeText" format="reference|color" />
</resources>
```

### Usar Atributos no Layout

```xml
<!-- Referência a atributo do tema -->
<EditText
    android:background="?attr/TextBox"
    android:textColor="?attr/TextBoxText"
    android:textColorHint="?attr/TextHint" />

<Button
    android:backgroundTint="?attr/ButtonBackground"
    android:textColor="?attr/ButtonText" />
```

---

## Boas Práticas

### ✅ Fazer

```xml
<!-- Usar strings.xml para textos -->
android:text="@string/titulo"

<!-- Usar dimens.xml para valores -->
android:padding="@dimen/margin_medium"

<!-- Usar colors.xml para cores -->
android:textColor="@color/primary"

<!-- Usar match_parent e wrap_content -->
android:layout_width="match_parent"
android:layout_height="wrap_content"

<!-- Usar IDs descritivos -->
android:id="@+id/et_username"
android:id="@+id/btn_login"
android:id="@+id/tv_welcome"

<!-- Adicionar contentDescription em ImageViews -->
android:contentDescription="@string/logo_descricao"
```

### ❌ Evitar

```xml
<!-- Hardcoded strings -->
android:text="Login"  <!-- ❌ -->

<!-- Hardcoded dimensions -->
android:padding="16dp"  <!-- ❌ quando usado repetidamente -->

<!-- Hardcoded colors -->
android:textColor="#000000"  <!-- ❌ -->

<!-- IDs genéricos -->
android:id="@+id/textView1"  <!-- ❌ -->
android:id="@+id/button"  <!-- ❌ -->

<!-- Nesting excessivo de layouts -->
<LinearLayout>
    <LinearLayout>
        <LinearLayout>  <!-- ❌ Usar ConstraintLayout -->
```

### Prefixos de IDs Recomendados

| Tipo | Prefixo | Exemplo |
|------|---------|---------|
| TextView | `tv_` | `tv_titulo` |
| EditText | `et_` | `et_email` |
| Button | `btn_` | `btn_login` |
| ImageView | `iv_` | `iv_foto` |
| ProgressBar | `progress_` | `progress_loading` |
| CheckBox | `cb_` | `cb_aceitar` |
| RecyclerView | `rv_` | `rv_veiculos` |
| CardView | `card_` | `card_veiculo` |
| Container | `container_` | `container_main` |

---

## 📚 Documentação Relacionada

| Documento | Descrição |
|-----------|-----------|
| [03_Activities_Fragments.md](03_Activities_Fragments.md) | Activities e Fragments |
| [04_Navigation_Drawer.md](04_Navigation_Drawer.md) | Navigation Drawer |
| [09_Implementar_Novas_Funcionalidades.md](09_Implementar_Novas_Funcionalidades.md) | Criar novos layouts |

---

**Última atualização:** Janeiro 2026
