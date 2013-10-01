hc-nettyapp
===========

Задача
===========
Необходимо реализовать http-сервер на фреймворке netty (http://netty.io/), со следующим функционалом:
 
1. По запросу на http://somedomain/hello отдает «Hello World» через 10 секунд
2. По запросу на http://somedomain/redirect?url=<url> происходит переадресация на указанный url
3. По запросу на http://somedomain/status выдается статистика:
 - общее количество запросов
 - количество уникальных запросов (по одному на IP)
 - счетчик запросов на каждый IP в виде таблицы с колонкам и IP, кол-во запросов, время последнего запроса
 - количество переадресаций по url'ам  в виде таблицы, с колонками url, кол-во переадресация
 - количество соединений, открытых в данный момент
 - в виде таблицы лог из 16 последних обработанных соединений, колонки src_ip, URI, timestamp,  sent_bytes, received_bytes, speed (bytes/sec)
 
Все это (вместе с особенностями имплементации в текстовом виде) выложить на github, приложить к этому:
- инструкции как билдить и запускать приложение
- скриншоты как выглядят станицы /status в рабочем приложении
- скриншот результата выполнения команды ab –c 100 –n 10000 “http://somedomain/status"
 
Комментарии
 - использовать самую последнюю стабильную версию нетти.
 - обратить внимание на многопоточность
 - разобраться в EventLoop’ами нетти

Инструкция
===========

Для установки приложения необходимы:

1. Java 1.6
2. MySQL 5.1.6
3. Maven

Перед запуском приложения, необходимо выполнить MySQL-запрос находящийся в файле /db/dump.sql
Ссылку на базу, логин и пароль пользователя необходимо указать в файле db.properties.
Сам сервер можно запустить в консоли из папки /mavenapp, введя команду mvn exec:java -Dexec.mainClass="core.HttpServer".
Так же команда mvn compile assembly:single создаст единый jar-архив.
Приложение использует порт 8082, например http://localhost:8082/status

Особенности имплементации
===========

Запись данных о запросах происходит с помощью класса JbdcLoggingHandler.
Базовый парсинг URL - в классе ControllerFactory, который возвращает объект контроллера в зависимости от запроса.
Специфическая логика обработки запросов реализована в отдельных контроллерах.
Для рендеринга страницы статуса используется класс StatusPage, являющийся аналогом вьюхи в MVC. 
Данные во "вьюху" передаются с помощью объекта-комманды StatusCommand.

Примечания
===========

При разработке приложения использовался Java-фреймворк Netty, как каркас реализующий drive-event архитектуру сервера. 
Работу с БД обеспечивала библиотека JDBC.
При запросе к серверу с локалхоста с операционной системой Ubuntu, приложение записывает IP как IPv6, в тоже время при запросах в браузере с удаленных клиентов, а так же при использовании утилиты
ab записывает как IPv4.
Под скоростью в статистике понимается сумма переданных и принятых байт поделенная на время выполнения запроса.