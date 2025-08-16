# Sprint 4 - API Automation Testing  

![Java](https://img.shields.io/badge/Java-17-blue)  
![Maven](https://img.shields.io/badge/Maven-Automation-orange)  
![TestNG](https://img.shields.io/badge/TestNG-Framework-green)  
![ExtentReports](https://img.shields.io/badge/Reports-ExtentReports-purple)  
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen)  
![License](https://img.shields.io/badge/License-MIT-lightgrey)  

---

## 📌 Overview  
This project is an **API automation framework** built with **Java, Maven, and TestNG**.  
It is designed to test a **Passenger Management System** with endpoints for:  
- Authentication  
- Adding passengers  
- Viewing passenger details  
- Deleting passengers  

The framework follows a **modular design** with separation of concerns (clients, config, POJOs, utilities). Test data is externalized in JSON files, and reports are generated using **ExtentReports**.  

---

## 🚀 Features  
- **Maven Project** with dependency management (`pom.xml`)  
- **TestNG** for test orchestration  
- **API Client Layer** (`AuthClient`, `PassengerClient`)  
- **Externalized configuration** (`config.properties`)  
- **JSON-driven test data** (`src/main/resources/testdata/`)  
- **POJOs for request/response mapping** (`Passenger`, `Contact`, etc.)  
- **Custom utilities** (`JsonReader`)  
- **ExtentReports Integration** – HTML reports with detailed logs and test status  

---

## 🛠 Tech Stack  
- **Language:** Java 17+  
- **Build Tool:** Maven  
- **Testing Framework:** TestNG  
- **Libraries:**  
  - `RestAssured` (API testing)  
  - `Jackson` (JSON parsing)  
  - `ExtentReports` (reporting)  
- **IDE:** Eclipse

---

## 📂 Project Structure  

```
sprint4/
├── pom.xml                  # Maven dependencies
├── testng.xml               # TestNG suite runner
├── reports/                 # Generated test reports
│   └── ExtentReport_<date>/
│       └── Index.html
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── clients/      # API clients
│   │   │   ├── config/       # ConfigManager
│   │   │   ├── pojo/         # Domain objects
│   │   │   └── util/         # Utilities
│   │   └── resources/
│   │       ├── config.properties
│   │       └── testdata/     # JSON test data
│   └── test/
│       ├── java/
│       │   ├── auth/         # Auth tests
│       │   ├── base/         # Base setup
│       │   └── passenger/    # Passenger-related tests
│       └── resources/
└── target/                   # Compiled output
```

---

## ⚙️ Setup & Installation  

1. **Clone the repository**  
   ```bash
   git clone <repo-url>
   cd sprint4
   ```

2. **Import as Maven Project** (Eclipse/IntelliJ)  

3. **Install dependencies**  
   ```bash
   mvn clean install
   ```

4. **Configure environment**  
   Update `src/main/resources/config.properties` with API base URLs, credentials, etc.  

---

## ▶️ Running Tests  

Run all tests with Maven:  
```bash
mvn clean test
```

Run specific suite with TestNG:  
```bash
mvn test -DsuiteXmlFile=testng.xml
```

---

## 📊 Reports  

After execution, HTML reports are generated at:  

```
/reports/ExtentReport_<timestamp>/Index.html
```

Open `Index.html` in a browser to view test results.  

---

## 🔮 Future Enhancements  
- Add CI/CD integration with Jenkins/GitHub Actions  
- Add Allure Reports for richer visualization  
- Dockerize the framework for portability  
- Add parallel execution support in TestNG  
