# Scramble Notes

A simple and efficient web-based note management application. 
Create, edit, and organize your text notes with a clean interface.

## Technical Stack

- **Language & Runtime**: Java 24:cite[1]:cite[6]
- **Server-Side Templating**: Thymeleaf:cite[2]
- **Application Framework**: Spring Boot (Dependency Injection):cite[3]:cite[7]
- **Build Tool**: Maven:cite[4]:cite[8]

## Major Features

| Feature | Description |
| :--- | :--- |
| **CRUD Operations** | Create, view, edit, and delete notes. |
| **Markdown Viewer** | Read markdown files (`*.md`) from a configured directory. |
| **Sorting** | Sort your notes by date in ascending or descending order. |
| **Status Tracking** | Mark notes as done/not done. |

## Prerequisites

- **JDK 24** or later:cite[1]:cite[6]
- **Apache Maven** 3.6 or later

## Building and Running

1.  **Clone the repository**
    ```bash
    git clone <your-repository-url>
    cd scramble-notes
    ```

2.  **Build the project**
    ```bash
    mvn clean package
    ```

3.  **Run the application**
    ```bash
    mvn spring-boot:run
    ```
    The application will be available at `http://localhost:8080`.
