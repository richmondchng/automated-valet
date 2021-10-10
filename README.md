# Automated Valet Parking System

Repository: automated-valet

To Build:
1. Clone (download) repository
2. Open a command line terminal (e.g., PowerShell)
3. Navigate to project `root` folder
   -  e.g., `C:\automated-valet`
4. Run the following command to build project
   - `.\mvmw clean package`
5. When build is completed, run the following command to start application; include the file as the first parameter
   - `java -jar .\target\automated-valet-1.0-SNAPSHOT.jar <file>`

Assumptions:
1. Current requirements only require a simple console application to read the parking instructions from a supplied file
2. No permanent data storage is implemented because application exits on completion of file
3. The first line of the file will always contain the parking configuration - number of lots for Car then Motorcycle type

Project Notes:
1. Built using Java 11
2. Require Java 11 to compile the project
3. Use the included maven wrapper to build the project
4. Additional dependencies
    - lombok
    - Apache common lang3
    - Apache collections4
    - Junit Jupiter (for unit test)
    - Mockito (for unit test)

Project Structure:
1. Main class is at `org.richmondchng.automatedvalet.AutomatedValet`
2. Source classes are in `/src/main`
3. Package `org.richmondchng.automatedvalet.config` contains configuration and context building
4. Package `org.richmondchng.automatedvalet.data` contains data repositories logic and entities definition
5. Package `org.richmondchng.automatedvalet.data.storage` contains the data that exists during the lifespan of the application
6. Package `org.richmondchng.automatedvalet.dto` contains data transfer object between main class and controller
7. Package `org.richmondchng.automatedvalet.exception` contains custom exceptions specific to business logic
8. Package `org.richmondchng.automatedvalet.file` contains file loading and reading code
9. Package `org.richmondchng.automatedvalet.model` contains data modeling definition used between controller and services
10. Package `org.richmondchng.automatedvalet.service` contains business logic services
11. Package `org.richmondchng.automatedvalet.util` contains utility classes
12. Test classes are in `/src/test`