<!-- Author: Jaydeep Dahiya -->
# PrimaryKeyAll

PrimaryKeyAll is a Spring Boot and Hibernate sample project demonstrating several JPA primary-key strategies. Spring Data REST exposes repository-backed HAL endpoints, while Flyway creates and updates the database schema.

## What this project demonstrates

- Database-generated numeric IDs with `IDENTITY`
- UUID primary keys stored as text and as `BINARY(16)`
- A natural key using a contact's email address
- Sequence-based IDs, including database-specific sequence handling
- Table-based ID generation with separate sequence segments shared through `common_seq`
- Composite keys using both `@IdClass` and `@EmbeddedId`
- Shared create/update audit fields supplied by Spring Data JPA auditing
- OpenAPI documentation and a Postman collection for REST API smoke tests

## Technology

- Java 25
- Spring Boot 4.1.2
- Spring Data JPA and Spring Data REST
- Hibernate ORM
- H2 for the default local database and tests; MySQL for the `prod` profile
- Flyway database migrations
- Springdoc OpenAPI / Swagger UI
- Maven

## Run locally

Use Java 25 and Maven, then start the application from the project root:

```shell
mvn spring-boot:run
```

By default, the application uses a file-based H2 database at `./mysqldatabase` and enables Flyway migrations on startup. The database files are created in the project root. To use the H2 console, visit [http://localhost:8080/h2-console](http://localhost:8080/h2-console); the console is enabled at `/h2-console`.

To package and run the application:

```shell
mvn clean package
java -jar target/PrimaryKeyAll-1.0-SNAPSHOT.jar
```

## Database profiles and migrations

The application supports these runtime profiles:

| Profile selection | Database / behavior | Maven command |
| --- | --- | --- |
| No active profile (default) | File-based H2 database at `./mysqldatabase`; Flyway applies pending migrations. | `mvn spring-boot:run` |
| `prod` | MySQL, configured in `application-prod.properties`; Flyway applies pending migrations. | `mvn spring-boot:run -Dspring-boot.run.profiles=prod` |
| `clean` | Uses the default H2 database and runs Flyway clean followed by migrate at startup. **All data in that H2 schema is deleted first.** | `mvn spring-boot:run -Dspring-boot.run.profiles=clean` |
| `prod,clean` | Uses MySQL and runs Flyway clean followed by migrate at startup. **All data in the configured MySQL schema is deleted first.** | `mvn spring-boot:run -Dspring-boot.run.profiles=prod,clean` |

The `clean` profile is intended only for disposable development databases. It enables Flyway's clean operation and installs a startup strategy that drops the configured schema objects before reapplying migrations. Do not combine `clean` with `prod` for a database containing data you need. The same profile combinations can be used with the packaged application by passing `--spring.profiles.active=prod,clean` (or the desired profile names) to `java -jar`.

The default configuration in `src/main/resources/application.properties` uses H2. The `prod` profile in `src/main/resources/application-prod.properties` configures MySQL. Its connection values can be overridden with `DATABASE_HOST`, `DATABASE_PORT`, `DATABASE_NAME`, and `DATABASE_USERNAME`; Flyway administrator credentials can be overridden with `DATABASE_ADMIN_USERNAME` and `DATABASE_ADMIN_PASSWORD`.

The `test` profile uses an in-memory H2 database configured in `src/test/resources/application-test.properties` and is intended for automated tests, not normal application startup.

### Initialize MySQL database and users

Use `Scripts/mysqlusers.sql` to create the `mysqldatabase` schema and the MySQL accounts used by the application and Flyway. Run it from the project root in Windows Command Prompt with a MySQL administrator account:

```shell
mysql -u root -p < Scripts\mysqlusers.sql
```

**Warning:** this script drops and recreates the `mysqldatabase` database and its `myadmin` and `myuser` accounts. It deletes existing database data. Back up any data you need and review the script before running it. Change the example credentials in the script and configure matching credentials for your environment before use; do not use example credentials outside a local development setup.

The script grants `myuser` the application data permissions and grants `myadmin` additional privileges needed for Flyway schema migrations. Configure `application-prod.properties` or its environment-variable overrides to use the database host, port, name, application username, and Flyway administrator credentials for your MySQL instance.

Do not run the Postman collection against a shared or production database: its requests create, update, and delete records.

Flyway migrations are in `src/main/resources/flyway_sqls/`:

- `common/` contains schema objects shared across database vendors.
- `h2/` contains H2-specific changes, including the native sequence.
- `mysql/` contains MySQL-specific changes, including its sequence-table setup.

Flyway applies the common migrations and the directory matching the connected database vendor. Hibernate schema validation is enabled; schema changes should be made through migrations.

The current V1 and V1.1 migration scripts define the contact schema without `message`, `subject`, or `user_type`. These scripts are intended for new databases. Because existing databases may have already applied earlier versions of these migrations, editing them changes their Flyway checksums; use a disposable/new database or an approved Flyway repair/migration process for existing databases rather than changing migration history casually.

## REST API

Spring Data REST exposes the following collection endpoints at `http://localhost:8080`:

| Endpoint | ID strategy / resource |
| --- | --- |
| `/contacts-identity` | Database identity (`Long`) |
| `/contacts-uuid` | UUID stored as text |
| `/contacts-uuid-binary` | UUID stored as `BINARY(16)` |
| `/contacts-natural` | Email natural key |
| `/contacts-sequence` | Sequence-generated `Long` |
| `/contacts-table` | Shared table-based ID segment for contacts |
| `/contacts-composite` | `@IdClass` key with `firstname` and `lastname` |
| `/contacts-composite-embedded` | `@EmbeddedId` key with `firstname` and `lastname` |
| `/books-table` | Shared table-based ID segment for books |
| `/authors-table` | Shared table-based ID segment for authors |

Spring Data REST provides HAL collection and item resources. Use the resource's `_links.self.href` for subsequent item requests rather than constructing generated IDs yourself. IDs generated by the database are response-only and should be omitted from create requests.

Contact requests generally include `email`, `name`, and `status`. The retired `message`, `subject`, and `userType` properties are not part of the contact entities or current schema. A typical request body is:

```json
{
  "email": "alex@example.com",
  "name": "Alex Example",
  "status": "NEW"
}
```

The `/contacts-composite` request also requires top-level `firstname` and `lastname`. The `/contacts-composite-embedded` request supplies those fields inside `compositeIdEmbedded`:

```json
{
  "compositeIdEmbedded": {
    "firstname": "Alex",
    "lastname": "Example"
  },
  "email": "alex@example.com",
  "name": "Alex Example",
  "status": "NEW"
}
```

Composite item IDs are URL-safe Base64 without padding, encoding the UTF-8 bytes of `firstname.lastname`. For example, `John.Doe` becomes `Sm9obi5Eb2U`. The custom composite-ID converter and entity lookup ensure item links use the complete key; a dedicated controller handles DELETE for the `@IdClass` endpoint.

Book requests use `title`, `isbn`, and `publisher`. Author requests use `firstName` and `lastName`.

## API documentation

When the application is running:

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- OpenAPI document: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Tests

Run the test suite with:

```shell
mvn test
```

The H2 and application-context tests use the in-memory `test` profile. The MySQL integration test uses `application-prod.properties` and requires an available MySQL database with suitable credentials and migrations; it will not run successfully without that database.

The tests cover repository persistence, ID generation, auditing, table sequence segments, and composite-ID REST links and operations.

## Postman test collection

Import `Postman_test_pack/PrimaryKeyAll.postman_collection.json` into Postman. The `baseUrl` collection variable defaults to `http://localhost:8080`. Each endpoint folder runs a create/read/update/delete lifecycle and checks the response and returned self link. Run it against a disposable database because the collection writes and deletes data.
