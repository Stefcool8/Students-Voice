# Student Teacher Evaluation System

## Overview
The **Student Teacher Evaluation System** is a Java EE web application that allows students to evaluate their teachers efficiently. Built with a **GlassFish server**, this system enables students to submit evaluations that include:

- Teacher’s name
- Activity type (e.g., course or lab)
- Score
- Comments

This application ensures a seamless and structured feedback process while maintaining security and performance through modern web technologies.

## Features

### Backend (Java EE)
- **RESTful API (JAX-RS)**: Provides structured endpoints for handling evaluations.
- **JWT-based Authentication**: Ensures secure access to the system.
- **Data Transfer Objects (DTOs)**: Optimizes communication between frontend and backend.
- **Response Caching**: Enhances performance by reducing redundant data retrieval.
- **Context and Dependency Injection (CDI)**: Enables efficient component management.

### Frontend (React & TypeScript)
- **Interactive User Interface**: Provides a smooth and dynamic user experience.
- **Type Safety with TypeScript**: Enhances code reliability and maintainability.
- **Seamless API Integration**: Connects efficiently with the backend for real-time updates.

## Impact
This system enhances **academic feedback mechanisms** by allowing students to provide structured evaluations while ensuring:

- **Security** through authentication and role-based access.
- **Efficiency** with performance optimizations like caching and dependency injection.
- **User Experience** with an intuitive React-based frontend.

## Tech Stack

- **Backend**: Java EE, JAX-RS, JWT, GlassFish, CDI, DTOs
- **Frontend**: React, TypeScript
- **Authentication**: JWT-based authentication
- **Server**: GlassFish

## Setup Instructions

### Prerequisites
Ensure you have the following installed:

- **Java JDK 11+**
- **GlassFish Server**
- **Node.js and npm** (for the frontend)
