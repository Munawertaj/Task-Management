# Task Management System

A web‑based Task Management System built with Spring Boot, Spring Data JPA, MySQL, Thymeleaf, and JWT‑based authentication/authorization. Users can register, log in, and perform full CRUD on tasks (create, view, update, delete), plus filter, sort and search. Administrators can log in and view all registered users.

## Table of Contents

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Usage](#usage)

    * [User flows](#user-flows)
    * [Admin flows](#admin-flows)
* [Default Credentials](#default-credentials)
* [Author](#author)

## Features

* **User**

    * Register, log in/out (JWT‑based)
    * Create, view, update, delete tasks
    * Filter tasks by status (Pending, In Progress, Completed)
    * Sort tasks by title, status, due date
    * Search tasks by keyword

* **Admin**

    * Log in/out
    * View list of all registered users

## Tech Stack

* **Backend:** Spring Boot, Spring Data JPA
* **Database:** MySQL
* **Security:** Spring Security with JWT
* **Frontend:** Thymeleaf (server‑side templates)
* **Build:** Maven
* **Java Version:** 21

## Usage

### User flows

1. **Register**

    * Navigate to `/register`
    * Fill in username, email, password

2. **Login**

    * Navigate to `/login`
    * On success you land on your Task Dashboard

3. **Manage Tasks**

    * **Create:** click “Create Task,” fill form, submit
    * **View:** see list on dashboard
    * **Update:** click “Edit” next to a task
    * **Delete:** click “Delete” and confirm

4. **Filter & Sort**

    * Use dropdowns on the dashboard to filter by status or sort by title/status/due date

5. **Search**

    * Enter keyword in search box to find tasks by title/description

6. **Logout**

    * Click “Logout” in the nav bar

### Admin flows

1. **Login**

    * Navigate to `/login`
    * On success you land on Admin Dashboard

2. **View Users**

    * Click “View All Users” to see username, email, registration date

3. **Logout**

    * Click “Logout” in the nav bar

## Default Credentials

> **Note:** Change these in production!

* **Admin**

    * Email: `admin@example.com`
    * Password: `admin@123`

## Author

**Tareq Munawer Taj**

* Email: [tareqmunawertaj@gmail.com](mailto:tareqmunawertaj@gmail.com)
* GitHub: [MunawerTaj](https://github.com/Munawertaj)
