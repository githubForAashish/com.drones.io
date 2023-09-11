# Drone Dispatch Controller

[[_TOC_]]

### Introduction
This project was prepared as an assessment for **Musala Soft**. A postman collection has been included under `postman` folder for testing.
A basic project is set up along with 2 Unit Test cases.

### Assumptions
Certain assumptions have been made. A drone life cycle is simulated. Conditions are:
* Lifecycle is determined by state changes and are in order `IDLE` -> `LOADING` -> `LOADED` -> `DELIVERING` -> `DELIVERED` -> `RETURNING`.
* On every scheduled cycle, a batter drain (configurable in `application.yml`) is incurred. `IDLE` drones aren't effected by the cycle.
* Only drones in `IDLE` or `LOADING` are eligible in the life cycle flow.
* Medications can be loaded to `IDLE` or `LOADING` drones until capacity is reached and state changes to `LOADED`.



### Configurations
* Application properties can be set in under `src/main/resources`

* CUSTOM PROPERTIES

| property                             | current value | description                                                |
|--------------------------------------|:-------------:|------------------------------------------------------------|
| `drone.setting.battery-threshold`    |      25       | Battery below which drone wont operate.                    |
| `drone.setting.fleet-capacity`       |      10       | Maximum Active Drones.                                     |
| `drone.setting.decay-rate`           |     0.15      | Rate at which battery decreases.                           |
| `drone.setting.maximum-weight-limit` |      500      | Total medication carrying capacity.                        |
| `scheduler.enabled`                  |     true      | Enable the Drone LifeCycle Simulation and battery logging. |
| `scheduler.interval`                 |      45       | Value in seconds. How often state changes.                 |


### Requirements
- java 17
- set `JAVA_HOME` env to java installation directory


### Build/Run instruction
- open terminal in project root and run `./mvnw spring-boot:run`
- Seed values are provided here `src/main/resources/data.sql`
- `logs` folder will be created at the current running directory containing battery logs
- Battery logs will only be generated if `scheduler.enabled` property is set to `true`
- To access the `H2` database, visit `/api/h2-console` after running the application.


