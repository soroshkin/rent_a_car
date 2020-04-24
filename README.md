# Simple application for rent-a-car service
1. В файле persistence.xml добавлено два persistence unit для подключений к базам PostgreSQL(rental-cars) и H2(test).
2. Сделал тесты для тестирования DAO, сущностей, отношений и ограничений для полей классов. 
3. Настройки выбора базы данных находятся в классе  AppSettings. По умолчанию используется persistence 
unit "test" с базой данных H2.
4. CRUD операции осуществляются через статические методы класса EntityManagerUtil, получение данных из базы вне транзакций, 
обновление добавление удаление внутри транзакций, EntityManager Каждый раз создается заново и закрывается.

#База данных
1. Приложение для сервиса прокатных автомобилей состоит из следующих сущностей: Пользователь, Счет, Автомобиль, Пасспорт, Чек.
 У пользователя может быть только один счет, к одному счету привязан только один пользователь (Один к одному), 
 счет без пользователя существовать не может. Во-первых у Account нет публичного конструктора без аргумента User, но даже если бы он был,
 то Hibernate бы выдал ошибку IdentifierGenerationException, потому что не может получить id из null сущности.
 Для тестирования операций с аккаунтом используется класс JpaAccountDAOTest. В нем же есть метод testTwoAccountToOneUserShouldFail(),
 который тестирует попытку присовения одному пользователю два разных аккаунта. При удалении пользователя автоматически удаляется связанный с ним акааунт.
 За это отвечает аннотация @OnDelete(action = OnDeleteAction.CASCADE), она добавляет к SQL запросу ON DELETE CASCADE, что приводит к 
 каскадному удалению сущностей. Проверить можно в методе deleteIfHasNoBillsPassports класса JpaUserDAOTest. Удаление аккаунта не приводит к 
 удалению пользователя (метод delete класса JpaAccountDAOTest). В классе RelationshipsTest есть метод для проверки каскадного создания аккаунта tryAutoCreationOfAccounts.
 2. У каждого пользователя может быть несколько паспортов (например, приложение работает в разных странах, соответственно у пользователя появляется загран.паспорт).
 Связь один-ко-многим. Паспорт добавляется методом addPassport в сущности User. При добавлении паспорта и сохранении через JpaUserDAO автоматически создается новая запись в таблице Паспорт, это происходит благодаря элементу cascade = CascadeType.ALL.
 Это можно проверить в методе deleteIfHasPassports класса JpaUserDAOTest. В методе removeOrphanPassports происходит проверка правильности работы элемента orphanRemoval, 
 тоже самое только с помощью CriteriaAPI в методе oToMRelationshipUserPassportOrphanRemove() класса RelationshipsTest. 
 При удалении паспорта из коллекции в сущности пользователь, удаляется запись паспорта из таблицы. При удалении записи паспорта из таблицы удаления пользователя не происходит (deletePassport, JpaPassportDAOTest).
 При удалении пользователя, у которого есть паспорта выбрасывается исключение (сначала нужно удалить паспорта, методы deleteIfHasPassports, deleteIfHasNoBillsPassports).
 3. Аналогично построены отношения User-Bill и Bill-Car.
 4. Также имеется связь многие-ко-многим User-Car. Один пользователь ездит на разных машинах, одна машина сдается в аренду многим пользователям.
 Для отображения отношения создается промежуточная таблица trips, которая содержит составной первичный ключ, состоящий из двух внешних user_if, car_id.
 Проверка осуществляется в методе mToMRelationshipUserCars класса RelationshipsTest.
   
 
 
 
