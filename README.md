
# **Vinci App 🚀**

**Overview:**
Vinci App is a modern web application developed during my internship at Vinci, under the guidance of Axians. The application has two distinct sides:

- **Admin Side**: Designed for managing user accounts, roles, permissions, and material stocks.
- **AI Chat Side**: Realized using React.js, REST API, Vaadin, LLM (Large Language Models), and advanced tools like Olama and RAG (Retrieval Augmented Generation), leveraging Spring AI and PVGStore for a dynamic AI chatbot interface.

The combination of these two sides allows Vinci App to streamline both inventory management and provide an advanced AI-powered chatbot interface for user support and interaction.

**Features 🎨**

- **Admin Side 🛠️**
  - **User Management:** Admins can add, update, and delete user accounts, manage user roles and permissions, and ensure proper access control.
  - **Role and Permission Management:** Admins have full control over defining and managing roles and permissions, ensuring that users have appropriate access based on their responsibilities.
  - **Inventory Management:** Admins can manage inventory items, including adding, updating, and deleting stock entries. They can view detailed reports and monitor stock levels to ensure optimal inventory management.
  - **Advanced Reporting:** Generate and view comprehensive reports on inventory status, user activities, and system performance.

- **AI Chat Side 🤖**
  - **AI Chatbot:** A dynamic chatbot interface realized using **React.js** for the front-end and **Vaadin** for the UI components. It is powered by **LLM (Large Language Models)** and technologies like **Olama** and **RAG (Retrieval Augmented Generation)** to provide intelligent responses and contextual assistance.
  - **API Integration:** Utilizes REST APIs to connect with the back-end, enabling smooth communication between the AI model and the front-end interface.
  - **Spring AI Integration:** Leveraging **Spring AI** to process and deliver responses, making the chat intelligent and responsive to user queries.
  - **PVGStore for Model Storage:** Provides efficient storage and retrieval of models, ensuring that the AI chat operates with up-to-date data and contextual understanding.

**Technologies Used 🛠️**

- **Spring Boot 3.3.1** 🌟: The core framework for building the application's backend, providing a comprehensive suite of features for developing and running Java-based applications.

- **Spring Security 6** 🔒: Ensures advanced security through authentication and authorization mechanisms, protecting both user data and application integrity.

- **Hibernate ORM** 💾: Facilitates object-relational mapping for database interactions, allowing for efficient data handling and manipulation within the application.

- **JPA (Java Persistence API)** 💾: Provides additional support for data persistence and mapping, working alongside Hibernate to manage database operations.

- **JWT (JSON Web Tokens)** 🗝️: Secures user sessions and API requests with token-based authentication, enhancing security and scalability.

- **MySQL** 🗄️: The relational database management system used for storing and managing application data, offering reliability and performance.

- **Tomcat Server** 🌐: The web server and servlet container used to deploy and run the Spring Boot application, providing a robust environment for handling HTTP requests and responses.

- **React.js** ⚛️: The JavaScript library used for building the interactive and dynamic front-end interface for both the admin and AI chat interfaces.

- **Vaadin** 🧩: Provides UI components for building a modern, reactive web interface, particularly used for the AI Chat side.

- **LLM (Large Language Models)** 🤖: Powers the AI chatbot's ability to generate contextual and intelligent responses.

- **Olama and RAG** 🔗: Advanced tools used for retrieval-augmented generation, enhancing the AI chatbot’s ability to understand and respond to user queries with precision.

- **Spring AI** 🧠: Used to manage AI models and their interactions, ensuring efficient and accurate responses from the chatbot.

- **PVGStore** 🗂️: Ensures the proper management and storage of AI models, facilitating fast access and retrieval of the necessary models for the AI chatbot.

- **Docker** 🐳: Provides containerization to ensure consistent deployment and running of the application across different environments.

**Installation 🛠️**

1. **Clone the Repository 💻**
   ```bash
   git clone https://github.com/samyrh/Vinci-Axians.git
   cd Vinci-Axians
   ```

2. **Set Up MySQL Database 🗄️**

   - **Create a Database:**
     Use MySQL Workbench or another MySQL client to create a new database (e.g., `vinci_db`).

   - **Configure Database Connection:**
     Update the `src/main/resources/application.properties` or `application.yml` file with your database connection details:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/vinci_db
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```

3. **Load Initial Data 📥**

   - **Using SQL Scripts:**
     Place SQL scripts like `schema.sql` and `data.sql` in the `db/` directory. Spring Boot will execute these scripts to initialize the database.

   - **Using Data Initialization Code:**
     Implement a `CommandLineRunner` or `ApplicationRunner` in a Spring Boot configuration class to programmatically load initial data:
     ```java
     @Bean
     public CommandLineRunner loadData(UserRepository userRepository) {
         return args -> {
             userRepository.save(new User("admin", "admin@example.com", "password"));
         };
     }
     ```

4. **Build and Run the Application 🏗️**

   - **Without Docker:**
     Build the project using Maven:
     ```bash
     mvn clean install
     ```
     Run the application using Tomcat:
     ```bash
     mvn spring-boot:run
     ```

   - **With Docker:**
     Build and run the application using Docker:
     ```bash
     docker-compose up --build
     ```

5. **Run the React Front-End ⚛️**

   - Navigate to the React project directory (e.g., `frontend/`).
   - Install dependencies:
     ```bash
     npm install
     ```
   - Start the React development server:
     ```bash
     npm start
     ```
   - The React app will be available at [http://localhost:3000/](http://localhost:3000/).

**Usage 🚀**

- **Access the Application 🌐**
  Open your web browser and go to [http://localhost:9092/](http://localhost:9092/) for the backend, and [http://localhost:3000/](http://localhost:3000/) for the front-end.

- **Login 🔑**
  - **Admin:** Log in with admin credentials to access the admin dashboard for managing users, roles, and inventory.
  - **AI Chat Side:** Users can interact with the chatbot for intelligent assistance powered by AI.

- **Managing Inventory 📦**
  - **Admin:** Perform tasks such as adding, updating, and deleting inventory items. Admins can also manage stock levels and generate detailed reports.

**API Usage 📡**

- **Endpoints:** The application provides various REST API endpoints for interacting with inventory and user data, as well as for AI chat functionalities.
- **Authentication:** API requests are secured using JWT tokens to ensure only authorized users can access protected resources.

**Contributing 🤝**

We welcome contributions! If you have suggestions, improvements, or bug fixes, please open an issue or submit a pull request.

**License 📜**

This project is licensed under the MIT License.

**Contact 📧**

For inquiries or support, please contact:

- **Email:** Rhalimsami8@gmail.com
- **Website:** [Axians Website](https://axians.com)

---
