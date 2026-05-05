# Implementation Log

Running log of work completed on the Online Toy Store - Toy Management component.

---

## Stage 1: Project Setup

**Goal:** Establish a working Maven + JSP/Servlet skeleton that deploys to Tomcat.

**Actions:**
- Created standard Maven webapp directory layout:
  - `src/main/java/com/toystore/{model,servlet,dao,util}`
  - `src/main/webapp/WEB-INF/views`
  - `src/main/webapp/{css,js}`
  - `data/` (for `toys.txt`)
- Created `pom.xml` with Jakarta Servlet 5.0, JSP API, JSTL 2.0
- Created `web.xml` deployment descriptor
- Created `index.jsp` landing page

**Outcome:** Project compiles and deploys; landing page reachable at `/`.

**Commit message:** `Initial Maven + JSP/Servlet project setup`

---

## Stage 2: OOP Model Classes

**Goal:** Build the toy domain model with all four OOP pillars in place before any persistence or servlet code is written.

**Actions:**
- `Toy.java` — abstract parent class. Holds the shared fields (`id`, `name`, `price`, `stock`, `ageGroup`) as `private` with public getters/setters → **encapsulation**. Setters for price and stock validate against negative values. Declares three abstract methods (`getCategory`, `calculateDiscount`, `getExtraField`) → **abstraction**. Provides a concrete `toFileLine()` that depends on the abstract methods → template method pattern.
- `ElectronicToy.java` — `extends Toy`, adds `needsBattery`, applies a 10% discount.
- `EducationalToy.java` — `extends Toy`, adds `skillType`, applies a 15% discount.
- `SoftToy.java` — `extends Toy`, adds `material`, applies a 5% discount.
- All three subclasses override `getCategory`, `calculateDiscount`, and `getExtraField` → **inheritance + polymorphism**.
- `ToyFactory.java` — single place that maps a category string to the correct subclass. Used both by the Add servlet (form → object) and by the file reader (line → object), keeping subclass selection logic out of the rest of the code.

**OOP coverage check:**
- Encapsulation ✓ (private fields, validating setters)
- Inheritance ✓ (three subclasses extend `Toy`)
- Polymorphism ✓ (each subclass overrides `calculateDiscount` differently)
- Abstraction ✓ (`Toy` is abstract, defines a contract)

**Commit message:** `Add Toy model with inheritance and polymorphism`

---

## Stage 3: File Handling and DAO

**Goal:** Provide a clean storage layer so servlets never touch raw file I/O.

**Actions:**
- `FileHandler.java` — utility around `BufferedReader` / `BufferedWriter`. Three operations: `readAllLines`, `appendLine`, `writeAllLines`. Auto-creates the file (and parent folder) on first use. The static `forContext` helper resolves a path inside `WEB-INF/data` of the deployed app, so the data file lives next to the WAR rather than in the project source tree.
- `ToyDAO.java` — Data Access Object that consumes a `FileHandler` and exposes domain-level CRUD: `add`, `findAll`, `findById`, `search` (by name fragment + category), `update`, `delete`. Also provides `nextId()` which scans existing IDs and returns the next `Txxx` value so the UI can suggest one. Uses `ToyFactory` from Stage 2 to translate file lines back into the correct subclass.
- `AppInitializer.java` — `ServletContextListener` annotated with `@WebListener`. Runs once when the webapp starts, builds a single `ToyDAO`, and stores it in `ServletContext` under `DAO_KEY`. Servlets (Stages 4–7) will read from there instead of constructing their own.

**Why this layout:** the servlets, the DAO, and the file utility each have one job. Adding MySQL later would only require swapping `FileHandler` for a JDBC-backed implementation behind the same DAO.

**Commit message:** `Add file handling utility and ToyDAO`

---
