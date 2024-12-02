# roq-with-blog

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

But mostly take a look at [quarkus-roq](https://github.com/quarkiverse/quarkus-roq)

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
quarkus dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
QUARKUS_ROQ_GENERATOR_BATCH=true ./mvnw package quarkus:run -Dsite.future=true -Dsite.draft=true
```

