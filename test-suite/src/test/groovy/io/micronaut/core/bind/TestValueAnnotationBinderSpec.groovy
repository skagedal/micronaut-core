package io.micronaut.core.bind

import io.micronaut.core.type.Argument
import io.micronaut.core.type.Executable
import spock.lang.Specification

class TestValueAnnotationBinderSpec extends Specification {


    void "test valid TestValueAnnotation binder"() {
        given:
        Executable testValue = new Executable() {
            @Override
            Argument[] getArguments() {
                [Argument.of(String, "foo")] as Argument[]
            }

            @Override
            Object invoke(Object instance, Object... arguments) {
                return arguments[0]
            }
        }

        TestValueAnnotationBinder binder = new TestValueAnnotationBinder()

        ArgumentBinderRegistry registry = Mock(ArgumentBinderRegistry)
        ArgumentBinder argumentBinder = Mock(ArgumentBinder)

        argumentBinder.bind(_, _) >> ({ args ->
            return { Optional.of(args[1].get(args[0].argument.name)) } as ArgumentBinder.BindingResult
        } )

        registry.findArgumentBinder(_,_) >> Optional.of( argumentBinder)

        when:
        def bound = binder.bind(testValue, registry, [foo:"bar"])

        then:
        bound != null
        bound.invoke(this) == 'bar'
    }

//    @Shared @AutoCleanup ApplicationContext context = ApplicationContext.run()
//
//    @Unroll
//    void "test bind map properties to object"() {
//        given:
//        TestValueAnnotationBinder binder = context.getBean(TestValueAnnotationBinder)
//        def result = binder.bind(type.newInstance(), map)
//
//        expect:
//        result == expected
//
//        where:
//        type   | map                                                                                            | expected
//        Author | ['name': 'Stephen King', 'publisher.name': 'Blah']                                             | new Author(name: "Stephen King", publisher: new Publisher(name: "Blah"))
//        Book   | ['authors[0].name': 'Stephen King', 'authors[0].publisher.name': 'Blah']                       | new Book(authors: [new Author(name: "Stephen King", publisher: new Publisher(name: 'Blah'))])
//        Book   | ['authorsByInitials[SK].name': 'Stephen King', 'authorsByInitials[SK].publisher.name': 'Blah'] | new Book(authorsByInitials: [SK: new Author(name: "Stephen King", publisher: new Publisher(name: 'Blah'))])
//        Book   | ['title': 'The Stand', url: 'http://foo.com']                                                  | new Book(title: "The Stand", url: new URL("http://foo.com"))
//        Book   | ['authors[0].name': 'Stephen King']                                                            | new Book(authors: [new Author(name: "Stephen King")])
//        Book   | ['authors[0].name': 'Stephen King', 'authors[0].age': 60]                                      | new Book(authors: [new Author(name: "Stephen King", age: 60)])
//        Book   | ['authors[0].name': 'Stephen King', 'authors[0].age': 60,
//                  'authors[1].name': 'JRR Tolkien', 'authors[1].age': 110]                                      | new Book(authors: [new Author(name: "Stephen King", age: 60), new Author(name: "JRR Tolkien", age: 110)])
//        Book   | ['authorsByInitials[SK].name' : 'Stephen King', 'authorsByInitials[SK].age': 60,
//                  'authorsByInitials[JRR].name': 'JRR Tolkien', 'authorsByInitials[JRR].age': 110]              | new Book(authorsByInitials: [SK: new Author(name: "Stephen King", age: 60), JRR: new Author(name: "JRR Tolkien", age: 110)])
//
//    }
//
//    void "test convert map to immutable object"() {
//        when:
//        def mapToObjectConverter = context.getBean(ConversionService)
//        def optional = mapToObjectConverter.convert(['first_name': 'Todd'], ImmutablePerson)
//
//        then:
//        optional.isPresent()
//        optional.get() instanceof ImmutablePerson
//        optional.get().firstName == 'Todd'
//    }
//
//    @EqualsAndHashCode
//    @ToString
//    static class Book {
//        String title
//        URL url
//
//        List<Author> authors = []
//        Map<String, Author> authorsByInitials = [:]
//    }
//
//    @EqualsAndHashCode
//    @ToString
//    static class Author {
//        String name
//        Integer age
//
//        Publisher publisher
//    }
//
//    @EqualsAndHashCode
//    @ToString
//    static class Publisher {
//        String name
//    }
//
//
//    @Introspected
//    static class ImmutablePerson {
//
//        @JsonProperty("first_name")
//        private String firstName
//
//        @JsonCreator
//        ImmutablePerson(@JsonProperty("first_name") String firstName) {
//            this.firstName = firstName
//        }
//
//        String getFirstName() {
//            return firstName
//        }
//    }

}
