In this project, I explored many technologies, with the objective of migrating a Java Swing application to a whole new web application.

The backend was reformulated from the commonly used 5-layered architecture to Clean Architecture, using Spring Boot and applying REST pattern to the API calls.

Using Angular to the front end, one of the most challenging this was to build a matrix 5×6 that uses drag-and-drop to allow user interaction, data modification and constraints check.

The software was designed to help the coordinators of the Federal Institute of Espírito Santo — Colatina Campus by managing teacher allocation throughout many courses in three available class times (morning, afternoon, noon).

Spring Security was used to control authentication and authorization by checking JWT secret that is stored on user's browser.

In this project, I achieve to improve the user experience by 100%, reduce workload of the coordinators of generating reports by exposing specific public routes to access the reports in the application and reduce paper impression.

This project it was part of my Final Paper, and was published by the Revista Contemporânea magazine and became a software that it is used by the federal government of Brazil, available at: http://samha.colatina.ifes.edu.br/

You can check the publication here: https://ojs.revistacontemporanea.com/ojs/index.php/home/article/view/3351

This was an amazing opportunity to grow as a developer, to every teacher that made this possible: Thank you! :)
----------------------------------------------------------------------------------------------------------------------
Manutenção SAMHA 2025 - Thaylan de Souza 
Melhorrias sugeridas pelo Gustavo Ludovico - coordenador do curso de SI


1 - Permitir acessar via https.

2 - Colocar no cabeçalho o curso, ano e semestre, possibilitando alternar entre telas sem ter que escolher esses dados toda vez que alternar de tela. As telas que abrirem seguirão o que está escolhido no cabeçalho; salvo os relatórios. Haverá a possibilidade de alterar os dados do cabeçalho.

3 - Conceito de publicação. Os relatório para o público (não logado) exibirão apenas o que está publicado. Ou estudar formas de melhorar o bloqueio da manipulação do horário do sistema.

<!-- 4 - Revisar os combobox para exibir os resultados ordenados. X -->

<!--5 - Adicionar o ano ao nome da turma (apenas ao exibir a turma, não no cadastro); auxiliando a exibição de turmas com o mesmo nome, diferenciando-as pelo ano de início. -->

6 - Interface Listagem de Disciplinas: Exibir a matriz curricular das disciplinas e ocultar a carga horária.


<!--7 - Permitir um número maior da CH ao cadastrar a disciplina (não travar limite máximo). Hoje é 120. Na nova grade há disciplinas 133,37. Avaliar a possibilidade de aceitar número fracionado. AVALIAR SE ESTE CAMPO É REALMENTE NECESSÁIO.
PS: FALTA COLOCAR NA TABELA PARA APARECER EM DOUBLE-->

8 - Ao excluir um professor de uma alocação e inserir outro sem sair da interface, é apresentado um erro dizendo que já existe um professor alocado à disciplina
ps : perguntar ao gustavo, pois não ha esse erro

9 - É gerado um " - 0" ao gerar o relatório das disciplinas por turma (verificar os demais relatórios). Acredito que este dado deveria se o semestre corrente da turma. Corrigi-lo.

10 - Produzir um relatório para o CRA das disciplinas, vagas e professore do ano semestre.
11 - Produzir um relatório para o CGP cadastrar o horário das disciplinas/professores no sistemas.
ps: perguntar ao Gustavo sobre o que é CRA e CPG

12 - Disciplinas divididas (tipo "POO & BD"). O sistema encara como uma única disciplina. Dá confusão ao visualizar\associar no acadêmico os professores de uma disciplina específica. A forma como isso será alterado afetará os itens 10 e 11.