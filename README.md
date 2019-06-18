# ZenDesk Coding Challenge

## Requirements


* [Java 12](https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html). If you are on Mac and you use `brew`, you can install it as follows:

```
brew cask install java
```

* This project uses [maven wrapper](https://github.com/takari/maven-wrapper) as the build tool. So everything else you need will be provided by  `./mvnw` command or `./mvnw.cmd` if on Windows.

## Compile

```
./mvnw package
```

## Run

```
java -jar target/zendesk-search-cli-0.0.1-SNAPSHOT.jar
```


## Design Decisions

1) In some cases I could have use higher level types such as `OffsetDateTime`, `TimeZone` and `Locale`. Given the simple functionality of this search tool (just string comparison) I decided to load them as a string. In real world, using higher level types may be a better option so we can access all the useful methods on those types.
2) I decided to use reflection in order to get all the fields and values in a generic way. This adds complexity but it favours code reusability where the same `SearchService` can be used for searching any new type of entity.
3) The value separator is a space so the search tool will only match full words. This means that a search term like `"flotonic"` will not match `"michaelburt@flotonic.com"`. This is said in the instructions but for the email use case I find it a bit rigid. Perhaps a further optimization would be to tokenize strings based on a set of different separators such as space but also `-`, `@`, `.` and `-`.
4) For the search implementation, I decided to use an [Inverted Index](https://en.wikipedia.org/wiki/Inverted_index). This is a searching mechanism that will resolve search using full word match in a constant time at the cost of processing time when indexing the documents.
5) I decided to use a library (`j-text-utils`) for writing results in a table view. This is not the core of the exercise so I thought it was better to use a well tested library.




## Performance test:

```
2019-06-19 00:38:16.882  INFO 50219 --- [           main] c.z.c.s.r.JsonArrayEntityRepository      : Loaded 25 entities to Organisation repository
2019-06-19 00:38:16.912  INFO 50219 --- [           main] c.z.c.s.service.InMemorySearchService    : Indexing 25 entities took 18ms
2019-06-19 00:38:16.964  INFO 50219 --- [           main] c.z.c.s.r.JsonArrayEntityRepository      : Loaded 200 entities to Ticket repository
2019-06-19 00:38:17.012  INFO 50219 --- [           main] c.z.c.s.service.InMemorySearchService    : Indexing 200 entities took 46ms
2019-06-19 00:38:23.084  INFO 50219 --- [           main] c.z.c.s.r.JsonArrayEntityRepository      : Loaded 400000 entities to User repository
2019-06-19 00:38:31.768  INFO 50219 --- [           main] c.z.c.s.service.InMemorySearchService    : Indexing 400000 entities took 8682ms
```