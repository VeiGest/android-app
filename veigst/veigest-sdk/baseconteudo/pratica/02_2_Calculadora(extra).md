Ficha Prática 2.2 Extra, AMSI - TeSP PSI - 2025/2026 1
Departamento de Engenharia Informática
TeSP em Programação de Sistemas de Informação
Acesso Móvel a Sistemas de Informação
2º Ano – 1º Semestre
2025/2026
Docentes: Sónia Luz, sonia.luz@ipleiria.pt
David Safadinho, david.safadinho@ipleiria.pt
Cátia Ledesma, catia.ledesma@ipleiria.pt
Guilherme Pereira, guilherme.pereira@ipleiria.pt
Ficha Prática 2.2 (extra)
Calculadora Simples
Objetivos da Ficha
• Criar um novo projeto “Calculadora”
• Inserir e manipular componentes visuais: TextView e Button
• Aplicar e utilizar o RelativeLayout
Introdução
Nesta ficha vamos criar uma aplicação que permita ao utilizador implementar uma calculadora simples,
para operações sobre inteiros.
A aplicação “Calculadora” terá um aspeto semelhante ao apresentado na figura seguinte.
Ficha Prática 2.2 Extra, AMSI - TeSP PSI - 2025/2026 2
Criar um novo projeto (Calculadora Simples)
1. Depois de criar o projeto, comece por desenhar a interface gráfica seguindo o modelo da imagem
abaixo.
Como estamos a usar um layout relativo (RelativeLayout), a posição dos componentes é relativa a
outros componentes presentes no layout, e o Android Studio tenta definir as relações
automaticamente com base na ordem de adição dos componentes e com base no local onde o
componente foi introduzido (atenção às setas horizontais/verticais de posicionamento). Por esse
motivo, recomenda-se que sejam adicionados os componentes começando de cima para baixo, da
esquerda para a direita.
Se o layout não estiver correto, será necessário editar o XML e definir, manualmente, todas as
relações. Neste caso devem ser usadas as propriedades:
• android:layout_below - Posiciona este componente debaixo de outro componente.
• android:layout_toEndOf - Alinha o início do componente pelo fim de outro componente
(prende à esquerda).
Ficha Prática 2.2 Extra, AMSI - TeSP PSI - 2025/2026 3
• android:layout_alignEnd - Alinha o fim do componente pelo fim de outro componente
(prende à direita).
Podem ser necessárias outras propriedades, pelo que devem consultar a tabela de propriedades
disponíveis em:
https://developer.android.com/reference/android/widget/RelativeLayout.LayoutParams.html
QUESTÃO: Como resolver o desafio de alinhar o botão "ENTER", de modo a ocupar o espaço de dois
botões?
2. Implementar as funcionalidades correspondentes a cada botão e apresentar o respetivo
resultado das operações selecionadas.
2.1. Deve considerar que o utilizador tem de inserir dados pela seguinte ordem:
1º - Inserir o número 1;
2º - Inserir o operador;
3º - Inserir o número 2;
4º - Selecionar o enter;
2.2. Quando o utilizador escolher o “Enter”, após apresentar o resultado no ecrã, o mesmo
passará a corresponder ao valor do número 1.
2.3. Adicione um método com a assinatura public void onClick(View v) na classe da atividade e
faça com que esta implemente a interface View.OnClickListener (assim todos os
componentes saberão responder ao método onClick).
3. De modo a melhorar o funcionamento da calculadora deve acrescentar os botões:
3.1. “AC” - para limpar todo o conteúdo inserido;
3.2. “Apagar“ - para limpar o último valor ou operação inserida;
4. Para testar o fluxo de execução da aplicação deve tentar executar uma divisão por zero.