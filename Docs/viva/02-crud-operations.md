# 2. CRUD Operations

All four CRUD operations are implemented. Each one has a dedicated servlet and a dedicated JSP view (except Delete, which doesn't need its own page — it is triggered from the catalog row).

---

## CREATE — Add a new toy

**Files:** `AddToyServlet.java`, `add-toy.jsp`

**Flow:**
1. User clicks "Add Toy" on the landing page or catalog → browser sends `GET /add-toy`.
2. `AddToyServlet.doGet` asks `ToyDAO.nextId()` for a suggested ID (e.g. `T011`) and forwards to `add-toy.jsp`.
3. User fills the form and submits → `POST /add-toy`.
4. `doPost` reads the parameters, validates non-empty fields, parses price/stock as numbers.
5. Calls `ToyFactory.create(type, id, …)` which returns the right subclass (`ElectronicToy`, `EducationalToy`, or `SoftToy`).
6. `ToyDAO.add(toy)` checks the ID is unique and appends a single line to `toys.txt`.
7. Servlet redirects to `/toys?msg=added` (Post-Redirect-Get pattern, prevents resubmission on refresh).

**Validation:**
- All fields required.
- Price and stock must be numeric and non-negative (setter throws if negative).
- Duplicate IDs are rejected.

**Special touch:** The "extra" field's label changes via JavaScript depending on the chosen category — Battery / Skill / Material — so the user always sees the right prompt.

---

## READ — List and search toys

**Files:** `ListToysServlet.java`, `toy-list.jsp`

**Flow:**
1. User visits `/toys` (optionally with `?name=…&category=…` parameters).
2. `ListToysServlet.doGet` reads the optional filters and calls `ToyDAO.search(name, category)`.
3. The DAO loads every line from `toys.txt`, parses each into the right `Toy` subclass via `ToyFactory.fromFileLine`, and filters in memory.
4. JSP renders a Bootstrap table with: ID, Name, Category, Age, Price, **Discounted Price** (this comes from `getDiscountedPrice()` which uses the polymorphic `calculateDiscount`), Stock (with badges), the category-specific extra, and Edit / Delete buttons.

**Search behaviour:** Case-insensitive substring match on name; exact-match on category (or "all" if blank).

---

## UPDATE — Edit an existing toy

**Files:** `EditToyServlet.java`, `edit-toy.jsp`

**Flow:**
1. User clicks "Edit" on a row → browser sends `GET /edit-toy?id=T001`.
2. `doGet` calls `ToyDAO.findById(id)` and forwards the toy to the JSP.
3. The JSP pre-populates every field. ID and category are shown as **disabled** inputs and resubmitted via hidden fields — they must not change because they are the toy's identity.
4. User edits and submits → `POST /edit-toy`.
5. `doPost` rebuilds the toy via `ToyFactory.create` (same validation path as Add) and calls `ToyDAO.update(toy)`.
6. The DAO loads all toys, finds the one with the matching ID, replaces it, and rewrites the entire file.
7. Redirect to `/toys?msg=updated`.

**Why ID is locked:** the ID is what identifies the toy in the data file. If it could change, links and existing references would break.

---

## DELETE — Remove a toy

**Files:** `DeleteToyServlet.java` (uses `toy-list.jsp` for the trigger)

**Flow:**
1. User clicks "Delete" on a catalog row.
2. JavaScript `confirm()` dialog asks "Delete toy T001 - Remote Control Car?". If they cancel, nothing happens.
3. Browser sends `GET /delete-toy?id=T001`.
4. `DeleteToyServlet.doGet` calls `ToyDAO.delete(id)`.
5. The DAO loads all toys, removes the matching one, and rewrites the file.
6. Redirect to `/toys?msg=deleted` (or `?msg=notfound` if the ID didn't exist — defensive, in case someone hand-types a URL).

**Why GET works for Delete:** the action is gated by a JS confirm dialog, and this is a single-user admin tool — using GET keeps the catalog markup simple. In a production app I'd use POST (or DELETE with fetch).

---

## How the file is updated for each operation

| Operation | File pattern |
|---|---|
| Create | Append one line to the end |
| Read   | Read every line, parse, filter in memory |
| Update | Read all, replace one in memory, rewrite all |
| Delete | Read all, drop one in memory, rewrite all |

Update and Delete rewrite the whole file. That's intentional — for a catalog of any reasonable size it's fast, simple, and avoids the complexity of in-place editing.
