# I/O a.k.a Input / Output

В модуле `io` реализованы классы для работы со всем, что вводит или выводит информацию, а это:

- консоль
- файлы
- дека (для работы с выполнением скриптов)
- парсеры (для чтения и записи файлов в различных форматах)

## Дерево `com.serezk4.io`
```text
├── IOWorker.java
├── console
│   ├── BufferedConsoleWorker.java
│   └── ConsoleWorker.java
├── deque
│   └── DequeWorker.java
├── file
│   ├── BufferedFileWorker.java
│   └── FileWorker.java
└── parser
    ├── Formatter.java
    ├── csv (реализация не предложена)
    ├── json
    │   ├── JsonFormatter.java
    │   ├── Root.java
    │   └── adapters
    │       ├── IgnoreFailureTypeAdapterFactory.java
    │       └── ResourceDeserializer.java
    └── xml (реализация не предложена)

```