# Online Toy Store - Toy Management Component Design

## Component Overview
The Toy Management module is the core component of the Online Toy Store. It allows administrators to add new toys to the catalog, browse and search the collection, edit toy details, and remove toys that are no longer available. All toy data is persisted in a plain-text file (`data/toys.txt`).

## Architecture (3-Layer)

```
+--------------------+     +----------------------+     +-----------------+
|  JSP Views (UI)    |---->|  Servlets (Control)  |---->|   ToyDAO        |
|  add-toy.jsp       |     |  AddToyServlet       |     |   (CRUD logic)  |
|  toy-list.jsp      |     |  ListToysServlet     |     +--------+--------+
|  edit-toy.jsp      |     |  EditToyServlet      |              |
|                    |     |  UpdateToyServlet    |              v
|                    |     |  DeleteToyServlet    |     +-----------------+
|                    |     |  SearchToyServlet    |     |  FileHandler    |
+--------------------+     +----------------------+     |  (file I/O)     |
                                                        +--------+--------+
                                                                 |
                                                                 v
                                                        +-----------------+
                                                        |  data/toys.txt  |
                                                        +-----------------+
```

## Class Hierarchy (OOP)

```
        Toy (abstract)
        ├─ id, name, price, stock, ageGroup  (private — encapsulation)
        ├─ getters / setters
        ├─ abstract String getCategory()
        ├─ abstract double calculateDiscount()
        └─ String toFileLine() / static fromFileLine()
                |
   +------------+------------+
   |            |            |
ElectronicToy EducationalToy SoftToy
(needsBattery)(skillType)    (material)
```

- **Encapsulation**: All fields private; access via getters/setters with validation.
- **Inheritance**: Three concrete subclasses extend `Toy`.
- **Polymorphism**: `getCategory()` and `calculateDiscount()` are overridden differently in each subclass.
- **Abstraction**: `Toy` is abstract — defines the contract without implementing category-specific behavior.

## Data File Format (`data/toys.txt`)
Pipe-delimited, one toy per line:
```
id|type|name|price|stock|ageGroup|extraField
T001|ELECTRONIC|Remote Car|2500.00|10|6+|true
T002|EDUCATIONAL|Math Puzzle|800.00|25|4+|Logic
T003|SOFT|Teddy Bear|1500.00|15|0+|Cotton
```
- `type` determines which subclass to instantiate when reading.
- `extraField` is the subclass-specific attribute (battery flag, skill type, or material).

## CRUD Operations

| Operation | Servlet | View | Behavior |
|---|---|---|---|
| Create | `AddToyServlet` | `add-toy.jsp` | Validates input, builds correct subclass, appends to file |
| Read | `ListToysServlet`, `SearchToyServlet` | `toy-list.jsp` | Reads file, displays in Bootstrap table; filter by name/category |
| Update | `EditToyServlet` + `UpdateToyServlet` | `edit-toy.jsp` | Loads toy by ID, allows edit, rewrites file |
| Delete | `DeleteToyServlet` | confirmation on `toy-list.jsp` | Removes line by ID, rewrites file |

## UI Pages (≥3 required)
1. `index.jsp` — landing / dashboard
2. `add-toy.jsp` — Create form
3. `toy-list.jsp` — Read + Search + Delete entry point
4. `edit-toy.jsp` — Update form

## Tech Stack
- **Build**: Maven (WAR packaging)
- **Server**: Apache Tomcat 10.x
- **Java**: 17
- **Servlet API**: Jakarta Servlet 5.0
- **View**: JSP + JSTL 2.0
- **Frontend**: Bootstrap 5 (CDN)
