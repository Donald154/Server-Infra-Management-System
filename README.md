# Server Infrastructure Management System

## Project Overview

The Server Infrastructure Management System is a Java-based Data Management System designed to store, organize, and manage information about physical or virtual servers in an IT environment.

The application is being developed as a Software Development Life Cycle (SDLC) project. Development is divided into multiple phases, beginning with core Java logic and input validation, then progressing through software testing, graphical user interface development, and relational database integration.

The final application will provide a graphical interface that allows users to manage server inventory records stored in a SQL database.

---

## Project Objectives

The main goals of the project are to:

- Apply the Software Development Life Cycle to a complete application
- Design and implement a Java-based Data Management System
- Use object-oriented programming principles
- Perform CRUD operations on server records
- Validate user input and handle errors safely
- Support bulk data loading
- Implement a custom mathematical action
- Test the core application logic
- Develop a graphical user interface
- Integrate a relational SQL database
- Maintain separation between the user interface, business logic, and data storage layers

---

## Server Data Model

Each server is represented by a `Server` object containing the following attributes:

| Attribute | Data Type | Description |
|---|---|---|
| `serverId` | `int` | Unique identifier for each server |
| `hostname` | `String` | Hostname assigned to the server |
| `operatingSystem` | `String` | Operating system installed on the server |
| `memoryGB` | `double` | Installed memory in gigabytes |
| `storageGB` | `double` | Storage capacity in gigabytes |
| `ipAddress` | `String` | IP address assigned to the server |
| `isOnline` | `boolean` | Indicates whether the server is currently online |

---

## Core Features

### Create

Users will be able to:

- Add server records manually
- Load multiple server records from a text file
- Add server records through the graphical interface in later phases

### Read

Users will be able to:

- Display all stored server records
- View server information including:
  - Server ID
  - Hostname
  - Operating system
  - Memory
  - Storage
  - IP address
  - Online status

### Update

Users will be able to:

- Locate an existing server
- Modify server information
- Update fields such as:
  - Hostname
  - Operating system
  - Memory
  - Storage
  - IP address
  - Online status

### Delete

Users will be able to:

- Remove a server using its unique server ID

---

## Custom Action

The project includes a custom mathematical operation:

### Calculate Online Server Percentage

The system calculates the percentage of servers currently marked as online.

Formula:

```text
Online Percentage =
(Number of Online Servers / Total Number of Servers) × 100