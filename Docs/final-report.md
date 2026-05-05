# Final Report — Online Toy Store

**Module:** SE1020 Object-Oriented Programming
**Project:** Online Toy Store
**Component implemented:** Toy Management
**Storage:** File-based (`WEB-INF/data/toys.txt`)
**Stack:** Java 17, Jakarta Servlet 6.0, JSP, JSTL 3.0, Maven, Apache Tomcat 10.1, Bootstrap 5

---

## 1. Project Overview

The Online Toy Store is a web-based application that lets a store administrator manage the toy catalog. The Toy Management component covers the full lifecycle of a toy: adding new toys, browsing and searching the catalog, editing toy details, and removing discontinued toys. All data is persisted to a single text file (`toys.txt`) using Java file I/O, satisfying the assignment's "no database" constraint.

## 2. Component Description

| CRUD | Route | Description |
|---|---|---|
| Create | `POST /add-toy` | Add a new toy. The form collects category, name, price, stock, age group, and one category-specific attribute (battery / skill / material). |
| Read | `GET /toys` | Display every toy in a Bootstrap table. Supports filtering by name fragment and category. |
| Update | `POST /edit-toy` | Modify an existing toy's details. ID and category are immutable. |
| Delete | `GET /delete-toy?id=...` | Remove a toy after a JavaScript confirmation. |

## 3. Object-Oriented Design

### Encapsulation
Every field in `Toy` is `private` and exposed through public getters and setters. Setters for `price` and `stock` reject negative values, ensuring invariants are enforced at the object level rather than scattered across the codebase.

### Inheritance
`Toy` is `abstract` and three concrete subclasses extend it:
- `ElectronicToy` adds `needsBattery`
- `EducationalToy` adds `skillType`
- `SoftToy` adds `material`

The shared fields (id, name, price, stock, ageGroup) and the file-serialization logic in `toFileLine()` live once on the parent class and are reused by all subclasses.

### Polymorphism
`calculateDiscount()` is declared abstract on `Toy` and overridden on each subclass — Electronic toys get 10%, Educational 15%, Soft 5%. The catalog table calls `getDiscountedPrice()` on each `Toy` reference and the right subclass implementation runs at runtime.

### Abstraction
`Toy` defines a contract with three abstract methods (`getCategory`, `calculateDiscount`, `getExtraField`). Callers work against the abstract type without caring which subclass they actually hold.

## 4. File Handling

`FileHandler` wraps `BufferedReader` and `BufferedWriter` and exposes three operations: `readAllLines`, `appendLine`, `writeAllLines`. It auto-creates the data file and its parent directory on first use, so a fresh deployment never crashes.

`ToyDAO` consumes a `FileHandler` and provides domain-level CRUD. Update and Delete are implemented by reading every line into memory, applying the change, and rewriting the entire file — simple, correct, and acceptable for a catalog of any reasonable size.

`ToyFactory` keeps the subclass-selection logic in one place. Both the Add servlet (form → object) and `ToyDAO` (line → object) go through it. If a fourth toy type is added later, only `ToyFactory` and the new subclass need to change.

## 5. User Interface

| Page | Purpose |
|---|---|
| `index.jsp` | Landing page — hero section, quick-action buttons, three navigation cards |
| `add-toy.jsp` | Create form with category-aware "extra" field driven by JavaScript |
| `toy-list.jsp` | Catalog table with search, filter, and Edit / Delete actions per row |
| `edit-toy.jsp` | Pre-populated edit form, ID and category locked |

All pages share a Bootstrap-styled navbar and a footer fragment. The catalog highlights low stock (yellow) and out-of-stock items (red) using badges, and shows each toy's discounted price live alongside the original price.

## 6. Project Structure

```
Online-Toy-Store/
├── pom.xml
├── src/main/java/com/toystore/
│   ├── model/         (Toy, ElectronicToy, EducationalToy, SoftToy, ToyFactory)
│   ├── dao/           (ToyDAO)
│   ├── servlet/       (AddToyServlet, ListToysServlet, EditToyServlet, DeleteToyServlet)
│   └── util/          (FileHandler, AppInitializer)
├── src/main/webapp/
│   ├── index.jsp
│   ├── css/style.css
│   └── WEB-INF/
│       ├── web.xml
│       ├── data/toys.txt
│       └── views/    (add-toy.jsp, toy-list.jsp, edit-toy.jsp, footer.jspf)
└── Docs/
    ├── TODO.md
    ├── design.md
    ├── class-diagram.md
    ├── test-plan.md
    ├── final-report.md          (this file)
    ├── logs/implementation-log.md
    └── viva/                    (viva preparation pack)
```

## 7. Build and Run Instructions

1. Install JDK 17 (Eclipse Temurin) and Apache Tomcat 10.1.x.
2. Open the project in IntelliJ IDEA — the Maven import runs automatically.
3. **File → Project Structure → Project**: SDK = 17, Language level = 17.
4. **Run → Edit Configurations** → add **Tomcat Server → Local**:
   - Application server: point at the Tomcat install folder.
   - URL: `http://localhost:8080/Online-Toy-Store/`
   - Deployment tab → add `Online-Toy-Store:war exploded` with context `/Online-Toy-Store`.
5. Click the green ▶ to launch.

## 8. Git Commit History

Eleven incremental commits, one per logical stage, demonstrate progressive development:

| # | Stage | Commit Message |
|---|---|---|
| 1 | Project setup | Initial Maven + JSP/Servlet project setup |
| 2 | OOP model | Add Toy model with inheritance and polymorphism |
| 3 | Storage layer | Add file handling utility and ToyDAO |
| 4 | Create | Implement Create operation (Add new toy) |
| 5 | Read | Implement Read operation (List and search toys) |
| 6 | Update | Implement Update operation (Edit toy details) |
| 7 | Delete | Implement Delete operation |
| 8 | UI polish | Add Bootstrap styling and improve UI |
| 9 | Sample data | Add sample data and final testing |
| 10 | Documentation | Add project documentation and viva prep |

Run `git log --oneline` from the project root to see the actual hashes and dates.

## 9. Self-Assessment Against Rubric

| Criterion | Marks | How addressed |
|---|---|---|
| CRUD Functionality | 30 | All four operations implemented and tested |
| OOP Concepts | 20 | Encapsulation, inheritance, polymorphism, abstraction all visible in the model layer |
| File Handling | 10 | `FileHandler` plus `ToyDAO` use Java I/O for read, append, and rewrite |
| UI Design | 10 | Bootstrap 5, custom CSS, responsive layout, status badges, hover states |
| GitHub Commit History | 10 | 11 progressive commits with clear messages |
| Viva Performance | 10 | Viva preparation pack in `Docs/viva/` |
| Documentation | 10 | Class diagram, design doc, test plan, final report, implementation log |
