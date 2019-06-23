# ZenDesk Coding Challenge

[![Build Status](https://travis-ci.com/nanducoder/zd-search-challenge.svg?branch=master)](https://travis-ci.com/nanducoder/zd-search-challenge)

## Requirements


* [Java 12](https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html). If you are on Mac, `brew` can be used for installing it.

```
brew cask install java
```

* This project uses [maven wrapper](https://github.com/takari/maven-wrapper) as the build tool. So everything else required in terms of libraries will be provided by  `./mvnw` command or `./mvnw.cmd` if on Windows.

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

To see the list of all searchable fields

```
shell:> showfields
```

## Searching an entity

### Searching for an specific value

```
shell:> search user _id 71
shell:> search organisation tags Erickson
shell:> search ticket subject Virgin
```

### Searching for empty value

The keyword to match null values is `<null>`

```
shell:> search ticket type <null>
```

### Error Handling

If the tool throws an exception it will show an error message, further information can be retrieved using the following command

```
shell:> stacktrace
```

## Need Help?

Type the following in the shell

```
shell:> help
```

Or help about an specific command:

```
shell:> help <command>
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


## Design Decisions/Tradeoffs

1) For the search implementation, I decided to use an [Inverted Index](https://en.wikipedia.org/wiki/Inverted_index). This is a searching mechanism that will resolve search using full word match in a constant time at the cost of processing time when indexing the documents.
2) In some cases I could have use higher level types such as `OffsetDateTime`, `TimeZone` and `Locale`. Given the simple functionality of this search tool (just string comparison) I decided to load them as a string. In real world, using higher level types may be a better option so we can access all the useful methods on those types.
3) I decided to use reflection in order to get all the fields and values in a generic way. This has a performance impact but it favours code reusability where the same `SearchService` can be used for searching any new type of entity. I also used the `Introspector` class that has a internal cache of already reflected types.
4) The value separator is a space so the search tool will only match full words. This means that a search term like `"flotonic"` will not match `"michaelburt@flotonic.com"`. This is said in the instructions but for the email use case I find it a bit rigid. Perhaps a further optimization would be to tokenize strings based on a set of different separators such as space but also `-`, `@`, `.` and `-`.
5) This search supports only single word search. Given the use of an inverted index, searching for multiple words would require getting the results of every word and then perform the intersection or union of those partial results based on what the user specifies. In ElasticSearch language this would be written as `tags: Gambrills AND Lund` or `tags: Gambrills OR Lund`. I decided to left this out of the scope as it would get more complicated. If this is needed, I would probably consider moving this tool to a Apache SolR or ElasticSearch server.


## Performance test:


A file was created with 400K Users, it loaded in 8.6 seconds on a Macbook Pro i5 with 8GB RAM. Trying to load files bigger than that may result in Out of Memory depending on how much memory the host has.

This test exceeds the requirement in the instructions of 10K+ users.

For a better and scalable search application we should be looking at ElasticSearch/Apache Solr which will do the job better.

```
Loaded 400000 entities to User repository
Indexing 400000 entities took 8682ms
```