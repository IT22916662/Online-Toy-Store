# Online Toy Store - Project TODO

**Component:** Toy Management System
**Storage:** File-based (`data/toys.txt`)
**Backend:** JSP + Servlets (Maven + Apache Tomcat)
**Frontend:** HTML/CSS/JS + Bootstrap 5
**Java Version:** 17 / Jakarta Servlet 5.0

---

## Stage Plan (with commit checkpoints)

### Stage 1: Project Setup ✅
- [x] Create Maven webapp directory structure
- [x] Add `pom.xml` with Servlet + JSP + JSTL dependencies
- [x] Add `web.xml` deployment descriptor
- [x] Create `index.jsp` landing page
- [x] **COMMIT 1:** `Initial Maven + JSP/Servlet project setup`

### Stage 2: OOP Model Classes ✅
- [x] Create abstract `Toy` class (encapsulation, abstract methods)
- [x] Create `ElectronicToy` subclass
- [x] Create `EducationalToy` subclass
- [x] Create `SoftToy` subclass
- [x] Create `ToyFactory` for type-safe construction and file parsing
- [ ] **COMMIT 2:** `Add Toy model with inheritance and polymorphism`

### Stage 3: File Handling Utility
- [ ] Create `FileHandler` utility for read/write to `toys.txt`
- [ ] Create `ToyDAO` (Data Access Object) for CRUD
- [ ] **COMMIT 3:** `Add file handling utility and ToyDAO`

### Stage 4: CREATE Operation
- [ ] `AddToyServlet` to handle POST
- [ ] `add-toy.jsp` form
- [ ] **COMMIT 4:** `Implement Create operation (Add new toy)`

### Stage 5: READ Operation
- [ ] `ListToysServlet` to fetch all toys
- [ ] `SearchToyServlet` to search by name/category
- [ ] `toy-list.jsp` display page
- [ ] **COMMIT 5:** `Implement Read operation (List and search toys)`

### Stage 6: UPDATE Operation
- [ ] `EditToyServlet` for edit form
- [ ] `UpdateToyServlet` to apply changes
- [ ] `edit-toy.jsp` form
- [ ] **COMMIT 6:** `Implement Update operation (Edit toy details)`

### Stage 7: DELETE Operation
- [ ] `DeleteToyServlet` to remove toy
- [ ] Confirmation flow on toy list
- [ ] **COMMIT 7:** `Implement Delete operation`

### Stage 8: Polish & Styling
- [ ] Apply Bootstrap navbar / cards / forms across all JSPs
- [ ] Add custom CSS (`style.css`)
- [ ] Add basic client-side validation
- [ ] **COMMIT 8:** `Add Bootstrap styling and improve UI`

### Stage 9: Sample Data & Testing
- [ ] Create sample `toys.txt` with seed entries
- [ ] Test all CRUD flows end-to-end
- [ ] **COMMIT 9:** `Add sample data and final testing`

### Stage 10: Documentation
- [ ] Class diagram (`Docs/class-diagram.md`)
- [ ] Final report (`Docs/final-report.md`)
- [ ] Viva preparation files
- [ ] **COMMIT 10:** `Add project documentation and viva prep`

---

## Marking Rubric Coverage

| Criterion | Marks | Where Addressed |
|---|---|---|
| CRUD Functionality | 30 | Stages 4–7 (4 operations implemented) |
| OOP Concepts | 20 | Stage 2 + servlets (encapsulation, inheritance, polymorphism, abstraction) |
| File Handling | 10 | Stage 3 (FileHandler + ToyDAO) |
| UI Design | 10 | Stage 8 (Bootstrap + custom CSS) |
| GitHub Commit History | 10 | Commits across all stages |
| Viva Performance | 10 | Stage 10 (viva prep docs) |
| Documentation | 10 | Stage 10 (class diagrams, final report) |
