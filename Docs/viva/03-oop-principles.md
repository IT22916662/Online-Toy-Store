# 3. OOP Principles in This Component

The four core OOP principles each have a clear, pointable place in the code. This is what to walk the panel through when they ask "show me your OOP".

---

## 1. Encapsulation

**Where:** `model/Toy.java`

Every field is `private`:
```java
private String id;
private String name;
private double price;
private int stock;
private String ageGroup;
```

Access is only through public getters and setters. Setters enforce invariants — for example:
```java
public void setPrice(double price) {
    if (price < 0) {
        throw new IllegalArgumentException("Price cannot be negative");
    }
    this.price = price;
}
```

**Why this is encapsulation:** the internal data is hidden, and the only way to change it is through methods we control. We can change the storage format inside the class (e.g. switch `price` to `BigDecimal` later) without breaking any caller.

---

## 2. Inheritance

**Where:** `model/ElectronicToy.java`, `EducationalToy.java`, `SoftToy.java`

Three concrete subclasses extend `Toy`:
```java
public class ElectronicToy extends Toy { … }
public class EducationalToy extends Toy { … }
public class SoftToy extends Toy { … }
```

Each subclass:
- Inherits all five common fields and their getters/setters from `Toy`
- Inherits `toFileLine()` (the file-serialization logic — written once, reused three times)
- Adds **one** category-specific field (`needsBattery`, `skillType`, or `material`)

**Why this is inheritance:** the shared structure lives once on the parent and is automatically available to each child. Adding a fourth toy type later only requires writing a new `extends Toy` class — none of the parent code is duplicated.

---

## 3. Polymorphism

**Where:** `calculateDiscount()` — declared abstract on `Toy`, overridden differently on each subclass.

```java
// In Toy.java
public abstract double calculateDiscount();

// In ElectronicToy.java
@Override
public double calculateDiscount() {
    return getPrice() * 0.10;     // 10% for electronics
}

// In EducationalToy.java
@Override
public double calculateDiscount() {
    return getPrice() * 0.15;     // 15% for educational
}

// In SoftToy.java
@Override
public double calculateDiscount() {
    return getPrice() * 0.05;     // 5% for soft toys
}
```

In the catalog JSP we just call `${t.discountedPrice}` on a `Toy` reference — at runtime Java picks the right subclass's `calculateDiscount()` based on the actual object type. **Same call, three different behaviors** — that's runtime polymorphism.

`getCategory()` and `getExtraField()` are also abstract and are overridden the same way.

---

## 4. Abstraction

**Where:** `Toy` is declared `abstract` and exposes three abstract methods:

```java
public abstract class Toy {
    public abstract String getCategory();
    public abstract double calculateDiscount();
    public abstract String getExtraField();
    …
}
```

You cannot do `new Toy(…)` — the compiler rejects it. The class defines a **contract** (every concrete toy must declare its category, its discount, and its extra field) without committing to any specific implementation.

The DAO and the servlets work entirely against the abstract `Toy` type:
```java
List<Toy> toys = dao.findAll();    // we don't care which subclass each is
for (Toy toy : toys) {
    System.out.println(toy.getDiscountedPrice());   // polymorphic call
}
```

**Why this is abstraction:** higher layers depend only on the abstract behavior, not on the concrete subclasses. Adding a new toy type does not require any change in the servlets or the DAO.

---

## Bonus — Information Hiding

The whole architecture is built around hiding implementation details:

- **Servlets** don't know how toys are stored — they only know `ToyDAO`.
- **`ToyDAO`** doesn't know what file format is used — it only knows `FileHandler` (and `ToyFactory` for parsing).
- **`FileHandler`** doesn't know what kind of objects the lines represent — it only handles lines of text.

Each layer hides what's behind it from the layer above. That's the practical payoff of encapsulation applied at the architectural level.

---

## Quick Reference for the Panel

| Principle | Class to point at | Method/field to point at |
|---|---|---|
| Encapsulation | `Toy.java` | `private double price` + `setPrice(double)` with validation |
| Inheritance | `ElectronicToy.java` | `extends Toy` line at the top |
| Polymorphism | `Toy.java` + 3 subclasses | `calculateDiscount()` — abstract on parent, overridden 3 times |
| Abstraction | `Toy.java` | `public abstract class Toy` + 3 abstract methods |
