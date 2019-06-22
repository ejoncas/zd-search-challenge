# ZenDesk Coding Challenge

[![Build Status](https://travis-ci.com/nanducoder/zd-search-challenge.svg?branch=master)](https://travis-ci.com/nanducoder/zd-search-challenge)

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

## Usage

## Showing Searchable Fields

To see the list of all the searchable fields

```
showfields
```

## Searching an entity

### Searching for an specific value

```
search user _id 71
search organisation tags Erickson
search ticket subject Virgin
```

### Searching for empty value

The keyword to match null values is `<null>`

```
search ticket type <null>
```


## Need Help?

Type the following in the shell

```
help
```

Or help about an specific command:

```
help <command>
```

For example:

```
shell:>help search
NAME
	search - Search for Tickets, Users or Organizations

SYNOPSYS
	search [--type] string  [--field] string  [--value] string

OPTIONS
	--type  string
		Type to search, can be Ticket, User or Organization
		[Mandatory]

	--field  string
		Field to search, for more info see the showfields commands
		[Mandatory]

	--value  string
		Value to search for, full word match only
		[Mandatory]

shell:>help showfields
NAME
	showfields - List all searchable fields

SYNOPSYS
	showfields
```


## Design Decisions

1) In some cases I could have use higher level types such as `OffsetDateTime`, `TimeZone` and `Locale`. Given the simple functionality of this search tool (just string comparison) I decided to load them as a string. In real world, using higher level types may be a better option so we can access all the useful methods on those types.
2) I decided to use reflection in order to get all the fields and values in a generic way. This adds complexity but it favours code reusability where the same `SearchService` can be used for searching any new type of entity.
3) The value separator is a space so the search tool will only match full words. This means that a search term like `"flotonic"` will not match `"michaelburt@flotonic.com"`. This is said in the instructions but for the email use case I find it a bit rigid. Perhaps a further optimization would be to tokenize strings based on a set of different separators such as space but also `-`, `@`, `.` and `-`.
4) For the search implementation, I decided to use an [Inverted Index](https://en.wikipedia.org/wiki/Inverted_index). This is a searching mechanism that will resolve search using full word match in a constant time at the cost of processing time when indexing the documents.


## Performance test:


I Created a file with 400K Users, it loaded in 8.6 seconds. Trying to load files bigger than that may result in Out of Memory.

This is fine, the instructions says we can assume the results fits in memory.

For a better and scalable search application we should be looking at ElasticSearch which will do the job way better than what I did here.

```
2019-06-19 00:38:23.084  INFO 50219 --- [           main] c.z.c.s.r.JsonArrayEntityRepository      : Loaded 400000 entities to User repository
2019-06-19 00:38:31.768  INFO 50219 --- [           main] c.z.c.s.service.InMemorySearchService    : Indexing 400000 entities took 8682ms
```