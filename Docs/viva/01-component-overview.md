# 1. Component Overview

## What I Built

The **Toy Management** component for an Online Toy Store. It is the part of the system a store administrator uses to maintain the catalog: adding new toys, browsing what's already there, editing details, and removing toys that are no longer sold.

## Why this component

A toy store catalog is the natural "core" component of the system — without it nothing else makes sense. It also gave the strongest opportunity to demonstrate OOP, because real toys naturally split into categories (electronic, educational, soft) that share core properties but have category-specific attributes and behavior.

## High-Level Picture

```
User (in browser)
      │
      ▼
JSP Pages (UI)  ── add-toy.jsp · toy-list.jsp · edit-toy.jsp · index.jsp
      │
      ▼
Servlets        ── AddToy · ListToys · EditToy · DeleteToy
      │
      ▼
DAO Layer       ── ToyDAO  (CRUD logic)
      │
      ▼
Util Layer      ── FileHandler · ToyFactory
      │
      ▼
WEB-INF/data/toys.txt
```

## What the User Can Do

| Action | Page | Behind the Scenes |
|---|---|---|
| Add a new toy | `/add-toy` | `AddToyServlet` → `ToyFactory.create` → `ToyDAO.add` |
| Browse the catalog | `/toys` | `ListToysServlet` → `ToyDAO.search` |
| Search by name / category | `/toys?name=…&category=…` | Same servlet, filtered list |
| Edit a toy | `/edit-toy?id=…` | `EditToyServlet` (GET shows form, POST saves) |
| Delete a toy | `/delete-toy?id=…` | `DeleteToyServlet.delete` |

## Tech Stack (one-line each)

- **Java 17** — language
- **Jakarta Servlet 6.0 + JSP** — server-side request handling and views
- **JSTL 3.0** — tag library for clean conditional/loop logic in JSPs
- **Apache Tomcat 10.1** — servlet container
- **Maven** — build and dependency management
- **Bootstrap 5** — UI styling
- **Plain text file** — data storage (`toys.txt`)

## Why a Three-Layer Design

Splitting the code into Servlet → DAO → File utility means each class has one job:
- Servlets only handle HTTP (parse parameters, pick the right view)
- DAO knows how to read and write toys
- FileHandler only knows how to talk to the file

If we ever switched from text files to MySQL, only `FileHandler` would need to change — the servlets and JSPs would stay the same. That separation is what makes the component clean and maintainable.
