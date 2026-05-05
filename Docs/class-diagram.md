# Class Diagram — Toy Management Component

## Overview

The component is organised into four packages, each with one job:

| Package | Responsibility |
|---|---|
| `com.toystore.model` | Domain classes — `Toy` and its subclasses |
| `com.toystore.dao` | Data Access Object — CRUD operations |
| `com.toystore.util` | File I/O helpers and the startup listener |
| `com.toystore.servlet` | HTTP entry points (one per CRUD action) |

---

## UML Class Diagram (Mermaid)

```mermaid
classDiagram
    class Toy {
        <<abstract>>
        -String id
        -String name
        -double price
        -int stock
        -String ageGroup
        +getId() String
        +setId(String)
        +getName() String
        +setName(String)
        +getPrice() double
        +setPrice(double)
        +getStock() int
        +setStock(int)
        +getAgeGroup() String
        +setAgeGroup(String)
        +getDiscountedPrice() double
        +toFileLine() String
        +getCategory()* String
        +calculateDiscount()* double
        +getExtraField()* String
    }

    class ElectronicToy {
        -boolean needsBattery
        +getCategory() String
        +calculateDiscount() double
        +getExtraField() String
    }

    class EducationalToy {
        -String skillType
        +getCategory() String
        +calculateDiscount() double
        +getExtraField() String
    }

    class SoftToy {
        -String material
        +getCategory() String
        +calculateDiscount() double
        +getExtraField() String
    }

    class ToyFactory {
        +create(type, id, name, price, stock, age, extra)$ Toy
        +fromFileLine(line)$ Toy
    }

    class ToyDAO {
        -FileHandler fileHandler
        +add(Toy)
        +findAll() List~Toy~
        +findById(String) Optional~Toy~
        +search(name, category) List~Toy~
        +update(Toy) boolean
        +delete(String) boolean
        +nextId() String
    }

    class FileHandler {
        -String filePath
        +readAllLines() List~String~
        +appendLine(String)
        +writeAllLines(List~String~)
    }

    class AppInitializer {
        +contextInitialized(ServletContextEvent)
    }

    class AddToyServlet
    class ListToysServlet
    class EditToyServlet
    class DeleteToyServlet

    Toy <|-- ElectronicToy
    Toy <|-- EducationalToy
    Toy <|-- SoftToy

    ToyFactory ..> Toy : creates
    ToyDAO --> FileHandler : uses
    ToyDAO ..> ToyFactory : uses
    AppInitializer --> ToyDAO : creates
    AppInitializer --> FileHandler : creates

    AddToyServlet ..> ToyDAO : uses
    AddToyServlet ..> ToyFactory : uses
    ListToysServlet ..> ToyDAO : uses
    EditToyServlet ..> ToyDAO : uses
    EditToyServlet ..> ToyFactory : uses
    DeleteToyServlet ..> ToyDAO : uses
```

---

## Plain Text Hierarchy

```
                       ┌─────────────────┐
                       │  Toy (abstract) │
                       │─────────────────│
                       │ - id            │
                       │ - name          │
                       │ - price         │
                       │ - stock         │
                       │ - ageGroup      │
                       │─────────────────│
                       │ + getters/setters│
                       │ + toFileLine()  │
                       │ # getCategory() │   ← abstract
                       │ # calculateDis… │   ← abstract
                       │ # getExtraField │   ← abstract
                       └────────┬────────┘
                                │
            ┌───────────────────┼───────────────────┐
            │                   │                   │
   ┌────────▼────────┐ ┌────────▼────────┐ ┌────────▼────────┐
   │ ElectronicToy   │ │ EducationalToy  │ │    SoftToy      │
   │─────────────────│ │─────────────────│ │─────────────────│
   │ - needsBattery  │ │ - skillType     │ │ - material      │
   │─────────────────│ │─────────────────│ │─────────────────│
   │ +getCategory()  │ │ +getCategory()  │ │ +getCategory()  │
   │ +calcDiscount() │ │ +calcDiscount() │ │ +calcDiscount() │
   │   = price * .10 │ │   = price * .15 │ │   = price * .05 │
   └─────────────────┘ └─────────────────┘ └─────────────────┘
```

## Layered Architecture

```
                 [ JSP Views ]
       index.jsp · add-toy.jsp · toy-list.jsp · edit-toy.jsp
                       │
                       ▼
                 [ Servlets ]
   AddToy · ListToys · EditToy · DeleteToy   (HTTP entry points)
                       │
                       ▼
                 [ DAO Layer ]
                    ToyDAO            (CRUD logic)
                       │
                       ▼
                 [ Util Layer ]
            FileHandler · ToyFactory  (I/O + parsing)
                       │
                       ▼
              WEB-INF/data/toys.txt
```
