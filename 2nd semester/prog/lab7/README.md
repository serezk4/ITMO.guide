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
└── main
    └── java
        └── com
            └── serezk4
                ├── Main.java
                ├── chat
                │   ├── Handler.java
                │   └── Router.java
                ├── collection
                │   ├── CollectionManager.java
                │   ├── id
                │   │   └── IdGenerator.java
                │   ├── model
                │   │   ├── Color.java
                │   │   ├── Coordinates.java
                │   │   ├── Country.java
                │   │   ├── Location.java
                │   │   └── Person.java
                │   └── util
                │       └── InputUtil.java
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
                │   └── SumOfHeight.java
                ├── configuration
                │   ├── FileConfiguration.java
                │   └── RecursionConfiguration.java
                └── io
                    ├── IOWorker.java
                    ├── console
                    │   ├── BufferedConsoleWorker.java
                    │   └── ConsoleWorker.java
                    ├── deque
                    │   └── DequeWorker.java
                    ├── file
                    │   ├── BufferedFileWorker.java
                    │   └── FileWorker.java
                    ├── parser
                    │   ├── Formatter.java
                    │   ├── csv
                    │   │   └── README.md
                    │   ├── json
                    │   │   ├── JsonFormatter.java
                    │   │   ├── Root.java
                    │   │   └── adapters
                    │   │       ├── IgnoreFailureTypeAdapterFactory.java
                    │   │       └── ResourceDeserializer.java
                    │   └── xml
                    │       └── README.md
                    └── trasnfer
                        ├── Request.java
                        └── Response.java

```