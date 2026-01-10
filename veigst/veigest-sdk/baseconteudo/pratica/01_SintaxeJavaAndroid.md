DAVID SAFADINHO | SÓNIA LUZ | CÁTIA LEDESMA | GUILHERME PEREIRA
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
ACESSO MÓVEL A SISTEMAS DE INFORMAÇÃO - 2025 /2026
TESP PROGRAMAÇÃO DE SISTEMAS DE INFORMAÇÃO
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
 Sintaxe baseada na linguagem C;
 Com semelhanças para:
 Delimitadores de blocos
 Terminadores de instrução
 Estruturas de decisão
 Estruturas de repetição
 Tipos de dados
 Classes com atributos e métodos
 Em vez de apenas funções e variáveis
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
PRIMEIRA APLICAÇÃO
Linguagem C Linguagem JAVA
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
PRIMEIRA APLICAÇÃO EM ANDROID
Versão atual
Versão anterior
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
 Blocos de código são delimitados por chavetas { }
 Instruções terminam sempre com ponto e virgula ;
 Comentários
 Linha única: //
 Linha múltipla ou bloco: /* */
 Comentários de documentação: /** */
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
TIPOS DE DADOS
 Inteiros
 Reais
 Carateres
Linguagem C Linguagem JAVA
Suporta dois tipos de dados
 Referências:
 Classes e Interfaces
 Primitivos:
 Inteiros
 Reais
 Carateres
 Booleanos (boolean: true e false)
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
TIPOS DE DADOS NA LINGUAGEM JAVA
Ao contrário do que acontece na linguagem C, todos os tipos têm um tamanho fixo e os
tipos inteiros têm sinal, não existindo o modificador unsigned.
Todos os tipos têm uma classe de adaptação (wrapper) que permite transformar um tipo
primitivo num objeto.Tipos
Primitivos Tamanho Mínimo Máximo Classe
Adaptação
boolean 1-bit - - Boolean
char 16-bit Unicode 0 Unicode 216-1 Character
byte 8-bit -128 127 Byte
short 16-bit -32,768 32,767 Short
int 32-bit -2,147,483,648 2,147,483,647 Integer
long 64-bit -9,223,372,036,854,775,808 9,223,372,036,854,775,807 Long
float 32-bit 3.4e-38 3.4e+38 Float
double 64-bit 1.7e-308 1.7e+308 Double
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
DECLARAÇÃO DE VARIÁVEIS EM JAVA
 As variáveis podem ser declaradas em qualquer parte do programa
 Dentro de um bloco não se pode declarar uma variável com o nome de
uma variável do bloco anterior
 Por convenção, as variáveis começam por letra minúscula
 Todas as variáveis têm de ser iniciadas antes de serem utilizadas
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
OPERADORES
Operador DescriçãoGrupo
Atribuição
+
Aritméticos
em Java, não podem ser aplicados a expressões booleanas
Aritméticos em Java, não podem ser aplicados a expressões booleanas
Relacionais
Em C, os operandos podem ser resultado de expressões não reais.
Por outro lado, em Java, os operadores >, >=, < e <=, não podem ser
aplicados ao tipo boolean, apenas os operadores == e != podem ser
aplicados.
O resultado dos operadores relacionais em Java produzem um
resultado do tipo boolean
> >=
< <=
== !=
+ -
* / %
++ --
= += -=
*= /=
%=
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
OPERADORES
Operador DescriçãoGrupo
Lógicos
em Java, estes operadores só podem ser aplicados a expressões do
tipo boolean.
Ao contrário do C, em Java, produzem um resultado do tipo boolean
Operador
Condicional (Ternário)
em Java, a condição (1º operando) tem de ser do tipo boolean
Se a condição for verificada, produz o resultado da expressão (2º
operando).
Caso contrário, produz o resultado da expressão (3º operando)
Operador
de Conversão (cast)
A conversão de um tipo mais pequeno para um maior, ou do mais
específico para o menos específico, é feito de forma automática.
Ao invés, a conversão de um tipo maior para um mais pequeno, ou do
menos específico para o mais específico, tem de ser efetuado de
forma explícita utilizando o operador de cast.
( )
? :
&&
||
!
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
ESTRUTURAS DE CONTROLO
 As estruturas de controlo são exatamente iguais às do C.
 No entanto, todas as condições têm de ser expressões do tipo boolean.
/* Em C a condição de uma
estrutura de controlo podia
ser qualquer expressão */
int a=3;
if (a) {
}
if (a=5) {
}
/* Em Java a condição de uma
estrutura de controlo tem de ser uma
expressão “booleana” */
int a=3;
if (a!=0) {
}
boolean b = true;
if (b){
}
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
ESTRUTURAS DE CONTROLO
 Estruturas de Decisão
 If-else
 switch
 Estruturas de Repetição
 for
 for (foreach)
 while
 do..while
 break e continue
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
EXERCÍCIOS
 Estruturas de Decisão: if-else
 Ex 1: Criar um método para verificar se um valor é superior a outro (utilizando 2 maneiras
diferentes)
/* com aplicação do if-else */
boolean eMaiorIf(int valor1, int
valor2){
if(valor1 > valor2)
return true;
else
return false;
}
/* com aplicação do operador ternário */
boolean eMaiorTernario(int valor1, int valor2){
return valor1 > valor2 ? true : false;
}
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
EXERCÍCIOS
 Estruturas de Decisão: switch
 Ex 2: Criar um método para verificar se o caractere é uma vogal, consoante ou estrangeiro.
void eSwitch(char opcao) {
switch (Character.toUpperCase(opcao)) {
case 'A': case 'E': case 'I': case '0': case 'U':
System.out.println("R: A opção selecionada foi uma vogal");
break;
case 'Y': case 'W': case 'K':
System.out.println("R: A opção selecionada foi uma letra do alfabeto estrangeiro");
break;
default:
System.out.println("R: A opção selecionada foi uma consoante");
}
}
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
EXERCÍCIOS
 Estruturas de Repetição: for
 Ex 3: Criar um método para apresentar todos os números inteiros entre dois números (ordem
crescente)
void eFor(int n1, int n2) {
if (n1 <= n2)
for (int i = n1; i <= n2; i++) {
System.out.println("I = " + i);
}
else
for (int i = n2; i <= n1; i++) {
System.out.println("I = " + i);
}
}
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
EXERCÍCIOS
 Estruturas de Repetição: while
 Ex 4: Criar um método para apresentar todos os números inteiros entre dois números
void eWhile(int n1, int n2) {
int j, max;
if (n1 <= n2) {
j = n1;
max = n2;
}
else{
j=n2;
max=n1;
}
while (j <= max) {
System.out.println(“J = " + j);
j++;
}
}
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
EXERCÍCIOS
 Estruturas de Repetição: do…while
 Ex 5: Criar um método para apresentar todos os números inteiros entre dois números
void eDoWhile(int n1, int n2) {
int k, max;
if (n1 <= n2) {
k = n1;
max = n2;
}
else{
k=n2;
max=n1;
}
do {
System.out.println("K = " + k);
k++;
} while (k <= max);
}
SINTAXE DA LINGUAGEM JAVA PARA ANDROID
EXERCÍCIOS
 Estruturas de Repetição: foreach
 Ex 6: Criar um método para apresentar todos os números de um array.
void eForeach(){
int[] numeros = { 8, 2, 13, 4, 1, 2, 7 };
for(int n : numeros){
System.out.println (“Num = " + n);
}
}
FICHA 1
BOM TRABALHO !