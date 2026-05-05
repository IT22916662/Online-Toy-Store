# 6. How to Present This Component to the Evaluation Panel

A practical script for the viva. Aim for **6–8 minutes** of demo + walk-through, then leave time for questions.

---

## Before You Walk In — Checklist

- [ ] Tomcat is configured and runs cleanly from IntelliJ
- [ ] `WEB-INF/data/toys.txt` has the 10 sample toys loaded
- [ ] Browser is open and ready at `http://localhost:8080/Online-Toy-Store/`
- [ ] IntelliJ project tree is expanded down to `com.toystore.model`
- [ ] `git log --oneline` works in the IntelliJ terminal (open it once before)
- [ ] You have read `Docs/viva/05-questions-and-answers.md` recently
- [ ] You know the file paths of:
  - `model/Toy.java`
  - `model/ElectronicToy.java`
  - `dao/ToyDAO.java`
  - `util/FileHandler.java`
  - `servlet/AddToyServlet.java`
  - `WEB-INF/views/toy-list.jsp`

---

## Suggested Script

### Opening (30 seconds)
> "I implemented the **Toy Management** component for the Online Toy Store. It lets an administrator add, browse, edit, and remove toys in the catalog. All four CRUD operations are working, with file-based storage."

### Live Demo (2–3 minutes)
Walk through these screens **in order**, in the browser:

1. **Landing page** — "This is the entry point. The brand is in the navbar, and the hero section gives quick access to the two main flows."
2. **Browse Catalog** — "The catalog reads every toy from `toys.txt` and shows it in a table. Notice the discounted price column — Electronic toys get 10% off, Educational 15%, Soft 5% — that's polymorphism, I'll show that in the code shortly."
3. **Search** — type "puzzle" → "case-insensitive substring match"; pick category "Educational" → "exact match on category".
4. **Add Toy** — fill the form, change the category dropdown to show the extra-field label switching. Submit. Show the new row in the catalog with the green success banner.
5. **Edit** — click Edit on the new row, change the price, save. Point out that ID and category are disabled.
6. **Delete** — click Delete on the new row, confirm. Yellow banner appears.

### Code Walk-Through (3–4 minutes)

**Step 1 — Open `model/Toy.java`** — *"This is the abstract parent. Every field is private, getters and setters control access — that's encapsulation. The setter for price rejects negatives."*

**Step 2 — Open `ElectronicToy.java` quickly, then `EducationalToy.java`** — *"All three subclasses extend `Toy`. They inherit the common fields and add one category-specific attribute. The `calculateDiscount` method is overridden differently in each — that's the polymorphism in action."*

**Step 3 — Back to `Toy.java`, point at the abstract methods** — *"`Toy` declares three abstract methods. You can't construct a `Toy` directly — that's abstraction. The class defines the contract; the subclasses provide the implementation."*

**Step 4 — Open `dao/ToyDAO.java`** — *"This is the Data Access Object. It owns all the CRUD logic. The servlets only talk to this class; they don't touch the file. If we ever switched to MySQL, only this class and `FileHandler` would change."*

**Step 5 — Open `util/FileHandler.java`** — *"The lowest layer. Three operations: read all lines, append one, rewrite all. Auto-creates the file on first use. Notice the try-with-resources — guarantees the writer closes even if an exception is thrown."*

**Step 6 — Open `WEB-INF/data/toys.txt`** — *"The persistent storage. Pipe-delimited, one toy per line. The second field is the category — that's what tells `ToyFactory` which subclass to build when reading the file."*

**Step 7 — Open `servlet/AddToyServlet.java`** — *"A representative servlet. `doGet` shows the form, `doPost` validates, builds the toy via the factory, calls `dao.add`, and redirects. Thin — no business logic here."*

### Git History (30 seconds)
Open the IntelliJ terminal, run:
```
git log --oneline
```
> "Eleven incremental commits, one per logical stage of the implementation. You can see the progression from project setup through each CRUD operation, styling, sample data, and documentation."

### Closing (30 seconds)
> "All four CRUD operations are working. The four OOP principles — encapsulation, inheritance, polymorphism, abstraction — each have a clear, pointable place in `Toy.java` and its subclasses. The layered architecture means each class has a single responsibility, which made the code easy to test and easy to evolve."

---

## What to Have Open in Two Windows

**Browser:** `http://localhost:8080/Online-Toy-Store/`

**IntelliJ:**
- Editor tabs pre-open: `Toy.java`, `ElectronicToy.java`, `ToyDAO.java`, `AddToyServlet.java`, `toys.txt`
- Project tree expanded to `com.toystore.model`
- Terminal open at the project root

---

## Tips for the Q&A

- **Don't memorize answers — understand them.** If you understand each layer's purpose, you can answer most questions on the fly.
- **Point at the code while you explain.** Hand on the screen is more convincing than words alone.
- **If you don't know something, say so honestly.** "I'm not sure, but I think it's because…" is better than guessing confidently.
- **Tie answers back to OOP whenever you can.** Even questions about file handling can be answered with "the DAO pattern hides the storage detail from the rest of the code".

## If a Question Trips You Up

Two recovery moves:
1. **"Let me show you in the code"** — opens the file you're most comfortable with, reframes the question into something concrete.
2. **"That's a good point — I made the trade-off because…"** — admits the limitation but justifies the design choice (e.g. file-based storage, GET-based delete, single-admin model).

## Common Mistakes to Avoid

- Don't claim a feature you didn't build. Stick to what's actually in the code.
- Don't skip past `Toy.java` — it's the heart of the OOP demonstration.
- Don't read straight from this script — these are bullet points to remind you, not lines to recite.
- Don't forget to show the **git log**. It's worth 10 marks on its own.

---

## One-Liner Summary

If you only have **30 seconds** to summarize:

> "I built a Java web application for managing the toy catalog of an online store. It uses an abstract `Toy` class with three subclasses to demonstrate inheritance and polymorphism, encapsulates state behind validating setters, and persists data through a DAO that writes to a plain text file. All four CRUD operations are wired up through Jakarta servlets and JSP views, with Bootstrap styling on the front."
