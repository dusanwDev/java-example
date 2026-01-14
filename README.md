# Java Learning Examples for TypeScript/Angular Developers

Welcome! This project is specifically designed for **JavaScript/TypeScript/Angular developers** who want to learn Java. All examples include comparisons to TypeScript/Angular to help you leverage your existing knowledge.

## 🎯 Who Is This For?

- TypeScript/Angular developers with 3-5 years of experience
- Developers wanting to transition to Java backend development
- Anyone familiar with OOP in TypeScript looking to learn Java

## 📚 What's Included?

This comprehensive learning project covers **60+ Java topics** organized from basics to advanced concepts, including JPA/Hibernate:

### 📌 Fundamentals (Basics Package)
- Control flow (if, switch, ternary, logical operators)
- Loops (for, while, nested loops, break/continue)
- Methods (declaration, overloading, varargs)
- Strings (manipulation, methods, substrings)
- Arrays (1D, 2D, searching)
- Math & random numbers
- Variable scope
- Calculator program (practical example)

### 🏗️ Object-Oriented Programming
- Classes & Objects
- Constructors (regular & overloaded)
- Inheritance & the `super` keyword
- Abstract classes & Abstraction
- Interfaces
- Polymorphism (compile-time & runtime)
- Method overriding
- Encapsulation (getters/setters)
- Aggregation & Composition
- Static members

### 📦 Collections & Data Structures
- ArrayLists (dynamic arrays)
- HashMaps (key-value pairs)
- Generics (type-safe collections)
- Enums (type-safe constants)
- Wrapper classes

### 🔧 Advanced Topics
- Exception handling (try-catch-finally)
- File I/O (reading & writing files)
- Dates & Times (LocalDateTime, formatting)
- Threading & Multithreading
- Anonymous classes
- TimerTasks (scheduled operations)

### 💾 JPA/Hibernate (Persistence)
- **Entity Mapping:** @Entity, @Table, @Id, @GeneratedValue, @Column
- **Relationships:** One-to-One, One-to-Many, Many-to-One, Many-to-Many
- **Fetch Strategies:** EAGER vs LAZY loading, N+1 problem solutions
- **CRUD Operations:** persist(), find(), merge(), remove()
- **HQL Queries:** SELECT, WHERE, JOIN, aggregations
- **Caching:** First-level (L1), Second-level (L2), Query cache
- **Entity Lifecycle:** Transient, Persistent, Detached, Removed states
- **Advanced Features:** get() vs load(), Cascade operations, Orphan removal
- **35+ Hibernate Annotations:** Complete reference with examples
- **TypeORM Comparisons:** Side-by-side with TypeScript equivalents

## 🚀 Getting Started

### Prerequisites

- **Java JDK 21** (or later) - Oracle JDK recommended
- **IntelliJ IDEA** (latest version)
- **Maven** (usually bundled with IntelliJ)
- **H2 Database** (included as Maven dependency - no separate installation needed)

### Running in IntelliJ IDEA

1. **Open the Project:**
   ```
   File → Open → Select the java-example folder
   ```

2. **Wait for Maven to Download Dependencies:**
   - IntelliJ will automatically detect the `pom.xml`
   - Wait for Maven to sync (bottom right of IDE)

3. **Run the Main Application:**
   - Navigate to: `src/main/java/com/javalearning/MainApp.java`
   - Right-click on `MainApp.java`
   - Select "Run 'MainApp.main()'"

4. **Interactive Menu:**
   - Use the interactive menu to run specific examples
   - Or run all examples at once

### Running Individual Examples

Each topic has its own runnable class:

**Basics:**
- `ControlFlow.java` - If statements, switches, ternary
- `LoopsAndMethods.java` - Loops, methods, varargs
- `MathAndStrings.java` - Math operations, string manipulation
- `ArrayExamples.java` - Arrays and array operations
- `Calculator.java` - Practical calculator program

**Applications:**
- `TaskManagementApp.java` - OOP, file I/O, exceptions, dates
- `BankingApp.java` - Inheritance, polymorphism, interfaces
- `EcommerceApp.java` - Generics, HashMaps, enums
- `ThreadingApp.java` - Threading, multithreading, timers

**JPA/Hibernate Demos:**
- `RelationshipsDemo.java` - All relationship types with examples
- `FetchStrategiesDemo.java` - EAGER vs LAZY, N+1 problem solutions
- `CrudOperationsDemo.java` - Complete CRUD with best practices
- `HqlExamplesDemo.java` - HQL queries, joins, aggregations
- `CachingDemo.java` - First-level, second-level, query cache
- `EntityLifecycleDemo.java` - Entity states and transitions

To run individually:
- Right-click on any class with a `main()` method
- Select "Run 'ClassName.main()'"

### Running from Command Line

```bash
# Compile the project
mvn clean compile

# Run the main application
mvn exec:java -Dexec.mainClass="com.javalearning.MainApp"

# Run a specific example
mvn exec:java -Dexec.mainClass="com.javalearning.basics.Calculator"
```

## 📖 Project Structure

```
java-example/
├── pom.xml                              # Maven configuration
├── README.md                            # This file
├── JPA_HIBERNATE_GUIDE.md               # Quick start guide
├── JPA_ANNOTATIONS_REFERENCE.md         # Part 1: Standard JPA annotations
├── JPA_ANNOTATIONS_REFERENCE_PART2.md   # Part 2: Advanced JPA annotations
├── HIBERNATE_SPECIFIC_ANNOTATIONS.md    # 35+ Hibernate-only annotations
│
└── src/main/
    ├── java/com/javalearning/
    │   ├── MainApp.java                 # Interactive menu (START HERE)
    │   │
    │   ├── basics/                      # Fundamental concepts
    │   │   ├── ControlFlow.java         # If, switch, ternary
    │   │   ├── LoopsAndMethods.java     # Loops, methods
    │   │   ├── MathAndStrings.java      # Math, strings
    │   │   ├── ArrayExamples.java       # Arrays
    │   │   └── Calculator.java          # Calculator program
    │   │
    │   ├── taskmanagement/              # OOP Application
    │   │   ├── Task.java                # Task model
    │   │   ├── TaskManager.java         # Manager class
    │   │   ├── Priority.java            # Enum example
    │   │   ├── TaskStatus.java          # Enum example
    │   │   └── TaskManagementApp.java   # Main app
    │   │
    │   ├── banking/                     # Inheritance Application
    │   │   ├── Transactable.java        # Interface
    │   │   ├── Account.java             # Abstract class
    │   │   ├── SavingsAccount.java      # Concrete class
    │   │   ├── CheckingAccount.java     # Concrete class
    │   │   ├── Customer.java            # Aggregation example
    │   │   └── BankingApp.java          # Main app
    │   │
    │   ├── ecommerce/                   # Generics Application
    │   │   ├── Product.java             # Product model
    │   │   ├── ProductCategory.java     # Enum
    │   │   ├── InventoryManager.java    # Generic class
    │   │   ├── ShoppingCart.java        # HashMap example
    │   │   └── EcommerceApp.java        # Main app
    │   │
    │   ├── threading/                   # Concurrency Examples
    │   │   └── ThreadingApp.java        # Threading demos
    │   │
    │   └── jpa/                         # JPA/Hibernate Examples
    │       ├── config/
    │       │   └── HibernateUtil.java   # EntityManager factory
    │       ├── entities/
    │       │   ├── Student.java         # Basic entity example
    │       │   └── Laptop.java          # Entity with relationships
    │       ├── embeddable/
    │       │   └── Address.java         # Embeddable value object
    │       ├── relationships/
    │       │   ├── Person.java          # One-to-One owner
    │       │   ├── Passport.java        # One-to-One inverse
    │       │   ├── Course.java          # One-to-Many
    │       │   └── StudentEnrollment.java  # Many-to-One, Many-to-Many
    │       └── demos/
    │           ├── RelationshipsDemo.java    # Relationship examples
    │           ├── FetchStrategiesDemo.java  # EAGER vs LAZY
    │           ├── CrudOperationsDemo.java   # CRUD operations
    │           ├── HqlExamplesDemo.java      # HQL queries
    │           ├── CachingDemo.java          # Caching strategies
    │           └── EntityLifecycleDemo.java  # Entity states
    │
    └── resources/
        └── META-INF/
            └── persistence.xml          # JPA configuration
```

## 🎓 Learning Path

### Recommended Order:

1. **Start with Basics** (1-2 days)
   - Run `ControlFlow.java`
   - Run `LoopsAndMethods.java`
   - Run `MathAndStrings.java`
   - Run `ArrayExamples.java`
   - Run `Calculator.java`

2. **Object-Oriented Programming** (2-3 days)
   - Run `TaskManagementApp.java`
   - Study the Task, TaskManager classes
   - Understand constructors, getters/setters

3. **Advanced OOP** (2-3 days)
   - Run `BankingApp.java`
   - Study inheritance, polymorphism
   - Understand interfaces vs abstract classes

4. **Collections & Generics** (1-2 days)
   - Run `EcommerceApp.java`
   - Study generics, HashMaps
   - Compare with TypeScript Map/Array

5. **Concurrency** (1-2 days)
   - Run `ThreadingApp.java`
   - Understand threads vs async/await
   - Learn about thread pools

6. **JPA/Hibernate Persistence** (3-4 days)
   - Read `JPA_HIBERNATE_GUIDE.md` for overview
   - Run `RelationshipsDemo.java` - Understand entity relationships
   - Run `CrudOperationsDemo.java` - Learn CRUD operations
   - Run `FetchStrategiesDemo.java` - Master fetch strategies
   - Run `HqlExamplesDemo.java` - Practice HQL queries
   - Run `CachingDemo.java` - Learn caching strategies
   - Run `EntityLifecycleDemo.java` - Understand entity states
   - Reference annotation guides as needed

## 🔄 TypeScript to Java Quick Reference

### Variables & Types
```typescript
// TypeScript
let name: string = "John";
const age: number = 25;
let items: string[] = [];
```
```java
// Java
String name = "John";
final int age = 25;
ArrayList<String> items = new ArrayList<>();
```

### Functions/Methods
```typescript
// TypeScript
function add(a: number, b: number): number {
  return a + b;
}
```
```java
// Java
public static int add(int a, int b) {
  return a + b;
}
```

### Classes
```typescript
// TypeScript
class User {
  constructor(private name: string, private age: number) {}

  greet(): void {
    console.log(`Hello, ${this.name}`);
  }
}
```
```java
// Java
public class User {
  private String name;
  private int age;

  public User(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public void greet() {
    System.out.println("Hello, " + name);
  }
}
```

### Arrays & Collections
```typescript
// TypeScript
const users: User[] = [];
users.push(new User("John", 25));
const found = users.find(u => u.age > 20);
```
```java
// Java
ArrayList<User> users = new ArrayList<>();
users.add(new User("John", 25));
User found = users.stream()
  .filter(u -> u.getAge() > 20)
  .findFirst()
  .orElse(null);
```

### Maps/Objects
```typescript
// TypeScript
const map = new Map<string, User>();
map.set("user1", new User("John", 25));
const user = map.get("user1");
```
```java
// Java
HashMap<String, User> map = new HashMap<>();
map.put("user1", new User("John", 25));
User user = map.get("user1");
```

### Database Persistence (TypeORM vs JPA/Hibernate)
```typescript
// TypeScript + TypeORM
@Entity()
export class User {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  name: string;

  @OneToMany(() => Post, post => post.user)
  posts: Post[];
}

// Usage
const user = await userRepository.save(new User());
const found = await userRepository.findOne({ where: { id: 1 } });
```
```java
// Java + JPA/Hibernate
@Entity
public class User {
  @Id
  @GeneratedValue
  private Long id;

  @Column
  private String name;

  @OneToMany(mappedBy = "user")
  private List<Post> posts;

  // Getters/setters...
}

// Usage
EntityManager em = emf.createEntityManager();
em.persist(new User());
User found = em.find(User.class, 1L);
```

## 📝 Topics Coverage

| # | Topic | Location | Real-World Use |
|---|-------|----------|----------------|
| 7 | If statements | ControlFlow.java | Conditional logic (daily) |
| 8 | Random numbers | MathAndStrings.java | Testing, games (weekly) |
| 9 | Math class | MathAndStrings.java | Calculations (daily) |
| 12 | Nested if | ControlFlow.java | Complex validation (daily) |
| 13 | String methods | MathAndStrings.java | Text processing (daily) |
| 14 | Substrings | MathAndStrings.java | Parsing (daily) |
| 16 | Ternary operator | ControlFlow.java | Concise conditionals (daily) |
| 18 | Enhanced switches | ControlFlow.java | State machines (weekly) |
| 19 | Calculator | Calculator.java | User interaction example |
| 20 | Logical operators | ControlFlow.java | Boolean logic (daily) |
| 21 | While loops | LoopsAndMethods.java | Iteration (common) |
| 23 | For loops | LoopsAndMethods.java | Iteration (daily) |
| 24 | Break & continue | LoopsAndMethods.java | Loop control (common) |
| 25 | Nested loops | LoopsAndMethods.java | 2D data (weekly) |
| 26 | Methods | LoopsAndMethods.java | Code organization (daily) |
| 27 | Overloaded methods | LoopsAndMethods.java | API flexibility (daily) |
| 28 | Variable scope | LoopsAndMethods.java | Understanding critical |
| 31 | Arrays | ArrayExamples.java | Data storage (daily) |
| 32 | User input arrays | ArrayExamples.java | Data collection (weekly) |
| 33 | Search arrays | ArrayExamples.java | Finding data (daily) |
| 34 | Varargs | LoopsAndMethods.java | Flexible APIs (weekly) |
| 35 | 2D arrays | ArrayExamples.java | Grids, tables (weekly) |
| 39 | OOP basics | TaskManagementApp.java | Everything! (daily) |
| 40 | Constructors | Task.java | Object creation (daily) |
| 41 | Overloaded constructors | Task.java | Flexible creation (daily) |
| 42 | Array of objects | TaskManager.java | Collections (daily) |
| 43 | Static | Account.java | Shared data (common) |
| 44 | Inheritance | SavingsAccount.java | Code reuse (daily) |
| 45 | Super | SavingsAccount.java | Parent access (daily) |
| 46 | Method overriding | SavingsAccount.java | Custom behavior (daily) |
| 47 | toString | Task.java | Debugging (daily) |
| 48 | Abstraction | Account.java | Design patterns (common) |
| 49 | Interfaces | Transactable.java | Contracts (daily) |
| 50 | Polymorphism | BankingApp.java | Flexibility (daily) |
| 51 | Runtime polymorphism | BankingApp.java | Dynamic behavior (daily) |
| 52 | Getters/Setters | Task.java | Encapsulation (daily) |
| 53 | Aggregation | Customer.java | Relationships (common) |
| 54 | Composition | Customer.java | Ownership (common) |
| 55 | Wrapper classes | Task.java | Nullable values (daily) |
| 56 | ArrayLists | TaskManager.java | Dynamic arrays (daily) |
| 57 | Exception handling | TaskManager.java | Error handling (daily) |
| 58 | Write files | TaskManager.java | Data persistence (weekly) |
| 59 | Read files | TaskManager.java | Data loading (weekly) |
| 62 | Dates & times | Task.java | Timestamps (daily) |
| 63 | Anonymous classes | ThreadingApp.java | Inline code (common) |
| 64 | TimerTasks | ThreadingApp.java | Scheduling (weekly) |
| 66 | Generics | InventoryManager.java | Type safety (daily) |
| 67 | HashMaps | InventoryManager.java | Fast lookup (daily) |
| 68 | Enums | Priority.java | Constants (daily) |
| 69 | Threading | ThreadingApp.java | Concurrency (common) |
| 70 | Multithreading | ThreadingApp.java | Parallelism (common) |

## 💡 Key Differences: TypeScript vs Java

### 1. Type System
- **TS:** Optional, compile-time only
- **Java:** Required, enforced at runtime

### 2. null/undefined
- **TS:** Has both `null` and `undefined`
- **Java:** Only `null` (use `Optional<T>` for safety)

### 3. Arrays
- **TS:** Dynamic size `const arr: number[] = []`
- **Java:** Fixed size `int[] arr = new int[5]` or use `ArrayList<Integer>`

### 4. Async/Concurrency
- **TS:** `async/await`, Promises, single-threaded
- **Java:** Threads, ExecutorService, multi-threaded

### 5. Modules/Packages
- **TS:** ES6 modules `import { } from ''`
- **Java:** Package system `import java.util.*`

### 6. Access Modifiers
- **TS:** `public`, `private`, `protected` (compile-time only)
- **Java:** `public`, `private`, `protected`, package-private (enforced)

## 🎯 Next Steps

After completing this course, you'll be ready to:

1. **Spring Boot Development** ⭐ RECOMMENDED NEXT
   - Build REST APIs (you already know JPA/Hibernate!)
   - Spring Data JPA (simplifies database operations)
   - Dependency Injection & IoC
   - Spring Security for authentication

2. **Testing & Quality**
   - JUnit 5 (unit testing)
   - Mockito (mocking)
   - Integration tests with Spring Boot Test
   - Test-driven development (TDD)

3. **Enterprise Java**
   - Maven/Gradle build tools (Maven already covered!)
   - Docker containerization
   - Microservices architecture
   - Design patterns (Gang of Four)

4. **Advanced Topics**
   - Java Streams API & Lambda expressions
   - Functional programming in Java
   - Reactive programming (Project Reactor)
   - Message queues (RabbitMQ, Kafka)

## 📚 Additional Resources

- [Official Java Documentation](https://docs.oracle.com/en/java/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Baeldung Java Tutorials](https://www.baeldung.com/)
- [Effective Java by Joshua Bloch](https://www.amazon.com/Effective-Java-Joshua-Bloch/dp/0134685997)

## 🤝 Contributing

This is a learning project. Feel free to:
- Add more examples
- Improve explanations
- Add TypeScript comparisons
- Fix errors

## 📄 License

This project is for educational purposes.

## 🙏 Acknowledgments

Designed specifically for TypeScript/Angular developers transitioning to Java. All examples include real-world use cases and TypeScript comparisons to accelerate your learning.

---

**Happy Learning! 🚀**

Remember: You already know programming. This is just learning Java syntax and ecosystem. You've got this! 💪
