# **Сourse4_Bank_Service_Recommendation**

Учебный проект: сервис рекомендаций кредитного продукта для пользователей банка.

### 1. Описание окружения.  
JDK 17,  
Project Maven,  
Spring Boot 3.4.5,  
встраиваемая база данных H2 (файл transaction.mv.db),
база данных PostgreSQL,  
телеграмбот

### 2. Шаги развертывания. 
#### *Установка зависимостей:*  
модуль Spring Web,  
JDBC-драйвер для работы с базой данных H2,  
зависимость для работы с postgresql,  
зависимость для работы с jpa,  
зависимость для работы с liquibase,  
библиотека Lombok,   
зависимость для работы со Swagger,   
зависимость для работы с springdoc-openapi,  
зависимости для тестирования приложения,  
библиотека Caffeine  для кеширования,  
зависимость для работы со Spring Cache,  
зависимости для работы с telegrambots   

#### *Настройка конфигураций (application.properties)*

server.port=8080  

#Подключение одной базы данных  
spring.datasource.url=jdbc:postgresql://localhost:5432/BankService  
spring.datasource.username=***  
spring.datasource.password=***  
spring.datasource.driver-class-name=org.postgresql.Driver  

#Подключение второй базы данных  
application.recommendations-db.url=jdbc:h2:file:./transaction  

#Настройка Hibernate  
spring.jpa.hibernate.ddl-auto= validate  
spring.jpa.show-sql=true  
spring.jpa.properties.hibernate.default_schema=public  

#Подключение liquibase  
spring.liquibase.change-log=classpath:liquibase/changelog-master.yml  

#Настройка логирования  
logging.level.org.hibernate.SQL=DEBUG  
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE  
logging.level.org.springframework.data=TRACE  

#Подключение телеграмбота  
telegram.bot.token=***  
telegram.bot.username=***  

### 3. Запуск приложения.
#### *Команды для сборки приложения*
mvn clean package
#### *Команда для запуска приложения*
java -jar target/BankingServiceApplication.jar  

### 4. Над проектом работали:  
Прохорова Мария  
Краснов Вячеслав  
Тарадаев Евгений 

