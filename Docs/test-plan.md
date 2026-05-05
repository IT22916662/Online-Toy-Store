# Manual Test Plan

End-to-end checks for the Toy Management component. Run all tests once before the viva to catch any regressions.

**Base URL:** `http://localhost:8080/Online-Toy-Store/`

---

## 1. Application Startup

| # | Step | Expected |
|---|---|---|
| 1.1 | Start Tomcat from IntelliJ | Console shows `Server startup in [xxxx] ms` and no SEVERE errors |
| 1.2 | Browser opens automatically | Landing page loads — hero section, three cards, navbar with 🧸 brand, footer |

## 2. Read — List All Toys

| # | Step | Expected |
|---|---|---|
| 2.1 | Click **Browse Catalog** | Catalog page loads at `/toys` showing 10 sample toys |
| 2.2 | Header row visible with columns: ID, Name, Category, Age, Price, Discounted, Stock, Extra, Actions | All columns present |
| 2.3 | Discounted price for `T001` (Electronic, price 2500) | `2250.00` (10% off) |
| 2.4 | Discounted price for `T003` (Educational, price 800) | `680.00` (15% off) |
| 2.5 | Discounted price for `T006` (Soft, price 1500) | `1425.00` (5% off) |
| 2.6 | Stock for `T009` (= 0) | Red `Out of stock` badge |
| 2.7 | Stock for `T008` (= 3) | Yellow `3 (low)` badge |

## 3. Read — Search and Filter

| # | Step | Expected |
|---|---|---|
| 3.1 | Type `bear` in search box → click Search | Only `T006 Teddy Bear` shown; count = 1 |
| 3.2 | Reset → choose `Educational` from category dropdown → Search | Three rows: T003, T004, T005, T009 (4 educational) |
| 3.3 | Combine: name `puzzle` + category `Educational` → Search | Two rows: T003, T009 |
| 3.4 | Click Reset | All 10 rows back |
| 3.5 | Search for `xyz123` | "No toys match your search" message shown |

## 4. Create — Add Toy

| # | Step | Expected |
|---|---|---|
| 4.1 | Click **+ Add Toy** | Add form loads with `T011` suggested as next ID |
| 4.2 | Submit empty form | Error: "All fields are required" |
| 4.3 | Type `abc` in price → submit | Error: "Price and stock must be numeric." Form keeps your typed values |
| 4.4 | Choose category `Electronic Toy` | Extra field label changes to `Needs Battery? (true / false)` |
| 4.5 | Choose category `Educational Toy` | Extra field changes to `Skill Type` |
| 4.6 | Choose category `Soft Toy` | Extra field changes to `Material` |
| 4.7 | Fill: id=`T011`, type=Soft, name=`Plush Cat`, price=`950`, stock=`8`, age=`0+`, extra=`Cotton` → Save | Redirect to catalog with green "Toy added successfully." Row visible |
| 4.8 | Try to add again with id=`T011` | Error: "Toy ID already exists: T011" |

## 5. Update — Edit Toy

| # | Step | Expected |
|---|---|---|
| 5.1 | Catalog → click **Edit** on `T011 Plush Cat` | Edit form loads with all current values, ID and Category disabled |
| 5.2 | Change Stock to `15` → Save | Redirect with green "Toy updated successfully." Catalog shows stock = 15 |
| 5.3 | Edit again — change Price to `abc` → Save | Error banner; user-typed value retained |
| 5.4 | Edit again — change Material to `Polyester` → Save | Catalog shows updated extra field |

## 6. Delete — Remove Toy

| # | Step | Expected |
|---|---|---|
| 6.1 | Catalog → click **Delete** on `T011 Plush Cat` | JS confirm dialog shows the toy id + name |
| 6.2 | Click **Cancel** | No change |
| 6.3 | Click Delete again → click **OK** | Redirect to catalog with yellow "Toy deleted." Row gone |
| 6.4 | Manually visit `/delete-toy?id=T999` | Red "Toy not found. Nothing was deleted." alert |

## 7. File Persistence

| # | Step | Expected |
|---|---|---|
| 7.1 | Open `WEB-INF/data/toys.txt` after each operation | Each pipe-delimited line reflects the current state |
| 7.2 | Stop Tomcat → restart → reload catalog | Same data shown (file persists across restarts) |
| 7.3 | Manually edit a price in `toys.txt` → reload catalog | Edit reflected (proves we read live, not cached) |

## 8. UI / Navigation

| # | Step | Expected |
|---|---|---|
| 8.1 | Click navbar brand from any page | Returns to landing page |
| 8.2 | Resize browser to mobile width | Bootstrap responsive layout — table scrolls horizontally, cards stack |
| 8.3 | Hover over a card | Card lifts up + shadow grows |
| 8.4 | Footer visible at bottom of every page | "© 2026 Online Toy Store — Toy Management Component" |

---

## Pass Criteria
All boxes checked off; no SEVERE entries in the Tomcat console; the data file matches the catalog after each test.
