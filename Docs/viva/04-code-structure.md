# 4. Code Structure Walk-Through

This is the order in which to take the panel through the codebase. Start at the top and follow the data flow downward.

---

## Project Layout

```
Online-Toy-Store/
├── pom.xml                          ← Maven build, dependencies
├── src/main/java/com/toystore/
│   ├── model/                       ← Domain classes (OOP lives here)
│   │   ├── Toy.java                 ← Abstract parent
│   │   ├── ElectronicToy.java
│   │   ├── EducationalToy.java
│   │   ├── SoftToy.java
│   │   └── ToyFactory.java          ← Builds the right subclass
│   ├── dao/
│   │   └── ToyDAO.java              ← All CRUD methods
│   ├── util/
│   │   ├── FileHandler.java         ← Raw file I/O
│   │   └── AppInitializer.java      ← Startup listener
│   └── servlet/
│       ├── AddToyServlet.java       ← /add-toy
│       ├── ListToysServlet.java     ← /toys
│       ├── EditToyServlet.java      ← /edit-toy
│       └── DeleteToyServlet.java    ← /delete-toy
└── src/main/webapp/
    ├── index.jsp                    ← Landing page
    ├── css/style.css
    └── WEB-INF/
        ├── web.xml
        ├── data/toys.txt            ← Persistent storage
        └── views/
            ├── add-toy.jsp
            ├── toy-list.jsp
            ├── edit-toy.jsp
            └── footer.jspf          ← Shared footer fragment
```

---

## File-by-File Tour (in walking order)

### 1. `Toy.java` — start here
- Abstract parent class. Five private fields, getters/setters, three abstract methods (`getCategory`, `calculateDiscount`, `getExtraField`).
- The concrete `toFileLine()` method shows the **template method** idea — it produces a full file line by calling the abstract methods, so each subclass automatically knows how to serialize itself.

### 2. The three subclasses
- Identical structure: `extends Toy`, one extra field, three method overrides.
- Use them to demonstrate inheritance + polymorphism. Each `calculateDiscount` returns a different percentage.

### 3. `ToyFactory.java`
- Two static methods: `create()` from form values and `fromFileLine()` from a file line.
- Centralizes the type-string-to-subclass mapping. **Only place** in the codebase that knows about all three concrete classes — adding a fourth toy type would mean editing this file plus adding a new subclass.

### 4. `FileHandler.java`
- Plain file I/O — no domain knowledge. Three operations: `readAllLines`, `appendLine`, `writeAllLines`.
- Auto-creates the file and parent folder. The `forContext` helper resolves the data file inside the deployed webapp at `WEB-INF/data/toys.txt`.

### 5. `ToyDAO.java`
- Sits between servlets and the file. Owns all six CRUD methods plus `nextId` (suggests the next sequential ID).
- Internally calls `FileHandler` for I/O and `ToyFactory` for parsing.
- Update and Delete read everything, modify in memory, then write everything back — simple and reliable.

### 6. `AppInitializer.java`
- A `@WebListener` that runs once when Tomcat starts the webapp.
- Builds a single `ToyDAO` and stores it in `ServletContext` under the key `"toyDao"`.
- Every servlet pulls the same instance from there — no need for each servlet to re-create its own DAO.

### 7. The four servlets
- All thin. They:
  1. Pull the DAO from `ServletContext`.
  2. Read parameters from the request.
  3. Call one DAO method.
  4. Forward to a JSP or redirect to another URL.
- `AddToyServlet` and `EditToyServlet` also do form validation and re-display the form with errors when needed.

### 8. The JSPs
- `index.jsp` — landing page, links to the catalog and add-form.
- `add-toy.jsp` — Bootstrap form, JavaScript switches the "extra" field label by category.
- `toy-list.jsp` — the catalog table; uses JSTL `<c:forEach>`, `<c:choose>`, and `<fmt:formatNumber>`.
- `edit-toy.jsp` — pre-populated form, ID and category disabled.
- `footer.jspf` — included on every page via `<%@ include … %>`.

### 9. `pom.xml`
- Java 17, packaging `war`.
- Four dependencies: Servlet API 6.0, JSP API 3.1, JSTL API 3.0, JSTL implementation 3.0.1 — all matching Tomcat 10.1.

### 10. `web.xml`
- Tiny: just a `display-name` and a `welcome-file-list` pointing to `index.jsp`.
- The four servlets are mapped via `@WebServlet` annotations — no XML mapping needed.

---

## Where Each Rubric Item Lives

| Rubric | Show this |
|---|---|
| CRUD | The four servlets in `com.toystore.servlet` and the four DAO methods in `ToyDAO` |
| OOP | `model/Toy.java` (abstract) and any subclass (e.g. `ElectronicToy.java`) |
| File handling | `FileHandler.java` + `ToyDAO.update` / `ToyDAO.delete` (rewrite-the-file logic) |
| UI | Open `toy-list.jsp` in a browser, then show `add-toy.jsp` with the JS that switches the extra-field label |
| File storage | `WEB-INF/data/toys.txt` — show that each line maps to one toy object |
