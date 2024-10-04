package vinci.ma.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vinci.ma.inventory.dao.entities.Permission;
import vinci.ma.inventory.dao.enums.PermissionType;

@SpringBootApplication
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

}
/*		// Generate a new HmacSHA256 SecretKey
		KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
		SecretKey secretKey = keyGen.generateKey();

		// Convert the SecretKey to a Base64 encoded string
		String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

		// Print the Base64 encoded key
		System.out.println("Generated SecretKey (Base64): " + encodedKey);*/