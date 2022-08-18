# java-jdbc
Aplicação utilizando java jdbc com HypersonicSQL para a disciplina de Técnicas de Programação ministrada pelo professor Rene, UNESP Bauru em 2022.

## 1 - Executar o comando que está no arquivo runServer.bat no cmd
## 2 - Compilar o arquivo EMI.java (javac EMA.java)
## 3 - Executar o comando que está no arquivo runStatements.bat substituindo a classe `Cria` pela classe `EMA`

# Descrição do Trabalho.

Segundo Trabalho Semestral
Desenvolva um pedaço funcional de uma aplicação que você fará em Java. Para isso, baseado no
que você viu na execução do MDI.java, essa aplicação deve ter, pelo menos, menus, quatro janelas
usando Swing e a tabela do banco de dados HypersonicSQL do trabalho anterior de banco de dados
que você já me enviou. No exemplo que eu pensei, para a minha tabela do trabalho passado, que
chama PRODUTOS e tem os campos: CODIGO_EAN (13 caracteres), DESCRICAO (30
caracteres), FABRICANTE (30 caracteres) e PRECO (REAL), eu desenvolveria para o trabalho
atual uma parte de uma aplicação para ser usada em uma padaria ou oficina mecânica. A sua tabela
como é diferente, possivelmente, será de uma aplicação diferente.
Diferente do MDI.java que tem um item de menu específico para criar a tabela, a sua aplicação, no
início da execução do programa, se as tabelas não existirem, elas devem ser criadas; reforçando, se
as tabelas já existirem e tiverem dados, as tabelas devem ser mantidas e os dados não devem ser
perdidos. Para isso, deve-se tentar criá-las todas as vezes que o programa iniciar, se elas já existirem
será gerada uma exceção, que poderá ser ignorada.
Este trabalho tem dez dias para ser concluído.
Observações:
1. Não use package, lembre-se, compilarei os seus programas na linha de comandos com javac
sem pacotes, apenas o classpath .;hsql.jar.
2. As janelas para cadastramento, inserção, consulta e apagamento de registros devem ser
diferentes das minhas. Não é permitido o uso das minhas janelas como base. Crie as janelas
necessárias com layouts diferentes das janelas do exemplo MDI.java. Se você apenas incluir
novos campos no FlowLayout de uma janela do programa MDI.java, o seu programa não
será aceito por mim.
O programa deve ser enviado contendo os fontes (apenas os arquivos .java).
