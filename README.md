# JDBC Curso Dev Dojo

Projeto de estudo de **JDBC puro** (sem ORM), conectando uma aplicação Java a um banco MySQL rodando em Docker. Objetivo: praticar conexão, CRUD e mapeamento manual objeto-relacional como base antes de avançar para frameworks como Spring Data / JPA.

## Stack

- Java 17
- Maven
- MySQL 8 (via Docker)
- mysql-connector-j 9.7.0

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

```
src/main/java/com/maratonajava/jdbc/
├── conn/
│   └── ConnectionFactory.java   # abre a conexão JDBC com o MySQL
└── test/
    └── ConnectionFactoryTest.java  # classe main para validar a conexão
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

## Roadmap do estudo

- [x] Configurar Docker + MySQL
- [x] Conexão JDBC básica (`ConnectionFactory`)
- [ ] DAO de `Producer` (CRUD)
- [ ] DAO de `Anime` (CRUD + join com `Producer`)
- [ ] `PreparedStatement` para evitar SQL injection
- [ ] Tratamento de exceções customizado (camada de exception)
- [ ] Pool de conexões (ex: HikariCP)
