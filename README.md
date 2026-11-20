# Cloud-Native Banking Microservices — Zero-Trust Security Architecture

An enterprise-grade, security-hardened digital banking microservices platform built with **Java 17**, **Spring Boot 3**, and **Spring Security**. This project secures inter-service and edge communication across **Accounts**, **Cards**, and **Loans** microservices using **OAuth2**, **OpenID Connect (OIDC)**, **JWT**, and **Role-Based Access Control (RBAC)** enforced at the API Gateway and resource server layers.

## Security Architecture

```text
+-----------+      1. Authenticate (OAuth2 / OIDC)      +------------------------+

|  Client   | ----------------------------------------> |  Authorization Server  |
| (Postman) | <---------------------------------------- |  (Keycloak / Auth0)    |
+-----+-----+      2. Issue Signed JWT Access Token     +------------------------+
      |
      | 3. Request + Bearer <JWT>
      v
+-----+--------------------------------------------------------------------------+

|                         Spring Cloud API Gateway                               |
|             (OAuth2 Resource Server — JWT Signature & Role Validation)         |
+-----+----------------------------------+-----------------------------------+--+

      |                                  |                                   |
      v                                  v                                   v
+-----+-------------+            +-------+-----------+            +----------+--------+

| Accounts Service  |            |   Cards Service   |            |   Loans Service   |
| (ROLE_ACCOUNTS)   |            |   (ROLE_CARDS)    |            |   (ROLE_LOANS)    |
+-------------------+            +-------------------+            +-------------------+
```

## Key Security Features Implemented

* **Edge Authentication & Token Validation:** Configured **Spring Cloud Gateway** as an OAuth2 Resource Server to validate JWT signatures, token expiration, and issuer claims before routing traffic downstream.
* **Fine-Grained Role-Based Access Control (RBAC):** Enforced method-level and route-level authorization (`hasRole("ACCOUNTS")`, `hasRole("CARDS")`, `hasRole("LOANS")`) using custom `JwtAuthenticationConverter` role mapping.
* **OAuth2 Client Credentials & Authorization Code Flows:** Supports both user-facing login flows and machine-to-machine (service-to-service) token propagation.
* **Defense-in-Depth Infrastructure:** Combined with **Spring Cloud Config**, **Netflix Eureka**, and **Docker Compose** for isolated, reproducible security testing.

## Tech Stack

* **Language & Framework:** Java 21, Spring Boot 3.3.2, Spring Security, Spring Cloud (Gateway, Config, Eureka)
* **Security Standards:** OAuth2, OpenID Connect (OIDC), JSON Web Tokens (JWT), Keycloak
* **Containerization:** Docker, Docker Compose

## Quick Start

1. **Clone the repository:**
   ```bash
   git clone https://github.com/jaganhyd/microservices-security.git
   cd microservices-security
   ```

2. **Start Keycloak & Infrastructure Containers:**
   ```bash
   docker-compose up -d
   ```

3. **Obtain an Access Token & Call Protected Endpoints:**
   * Request a JWT token from the Authorization Server (`http://localhost:7080/realms/master/protocol/openid-connect/token`).
   * Pass the token in the `Authorization: Bearer <token>` header when calling `http://localhost:8072/jbmicroservice/accounts-1/api/create`.
