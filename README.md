## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

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
ON UPDATE NO ACTION);

    CREATE TABLE `anime_store`.`producer` (

`id` INT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(255) NOT NULL,
PRIMARY KEY (`id`));
