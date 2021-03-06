# TP de Teste de Software - UFMG - 2022

#### Referências: https://www.youtube.com/playlist?list=PL62G310vn6nFBIxp6ZwGnm8xMcGE3VA5H

### Membros:

- Celso Junio Simões de Oliveira Santos
- Felipe Matheus Guimarães dos Santos
- Matheus Felipe Akira de Assis Oliveira
- Matheus Ferreira Coelho

### Sistema:

O sistema consiste em uma API REST, a qual tem as funções de adicionar, editar, remover e listar
animes.

O sistema foi desenvolvido seguindo as instruções de um curso que ensinava REST API com Spring
Boot.

### Tecnologias:

O sistema foi desenvolvido em Java, utilizando o framework Spring Boot e o gerenciador de build e
dependências Maven.

Para persistência dos dados, foi usado o banco de dados MySQL, utilizando docker e a framework JPA.

Os testes foram desenvolvidos usando dois frameworks:

- Spock para os testes de unidade;
- MockMVC e JUnit para os testes de componente e integração

## Codecov

**Link**: [Codecov](https://app.codecov.io/gh/FelipeGuimaraes42/tp-teste-software)

**Link**: [Codecov without class exclusions](https://app.codecov.io/gh/FelipeGuimaraes42/tp-teste-software/branch/test%2Fremove-jacoco-exclusions)

## PIT

Para rodar os testes de mutação do sistema, primeiro você precisa limpar a pasta target e verificar
a corretude da bateria de testes com o comando:

```
mvn clean test
```

Após, rode o pitest

```
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```

Após terminar de rodar as mutações e os testes, irá gerar uma nova pasta no seguinte caminho:

```
tp-teste-software/target/pit-reports
```

Abrindo essa pasta, o report será o *index.html*. Abra-o em seu navegador para vê-lo estilizado.
