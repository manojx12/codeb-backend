# CodeB - MIS & Invoicing System (Backend)

Backend REST API for the CodeB Internal Management System (IMS) — built to help the Sales Team manage client data, estimates, invoices, payments, and HR operations.

## Tech Stack

- **Framework:** Spring Boot
- **Database:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **API Testing:** Postman

## Modules

### Client Management
- Client, Chain, Brand, Subzone, Group — client categorization and organization structure

### Billing
- Estimate — sales estimates created by the Sales Team
- Invoice — invoices with auto-calculated GST and total amount
- Payment — payment tracking, auto-updates invoice status on payment

### HR
- Employee, Attendance, Leave, Payroll

### Reports
- Dashboard — summary stats across all modules

## Setup Instructions

1. Clone the repository
```
   git clone <repo-url>
```

2. Create a MySQL database:
```sql
   CREATE DATABASE mis_invoicing_db;
```

3. Copy `application.properties.example` to `application.properties` inside `src/main/resources/`, and update it with your own MySQL username and password.

4. Run the application:
```
   mvn spring-boot:run
```

5. The API will start on `http://localhost:8080`

## API Endpoints

All endpoints follow REST conventions under `/api/<resource>`, for example:

- `GET /api/clients` — list all clients
- `POST /api/clients` — create a client
- `PUT /api/clients/{id}` — update a client
- `DELETE /api/clients/{id}` — delete a client

Same pattern applies to `/api/chains`, `/api/brands`, `/api/subzones`, `/api/groups`, `/api/estimates`, `/api/invoices`, `/api/payments`, `/api/employees`, `/api/attendance`, `/api/leaves`, `/api/payrolls`, and `/api/dashboard/summary`.

## Author

Manoj