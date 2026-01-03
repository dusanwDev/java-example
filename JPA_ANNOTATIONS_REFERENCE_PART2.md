# JPA & Hibernate Annotations - Part 2 (Advanced)

## Query Annotations

### @NamedQuery
**What it does:** Defines a named HQL/JPQL query

**When to use:** Reusable queries, centralized query management

**Example:**
```java
@Entity
@NamedQuery(
    name = "User.findByEmail",
    query = "SELECT u FROM User u WHERE u.email = :email"
)
public class User {
    // ...
}

// Usage:
TypedQuery<User> query = em.createNamedQuery("User.findByEmail", User.class);
query.setParameter("email", "john@email.com");
User user = query.getSingleResult();
```

**Benefits:**
- ✅ Query validated at startup
- ✅ Centralized in entity
- ✅ Reusable across application
- ✅ Can be cached

**TypeScript comparison:**
```typescript
// No direct equivalent
// Usually just repository methods
```

**Frequency:** Common in large applications (40%)

---

### @NamedQueries
**What it does:** Defines multiple named queries

**When to use:** Entity has several common queries

**Example:**
```java
@Entity
@NamedQueries({
    @NamedQuery(
        name = "User.findAll",
        query = "SELECT u FROM User u"
    ),
    @NamedQuery(
        name = "User.findByEmail",
        query = "SELECT u FROM User u WHERE u.email = :email"
    ),
    @NamedQuery(
        name = "User.findActive",
        query = "SELECT u FROM User u WHERE u.active = true"
    ),
    @NamedQuery(
        name = "User.countByRole",
        query = "SELECT COUNT(u) FROM User u WHERE u.role = :role"
    )
})
public class User {
    // ...
}
```

**Frequency:** Common (35%)

---

### @NamedNativeQuery
**What it does:** Defines named SQL query (not HQL)

**When to use:** Complex SQL that HQL can't handle

**Example:**
```java
@Entity
@NamedNativeQuery(
    name = "User.findWithComplexJoin",
    query = "SELECT u.* FROM users u " +
            "LEFT JOIN orders o ON u.id = o.user_id " +
            "WHERE o.total > :minTotal " +
            "GROUP BY u.id " +
            "HAVING COUNT(o.id) > 5",
    resultClass = User.class
)
public class User {
    // ...
}
```

**Use sparingly:** Loses database portability

**Frequency:** Rare (10%)

---

## Caching Annotations

### @Cacheable
**What it does:** Enables second-level caching for entity

**When to use:** Frequently read, rarely changed data

**Example:**
```java
@Entity
@Cacheable  // Enable second-level cache
public class Country {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String code;
}
```

**What gets cached:**
- Entity data by ID
- Reduces database queries
- Shared across sessions

**TypeScript comparison:**
```typescript
// No direct equivalent
// Usually handled by Redis or application cache
```

**Frequency:** Common in read-heavy applications (30%)

---

### @Cache (Hibernate-specific)
**What it does:** Configures cache strategy

**When to use:** Fine-tune caching behavior

**Strategies:**

#### 1. **READ_ONLY** (Best performance)
```java
@Entity
@Cacheable
@org.hibernate.annotations.Cache(
    usage = CacheConcurrencyStrategy.READ_ONLY
)
public class Country {
    // Never changes, safe to cache
}
```
- **Use for:** Static reference data
- **Examples:** Countries, currencies, timezones

#### 2. **NONSTRICT_READ_WRITE** (Good performance)
```java
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product {
    // Changes rarely
}
```
- **Use for:** Rarely updated data
- **Allows:** Occasional inconsistency

#### 3. **READ_WRITE** (Strict consistency)
```java
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    // Changes frequently but consistency matters
}
```
- **Use for:** Frequently read and updated
- **Ensures:** Consistency but slower

#### 4. **TRANSACTIONAL** (Full ACID)
```java
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class BankAccount {
    // Critical data, must be consistent
}
```
- **Use for:** Financial, critical data
- **Slowest but safest**

**Example with region:**
```java
@Entity
@Cacheable
@Cache(
    usage = CacheConcurrencyStrategy.READ_WRITE,
    region = "products"  // Custom cache region
)
public class Product {
    // ...
}
```

**Frequency:** Common in production apps (40%)

---

### @NaturalId (Hibernate-specific)
**What it does:** Marks field as natural identifier

**When to use:** Business key that's unique and immutable

**Example:**
```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;  // Surrogate key

    @NaturalId
    @Column(unique = true, nullable = false)
    private String email;  // Natural key

    @NaturalId
    @Column(unique = true)
    private String username;  // Natural key
}

// Usage:
User user = session.byNaturalId(User.class)
    .using("email", "john@email.com")
    .load();
```

**Benefits:**
- Cached lookups by natural ID
- More intuitive than surrogate keys
- Better for business logic

**Real-world examples:**
- Email (users)
- SKU (products)
- ISBN (books)
- SSN (persons)
- License plate (vehicles)

**Frequency:** Common (30%)

---

## Inheritance Annotations

### @Inheritance
**What it does:** Defines inheritance strategy

**When to use:** Entity hierarchy (base class with subclasses)

**Three strategies:**

#### 1. **SINGLE_TABLE** (Default, best performance)
```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Payment {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private BigDecimal amount;
}

@Entity
@DiscriminatorValue("CREDIT_CARD")
public class CreditCardPayment extends Payment {
    @Column
    private String cardNumber;

    @Column
    private String cvv;
}

@Entity
@DiscriminatorValue("PAYPAL")
public class PayPalPayment extends Payment {
    @Column
    private String paypalEmail;
}
```

**Database:**
```
payments table:
id | amount | payment_type | card_number | cvv | paypal_email
1  | 100.00 | CREDIT_CARD  | 1234...     | 123 | NULL
2  | 50.00  | PAYPAL       | NULL        | NULL| john@paypal.com
```

**Pros:**
- ✅ Best performance (no joins)
- ✅ Simple queries
- ✅ Easy polymorphic queries

**Cons:**
- ❌ NULL columns (waste space)
- ❌ Can't have NOT NULL on subclass columns
- ❌ Single table can get huge

**Use when:** Performance critical, few subclasses

#### 2. **JOINED** (Normalized)
```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Payment {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private BigDecimal amount;
}

@Entity
@Table(name = "credit_card_payments")
public class CreditCardPayment extends Payment {
    @Column(nullable = false)
    private String cardNumber;
}

@Entity
@Table(name = "paypal_payments")
public class PayPalPayment extends Payment {
    @Column(nullable = false)
    private String paypalEmail;
}
```

**Database:**
```
payments table:
id | amount

credit_card_payments table:
id | card_number | cvv
1  | 1234...     | 123

paypal_payments table:
id | paypal_email
2  | john@paypal.com
```

**Pros:**
- ✅ Normalized (no NULLs)
- ✅ Can use NOT NULL
- ✅ Clean database design

**Cons:**
- ❌ Slower (requires JOINs)
- ❌ More complex queries

**Use when:** Many subclasses, normalized DB important

#### 3. **TABLE_PER_CLASS** (Rarely used)
```java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column
    private BigDecimal amount;
}
```

**Database:**
```
credit_card_payments table:
id | amount | card_number | cvv

paypal_payments table:
id | amount | paypal_email
```

**Pros:**
- ✅ No joins for single class queries
- ✅ No NULLs

**Cons:**
- ❌ Polymorphic queries very slow (UNION)
- ❌ Data duplication

**Use when:** Rarely - avoid if possible

**Frequency:** Moderate (35% of apps with inheritance)

---

### @DiscriminatorColumn
**What it does:** Defines discriminator column for SINGLE_TABLE

**When to use:** With InheritanceType.SINGLE_TABLE

**Example:**
```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "type",
    discriminatorType = DiscriminatorType.STRING,
    length = 20
)
public abstract class Vehicle {
    // ...
}
```

**Frequency:** Common with SINGLE_TABLE (25%)

---

### @DiscriminatorValue
**What it does:** Specifies value for discriminator column

**When to use:** On each subclass

**Example:**
```java
@Entity
@DiscriminatorValue("CAR")
public class Car extends Vehicle {
    // ...
}

@Entity
@DiscriminatorValue("TRUCK")
public class Truck extends Vehicle {
    // ...
}
```

**Frequency:** Common with SINGLE_TABLE (25%)

---

### @MappedSuperclass
**What it does:** Base class that's not an entity itself

**When to use:** Share common fields without inheritance table

**Example:**
```java
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

@Entity
public class User extends BaseEntity {
    @Column
    private String username;
    // Inherits: id, createdAt, updatedAt
}

@Entity
public class Product extends BaseEntity {
    @Column
    private String name;
    // Inherits: id, createdAt, updatedAt
}
```

**Database:**
```
users table:
id | created_at | updated_at | username

products table:
id | created_at | updated_at | name
```

**Difference from @Inheritance:**
- ❌ No polymorphic queries (can't query BaseEntity)
- ✅ No discriminator column
- ✅ Simpler, cleaner
- ✅ Just code reuse

**Real-world use:**
Extremely common for:
- Audit fields (createdAt, updatedAt, createdBy)
- ID fields
- Soft delete (deletedAt)

**Frequency:** VERY common (80%)

---

## Advanced Annotations

### @Version
**What it does:** Optimistic locking

**When to use:** Prevent concurrent update conflicts

**Example:**
```java
@Entity
public class Product {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @Version
    private Long version;  // Hibernate manages this
}
```

**How it works:**
```java
// User 1 loads product
Product p1 = em.find(Product.class, 1L);  // version = 0

// User 2 loads same product
Product p2 = em.find(Product.class, 1L);  // version = 0

// User 1 updates
p1.setPrice(new BigDecimal("99.99"));
em.merge(p1);  // version becomes 1

// User 2 tries to update
p2.setPrice(new BigDecimal("89.99"));
em.merge(p2);  // EXCEPTION! OptimisticLockException
               // Because version is still 0 but DB has 1
```

**Benefits:**
- ✅ Prevents lost updates
- ✅ No database locks
- ✅ Better scalability

**Real-world use:**
- Shopping carts
- Inventory management
- Booking systems
- Banking transactions

**TypeScript comparison:**
```typescript
@VersionColumn()
version: number;
```

**Frequency:** Very common in multi-user apps (60%)

---

### @Formula (Hibernate-specific)
**What it does:** Calculated/derived column using SQL

**When to use:** Computed values from database

**Example:**
```java
@Entity
public class Order {
    @Column
    private BigDecimal subtotal;

    @Column
    private BigDecimal tax;

    @Formula("subtotal + tax")
    private BigDecimal total;  // Calculated by database

    @Formula("(SELECT COUNT(*) FROM order_items oi WHERE oi.order_id = id)")
    private int itemCount;  // Subquery
}
```

**Benefits:**
- ✅ Always up-to-date
- ✅ Calculated by database
- ✅ No extra code

**Cons:**
- ❌ Database-specific SQL
- ❌ Read-only
- ❌ Not portable

**Frequency:** Moderate (20%)

---

### @Where (Hibernate-specific)
**What it does:** Filter SQL WHERE clause

**When to use:** Soft deletes, filtered collections

**Example:**
```java
@Entity
@Where(clause = "deleted = false")
public class User {
    @Id
    private Long id;

    @Column
    private String username;

    @Column
    private boolean deleted = false;  // Soft delete
}

// All queries automatically filter deleted users
List<User> users = em.createQuery("FROM User", User.class).getResultList();
// SQL: SELECT * FROM users WHERE deleted = false
```

**Collection filtering:**
```java
@Entity
public class Post {
    @OneToMany
    @Where(clause = "status = 'PUBLISHED'")
    private List<Comment> publishedComments;

    @OneToMany
    @Where(clause = "status = 'PENDING'")
    private List<Comment> pendingComments;
}
```

**Frequency:** Common for soft deletes (40%)

---

### @OrderBy
**What it does:** Default ordering for collections

**When to use:** Collections that should always be ordered

**Example:**
```java
@Entity
public class Blog {
    @OneToMany(mappedBy = "blog")
    @OrderBy("publishedDate DESC, title ASC")
    private List<Post> posts;
}
```

**SQL generated:**
```sql
SELECT * FROM posts WHERE blog_id = ?
ORDER BY published_date DESC, title ASC
```

**TypeScript comparison:**
```typescript
@OneToMany(() => Post, post => post.blog)
@OrderBy({ publishedDate: 'DESC', title: 'ASC' })
posts: Post[];
```

**Frequency:** Very common (60%)

---

### @BatchSize (Hibernate-specific)
**What it does:** Fetch multiple associations in batches

**When to use:** Optimize N+1 query problem

**Example:**
```java
@Entity
public class Department {
    @OneToMany(mappedBy = "department")
    @BatchSize(size = 10)  // Fetch 10 at a time
    private List<Employee> employees;
}

// Without @BatchSize:
// 1. SELECT * FROM departments
// 2. SELECT * FROM employees WHERE department_id = 1
// 3. SELECT * FROM employees WHERE department_id = 2
// ... (N queries)

// With @BatchSize(size=10):
// 1. SELECT * FROM departments
// 2. SELECT * FROM employees WHERE department_id IN (1,2,3,4,5,6,7,8,9,10)
// 3. SELECT * FROM employees WHERE department_id IN (11,12,13,14,15,16,17,18,19,20)
// ... (N/10 queries)
```

**Frequency:** Common for performance (30%)

---

### @Fetch (Hibernate-specific)
**What it does:** Specifies fetch strategy

**When to use:** Optimize fetching behavior

**Strategies:**

#### 1. **SELECT** (Default)
```java
@OneToMany
@Fetch(FetchMode.SELECT)
private List<Employee> employees;
// Separate SELECT for each collection
```

#### 2. **JOIN**
```java
@OneToMany
@Fetch(FetchMode.JOIN)
private List<Employee> employees;
// Single JOIN query
```

#### 3. **SUBSELECT**
```java
@OneToMany
@Fetch(FetchMode.SUBSELECT)
private List<Employee> employees;
// Single query with subselect
```

**Frequency:** Advanced optimization (15%)

---

### @ElementCollection
**What it does:** Collection of basic types or embeddables

**When to use:** Simple collections (not entities)

**Example:**
```java
@Entity
public class User {
    @ElementCollection
    @CollectionTable(
        name = "user_phones",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "phone_number")
    private Set<String> phoneNumbers = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_addresses")
    private List<Address> addresses = new ArrayList<>();  // Address is @Embeddable
}
```

**Database:**
```
users table:
id | username

user_phones table:
user_id | phone_number
1       | 555-1234
1       | 555-5678

user_addresses table:
user_id | street | city | zip_code
1       | 123 Main | Boston | 02101
1       | 456 Oak | Cambridge | 02139
```

**TypeScript comparison:**
```typescript
// Usually just JSON column
@Column('json')
phoneNumbers: string[];
```

**Frequency:** Moderate (30%)

---

### @Convert / @Converter
**What it does:** Custom type conversion

**When to use:** Custom data type mapping

**Example:**
```java
// Converter
@Converter
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean value) {
        return (value != null && value) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "Y".equals(value);
    }
}

// Usage
@Entity
public class User {
    @Convert(converter = BooleanToYNConverter.class)
    @Column(length = 1)
    private Boolean active;  // Stored as 'Y' or 'N'
}
```

**Real-world uses:**
- Encryption/decryption
- JSON serialization
- Enum to custom value
- Custom formatting

**Frequency:** Common (35%)

---

## Summary Table

| Annotation | Category | Frequency | Must-Know |
|------------|----------|-----------|-----------|
| @Entity | Basic | 100% | ✅ YES |
| @Table | Basic | 80% | ✅ YES |
| @Id | Basic | 100% | ✅ YES |
| @GeneratedValue | ID | 95% | ✅ YES |
| @Column | Mapping | 90% | ✅ YES |
| @OneToOne | Relationship | 60% | ✅ YES |
| @ManyToOne | Relationship | 95% | ✅ YES |
| @OneToMany | Relationship | 80% | ✅ YES |
| @ManyToMany | Relationship | 40% | ✅ YES |
| @JoinColumn | Relationship | 90% | ✅ YES |
| @Embeddable | Embedded | 50% | ✅ YES |
| @Embedded | Embedded | 50% | ✅ YES |
| @Transient | Mapping | 50% | ✅ YES |
| @Enumerated | Mapping | 70% | ✅ YES |
| @Lob | Mapping | 30% | ⚠️ Good to know |
| @Temporal | Date/Time | 10% | ❌ Legacy only |
| @PrePersist | Lifecycle | 70% | ✅ YES |
| @PreUpdate | Lifecycle | 70% | ✅ YES |
| @Version | Locking | 60% | ✅ YES |
| @NamedQuery | Query | 40% | ⚠️ Good to know |
| @Cacheable | Cache | 30% | ⚠️ Good to know |
| @MappedSuperclass | Inheritance | 80% | ✅ YES |
| @Inheritance | Inheritance | 35% | ⚠️ Good to know |
| @Where | Hibernate | 40% | ⚠️ Good to know |
| @Formula | Hibernate | 20% | ❌ Advanced |
| @BatchSize | Hibernate | 30% | ❌ Advanced |

---

## Must-Know Annotations (Top 15)

If you're starting with JPA, focus on these first:

1. **@Entity** - Mark entity
2. **@Table** - Custom table name
3. **@Id** - Primary key
4. **@GeneratedValue** - Auto ID
5. **@Column** - Column customization
6. **@ManyToOne** - Most common relationship
7. **@OneToMany** - Collection side
8. **@JoinColumn** - Foreign key
9. **@Embedded** / **@Embeddable** - Value objects
10. **@Transient** - Exclude field
11. **@Enumerated** - Enum mapping
12. **@PrePersist** / **@PreUpdate** - Timestamps
13. **@Version** - Optimistic locking
14. **@MappedSuperclass** - Base entity
15. **@OneToOne** / **@ManyToMany** - Other relationships

Master these 15 and you can build 90% of applications!

---

**End of Annotations Reference**
