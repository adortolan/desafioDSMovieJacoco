# Desafio DSMovie Jacoco

### Você deve implementar todos os testes unitários de service para o projeto DSMovie.

## Sobre o projeto DSMovie:
Este é um projeto de filmes e avaliações de filmes. A visualização dos dados dos filmes é pública (não necessita login), porém as alterações de filmes (inserir, atualizar, deletar) são permitidas apenas para usuários ADMIN. As avaliações de filmes podem ser registradas por qualquer usuário logado CLIENT ou ADMIN. A entidade Score armazena uma nota de 0 a 5 (score) que cada usuário deu a cada filme. Sempre que um usuário registra uma nota, o sistema calcula a média das notas de todos usuários, e armazena essa nota média (score) na entidade Movie, juntamente com a contagem de votos (count).  Veja vídeo explicativo.


<h1 align="left">
  <img alt="NextLevelWeek" title="#NextLevelWeek" src="./assets/ModeloConceitual.png" />
</h1>

### 🛠 Tecnologias

As seguintes ferramentas foram usadas na construção do projeto:

- [Java Spring Boot](https://spring.io/)
- [Java Spring Data](https://spring.io/projects/spring-data)
- [Java Spring Web](https://spring.io/projects/spring-ws)
- [Java Spring H2 Database](https://www.baeldung.com/spring-boot-h2-database)
- [Java Spring Validation](https://spring.io/guides/gs/validating-form-input)
- [Java Spring Security](https://spring.io/projects/spring-security)

#### Abaixo estão os testes unitários que você deverá implementar. Com todos os testes, o Jacoco deve reportar 100% de cobertura, mas o mínimo para aprovação no desafio são 12 dos 15 testes.

### MovieServiceTests:
- findAllShouldReturnPagedMovieDTO
- findByIdShouldReturnMovieDTOWhenIdExists
- findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist
- insertShouldReturnMovieDTO
- updateShouldReturnMovieDTOWhenIdExists
- updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist
- deleteShouldDoNothingWhenIdExists
- deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist
- deleteShouldThrowDatabaseExceptionWhenDependentId
### ScoreServiceTests:
- saveScoreShouldReturnMovieDTO
- saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId
### UserServiceTests:
- authenticatedShouldReturnUserEntityWhenUserExists
- authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists
- loadUserByUsernameShouldReturnUserDetailsWhenUserExists
- loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists



