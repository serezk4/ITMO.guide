# 6 лабораторная работа [`Вариант`](lab6__variant)
Исходный код лежит в папке `src`.

----

### Диаграмма
Как работает
<img src="lab5__addition.svg">
Полная диаграмма по всем классам
<img src="lab5.png">

----

### Дерево классов
```text
src/main/java/com/serezk4/
├── Main.java
├── chat
│   ├── Handler.java
│   ├── Router.java
│   └── package-info.java
├── collection
│   ├── CollectionManager.java
│   ├── id
│   │   ├── IdGenerator.java
│   │   └── package-info.java
│   ├── package-info.java
│   └── util
│       ├── InputUtil.java
│       └── package-info.java
├── command
│   ├── Active.java
│   ├── Add.java
│   ├── Clear.java
│   ├── Command.java
│   ├── ExecuteScript.java
│   ├── Exit.java
│   ├── Head.java
│   ├── PrintFieldDescendingHairColor.java
│   ├── RemoveById.java
│   ├── RemoveFirst.java
│   ├── RemoveGreater.java
│   ├── Save.java
│   ├── Show.java
│   ├── SumOfHeight.java
│   └── package-info.java
├── configuration
│   ├── DatabaseConfiguration.java
│   ├── FileConfiguration.java
│   ├── RecursionConfiguration.java
│   └── package-info.java
├── database
│   ├── ConnectionManager.java
│   ├── DatabaseList.java
│   ├── dto
│   │   ├── UserDto.java
│   │   └── package-info.java
│   ├── mapper
│   │   ├── Mapper.java
│   │   ├── UserMapper.java
│   │   └── package-info.java
│   ├── model
│   │   ├── Color.java
│   │   ├── Coordinates.java
│   │   ├── Country.java
│   │   ├── Location.java
│   │   ├── Person.java
│   │   ├── User.java
│   │   └── package-info.java
│   ├── package-info.java
│   ├── query
│   │   ├── PersonQuery.java
│   │   ├── UserQuery.java
│   │   └── package-info.java
│   ├── repository
│   │   ├── PersonRepository.java
│   │   ├── UserRepository.java
│   │   └── package-info.java
│   └── service
│       ├── PersonService.java
│       ├── UserService.java
│       └── package-info.java
├── io
│   ├── IOWorker.java
│   ├── console
│   │   ├── BufferedConsoleWorker.java
│   │   ├── ConsoleWorker.java
│   │   └── package-info.java
│   ├── deque
│   │   ├── DequeWorker.java
│   │   └── package-info.java
│   ├── file
│   │   ├── BufferedFileWorker.java
│   │   ├── FileWorker.java
│   │   └── package-info.java
│   ├── package-info.java
│   ├── parser
│   │   ├── Formatter.java
│   │   ├── csv
│   │   │   ├── README.md
│   │   │   └── package-info.java
│   │   ├── json
│   │   │   ├── JsonFormatter.java
│   │   │   ├── Root.java
│   │   │   ├── adapters
│   │   │   │   ├── IgnoreFailureTypeAdapterFactory.java
│   │   │   │   ├── LocalDateAdapter.java
│   │   │   │   └── ResourceDeserializer.java
│   │   │   └── package-info.java
│   │   ├── package-info.java
│   │   └── xml
│   │       ├── README.md
│   │       └── package-info.java
│   ├── socket
│   │   ├── client
│   │   │   ├── Client.java
│   │   │   ├── ClientConfiguration.java
│   │   │   └── package-info.java
│   │   ├── package-info.java
│   │   └── server
│   │       ├── Server.java
│   │       ├── ServerConfiguration.java
│   │       └── package-info.java
│   └── trasnfer
│       ├── Request.java
│       ├── Response.java
│       └── package-info.java
├── package-info.java
└── security
    └── PasswordUtil.java
```
