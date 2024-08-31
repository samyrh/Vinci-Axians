
**Vinci App 🚀**

**Overview:**
Vinci App is an advanced web application developed during my internship at Vinci, under the guidance of Axians. It is designed to streamline the management of material stocks through two distinct user interfaces: Admin and User. The application leverages modern technologies and best practices to provide a robust and secure platform for inventory management.

**Features 🎨**

- **Admin Interface 🛠️**
  - **User Management:** Admins can add, update, and delete user accounts, manage user roles and permissions, and ensure proper access control.
  - **Role and Permission Management:** Admins have full control over defining and managing roles and permissions, ensuring that users have appropriate access based on their responsibilities.
  - **Inventory Management:** Admins can manage inventory items, including adding, updating, and deleting stock entries. They can view detailed reports and monitor stock levels to ensure optimal inventory management.
  - **Advanced Reporting:** Generate and view comprehensive reports on inventory status, user activities, and system performance.

- **User Interface 🧑‍💼**
  - **Personal Information Management:** Users can update their personal details, such as contact information and profile picture, directly from their dashboard.
  - **Inventory Interaction:** Users can view and interact with inventory items, such as checking item details, stock levels, and availability. Depending on their permissions, users may also be able to request or reserve items.

**Technologies Used 🛠️**

- **Spring Boot 3.3.1** 🌟: The core framework for building the application's backend, providing a comprehensive suite of features for developing and running Java-based applications.
- **Spring Security 6** 🔒: Ensures advanced security through authentication and authorization mechanisms, protecting both user data and application integrity.
- **JPA (Java Persistence API)** 💾: Manages database interactions using object-relational mapping, simplifying data handling and database operations.
- **DTO (Data Transfer Objects)** 📦: Facilitates efficient data transfer between different layers of the application, reducing overhead and improving performance.
- **JWT (JSON Web Tokens)** 🗝️: Secures user sessions and API requests with token-based authentication, enhancing security and scalability.
- **MySQL** 🗄️: The relational database management system used for storing and managing application data, offering reliability and performance.
- **HTML/CSS/JavaScript** 🌐: Technologies used to create a responsive and interactive front-end, ensuring a seamless user experience across various devices.
- **API** 🔗: Exposes RESTful endpoints for interaction between the backend services and the front-end components, enabling smooth communication and data exchange.
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

   - **With Docker:**
     Build and run the application using Docker:
     ```bash
     docker-compose up --build
     ```

**Usage 🚀**

- **Access the Application 🌐**
  Open your web browser and go to [http://localhost:9092/](http://localhost:9092/) to access the application.

- **Login 🔑**
  - **Admin:** Log in with admin credentials to access the admin dashboard for managing users, roles, and inventory.
  - **User:** Log in with user credentials to manage personal information and interact with inventory items.

- **Managing Inventory 📦**
  - **Admin:** Perform tasks such as adding, updating, and deleting inventory items. Admins can also manage stock levels and generate detailed reports.
  - **User:** View and interact with inventory items based on assigned permissions. Users can request or reserve items as needed.

- **API Usage 📡**
  - **Endpoints:** The application provides various REST API endpoints for interacting with inventory and user data.
  - **Authentication:** API requests are secured using JWT tokens to ensure only authorized users can access protected resources.

**Contributing 🤝**

We welcome contributions! If you have suggestions, improvements, or bug fixes, please open an issue or submit a pull request.

**License 📜**

This project is licensed under the MIT License.

**Contact 📧**

For inquiries or support, please contact:

- **Email:** Rhalimsami8.com
- **Website:** [Axians Website](https://axians.com)
