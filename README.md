
# **Vinci App 🚀**

**Overview:**
Vinci App is a modern web application developed during my internship at Vinci, under the guidance of Axians. It has two main components:

- **Admin Side**: For managing user accounts, roles, permissions, and material stocks.
- **AI Chat Side**: Leveraging React.js, Vaadin, Spring AI, and PVGStore to provide an intelligent AI chatbot interface using advanced tools like Olama and RAG.

These components streamline inventory management and provide an advanced AI chatbot for user interaction.

---

## **Features 🎨**

### **Admin Side 🛠️**
- **User Management**: Admins can add, update, and delete user accounts, manage roles and permissions.
- **Role and Permission Management**: Full control over defining roles and permissions.
- **Inventory Management**: Manage stock items with detailed reports.
- **Advanced Reporting**: Generate comprehensive inventory and user activity reports.
- **Commenting on Materials**: Admins can provide feedback via material comments.

### **AI Chat Side 🤖**
- **AI Chatbot**: Powered by LLM (Large Language Models), Olama, and RAG for intelligent responses.
- **API Integration**: REST APIs ensure smooth communication between AI models and the interface.
- **Spring AI Integration**: Efficient, intelligent responses powered by Spring AI.
- **Model Management with PVGStore**: Ensures AI chat operates with up-to-date models.

---

## **Technologies Used 🛠️**

- **Spring Boot 3.3.1** 🌟: Backend framework for building Java-based applications.
- **Spring Security 6** 🔒: Provides advanced authentication and authorization.
- **Hibernate ORM & JPA** 💾: Object-relational mapping for database interactions.
- **JWT** 🗝️: Token-based authentication for secure API requests.
- **MySQL** 🗄️: Relational database management.
- **Tomcat Server** 🌐: Web server for handling HTTP requests.
- **React.js** ⚛️: Front-end library for dynamic user interfaces.
- **Vaadin** 🧩: UI components for reactive interfaces.
- **LLM (Large Language Models)** 🤖: AI chatbot responses.
- **Olama & RAG** 🔗: Retrieval-augmented generation for intelligent query handling.
- **Spring AI** 🧠: AI model management.
- **PVGStore** 🗂️: Efficient storage and retrieval of AI models.
- **Maven** ⚙️: Build and dependency management tool for Java projects.

---

## **Installation 🛠️**

### **1. Clone the Repository 💻**
```bash
git clone https://github.com/samyrh/Vinci-Axians.git
cd Vinci-Axians
```

### **2. Set Up MySQL Database 🗄️**
- **Create a Database**:
  Use MySQL Workbench to create a new database (e.g., `vinci_db`).
  
- **Configure Database Connection**:
  Update the `src/main/resources/application.properties` file with your database details:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/vinci_db
  spring.datasource.username=your_username
  spring.datasource.password=your_password
  ```

### **3. Load Initial Data 📥**
You can initialize the database either by SQL scripts or programmatically:

- **Using SQL Scripts**:
  Place `schema.sql` and `data.sql` files in the `db/` directory.
  
- **Using Initialization Code**:
  ```java
  @Bean
  public CommandLineRunner loadData(UserRepository userRepository) {
      return args -> {
          userRepository.save(new User("admin", "admin@example.com", "password"));
      };
  }
  ```

### **4. Build and Run the Application 🏗️**

#### **Admin Side (Backend + Frontend)**
- Run on port **9092**:
  ```bash
  mvn clean install
  mvn spring-boot:run -Dserver.port=9092
  ```

#### **AI Side (Backend + Frontend)**
- Run on port **8080**:
  ```bash
  mvn clean install
  mvn spring-boot:run -Dserver.port=8080
  ```

---

## **Usage 🚀**

### **Access the Application 🌐**

- **Admin Side**: Access both the front-end and back-end at [http://localhost:9092/](http://localhost:9092/).
- **AI Chat Side**: Access the AI chatbot at [http://localhost:8080/](http://localhost:8080/).

### **Login 🔑**
- **Admin Login**: Use admin credentials to manage users, roles, and inventory.
- **AI Chat**: Interact with the AI-powered chatbot interface.

---

## **Contributing 🤝**
We welcome contributions! If you have suggestions or improvements, feel free to open an issue or submit a pull request.

---

## **License 📜**
This project is licensed under the MIT License.

---

## **Contact 📧**
- **Email**: Rhalimsami8@gmail.com
- **Website**: [Axians Website](https://axians.com)

---
