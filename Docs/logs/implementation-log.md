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
