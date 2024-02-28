# IoT Infrastructure Project

This project facilitates IoT manufacturers in gathering data about their device usage through a web service. It comprises two main components: a website and a Gateway server. The website is developed using **React.JS** and **Bootstrap** for frontend, and **Node.JS** with **Express framework** for backend, while the Gateway server is coded in **Java**.

## Components

### 1. Website

- **Frontend:** React.JS with Bootstrap styling.
- **Backend:** Node.JS with Express framework.
- **Database:** MySQL for storing company and product information.
- **Functionality:** Provides a user interface for registration, enabling companies to register themselves and their products.

### 2. Gateway Server

- **Language:** Java.
- **Features:**
  - Acts as a concurrent, configurable, and scalable server.
  - Acts as an intermediary for interacting with the company-specific database (MongoDB).
  - Supports dynamic addition of functionalities via "Plug and Play" feature.
  - Utilizes a thread pool for concurrent processing and vertical scaling.

## Functionalities

1. **Register Company**
   - Companies register themselves via the website.
   - Information stored in MySQL database.
   - Upon registration, a company-specific NoSQL database (MongoDB) is created for storing device usage data.

2. **Register Product**
   - Registered companies can register their products through the website.

3. **Register IoT**
   - Facilitates the registration of IoT devices belonging to registered products.
   - Requests are sent to the Gateway server for registration.

4. **Update**
   - Devices can send usage information to the Gateway server.
   - Information is stored in the respective company's NoSQL database.

## How It Works

1. **Company Registration**
   - Companies provide necessary details via the website for registration.

2. **Product Registration**
   - Registered companies register their products through the website.

3. **IoT Device Registration**
   - Devices belonging to registered products are registered by sending a request to the Gateway server.

4. **Data Update**
   - Registered devices send data updates to the Gateway server.
   - Information is stored in the respective company's NoSQL database.

## Development Status

The project is still in development, focusing on the integration between the website and the Gateway server, as well as building the HTTP server unit.
