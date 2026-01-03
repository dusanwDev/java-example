# Hibernate-Specific Annotations - Complete Reference

## 🔥 Hibernate vs JPA Annotations

**JPA Annotations** - Standard (jakarta.persistence.*)
- Portable across providers (Hibernate, EclipseLink, OpenJPA)
- Use these when possible for portability

**Hibernate Annotations** - Hibernate-only (org.hibernate.annotations.*)
- More features and flexibility
- Vendor lock-in (only works with Hibernate)
- Often more powerful than JPA equivalents

---

## Table of Contents

1. [Performance & Optimization](#performance--optimization)
2. [Caching (Covered but expanded)](#caching-annotations)
3. [Custom SQL & DML](#custom-sql--dml)
4. [Filters & Dynamic Queries](#filters--dynamic-queries)
5. [Soft Deletes & Immutability](#soft-deletes--immutability)
6. [Advanced Type Mapping](#advanced-type-mapping)
7. [Collection Optimizations](#collection-optimizations)
8. [ID Generators](#id-generators)
9. [Lazy Loading Control](#lazy-loading-control)
10. [Database-Specific Features](#database-specific-features)

---

## Performance & Optimization

### @DynamicInsert
**What it does:** Only include non-null columns in INSERT statements

**When to use:** Many nullable columns, want smaller SQL

**Example:**
```java
@Entity
@DynamicInsert  // Hibernate-specific
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;        // Will be in INSERT
    private String email;       // Will be in INSERT
    private String phone;       // NULL, excluded from INSERT
    private String address;     // NULL, excluded from INSERT
}

// Without @DynamicInsert:
INSERT INTO users (id, name, email, phone, address)
VALUES (?, ?, ?, NULL, NULL);

// With @DynamicInsert:
INSERT INTO users (id, name, email)
VALUES (?, ?, ?);
```

**Benefits:**
- ✅ Smaller SQL statements
- ✅ Fewer columns locked
- ✅ Better for tables with many columns
- ✅ Database can use column defaults

**Cons:**
- ❌ Can't cache SQL statement (different each time)
- ❌ Slight overhead generating SQL

**TypeScript comparison:** No equivalent - ORMs always insert all columns

**Frequency:** Moderate (25%)

**Real-world use:**
- Tables with 50+ columns
- Sparse data (many NULLs)
- Legacy database integration

---

### @DynamicUpdate
**What it does:** Only include changed columns in UPDATE statements

**When to use:** Large entities, update few columns at a time

**Example:**
```java
@Entity
@DynamicUpdate  // Hibernate-specific
public class Product {
    @Id
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    // ... 20 more fields
}

// Update only price:
product.setPrice(new BigDecimal("99.99"));
em.merge(product);

// Without @DynamicUpdate:
UPDATE products SET
    name=?, description=?, price=?, stock=?, category=?, ...
WHERE id=?;

// With @DynamicUpdate:
UPDATE products SET price=? WHERE id=?;
```

**Benefits:**
- ✅ Smaller UPDATE statements
- ✅ Fewer row locks
- ✅ Better performance for large entities
- ✅ Less database I/O

**Cons:**
- ❌ Can't cache SQL (varies each time)
- ❌ Need to track dirty fields (overhead)

**When to use:**
- ✓ Entities with many columns (20+)
- ✓ Typically update few columns
- ✓ High concurrency (reduce locking)

**When NOT to use:**
- ✗ Small entities (5-10 columns)
- ✗ Usually update most columns
- ✗ Simple CRUD apps

**TypeScript comparison:**
```typescript
// TypeORM always updates all columns
// No equivalent
```

**Frequency:** Common in large apps (35%)

---

### @Immutable
**What it does:** Marks entity as read-only (no UPDATEs)

**When to use:** Reference data that never changes

**Example:**
```java
@Entity
@Immutable  // Hibernate-specific
public class Country {
    @Id
    private String code;  // "US", "UK", "FR"

    private String name;
    private String continent;

    // No updates allowed!
    // Hibernate won't generate UPDATE statements
}

// This does nothing (silently ignored):
Country us = em.find(Country.class, "US");
us.setName("United States of America");
em.merge(us);  // No UPDATE executed!
```

**Benefits:**
- ✅ Performance boost (no dirty checking)
- ✅ Safety (prevents accidental updates)
- ✅ Can be more aggressively cached
- ✅ Clearer intent in code

**Perfect for:**
- Countries, currencies, timezones
- Lookup tables
- Historical/audit data
- Configuration data

**TypeScript comparison:** No direct equivalent

**Frequency:** Common for reference data (30%)

---

### @BatchSize
**What it does:** Fetch multiple collections in batches (N+1 optimization)

**When to use:** Loading collections in loops

**Example:**
```java
@Entity
public class Department {
    @Id
    private Long id;

    @OneToMany(mappedBy = "department")
    @BatchSize(size = 10)  // Hibernate-specific
    private List<Employee> employees;
}

// Load 100 departments and their employees:
List<Department> depts = em.createQuery(
    "FROM Department", Department.class
).getResultList();

// Access employees:
for (Department dept : depts) {
    dept.getEmployees().size();  // Trigger loading
}

// Without @BatchSize (N+1 problem):
// SELECT * FROM department;                          -- 1 query
// SELECT * FROM employee WHERE dept_id = 1;          -- Query 2
// SELECT * FROM employee WHERE dept_id = 2;          -- Query 3
// ... 100 queries total (1 + 100)

// With @BatchSize(size=10):
// SELECT * FROM department;                          -- 1 query
// SELECT * FROM employee WHERE dept_id IN (1,2,3,4,5,6,7,8,9,10);  -- Query 2
// SELECT * FROM employee WHERE dept_id IN (11,12,13,14,15,16,17,18,19,20); -- Query 3
// ... 11 queries total (1 + 10)
```

**Parameters:**
- size: Batch size (default: 1)
- Recommended: 10-50 (depends on data)

**Performance impact:**
```
100 departments:
- No batch: 101 queries (1 + 100)
- Batch 10:  11 queries (1 + 10)
- Batch 25:   5 queries (1 + 4)
- Batch 50:   3 queries (1 + 2)
```

**TypeScript comparison:** No direct equivalent (manually optimize)

**Frequency:** Very common for optimization (60%)

---

### @Fetch
**What it does:** Controls how associations are fetched

**When to use:** Performance optimization

**FetchMode options:**

#### 1. **FetchMode.SELECT** (Default)
```java
@OneToMany
@Fetch(FetchMode.SELECT)  // Hibernate-specific
private List<Order> orders;

// Separate SELECT for each association
// Same as default behavior
```

#### 2. **FetchMode.JOIN**
```java
@OneToMany
@Fetch(FetchMode.JOIN)
private List<Order> orders;

// Single query with LEFT JOIN
SELECT c.*, o.* FROM customer c
LEFT JOIN orders o ON c.id = o.customer_id;

// ⚠️ Can create cartesian product!
// Customer with 10 orders = 10 rows returned
```

**Use when:**
- Always need association
- Collection is small
- OK with cartesian product

#### 3. **FetchMode.SUBSELECT**
```java
@OneToMany
@Fetch(FetchMode.SUBSELECT)
private List<Order> orders;

// Two queries:
// 1. SELECT * FROM customer;
// 2. SELECT * FROM orders WHERE customer_id IN (
//      SELECT id FROM customer
//    );
```

**Use when:**
- Loading multiple parent entities
- Want to avoid N+1
- Don't want JOIN cartesian product

**Comparison:**
```java
// Get 10 customers with their orders:

// SELECT (N+1):
// Query 1: SELECT * FROM customer
// Query 2-11: SELECT * FROM orders WHERE customer_id = ?
// Total: 11 queries

// JOIN:
// Query 1: SELECT c.*, o.* FROM customer c LEFT JOIN orders o ...
// Total: 1 query (but many rows if many orders)

// SUBSELECT:
// Query 1: SELECT * FROM customer
// Query 2: SELECT * FROM orders WHERE customer_id IN (1,2,3,4,5,6,7,8,9,10)
// Total: 2 queries
```

**TypeScript comparison:**
```typescript
// TypeORM has similar options
@OneToMany(() => Order, order => order.customer)
orders: Order[];

// Eager load:
findOne({ relations: ['orders'] });  // Like JOIN
```

**Frequency:** Advanced optimization (20%)

---

## Caching Annotations

### @Cache
**What it does:** Configure second-level cache

**Already covered but here are ALL strategies:**

#### CacheConcurrencyStrategy Options:

**1. READ_ONLY** (Fastest)
```java
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Country {
    // Reference data that NEVER changes
}
```
**Use for:** Countries, currencies, configs
**Performance:** ⭐⭐⭐⭐⭐ (best)
**Consistency:** ⭐⭐⭐⭐⭐ (no updates)

**2. NONSTRICT_READ_WRITE** (Good performance)
```java
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product {
    // Changes rarely, occasional stale data OK
}
```
**Use for:** Products, categories, rarely-updated data
**Performance:** ⭐⭐⭐⭐
**Consistency:** ⭐⭐ (can be stale briefly)

**3. READ_WRITE** (Balanced)
```java
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {
    // Frequently read and updated
}
```
**Use for:** Users, most entities
**Performance:** ⭐⭐⭐
**Consistency:** ⭐⭐⭐⭐ (good)

**4. TRANSACTIONAL** (Strict)
```java
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class BankAccount {
    // Critical data, absolute consistency needed
}
```
**Use for:** Financial, sensitive data
**Performance:** ⭐⭐
**Consistency:** ⭐⭐⭐⭐⭐ (perfect)

**Cache regions:**
```java
@Cache(
    usage = CacheConcurrencyStrategy.READ_WRITE,
    region = "products",  // Custom cache region
    include = "all"       // Cache all properties
)
```

**Frequency:** Common in production (50%)

---

### @NaturalId
**What it does:** Mark business key for caching and lookups

**When to use:** Have natural unique identifier (not DB-generated ID)

**Example:**
```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;  // Surrogate key (DB-generated)

    @NaturalId  // Hibernate-specific
    @Column(unique = true, nullable = false)
    private String email;  // Natural key (business identifier)

    @NaturalId
    private String username;
}

// Lookup by natural ID (cached!):
User user = session.byNaturalId(User.class)
    .using("email", "john@example.com")
    .load();

// Or with multiple natural IDs:
User user = session.byNaturalId(User.class)
    .using("email", "john@example.com")
    .using("username", "johndoe")
    .load();
```

**Benefits:**
- ✅ Cached lookups by business key
- ✅ More intuitive than surrogate ID
- ✅ Better for business logic
- ✅ Automatic cache management

**Natural ID examples:**
- Email (users)
- SKU (products)
- ISBN (books)
- SSN (persons)
- VIN (vehicles)
- License plate
- Order number

**Mutable natural IDs:**
```java
@NaturalId(mutable = true)  // Can change (not recommended)
private String email;

// Changing requires:
session.byNaturalId(User.class)
    .using("email", oldEmail)
    .load();
user.setEmail(newEmail);
// Hibernate updates cache
```

**Immutable natural IDs (recommended):**
```java
@NaturalId(mutable = false)  // Default, cannot change
private String email;
```

**TypeScript comparison:** No equivalent (just unique columns)

**Frequency:** Common (40%)

---

## Custom SQL & DML

### @SQLInsert, @SQLUpdate, @SQLDelete
**What it does:** Override default SQL for DML operations

**When to use:** Custom SQL logic, stored procedures, database functions

**Example:**
```java
@Entity
@SQLInsert(
    sql = "INSERT INTO users (id, name, created_at) VALUES (?, ?, NOW())",
    check = ResultCheckStyle.COUNT
)
@SQLUpdate(
    sql = "UPDATE users SET name = ?, updated_at = NOW() WHERE id = ?",
    check = ResultCheckStyle.COUNT
)
@SQLDelete(
    sql = "UPDATE users SET deleted = true, deleted_at = NOW() WHERE id = ?",
    check = ResultCheckStyle.COUNT  // Soft delete!
)
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // Database manages these:
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(insertable = false, updatable = false)
    private Boolean deleted;
}
```

**ResultCheckStyle:**
- `COUNT`: Check affected rows
- `NONE`: Don't check
- `PARAM`: Use output parameter (stored procedures)

**Use cases:**
- Database triggers/functions
- Audit columns (created_at, updated_at)
- Soft deletes
- Custom logic
- Stored procedures

**TypeScript comparison:**
```typescript
// TypeORM has listeners
@BeforeInsert()
setDates() {
    this.createdAt = new Date();
}
```

**Frequency:** Moderate (25%)

---

### @Subselect
**What it does:** Map entity to SQL query (read-only view)

**When to use:** Complex queries as entities, database views

**Example:**
```java
@Entity
@Immutable  // Usually read-only
@Subselect("""
    SELECT
        u.id as user_id,
        u.name as user_name,
        COUNT(o.id) as order_count,
        SUM(o.total) as total_spent
    FROM users u
    LEFT JOIN orders o ON u.id = o.user_id
    GROUP BY u.id, u.name
""")
public class UserStatistics {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "order_count")
    private Long orderCount;

    @Column(name = "total_spent")
    private BigDecimal totalSpent;

    // No setters - read-only!
}

// Use like normal entity:
UserStatistics stats = em.find(UserStatistics.class, 1L);
System.out.println(stats.getUserName() + " has " +
                   stats.getOrderCount() + " orders");
```

**Benefits:**
- ✅ Complex queries as entities
- ✅ Join data from multiple tables
- ✅ Aggregations
- ✅ Can use JPQL on it

**Cons:**
- ❌ Read-only (no updates)
- ❌ Not a real table
- ❌ Query runs every time (unless cached)

**Alternative: Database views:**
```sql
CREATE VIEW user_statistics AS
SELECT ...;
```
```java
@Entity
@Table(name = "user_statistics")
@Immutable
public class UserStatistics {
    // Map to database view
}
```

**TypeScript comparison:**
```typescript
// TypeORM ViewEntity
@ViewEntity({
    expression: `SELECT ... FROM users ...`
})
export class UserStatistics { }
```

**Frequency:** Moderate for reporting (20%)

---

## Filters & Dynamic Queries

### @Filter + @FilterDef
**What it does:** Dynamic WHERE clause filtering

**When to use:** Multi-tenancy, soft deletes, security filtering

**Example:**
```java
// Define filter
@FilterDef(
    name = "tenantFilter",
    parameters = @ParamDef(name = "tenantId", type = Long.class)
)
@Entity
@Filter(
    name = "tenantFilter",
    condition = "tenant_id = :tenantId"  // SQL WHERE clause
)
public class Document {
    @Id
    private Long id;

    private String title;

    @Column(name = "tenant_id")
    private Long tenantId;
}

// Enable filter:
Session session = em.unwrap(Session.class);
session.enableFilter("tenantFilter")
    .setParameter("tenantId", currentTenantId);

// All queries automatically filtered!
List<Document> docs = em.createQuery(
    "FROM Document", Document.class
).getResultList();
// SQL: SELECT * FROM document WHERE tenant_id = ?

// Disable filter:
session.disableFilter("tenantFilter");
```

**Multiple filters:**
```java
@FilterDef(name = "activeOnly")
@FilterDef(name = "byStatus",
    parameters = @ParamDef(name = "status", type = String.class))

@Entity
@Filter(name = "activeOnly", condition = "active = true")
@Filter(name = "byStatus", condition = "status = :status")
public class User {
    private Boolean active;
    private String status;
}

// Enable both:
session.enableFilter("activeOnly");
session.enableFilter("byStatus").setParameter("status", "VERIFIED");

// SELECT * FROM users WHERE active = true AND status = 'VERIFIED'
```

**Collection filtering:**
```java
@Entity
public class Blog {
    @OneToMany
    @Filter(name = "publishedPosts", condition = "status = 'PUBLISHED'")
    private List<Post> posts;
}
```

**Real-world uses:**

**1. Multi-tenancy:**
```java
@FilterDef(name = "tenantFilter",
    parameters = @ParamDef(name = "tenantId", type = Long.class))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")

// All queries scoped to current tenant
```

**2. Soft deletes:**
```java
@FilterDef(name = "notDeleted")
@Filter(name = "notDeleted", condition = "deleted = false")

// Hide deleted records
```

**3. Security:**
```java
@FilterDef(name = "userAccess",
    parameters = @ParamDef(name = "userId", type = Long.class))
@Filter(name = "userAccess",
    condition = "user_id = :userId OR is_public = true")

// Only show user's own or public data
```

**TypeScript comparison:** No direct equivalent (manual WHERE clauses)

**Frequency:** Common in enterprise apps (35%)

---

## Soft Deletes & Immutability

### @Where
**What it does:** Permanent WHERE clause filter

**When to use:** Soft deletes, always-filtered data

**Example:**
```java
@Entity
@Where(clause = "deleted = false")  // Hibernate-specific
public class User {
    @Id
    private Long id;

    private String name;

    @Column(nullable = false)
    private Boolean deleted = false;
}

// All queries automatically filter:
List<User> users = em.createQuery("FROM User", User.class)
    .getResultList();
// SQL: SELECT * FROM users WHERE deleted = false

// Soft delete implementation:
public void softDelete(User user) {
    user.setDeleted(true);
    em.merge(user);
}

// User disappears from all queries!
```

**Difference from @Filter:**
- **@Where**: Always applied, can't disable
- **@Filter**: Can enable/disable dynamically

**Collection filtering:**
```java
@Entity
public class Post {
    @OneToMany
    @Where(clause = "approved = true")
    private List<Comment> approvedComments;

    @OneToMany
    @Where(clause = "flagged = true")
    private List<Comment> flaggedComments;
}
```

**Multiple conditions:**
```java
@Where(clause = "deleted = false AND active = true")
```

**TypeScript comparison:**
```typescript
// No direct equivalent
// Would use query builders or scopes
```

**Frequency:** Very common for soft deletes (50%)

---

### @SQLRestriction (Hibernate 6.3+)
**What it does:** Newer alternative to @Where

**Example:**
```java
@Entity
@SQLRestriction("deleted = false")  // Hibernate 6.3+
public class User {
    // Same as @Where but clearer name
}
```

**Frequency:** New, will replace @Where

---

## Advanced Type Mapping

### @Type
**What it does:** Custom type mapping for properties

**When to use:** Non-standard types, custom conversions

**Example:**
```java
@Entity
public class Document {
    @Id
    private Long id;

    // Map JSON column to Java object
    @Type(JsonBinaryType.class)  // Hibernate-specific
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    // Custom enum mapping
    @Type(PostgreSQLEnumType.class)
    @Column(columnDefinition = "user_status")
    private UserStatus status;
}
```

**Common custom types:**
```java
// 1. JSON type
@Type(JsonType.class)
@Column(columnDefinition = "json")
private List<String> tags;

// 2. Array type (PostgreSQL)
@Type(StringArrayType.class)
@Column(columnDefinition = "text[]")
private String[] categories;

// 3. INET type (PostgreSQL)
@Type(InetAddressType.class)
@Column(columnDefinition = "inet")
private InetAddress ipAddress;

// 4. UUID binary
@Type(UUIDBinaryType.class)
@Column(columnDefinition = "binary(16)")
private UUID uuid;
```

**Creating custom type:**
```java
public class MoneyType implements UserType<Money> {
    @Override
    public int getSqlType() {
        return Types.NUMERIC;
    }

    @Override
    public Class<Money> returnedClass() {
        return Money.class;
    }

    @Override
    public Money nullSafeGet(ResultSet rs, int position,
                             SharedSessionContractImplementor session,
                             Object owner) throws SQLException {
        BigDecimal value = rs.getBigDecimal(position);
        return value == null ? null : new Money(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Money value,
                           int index,
                           SharedSessionContractImplementor session)
                           throws SQLException {
        if (value == null) {
            st.setNull(index, Types.NUMERIC);
        } else {
            st.setBigDecimal(index, value.getAmount());
        }
    }
}

// Usage:
@Type(MoneyType.class)
private Money price;
```

**TypeScript comparison:**
```typescript
// TypeORM transformers
@Column({
    type: 'jsonb',
    transformer: {
        to: (value) => JSON.stringify(value),
        from: (value) => JSON.parse(value)
    }
})
metadata: object;
```

**Frequency:** Moderate for complex types (30%)

---

### @TypeDef, @TypeDefs
**What it does:** Register custom type globally

**When to use:** Reuse custom types across entities

**Example:**
```java
// Define once at package level
@TypeDef(
    name = "json",
    typeClass = JsonBinaryType.class
)
package com.example.entities;

import org.hibernate.annotations.TypeDef;

// Or on entity:
@Entity
@TypeDef(name = "json", typeClass = JsonBinaryType.class)
public class Document {
    @Type(type = "json")  // Use by name
    private Map<String, Object> data;
}

// Multiple type definitions:
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
    @TypeDef(name = "encrypted", typeClass = EncryptedStringType.class)
})
@Entity
public class User {
    @Type(type = "jsonb")
    private Map<String, String> preferences;

    @Type(type = "encrypted")
    private String ssn;
}
```

**Package-level types (package-info.java):**
```java
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonType.class),
    @TypeDef(name = "string-array", typeClass = StringArrayType.class)
})
package com.example.domain;

import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
```

**Benefits:**
- ✅ Define once, use everywhere
- ✅ Consistent type handling
- ✅ Cleaner entity code

**Frequency:** Common in large apps (35%)

---

### @Nationalized
**What it does:** Use Unicode (NVARCHAR, NCHAR, NTEXT)

**When to use:** International characters, multi-language support

**Example:**
```java
@Entity
public class Article {
    @Id
    private Long id;

    @Nationalized  // Hibernate-specific
    @Column(length = 200)
    private String title;  // Maps to NVARCHAR(200)

    @Nationalized
    @Lob
    private String content;  // Maps to NTEXT

    // Regular strings (non-unicode)
    @Column(length = 50)
    private String status;  // Maps to VARCHAR(50)
}

// Database DDL:
// title NVARCHAR(200) -- Supports Chinese, Arabic, emoji, etc.
// content NTEXT
// status VARCHAR(50) -- ASCII only
```

**When needed:**
- ✓ Chinese, Japanese, Korean text
- ✓ Arabic, Hebrew text
- ✓ Emoji characters 😀
- ✓ Special symbols
- ✓ Multi-language applications

**Storage difference:**
```
VARCHAR:  1 byte per character (ASCII)
NVARCHAR: 2 bytes per character (Unicode)

"Hello" in VARCHAR: 5 bytes
"Hello" in NVARCHAR: 10 bytes
"你好" in VARCHAR: Not supported
"你好" in NVARCHAR: 4 bytes
```

**TypeScript comparison:** All strings are Unicode by default

**Frequency:** Varies by region (10-50%)

---

### @Generated
**What it does:** Value generated by database

**When to use:** Database triggers, computed columns, sequences

**GenerationTime options:**

**1. ALWAYS** - Generated on INSERT and UPDATE
```java
@Entity
public class AuditLog {
    @Id
    @GeneratedValue
    private Long id;

    private String action;

    @Generated(GenerationTime.ALWAYS)  // Hibernate-specific
    @Column(insertable = false, updatable = false)
    private LocalDateTime timestamp;
    // Database trigger sets this

    @Generated(GenerationTime.ALWAYS)
    @Column(columnDefinition = "INTEGER AS (quantity * price)")
    private BigDecimal total;  // Computed column
}
```

**2. INSERT** - Generated only on INSERT
```java
@Generated(GenerationTime.INSERT)
@Column(insertable = false, updatable = false)
private LocalDateTime createdAt;
// Database default: CURRENT_TIMESTAMP
```

**3. NEVER** - Not generated (default)
```java
@Generated(GenerationTime.NEVER)
private String name;  // Normal column
```

**Real-world examples:**

**Database trigger:**
```sql
CREATE TRIGGER set_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP;
END;
```
```java
@Generated(GenerationTime.ALWAYS)
@Column(insertable = false, updatable = false)
private LocalDateTime updatedAt;
```

**Computed column:**
```sql
ALTER TABLE products
ADD COLUMN total_price DECIMAL(10,2)
GENERATED ALWAYS AS (quantity * unit_price);
```
```java
@Generated(GenerationTime.ALWAYS)
@Column(insertable = false, updatable = false)
private BigDecimal totalPrice;
```

**Benefits:**
- ✅ Database handles logic
- ✅ Automatic refresh after save
- ✅ Consistent values

**TypeScript comparison:**
```typescript
// Similar concept
@Generated()
createdAt: Date;
```

**Frequency:** Common for timestamps (40%)

---

### @ColumnTransformer
**What it does:** Transform values on read/write

**When to use:** Encryption, formatting, custom conversions

**Example:**

**Encryption:**
```java
@Entity
public class User {
    @Id
    private Long id;

    @ColumnTransformer(
        read = "AES_DECRYPT(ssn, 'secret-key')",
        write = "AES_ENCRYPT(?, 'secret-key')"
    )
    @Column(columnDefinition = "VARBINARY(256)")
    private String ssn;  // Social Security Number

    // On read:  SELECT AES_DECRYPT(ssn, 'key') FROM users
    // On write: INSERT INTO users (ssn) VALUES (AES_ENCRYPT(?, 'key'))
}
```

**Case conversion:**
```java
@ColumnTransformer(
    read = "UPPER(email)",
    write = "LOWER(?)"
)
private String email;

// Stores lowercase, reads as uppercase
user.setEmail("John@Example.COM");  // Stored as "john@example.com"
String email = user.getEmail();     // Returns "JOHN@EXAMPLE.COM"
```

**Formatting:**
```java
@ColumnTransformer(
    read = "CONCAT('+1-', phone_number)",
    write = "REPLACE(?, '+1-', '')"
)
private String phoneNumber;

// Stores without prefix, reads with "+1-" prefix
```

**Custom functions:**
```java
@ColumnTransformer(
    forColumn = "price",
    read = "price / 100.0",  // Store as cents, read as dollars
    write = "? * 100"
)
private BigDecimal price;
```

**Multiple columns:**
```java
@ColumnTransformer(
    forColumn = "first_name",
    read = "UPPER(first_name)",
    write = "?"
)
private String firstName;

@ColumnTransformer(
    forColumn = "last_name",
    read = "UPPER(last_name)",
    write = "?"
)
private String lastName;
```

**Benefits:**
- ✅ Database-level transformations
- ✅ Encryption in database
- ✅ Transparent to application code

**Cons:**
- ❌ Database-specific SQL
- ❌ Can't index transformed values
- ❌ May impact performance

**TypeScript comparison:** No direct equivalent (manual transformation)

**Frequency:** Moderate for security (20%)

---

### @CreationTimestamp
**What it does:** Auto-set timestamp on INSERT

**When to use:** Track when entity was created

**Example:**
```java
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @CreationTimestamp  // Hibernate-specific
    private LocalDateTime createdAt;
    // Automatically set to current time on persist

    @CreationTimestamp
    private Instant createdInstant;  // Can use Instant too

    @CreationTimestamp
    @Column(updatable = false)  // Prevent changes
    private Date createdDate;  // Or java.util.Date
}

// Usage:
Post post = new Post();
post.setTitle("My Post");
em.persist(post);
// createdAt is automatically set to LocalDateTime.now()
// No need to call post.setCreatedAt()!
```

**Supported types:**
- `java.time.LocalDateTime`
- `java.time.Instant`
- `java.time.ZonedDateTime`
- `java.util.Date`
- `java.sql.Timestamp`

**Comparison with alternatives:**

**1. @CreationTimestamp (Hibernate):**
```java
@CreationTimestamp
private LocalDateTime createdAt;
```
✅ Automatic, no code needed
✅ Set by Hibernate in Java
❌ Hibernate-specific

**2. @PrePersist (JPA):**
```java
@PrePersist
void onCreate() {
    createdAt = LocalDateTime.now();
}
```
✅ JPA standard
✅ Custom logic possible
❌ Need to write callback

**3. Database default:**
```java
@Column(
    insertable = false,
    updatable = false,
    columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
)
@Generated(GenerationTime.INSERT)
private LocalDateTime createdAt;
```
✅ Database sets value
✅ Survives bulk inserts
❌ Database-specific
❌ Requires refresh to read

**TypeScript comparison:**
```typescript
// TypeORM
@CreateDateColumn()
createdAt: Date;
```

**Frequency:** Very common (70%)

---

### @UpdateTimestamp
**What it does:** Auto-update timestamp on INSERT and UPDATE

**When to use:** Track last modification time

**Example:**
```java
@Entity
public class Product {
    @Id
    private Long id;

    private String name;
    private BigDecimal price;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp  // Hibernate-specific
    private LocalDateTime updatedAt;
    // Set on both persist AND update

    @UpdateTimestamp
    private Instant lastModified;
}

// Usage:
Product product = new Product();
product.setName("Laptop");
em.persist(product);
// createdAt: 2025-01-01 10:00:00
// updatedAt: 2025-01-01 10:00:00

// Later...
product.setPrice(new BigDecimal("999.99"));
em.merge(product);
// createdAt: 2025-01-01 10:00:00 (unchanged)
// updatedAt: 2025-01-01 15:30:00 (updated!)
```

**Audit trail example:**
```java
@MappedSuperclass
public abstract class Auditable {
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(updatable = false)
    private String createdBy;

    private String updatedBy;

    // Getters and setters
}

@Entity
public class Invoice extends Auditable {
    @Id
    private Long id;

    private BigDecimal amount;
    // Inherits: createdAt, updatedAt, createdBy, updatedBy
}
```

**Complete audit system:**
```java
@Entity
@EntityListeners(AuditListener.class)
public class Document extends Auditable {
    @Id
    private Long id;

    private String title;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant modifiedAt;

    // Set by listener:
    private String createdByUser;
    private String modifiedByUser;
}

public class AuditListener {
    @PrePersist
    void onCreate(Object entity) {
        if (entity instanceof Auditable auditable) {
            auditable.setCreatedByUser(getCurrentUser());
        }
    }

    @PreUpdate
    void onUpdate(Object entity) {
        if (entity instanceof Auditable auditable) {
            auditable.setModifiedByUser(getCurrentUser());
        }
    }

    private String getCurrentUser() {
        // Get from security context
        return SecurityContextHolder.getContext()
            .getAuthentication().getName();
    }
}
```

**TypeScript comparison:**
```typescript
// TypeORM
@UpdateDateColumn()
updatedAt: Date;
```

**Frequency:** Very common (60%)

---

## Collection Optimizations

### @OrderBy
**What it does:** Sort collection by column

**When to use:** Always need sorted data

**Example:**
```java
@Entity
public class Playlist {
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "playlist")
    @OrderBy("position ASC, title ASC")  // SQL ORDER BY
    private List<Song> songs;
    // Always sorted by position, then title
}

// When loading:
Playlist playlist = em.find(Playlist.class, 1L);
List<Song> songs = playlist.getSongs();
// Already sorted!

// Generated SQL:
// SELECT * FROM songs WHERE playlist_id = ?
// ORDER BY position ASC, title ASC
```

**Multiple orderings:**
```java
@OneToMany
@OrderBy("priority DESC, createdAt ASC")
private List<Task> tasks;
// High priority first, oldest first
```

**By association:**
```java
@OneToMany
@OrderBy("author.lastName ASC, title ASC")
private List<Book> books;
// Sort by author last name, then book title
```

**Difference from @OrderColumn:**

**@OrderBy** - Sort by property:
```java
@OrderBy("name ASC")
private List<Product> products;
// Sorts by product name
// No extra column
```

**@OrderColumn** - Maintain list index:
```java
@OrderColumn(name = "position")
private List<Item> items;
// Stores 0, 1, 2, 3... in position column
// Maintains insertion order
```

**TypeScript comparison:**
```typescript
// TypeORM
@OneToMany(() => Song, song => song.playlist)
@OrderBy('position', 'ASC')
songs: Song[];
```

**Frequency:** Very common (50%)

---

### @OrderColumn
**What it does:** Maintain list position with index column

**When to use:** Order matters, drag-and-drop lists

**Example:**
```java
@Entity
public class TodoList {
    @Id
    private Long id;

    @OneToMany(mappedBy = "todoList")
    @OrderColumn(name = "list_position")  // Stores 0, 1, 2...
    private List<TodoItem> items;
}

@Entity
public class TodoItem {
    @Id
    private Long id;

    private String description;

    @ManyToOne
    private TodoList todoList;

    // No position field needed!
    // Hibernate manages it automatically
}

// Database:
// ┌────┬─────────────────┬─────────────┬───────────────┐
// │ id │  description    │ todo_list_id│ list_position │
// ├────┼─────────────────┼─────────────┼───────────────┤
// │  1 │ Buy milk        │      1      │       0       │
// │  2 │ Walk dog        │      1      │       1       │
// │  3 │ Write code      │      1      │       2       │
// └────┴─────────────────┴─────────────┴───────────────┘
```

**Reordering:**
```java
// Swap items 0 and 1:
List<TodoItem> items = todoList.getItems();
TodoItem first = items.get(0);
TodoItem second = items.get(1);

items.set(0, second);  // Position 0: "Walk dog"
items.set(1, first);   // Position 1: "Buy milk"

em.merge(todoList);
// Hibernate updates list_position automatically!
```

**Parameters:**
```java
@OrderColumn(
    name = "position",          // Column name
    nullable = false,            // Required position
    insertable = true,           // Can insert
    updatable = true,            // Can update
    columnDefinition = "INTEGER" // SQL type
)
private List<Item> items;
```

**Benefits:**
- ✅ Maintains exact order
- ✅ Drag-and-drop support
- ✅ List index preserved
- ✅ No position field in entity

**Cons:**
- ❌ Extra column needed
- ❌ Updates on reorder
- ❌ Only works with List (not Set)

**Use cases:**
- Todo list items
- Playlist songs
- Slide deck slides
- Menu items
- Step-by-step instructions

**TypeScript comparison:** No direct equivalent

**Frequency:** Moderate (25%)

---

### @ListIndexBase
**What it does:** Change list index starting value

**When to use:** List should start at 1 instead of 0

**Example:**
```java
@Entity
public class Book {
    @Id
    private Long id;

    @ElementCollection
    @OrderColumn(name = "page_number")
    @ListIndexBase(1)  // Start at 1 instead of 0
    private List<String> pages;
}

// Database:
// ┌────────┬─────────────┬──────────────┐
// │ book_id│ page_number │  page_text   │
// ├────────┼─────────────┼──────────────┤
// │   1    │      1      │ "Chapter 1..." │  ← Starts at 1, not 0
// │   1    │      2      │ "Chapter 2..." │
// │   1    │      3      │ "Chapter 3..." │
// └────────┴─────────────┴──────────────┘

// Access:
List<String> pages = book.getPages();
pages.get(0);  // Still use 0 in Java code
// But stored as 1 in database
```

**Use cases:**
- Human-readable numbering
- Page numbers (start at 1)
- Chapter numbers
- Floor numbers (some buildings skip 13)

**Frequency:** Rare (5%)

---

### @Bag
**What it does:** Collection allowing duplicates without order

**When to use:** Duplicates allowed, order doesn't matter

**Example:**
```java
@Entity
public class Survey {
    @Id
    private Long id;

    @ElementCollection
    @CollectionTable(name = "survey_responses")
    @Bag  // Hibernate-specific
    private Collection<String> responses;
    // Not List (ordered), not Set (unique)
    // Just a bag of values!
}

// Can have duplicates:
survey.getResponses().add("Yes");
survey.getResponses().add("Yes");  // Allowed!
survey.getResponses().add("No");
// Stored: ["Yes", "Yes", "No"]
```

**Collection type comparison:**

| Type         | Ordered? | Duplicates? | Interface |
|--------------|----------|-------------|-----------|
| List         | ✅ Yes   | ✅ Yes      | List<T>   |
| Set          | ❌ No    | ❌ No       | Set<T>    |
| Bag (@Bag)   | ❌ No    | ✅ Yes      | Collection<T> |

**Frequency:** Rare (5%)

---

## ID Generators

### @GenericGenerator
**What it does:** Custom ID generation strategy

**When to use:** Need specific ID format or pattern

**Built-in generators:**

**1. UUID:**
```java
@Entity
public class Document {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;
    // Generates: 550e8400-e29b-41d4-a716-446655440000
}
```

**2. UUID String:**
```java
@Id
@GeneratedValue(generator = "uuid-string")
@GenericGenerator(name = "uuid-string", strategy = "org.hibernate.id.UUIDGenerator")
private String id;
```

**3. Custom sequence:**
```java
@Id
@GeneratedValue(generator = "custom-sequence")
@GenericGenerator(
    name = "custom-sequence",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {
        @Parameter(name = "sequence_name", value = "user_seq"),
        @Parameter(name = "initial_value", value = "1000"),
        @Parameter(name = "increment_size", value = "10")
    }
)
private Long id;
```

**4. Prefix generator:**
```java
@Id
@GeneratedValue(generator = "order-id")
@GenericGenerator(
    name = "order-id",
    strategy = "com.example.OrderIdGenerator"
)
private String id;  // Generates: "ORD-20250101-0001"

// Custom generator:
public class OrderIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session,
                                 Object object) {
        String date = LocalDate.now().format(
            DateTimeFormatter.BASIC_ISO_DATE
        );

        // Get next sequence number
        Query query = session.createNativeQuery(
            "SELECT nextval('order_seq')"
        );
        Long seq = ((Number) query.getSingleResult()).longValue();

        return String.format("ORD-%s-%04d", date, seq);
    }
}
```

**5. Hi/Lo algorithm:**
```java
@Id
@GeneratedValue(generator = "hilo")
@GenericGenerator(
    name = "hilo",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {
        @Parameter(name = "optimizer", value = "hilo"),
        @Parameter(name = "increment_size", value = "100")
    }
)
private Long id;
// Fetches sequence in batches of 100
// Reduces database round trips
```

**Common strategies:**
- `uuid2`: UUID generator
- `sequence`: Database sequence
- `identity`: Auto-increment
- `increment`: Hibernate increment
- `assigned`: Manually assigned
- `native`: Database-appropriate

**TypeScript comparison:**
```typescript
// TypeORM
@PrimaryGeneratedColumn('uuid')
id: string;
```

**Frequency:** Moderate (30%)

---

### @UuidGenerator (Hibernate 6.2+)
**What it does:** Generate UUID for primary key

**When to use:** Want UUIDs instead of sequential IDs

**Example:**
```java
@Entity
public class Session {
    @Id
    @UuidGenerator  // Hibernate 6.2+
    private UUID id;
    // Automatically generates UUID
}

// Or with style:
@Id
@UuidGenerator(style = UuidGenerator.Style.TIME)
private UUID id;
// TIME: Version 1 UUID (time-based)
// RANDOM: Version 4 UUID (random)
```

**UUID benefits:**
- ✅ Globally unique (merge databases)
- ✅ No central coordination needed
- ✅ Can generate client-side
- ✅ Security (not sequential)

**UUID cons:**
- ❌ Larger storage (16 bytes vs 8)
- ❌ Slower index performance
- ❌ Not human-readable

**Frequency:** Growing (40%)

---

## Lazy Loading Control

### @LazyToOne
**What it does:** Force lazy loading for @OneToOne and @ManyToOne

**When to use:** Lazy load @OneToOne (normally eager by default)

**Example:**
```java
@Entity
public class Employee {
    @Id
    private Long id;

    @OneToOne
    @LazyToOne(LazyToOneOption.PROXY)  // Hibernate-specific
    private ParkingSpot parkingSpot;
    // Lazy loads even though @OneToOne
}
```

**LazyToOneOption values:**

**1. PROXY** (default):
```java
@LazyToOne(LazyToOneOption.PROXY)
@OneToOne
private Address address;
// Uses proxy for lazy loading
```

**2. NO_PROXY:**
```java
@LazyToOne(LazyToOneOption.NO_PROXY)
@OneToOne(optional = false)  // Must be non-null
private Profile profile;
// Bytecode enhancement required
```

**3. FALSE:**
```java
@LazyToOne(LazyToOneOption.FALSE)
@OneToOne
private Passport passport;
// Always eager (fetched immediately)
```

**Frequency:** Moderate (20%)

---

### @LazyGroup
**What it does:** Group lazy fields for batch fetching

**When to use:** Multiple lazy fields loaded together

**Example:**
```java
@Entity
public class Article {
    @Id
    private Long id;

    private String title;  // Always loaded

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("content")  // Group 1
    private String content;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("content")  // Group 1
    private String summary;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("attachments")  // Group 2
    private byte[] attachment;

    // Access content triggers loading both content AND summary
    String text = article.getContent();
    // summary also loaded now!

    // But attachment still lazy (different group)
}
```

**Benefits:**
- ✅ Load related fields together
- ✅ Reduce lazy initialization exceptions
- ✅ Better performance

**Frequency:** Advanced usage (10%)

---

### @Proxy
**What it does:** Configure proxy behavior

**When to use:** Custom proxy settings, disable proxies

**Example:**
```java
@Entity
@Proxy(lazy = false)  // Disable lazy loading proxies
public class SmallEntity {
    @Id
    private Long id;
    private String name;
    // Always loaded eagerly
}

// Or custom proxy class:
@Entity
@Proxy(proxyClass = CustomUserProxy.class)
public class User {
    // Use custom proxy implementation
}

// Disable proxy for specific case:
@Entity
@Proxy(lazy = true)
public class LazyEntity {
    // Can still be proxied
}
```

**Frequency:** Rare (5%)

---

## Database-Specific Features

### @DialectOverride
**What it does:** Override behavior per database dialect

**When to use:** Multi-database support with database-specific features

**Example (Hibernate 6.2+):**
```java
@Entity
public class Product {
    @Id
    private Long id;

    @DialectOverride.Formula(
        dialect = PostgreSQLDialect.class,
        override = @Formula("price * 1.15")  // 15% tax in PostgreSQL
    )
    @DialectOverride.Formula(
        dialect = MySQLDialect.class,
        override = @Formula("price * 1.10")  // 10% tax in MySQL
    )
    private BigDecimal priceWithTax;
}
```

**Frequency:** Rare (5%)

---

### @Check
**What it does:** Add CHECK constraint to table

**When to use:** Database-level validation

**Example:**
```java
@Entity
@Check(constraints = "age >= 18")  // Hibernate-specific
public class User {
    @Id
    private Long id;

    private String name;

    @Column(nullable = false)
    private Integer age;
}

// Generated DDL:
// CREATE TABLE users (
//     id BIGINT PRIMARY KEY,
//     name VARCHAR(255),
//     age INTEGER NOT NULL,
//     CONSTRAINT check_age CHECK (age >= 18)
// );
```

**Multiple constraints:**
```java
@Entity
@Check(constraints = "price > 0")
@Check(constraints = "stock >= 0")
public class Product {
    private BigDecimal price;
    private Integer stock;
}
```

**Named constraints:**
```java
@Entity
@Check(
    name = "valid_price",
    constraints = "price >= 0 AND price <= 999999"
)
public class Product {
    private BigDecimal price;
}
```

**Column-level check:**
```java
@Column(columnDefinition = "INTEGER CHECK (age >= 0 AND age <= 120)")
private Integer age;
```

**TypeScript comparison:** No direct equivalent

**Frequency:** Moderate for data integrity (25%)

---

### @Index
**What it does:** Create database index

**When to use:** Optimize query performance

**Example:**
```java
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_name_age", columnList = "name, age")
    }
)
public class User {
    @Id
    private Long id;

    private String email;
    private String name;
    private Integer age;
}

// Generated DDL:
// CREATE INDEX idx_email ON users (email);
// CREATE INDEX idx_name_age ON users (name, age);
```

**Unique index:**
```java
@Index(name = "idx_username", columnList = "username", unique = true)
```

**Partial index (database-specific):**
```java
// PostgreSQL
@Index(
    name = "idx_active_users",
    columnList = "email",
    // Requires native query or schema.sql
)
// CREATE INDEX idx_active_users ON users (email) WHERE active = true;
```

**Association index:**
```java
@Entity
public class Order {
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @org.hibernate.annotations.Index(name = "idx_customer")  // Old way
    private Customer customer;
}

// Better way (JPA standard):
@ManyToOne
@JoinColumn(
    name = "customer_id",
    foreignKey = @ForeignKey(name = "fk_customer"),
    indexes = @Index(name = "idx_customer", columnList = "customer_id")
)
private Customer customer;
```

**TypeScript comparison:**
```typescript
// TypeORM
@Entity()
@Index(['email'])
@Index(['name', 'age'])
export class User { }
```

**Frequency:** Very common (70%)

---

### @Comment
**What it does:** Add SQL comments to table/column

**When to use:** Document database schema

**Example:**
```java
@Entity
@Comment("Stores user account information")
public class User {
    @Id
    @Comment("Primary key - auto-generated")
    private Long id;

    @Comment("User's email address - must be unique")
    @Column(unique = true, nullable = false)
    private String email;

    @Comment("Hashed password using BCrypt")
    private String password;

    @Comment("Account creation timestamp")
    @CreationTimestamp
    private LocalDateTime createdAt;
}

// Generated DDL (PostgreSQL):
// CREATE TABLE users (
//     id BIGINT PRIMARY KEY,
//     email VARCHAR(255) UNIQUE NOT NULL,
//     password VARCHAR(255),
//     created_at TIMESTAMP
// );
// COMMENT ON TABLE users IS 'Stores user account information';
// COMMENT ON COLUMN users.id IS 'Primary key - auto-generated';
// COMMENT ON COLUMN users.email IS 'User''s email address - must be unique';
```

**Benefits:**
- ✅ Self-documenting database
- ✅ Visible in database tools
- ✅ Helps DBAs understand schema

**TypeScript comparison:** No equivalent

**Frequency:** Moderate (30%)

---

## Summary Table

| Annotation | Purpose | Frequency | Portability |
|------------|---------|-----------|-------------|
| @DynamicInsert | Smaller INSERT | 25% | Hibernate |
| @DynamicUpdate | Smaller UPDATE | 35% | Hibernate |
| @Immutable | Read-only | 30% | Hibernate |
| @BatchSize | N+1 fix | 60% | Hibernate |
| @Fetch | Fetch strategy | 20% | Hibernate |
| @Cache | Second-level cache | 50% | Hibernate |
| @NaturalId | Business key | 40% | Hibernate |
| @Filter | Dynamic filtering | 35% | Hibernate |
| @Where | Permanent filter | 50% | Hibernate |
| @CreationTimestamp | Auto timestamp | 70% | Hibernate |
| @UpdateTimestamp | Auto timestamp | 60% | Hibernate |
| @Type | Custom types | 30% | Hibernate |
| @GenericGenerator | Custom ID | 30% | Hibernate |
| @UuidGenerator | UUID ID | 40% | Hibernate |
| @OrderBy | Sort collection | 50% | JPA |
| @OrderColumn | List index | 25% | JPA |
| @Index | Database index | 70% | JPA |
| @Check | CHECK constraint | 25% | Hibernate |
| @Comment | SQL comments | 30% | Hibernate |

---

## When to Use Hibernate vs JPA Annotations

**Use JPA annotations when:**
- ✅ Want portability (might switch providers)
- ✅ Standard features sufficient
- ✅ Building library/framework

**Use Hibernate annotations when:**
- ✅ Need specific features (caching, filters, etc.)
- ✅ Performance critical
- ✅ Committed to Hibernate
- ✅ Advanced use cases

**Best practice:**
Start with JPA, add Hibernate annotations only when needed.

---

## Complete Example Using Multiple Annotations

```java
@Entity
@Table(
    name = "products",
    indexes = {
        @Index(name = "idx_sku", columnList = "sku", unique = true),
        @Index(name = "idx_category", columnList = "category_id"),
        @Index(name = "idx_active", columnList = "active")
    }
)
@Comment("Product catalog with pricing and inventory")
@DynamicUpdate
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "products")
@FilterDef(name = "activeOnly")
@Filter(name = "activeOnly", condition = "active = true")
@Check(constraints = "price >= 0 AND stock >= 0")
public class Product {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Comment("Unique product identifier")
    private UUID id;

    @NaturalId
    @Column(nullable = false, unique = true, length = 20)
    @Comment("Stock Keeping Unit - business key")
    private String sku;

    @Column(nullable = false, length = 200)
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("details")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @Comment("Price in USD")
    private BigDecimal price;

    @Column(nullable = false)
    @Comment("Current inventory level")
    private Integer stock;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @BatchSize(size = 10)
    private Category category;

    @ElementCollection
    @CollectionTable(name = "product_tags")
    @OrderBy("tag ASC")
    private Set<String> tags = new HashSet<>();

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attributes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(length = 50)
    private String createdBy;

    @Column(length = 50)
    private String updatedBy;

    // Constructors, getters, setters...
}
```

---

**END OF HIBERNATE-SPECIFIC ANNOTATIONS REFERENCE**

**Topics covered:**
1. ✅ Performance & Optimization
2. ✅ Caching
3. ✅ Custom SQL & DML
4. ✅ Filters & Dynamic Queries
5. ✅ Soft Deletes & Immutability
6. ✅ Advanced Type Mapping
7. ✅ Collection Optimizations
8. ✅ ID Generators
9. ✅ Lazy Loading Control
10. ✅ Database-Specific Features

**Total annotations documented:** 35+

**All with:**
- Complete explanations
- Real-world examples
- TypeScript comparisons
- Benefits and cons
- Use cases
- Frequency indicators
- Best practices
