# JPA & Hibernate Complete Guide

## 🎯 What You've Learned So Far

### ✅ Completed Topics:

1. **Configuration** (`persistence.xml`, `HibernateUtil`)
   - Entity Manager Factory setup
   - Database configuration (H2 in-memory)
   - Hibernate properties

2. **Basic Annotations** (`Student` entity)
   - `@Entity`, `@Table`, `@Id`, `@GeneratedValue`
   - `@Column` with customizations
   - `@Enumerated`, `@Transient`, `@Lob`

3. **Embeddable Objects** (`Address`)
   - `@Embeddable` annotation
   - Value objects without separate tables
   - Reusable components

## 📚 Remaining Topics to Cover

### Next Steps:

1. **Mapping Relations** (#12-13 from course)
   - One-to-One
   - One-to-Many / Many-to-One
   - Many-to-Many
   - Bidirectional relationships

2. **Fetch Strategies** (#14)
   - EAGER vs LAZY loading
   - Performance implications
   - N+1 query problem

3. **Caching** (#15-18)
   - Level 1 Cache (Session)
   - Level 2 Cache (SessionFactory)
   - Query Cache

4. **HQL - Hibernate Query Language** (#19-22)
   - Basic queries
   - Joins
   - Aggregations
   - Named queries

5. **Entity Lifecycle** (#23-24)
   - Transient
   - Persistent
   - Detached
   - Removed

6. **get() vs load()** (#25)
   - Eager vs Lazy initialization
   - When to use which

7. **JPA vs Hibernate** (#26)
   - Standards vs Implementation
   - When to use pure JPA

## 🚀 Quick Start Guide

### Running the Examples:

```java
// In IntelliJ IDEA:
// 1. Open MainApp.java
// 2. Add JPA examples to menu
// 3. Run and select JPA option

// From command line:
mvn clean compile
mvn exec:java -Dexec.mainClass="com.javalearning.jpa.JPADemoApp"
```

### Basic CRUD Operations:

```java
// Create (INSERT)
EntityManager em = HibernateUtil.getEntityManager();
em.getTransaction().begin();

Student student = new Student("John Doe", "john@example.com", 20);
em.persist(student);  // Save to database

em.getTransaction().commit();
em.close();

// Read (SELECT)
em = HibernateUtil.getEntityManager();
Student found = em.find(Student.class, 1L);  // Find by ID
System.out.println(found);
em.close();

// Update
em = HibernateUtil.getEntityManager();
em.getTransaction().begin();

Student student = em.find(Student.class, 1L);
student.setAge(21);  // Just modify - auto-updated!

em.getTransaction().commit();
em.close();

// Delete
em = HibernateUtil.getEntityManager();
em.getTransaction().begin();

Student student = em.find(Student.class, 1L);
em.remove(student);  // Delete from database

em.getTransaction().commit();
em.close();
```

## 📊 TypeScript vs Java Comparison

### Entity Definition:

**TypeScript (TypeORM):**
```typescript
@Entity()
export class Student {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  name: string;

  @Column({ unique: true })
  email: string;

  @ManyToOne(() => Course)
  course: Course;
}
```

**Java (JPA/Hibernate):**
```java
@Entity
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Column(unique = true)
  private String email;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;
}
```

### CRUD Operations:

**TypeScript (TypeORM):**
```typescript
const repository = dataSource.getRepository(Student);

// Create
const student = new Student();
student.name = "John";
await repository.save(student);

// Read
const found = await repository.findOne({ where: { id: 1 } });

// Update
found.name = "Jane";
await repository.save(found);

// Delete
await repository.remove(found);
```

**Java (JPA/Hibernate):**
```java
EntityManager em = HibernateUtil.getEntityManager();

// Create
em.getTransaction().begin();
Student student = new Student();
student.setName("John");
em.persist(student);
em.getTransaction().commit();

// Read
Student found = em.find(Student.class, 1L);

// Update
em.getTransaction().begin();
found.setName("Jane");
em.getTransaction().commit();

// Delete
em.getTransaction().begin();
em.remove(found);
em.getTransaction().commit();
```

## 🔑 Key Concepts

### Entity Manager Lifecycle:

```
1. EntityManagerFactory (ONE per application)
   ↓
2. EntityManager (ONE per transaction/request)
   ↓
3. Entity (MANY managed objects)
```

### Transaction Pattern:

```java
EntityManager em = HibernateUtil.getEntityManager();
try {
    em.getTransaction().begin();

    // Your database operations here

    em.getTransaction().commit();
} catch (Exception e) {
    em.getTransaction().rollback();
    throw e;
} finally {
    em.close();
}
```

### Or use try-with-resources (recommended):

```java
try (EntityManager em = HibernateUtil.getEntityManager()) {
    em.getTransaction().begin();

    // Your operations

    em.getTransaction().commit();
} // Automatically closed!
```

## 💡 Best Practices

### ✓ DO:
- Always close EntityManager after use
- Use transactions for write operations
- Use try-with-resources for automatic cleanup
- Keep transactions short
- Use LAZY loading by default
- Enable SQL logging during development
- Override equals() and hashCode() for entities
- Use proper column sizes

### ✗ DON'T:
- Share EntityManager between threads
- Keep transactions open too long
- Use EAGER loading everywhere
- Forget to close EntityManager
- Use bidirectional relationships without care
- Store sensitive data in plain text
- Use database-generated IDs for business logic

## 🎓 Learning Path

1. **Week 1: Basics**
   - Entity mapping
   - CRUD operations
   - Embeddable objects

2. **Week 2: Relationships**
   - One-to-One
   - One-to-Many / Many-to-One
   - Many-to-Many

3. **Week 3: Queries & Performance**
   - HQL basics
   - Fetch strategies
   - Caching

4. **Week 4: Advanced**
   - Lifecycle management
   - Query optimization
   - Real-world patterns

## 📖 Additional Resources

- [Jakarta Persistence Specification](https://jakarta.ee/specifications/persistence/)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [Baeldung JPA Tutorial](https://www.baeldung.com/jpa-hibernate-tutorial)
- [Thorben Janssen's Blog](https://thorben-janssen.com/)

---

**Next:** We'll implement all relationship types and complete the course topics!
