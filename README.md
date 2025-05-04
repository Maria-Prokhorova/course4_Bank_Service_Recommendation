# **Сourse4_Bank_Service_Recommendation**

Учебный проект: сервис рекомендаций кредитного продукта для пользователей банка.

### 1. Описание окружения.  
JDK 17,  
Project Maven,  
Spring Boot 3.4.5,  
встраиваемая база данных H2 (файл transaction.mv.db)  

### 2. Шаги развертывания. 
#### *Установка зависимостей:*  
модуль Spring Web,  
JDBC-драйвер для работы с базой данных H2,  
библиотека Lombok,  
зависимость для работы со Swagger,  
зависимости для тестирования приложения  

#### *Настройка конфигураций (application.properties)*

server.port=8080  
spring.datasource.url=jdbc:h2:file:./transaction  
spring.datasource.driver-class-name=org.h2.Driver  
spring.datasource.hikari.maximum-pool-size=1  
spring.h2.console.enabled=true  
spring.h2.console.path=/h2  
spring.jpa.show-sql=true  
spring.jpa.hibernate.ddl-auto=none  
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect  

### 3. Запуск приложения.
#### *Команды для сборки приложения*
mvn clean package
#### *Команда для запуска приложения*
java -jar target/BankingServiceApplication.jar

