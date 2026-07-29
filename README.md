# Projeto do uso de JDBC e CRUD

Projeto de estudo de **JDBC puro** (sem ORM), conectando uma aplicação Java a um banco MySQL rodando em Docker. Objetivo: praticar conexão, CRUD e mapeamento manual objeto-relacional como base antes de avançar para frameworks como Spring Data / JPA.

## Stack

- Java 17
- Maven
- MySQL 8 (via Docker)
- mysql-connector-j 9.7.0
- Lombok 1.18.46 (redução de boilerplate: `@Value`, `@Builder`)
- Log4j2 2.25.4 (logging estruturado, substituindo `System.out` / `printStackTrace`)

## Como rodar o banco

O banco sobe via Docker Compose:

```bash
docker-compose up -d
```

Isso disponibiliza o MySQL em `localhost:3306`. Credenciais e schema usados no projeto (ajustar se você alterou o `docker-compose.yml`):

- **Database:** `anime_store`
- **User:** `root`
- **Password:** `root`

## Estrutura do banco

O schema `anime_store` possui duas tabelas relacionadas por chave estrangeira:

```sql
CREATE TABLE `anime_store`.`producer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `anime_store`.`anime` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(300) NOT NULL,
  `episodes` INT NOT NULL,
  `producer_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `producer_id_idx` (`producer_id` ASC) VISIBLE,
  CONSTRAINT `producer_id`
    FOREIGN KEY (`producer_id`)
    REFERENCES `anime_store`.`producer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
```

> Um `producer` pode ter vários `anime`s (relação 1:N).

## Estrutura do projeto

O código-fonte reúne dois módulos independentes: `jdbc`, onde ficam os experimentos e o estudo aprofundado dos recursos da API JDBC, e `crud`, um projeto novo e mais enxuto que reaproveita esse aprendizado para construir uma aplicação de console real. Ambos seguem a mesma divisão em camadas (`repository` / `service` / `dominio`), separando a lógica de acesso a dados das regras de negócio:

```
src/main/java/com/maratonajava/jdbc/
├── conn/
│   └── ConnectionFactory.java          # abre a conexão JDBC (Connection, JdbcRowSet e CachedRowSet)
├── dominio/
│   └── Producer.java                   # entidade (Lombok @Value + @Builder)
├── listener/
│   └── CustomRowSetListener.java       # RowSetListener: loga e reage a mudanças/execuções de um RowSet
├── repository/
│   ├── ProducerRepository.java         # CRUD via Statement / PreparedStatement / CallableStatement / transações
│   └── ProducerRepositoryRowSet.java   # variações usando JdbcRowSet e CachedRowSet
├── service/
│   ├── ProducerService.java            # regras de negócio (ex: validação de id) sobre o repository
│   └── ProducerServiceRowSet.java      # regras de negócio sobre o repository RowSet
└── test/
    ├── ConnectionFactoryTest.java            # classe main para validar a conexão
    ├── ConnectionFactoryTestRowSet.java      # classe main para validar o uso de JdbcRowSet/CachedRowSet
    └── ConnectionFactoryTestTransaction.java # classe main para validar o insert transacional

src/main/java/com/maratonajava/crud/
├── conn/
│   └── ConnectionFactory.java          # abre a conexão JDBC do módulo crud
├── dominio/
│   ├── Producer.java                   # entidade (Lombok @Value + @Builder)
│   └── Anime.java                      # entidade (Lombok @Value + @Builder), referencia um Producer
├── repository/
│   ├── ProducerRespository.java        # CRUD completo de Producer via PreparedStatement
│   └── AnimeRespository.java           # CRUD completo de Anime via PreparedStatement (join com Producer)
├── service/
│   ├── ProducerService.java            # regras de negócio + orquestração do menu de Producer
│   └── AnimeService.java               # regras de negócio + orquestração do menu de Anime
└── test/
    └── CrudTest.java                   # classe main com o menu interativo (CLI), com submenus Producer/Anime

src/main/resources/
└── log4j2.xml                          # configuração de logging
```

## Como rodar

Com o banco no ar (`docker-compose up -d`), compile e execute via Maven:

```bash
mvn clean compile
mvn compile exec:java -Dexec.mainClass="com.maratonajava.jdbc.test.ConnectionFactoryTest"
```

Se a conexão for bem-sucedida, o console imprime algo como:

```
com.mysql.cj.jdbc.ConnectionImpl@d21a74c
```

Para rodar a aplicação CRUD de console (módulo `crud`):

```bash
mvn compile exec:java -Dexec.mainClass="com.maratonajava.crud.test.CrudTest"
```

## Funcionalidades implementadas

### Módulo `jdbc` (estudo dos recursos da API)

#### `ProducerRepository` / `ProducerService`

- **Insert** (`save`) e **Delete** (`delete`) de `Producer` via `Statement`.
- **Update** de `Producer`, com duas versões: uma via `Statement` (string concatenada) e outra via `PreparedStatement` (`updatePreparedStatemets`), evitando SQL injection.
- **Insert transacional** (`saveTransaction`): insere uma lista de `Producer`s dentro de uma mesma transação (`setAutoCommit(false)` + `commit()`), preparado para `rollback()` em caso de falha.
- **Consultas** (`findAll`, `findByName`) retornando `List<Producer>` a partir do `ResultSet`.
- **Metadados**: `showProducerMetaData` (colunas/tipos da tabela via `ResultSetMetaData`) e `showDriveMetaData` (capacidades do driver/`DatabaseMetaData`, como suporte a `TYPE_SCROLL_INSENSITIVE`/`TYPE_SCROLL_SENSITIVE` e `CONCUR_UPDATABLE`).
- **`ResultSet` navegável e atualizável** (`TYPE_SCROLL_INSENSITIVE` + `CONCUR_UPDATABLE`):
  - `showTypeScrollWorking`: navegação com `first`, `last`, `absolute`, `relative`, `previous`, `isFirst`, `isLast`.
  - `findByNameAndUpdateToUpperCase`: atualiza registros in-place via `updateRow`.
  - `findByNameAndInsertWhenNotFound`: insere um novo registro via `moveToInsertRow`/`insertRow` quando a busca não encontra nada.
  - `findByNameAndDelete`: remove registros encontrados via `deleteRow`.
- **`PreparedStatement`** (`findByNamePreparedStatement`) e **`CallableStatement`** (`findByNameCallableStataments`, chamando a stored procedure `sp_get_producer_by_name`).

#### `ProducerRepositoryRowSet` / `ProducerServiceRowSet`

- Uso de `JdbcRowSet` (`javax.sql.rowset`) como alternativa ao `Connection`/`Statement` tradicional, com `findByNameJdbcRowSet`.
- `updateJdbcRowSet`: busca o registro pelo `id` via `JdbcRowSet` e atualiza a linha com `updateString`/`updateRow` (em vez de montar um `UPDATE` manualmente).
- `updateCachedRowSet`: mesma ideia, agora com `CachedRowSet` (desconectado do banco) — carrega a linha, altera em memória e sincroniza com `acceptChanges(connection)`.
- `CustomRowSetListener`: implementação de `RowSetListener` registrada nos RowSets (`addRowSetListener`) para logar via Log4j2 quando o comando é executado (`rowSetChanged`), uma linha é alterada (`rowChanged`) ou o cursor se move (`cursorMoved`).

### Módulo `crud` (aplicação de console)

Aplicação CRUD completa orientada a menu de texto, reaproveitando os conceitos estudados no módulo `jdbc`:

- `CrudTest`: loop principal que lê a opção do usuário via `Scanner` e navega por um menu com submenus — `1` para `Producer`, `2` para `Anime`, `0` para sair; cada submenu tem `9` para voltar.
- `ProducerService` / `AnimeService`: roteiam a opção escolhida em cada submenu (`findByName`, `delete`, `save`, `update`) e orquestram a interação via console.
- `ProducerRespository`: CRUD completo de `Producer` via `PreparedStatement` (`findByName`, `findById`, `save`, `uptade`, `delete`).
- `AnimeRespository`: CRUD completo de `Anime` via `PreparedStatement`, com `findByName`/`findById` fazendo `INNER JOIN` com `producer` para trazer o nome do produtor relacionado.
- Entidade `Anime` (`id`, `name`, `episodes`, `producer`) relacionada a um `Producer`, refletindo a FK do banco.

## Roadmap do estudo

- [x] Configurar Docker + MySQL
- [x] Conexão JDBC básica (`ConnectionFactory`)
- [x] DAO de `Producer` (CRUD via `Statement`)
- [x] `PreparedStatement` para evitar SQL injection
- [x] `CallableStatement` (stored procedures)
- [x] `ResultSet` navegável e atualizável (scroll + update/insert/delete de linhas)
- [x] `JdbcRowSet` e `CachedRowSet` como alternativa ao `Connection` direto
- [x] `RowSetListener` para reagir a eventos de um `RowSet`
- [x] Transações (`commit`/`rollback`) em operações com múltiplos registros
- [x] Logging estruturado com Log4j2 e Lombok (`@Log4j2`, `@Value`, `@Builder`)
- [x] Módulo `crud`: aplicação de console com menu (submenus `Producer` / `Anime`)
- [x] CRUD completo de `Producer` no módulo `crud` (insert/update/delete via menu)
- [x] CRUD completo de `Anime` no módulo `crud` (join com `Producer` via menu)
- [ ] Tratamento de exceções customizado (camada de exception)
- [ ] Pool de conexões (ex: HikariCP)
