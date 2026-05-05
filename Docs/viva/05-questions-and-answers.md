# 5. Possible Viva Questions & Answers

Grouped by topic. Read through these the night before the viva. The answers are intentionally short — give the panel just enough, and let them ask follow-ups.

---

## A. OOP Concepts

### Q1. Explain encapsulation in your code.
All fields in `Toy` are private. The outside world reaches them only through getters and setters. The setters for `price` and `stock` validate that the value is non-negative — that means the rule "a toy cannot have a negative price" is enforced inside the class, not scattered around the codebase.

### Q2. Why did you make `Toy` abstract?
Because there is no such thing as "just a toy" in this system — every toy belongs to one of three categories, each with its own extra attribute and its own discount rule. Making `Toy` abstract forces every concrete toy to declare its category and its discount, and prevents anyone from accidentally creating a toy that has no category.

### Q3. Where does polymorphism happen?
In `calculateDiscount()`. It is declared abstract on `Toy` and overridden differently on each subclass — 10% on `ElectronicToy`, 15% on `EducationalToy`, 5% on `SoftToy`. The catalog JSP calls `getDiscountedPrice()` on a `Toy` reference, and at runtime Java picks the right subclass version. Same call, three different behaviors.

### Q4. What's the difference between abstraction and encapsulation?
- **Encapsulation** hides the internal *data* — the fields are private.
- **Abstraction** hides the internal *behavior* — `Toy` declares what every toy must do (`calculateDiscount`, `getCategory`, `getExtraField`) without specifying how. The DAO and servlets work against the abstract type without caring which subclass they're holding.

### Q5. Could you achieve the same thing with an interface instead of an abstract class?
Partially. An interface would give us the abstract methods, but it can't hold the shared fields (`id`, `name`, `price`, etc.) or the concrete `toFileLine()` logic. An abstract class is the right tool here because we have **shared state and shared behavior** in addition to a shared contract.

### Q6. What is method overriding?
Re-implementing a method declared in the parent class on a child class. In our code, every subclass overrides `getCategory`, `calculateDiscount`, and `getExtraField`. The `@Override` annotation lets the compiler check that the signature matches.

### Q7. What's the difference between method overloading and method overriding?
- **Overloading** — same method name, different parameter list, in the same class. Resolved at compile time.
- **Overriding** — same method signature, in a subclass, replacing the parent's version. Resolved at runtime.

---

## B. File Handling

### Q8. Why use a text file instead of a database?
The assignment specifies file read/write OR a database, and a text file keeps the project self-contained — no MySQL setup needed for the viva demo. The DAO pattern means we could replace `FileHandler` with a JDBC-based one tomorrow without changing anything else.

### Q9. How do you write to the file?
`FileHandler` uses `BufferedWriter`. Two methods: `appendLine` opens the file in append mode and adds one line; `writeAllLines` opens in overwrite mode and rewrites the whole file. Both are inside try-with-resources blocks so the writer always closes.

### Q10. Why do Update and Delete rewrite the whole file?
Because text files don't support in-place line edits — you can't change one line without affecting the bytes after it. Reading everything in, modifying in memory, and writing back is the simplest correct approach. For this catalog size it's instant.

### Q11. What's the data file format?
Pipe-delimited, one toy per line, seven fields:
```
T001|ELECTRONIC|Remote Control Car|2500.00|10|6+|true
```
That's: `id | type | name | price | stock | ageGroup | extraField`. The `type` tells `ToyFactory` which subclass to build.

### Q12. What happens if the file doesn't exist?
`FileHandler` creates it (and any missing parent directory) the first time it's used. Same for an empty file — the catalog just shows zero rows.

### Q13. What if two users edit at the same time?
This is a single-admin tool, so it doesn't handle concurrent writes — the last write wins. For a real multi-user system I'd add file locking with `FileChannel.tryLock()`, or move to a database that handles transactions.

---

## C. CRUD Operations

### Q14. Walk me through what happens when I click "Save" on the Add form.
1. Browser sends `POST /add-toy` with the form fields.
2. `AddToyServlet.doPost` reads the parameters and validates them.
3. It calls `ToyFactory.create(type, …)` which builds the right subclass.
4. `ToyDAO.add(toy)` checks the ID isn't a duplicate and appends one line to `toys.txt`.
5. The servlet sends a redirect to `/toys?msg=added`.
6. The browser follows the redirect, the catalog re-loads, and a green success banner shows.

### Q15. Why redirect after a POST instead of forwarding?
Post-Redirect-Get pattern. If we forwarded, the URL bar would still say `/add-toy` and a refresh would re-submit the form. Redirecting moves the browser to a new URL so refreshing only re-reads the catalog.

### Q16. How does the search work?
Both filters are optional. The DAO loads all toys, then keeps only those whose name contains the search string (case-insensitive) and whose category matches the dropdown. No name → match all names; no category → match all categories.

### Q17. Why can't I edit the toy ID?
The ID is the toy's identity — it's how we find the row in the file. If it changed, internal references would break. The Edit form shows it as a disabled input and posts it back unchanged via a hidden field.

### Q18. Why does Delete use GET instead of POST?
For simplicity in a single-admin tool. The action is gated by a JavaScript `confirm()` dialog before the link is followed. In a production app I'd switch to a POST form to follow REST best practice.

---

## D. Architecture and Design

### Q19. What is the DAO pattern?
DAO stands for Data Access Object. It's a class that encapsulates all the storage operations for one type of object. The rest of the application talks to the DAO instead of touching the file directly. That way, switching from files to a database means changing one class.

### Q20. Why a separate `ToyFactory`?
To keep the type-to-subclass mapping in **one** place. Both the Add servlet and the file reader need to build the right subclass — without the factory, that `switch` statement would be duplicated in two files. A future fourth toy type only requires adding a case in `ToyFactory` plus the new subclass.

### Q21. What is `AppInitializer` for?
It's a `ServletContextListener` annotated with `@WebListener`. Tomcat calls its `contextInitialized` once when the webapp starts. We build a single `ToyDAO` there and store it in `ServletContext`. Every servlet shares that instance — no duplicated setup, and the file path is resolved exactly once.

### Q22. What's a Servlet?
A Java class that handles HTTP requests. It extends `HttpServlet` and overrides `doGet` and/or `doPost`. Tomcat instantiates it once per URL pattern and routes incoming requests to the matching method.

### Q23. What does `@WebServlet("/add-toy")` do?
It tells Tomcat: "any request for `/add-toy` should be handled by this class." This replaces the older XML-based mapping in `web.xml`.

### Q24. Why are the JSPs under `WEB-INF/views/` instead of directly under `webapp/`?
Files inside `WEB-INF` cannot be accessed directly by the browser. Putting JSPs there forces every request to go through a servlet first — the servlet prepares the data and forwards to the JSP. That stops users from bypassing validation by hitting the JSP directly.

### Q25. What is a Forward vs a Redirect?
- **Forward** — server-side. The same request continues, just routed to a different resource. URL bar doesn't change.
- **Redirect** — client-side. Server returns an HTTP 302 with a new URL; browser issues a fresh request. URL bar changes.

We forward to JSPs (server-side render) and redirect after successful POSTs (Post-Redirect-Get).

---

## E. JSPs and Front-End

### Q26. What is JSTL?
Jakarta Standard Tag Library. It provides tags like `<c:forEach>`, `<c:if>`, `<c:choose>` that replace Java scriptlets in JSPs. Cleaner and easier to read.

### Q27. What does `${pageContext.request.contextPath}` mean?
It's the application's deployment path — for our app it's `/Online-Toy-Store`. Using it on every link means the URLs work no matter what context the app is deployed under.

### Q28. How does the Bootstrap styling work?
Bootstrap 5 is loaded via CDN in each JSP's `<head>`. The custom `style.css` adds project-specific touches — gradient background, card hover lift, branded footer, blue table header.

### Q29. Why does the "extra" field label change on the Add form?
A small JavaScript listener watches the category dropdown and rewrites the label and placeholder text. So when the user picks "Electronic Toy" they see "Needs Battery? (true / false)", and switching to "Soft Toy" turns that into "Material".

---

## F. General

### Q30. What is Maven and why do we use it?
A Java build tool. It downloads dependencies from Maven Central, compiles the code, and packages a WAR file. The `pom.xml` lists what we need; Maven handles the rest.

### Q31. Why Tomcat 10.1?
Tomcat 10.1 supports Jakarta EE 10 — that's the version that uses the `jakarta.*` namespace (we import `jakarta.servlet.http.HttpServlet`, not `javax.servlet.http.HttpServlet`).

### Q32. What was the hardest part?
Getting the JSTL version right. I first used JSTL 2.0 which works on Tomcat 10.0 but not 10.1 — Tomcat 10.1 needs JSTL 3.0 because it ships with Jakarta EE 10. The error message was "Unable to get JAR resource [/WEB-INF/views/jakarta.tags.core] containing TLD". Bumping Servlet to 6.0 and JSTL to 3.0 fixed it.

### Q33. If you had more time, what would you add?
- Replace text-file storage with MySQL
- Add a public "shop" view for customers (today only the admin view exists)
- File upload for toy images
- Pagination on the catalog when the list gets long

### Q34. What did you learn?
The clearest takeaway was how much **the layered design pays off**. Because the servlet doesn't talk to the file directly, fixing the JSTL version problem required zero changes to my domain model or my CRUD logic. Each layer absorbed its own change.
