# Сетевой Чат

Консольное чат-приложение на Java, позволяющее нескольким пользователям общаться через центральный сервер. Приложение состоит из серверного компонента, обрабатывающего подключения нескольких клиентов, и клиентского компонента, позволяющего пользователям отправлять и получать сообщения.

## Возможности

- Поддержка многопользовательского чата
- Проверка уникальности имени пользователя
- Сохранение истории сообщений
- Настраиваемые параметры сервера
- Консольный интерфейс
- Корректная обработка подключений
- Поддержка команды "/exit" для выхода из чата

## Структура проекта

```
NetworkChat/
├── src/
│   └── main/
│       ├── java/
│       │   └── ru/
│       │       └── netology/
│       │           └── chat/
│       │               ├── server/
│       │               │   ├── ChatServer.java
│       │               │   ├── ClientHandler.java
│       │               │   └── ServerMain.java
│       │               ├── client/
│       │               │   ├── ChatClient.java
│       │               │   └── ClientMain.java
│       │               └── config/
│       │                   └── Settings.java
│       └── resources/
│           └── logback.xml
├── settings.txt
├── file.log
└── pom.xml
```

## Требования

- Java 21 или выше
- Maven

## Конфигурация

Приложение можно настроить через файл `settings.txt`:

```properties
server.host=localhost
server.port=8989
```

## Сборка проекта

Для сборки проекта выполните:

```bash
mvn clean package
```

## Запуск приложения

1. Запуск сервера:
```bash
mvn compile exec:java -Dexec.mainClass="ru.netology.chat.server.ServerMain"
```

2. Запуск одного или нескольких клиентов в отдельных окнах терминала:
```bash
mvn compile exec:java -Dexec.mainClass="ru.netology.chat.client.ClientMain"
```

## Использование

1. При запуске клиента вам будет предложено ввести имя пользователя
2. Начните общение, набирая сообщения и нажимая Enter
3. Для выхода из чата введите `/exit`

## Логирование

Все сообщения чата и системные события записываются в:
- Консольный вывод
- `file.log` (файл журнала)

Настройки логирования можно изменить в `src/main/resources/logback.xml`

## Компоненты

### Сервер

- **ChatServer**: Основной класс сервера, обрабатывающий подключения клиентов и рассылку сообщений
- **ClientHandler**: Управляет отдельными клиентскими подключениями и обработкой сообщений
- **ServerMain**: Точка входа для серверного приложения

### Клиент

- **ChatClient**: Обрабатывает подключение к серверу и отправку/получение сообщений
- **ClientMain**: Точка входа для клиентского приложения

### Конфигурация

- **Settings**: Загрузка и управление настройками приложения
- **logback.xml**: Настройка поведения логирования

## Зависимости

- JUnit Jupiter (5.9.2) для тестирования
- Logback Classic (1.4.7) для логирования

## Протокол

Чат-приложение использует простой текстовый протокол:
1. Клиент подключается к серверу
2. Сервер запрашивает имя пользователя
3. Клиент отправляет имя пользователя
4. Если имя допустимо, клиент присоединяется к чату
5. Сообщения рассылаются всем подключенным клиентам
6. Команда "/exit" завершает соединение

## Обработка ошибок

Приложение включает надежную обработку ошибок для:
- Проблем с подключением
- Недопустимых имен пользователей
- Дублирования имен пользователей
- Неожиданных отключений
