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

## Stage 4: Create Operation

**Goal:** Add a new toy to the catalog through a Bootstrap form, validated server-side.

**Actions:**
- `AddToyServlet.java` mapped to `/add-toy` via `@WebServlet`. `doGet` shows the form (with a suggested next ID from `ToyDAO.nextId()`); `doPost` reads the form parameters, builds the right subclass through `ToyFactory.create`, and calls `ToyDAO.add`. On success it redirects to `/toys?msg=added` (Post/Redirect/Get pattern, prevents double submission). On any validation problem it forwards back to the form with an error banner and the user's previously typed values preserved.
- `add-toy.jsp` placed under `WEB-INF/views/` so it cannot be accessed directly without the servlet. Bootstrap-styled card layout with a category dropdown (Electronic / Educational / Soft). The "extra" input field's label and hint text change automatically via JavaScript depending on the chosen category — battery flag, skill type, or material.
- Updated `index.jsp` buttons to use `${pageContext.request.contextPath}` so links work regardless of the deployment context name.

**Commit message:** `Implement Create operation (Add new toy)`

**Note:** This commit also bumps Servlet API to 6.0, JSP API to 3.1, and JSTL to 3.0 — required for Tomcat 10.1.x. The earlier 2.0/5.0 versions threw `Unable to get JAR resource [/WEB-INF/views/jakarta.tags.core]` because JSTL 2.0's TLDs are not registered under Jakarta EE 10.

---

## Stage 5: Read Operation

**Goal:** Show every toy in a Bootstrap table and let the user filter by name and/or category.

**Actions:**
- `ListToysServlet.java` mapped to `/toys`. Reads optional `name` and `category` query parameters and delegates filtering to `ToyDAO.search`. Stores the filtered list, the original query values (so the form remembers them) and any `msg` parameter (`added` / `updated` / `deleted`) in request scope.
- `toy-list.jsp` placed under `WEB-INF/views/`. Renders a search card, a status alert for the `msg` parameter, and a Bootstrap-styled table that includes the discounted price (proves polymorphism — the discount comes from each subclass's overridden `calculateDiscount`). Stock cells show coloured badges: red for 0, yellow for less than 5, plain number otherwise. Each row carries Edit and Delete action buttons (the Delete uses a JavaScript confirm).

**Commit message:** `Implement Read operation (List and search toys)`

---

## Stage 6: Update Operation

**Goal:** Allow modifying existing toys.

**Actions:**
- `EditToyServlet.java` mapped to `/edit-toy`. `doGet` looks up the toy by `id` query parameter via `ToyDAO.findById`; if missing, redirects back to the list. Otherwise it forwards to the JSP with the existing `Toy` in request scope. `doPost` rebuilds the toy object via `ToyFactory.create` (so the same validation path as Add is used), calls `ToyDAO.update`, and redirects with `?msg=updated`.
- `edit-toy.jsp` placed under `WEB-INF/views/`. The toy ID and category are shown as disabled inputs and posted back via hidden fields — they cannot change. The other fields are pre-populated from the existing toy, but if a validation error occurred, the user's last typed values are kept instead. The "extra" field's label switches based on the category (Battery / Skill / Material) just like the Add form.

**Commit message:** `Implement Update operation (Edit toy details)`

---

## Stage 7: Delete Operation

**Goal:** Remove a toy from the catalog with confirmation.

**Actions:**
- `DeleteToyServlet.java` mapped to `/delete-toy`. Reads the `id` query parameter, calls `ToyDAO.delete`, and redirects to `/toys?msg=deleted` (or `?msg=notfound` if no row matched). Single GET handler keeps it simple — the catalog already guards the action with a JavaScript `confirm()` dialog before navigating.
- Added a `notfound` alert branch in `toy-list.jsp` for the unlikely case where the ID does not match any record.

**All four CRUD operations are now in place.**

**Commit message:** `Implement Delete operation`

---

## Stage 8: UI Polish

**Goal:** Lift the visual quality so the UI Design rubric criterion (10 marks) is comfortably hit.

**Actions:**
- Expanded `style.css`: subtle gradient body background, rounded cards with hover lift, blue header on tables with light hover row tint, focused form inputs glowing in primary blue, branded dark-blue footer.
- Created a reusable `footer.jspf` fragment listing project name and module — included on every page (`index`, `add-toy`, `toy-list`, `edit-toy`) so the layout is consistent.
- Added a 🧸 emoji prefix to the navbar brand via CSS pseudo-element.
- Reworked the landing-page hero into a white shadowed jumbotron with two prominent CTA buttons (Browse Catalog / Add New Toy).

**Commit message:** `Add Bootstrap styling and improve UI`

---

## Stage 9: Sample Data and Testing

**Goal:** Provide demo-ready content and a documented test plan for the viva.

**Actions:**
- Seeded `WEB-INF/data/toys.txt` with 10 toys covering all three subclasses, including edge cases: a 0-stock entry (`T009`) to exercise the "Out of stock" badge and a stock=3 entry (`T008`) for the "low" badge.
- Wrote `Docs/test-plan.md` — a numbered checklist that walks the evaluator (or me before the viva) through each of the 8 functional areas: startup, list, search, create, update, delete, file persistence, UI/navigation. Each row has a step and an expected outcome.

**Commit message:** `Add sample data and final testing`

---

## Stage 10: Documentation and Viva Prep

**Goal:** Produce every artefact the viva and the rubric ask for.

**Actions:**
- `Docs/class-diagram.md` — UML in Mermaid format, plain-text hierarchy, and a layered architecture diagram showing data flow from JSPs down to `toys.txt`.
- `Docs/final-report.md` — overview, component description, OOP analysis, file handling write-up, project structure, build instructions, and the commit history table.
- `Docs/viva/` — six-file preparation pack:
  1. `01-component-overview.md` — the elevator pitch
  2. `02-crud-operations.md` — flow walk-through of each CRUD operation
  3. `03-oop-principles.md` — exactly which class and method to point at for each principle
  4. `04-code-structure.md` — file-by-file tour in the order to walk the panel through
  5. `05-questions-and-answers.md` — 34 likely viva questions with concise answers
  6. `06-presenting-to-panel.md` — opening / demo / code walk script for the day
- `Docs/viva/README.md` — index that lists which file to read when.

**Component complete.**

**Commit message:** `Add project documentation and viva prep`

---
