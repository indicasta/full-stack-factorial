package com.indicasta.fullstackfactorial;

import com.indicasta.fullstackfactorial.customer.Customer;
import com.indicasta.fullstackfactorial.customer.CustomerRepository;
import com.indicasta.fullstackfactorial.customer.Role;
import com.indicasta.fullstackfactorial.s3.S3Bucket;
import com.indicasta.fullstackfactorial.s3.S3Service;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;


/**
 * The type Full stack factorial application.
 */
@SpringBootApplication
@Slf4j
public class FullStackFactorialApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FullStackFactorialApplication.class, args);
	}

	/**
	 * Runner command line runner.
	 *
	 * @param customerRepository the customer repository
	 * @param passwordEncoder    the password encoder
	 * @param s3Service          the Amazon s3 service
	 * @param s3Bucket           the Amazon s3 bucket
	 * @return the command line runner
	 */
	@Bean
	CommandLineRunner runner(
			CustomerRepository customerRepository,
			PasswordEncoder passwordEncoder,
			S3Service s3Service,
			S3Bucket s3Bucket) {
		return args -> {
//             uploading2S3DownloadingFromS3(s3Service, s3Bucket);
			createRandomCustomer(customerRepository, passwordEncoder);
		};
	}

	private static void uploading2S3DownloadingFromS3(S3Service s3Service, S3Bucket s3Bucket) {
		s3Service.putObjectIntoS3(s3Bucket.getCustomer(),"application", "Hello Factorial!".getBytes());

		byte[] objectFromS3 = s3Service.getObjectFromS3(s3Bucket.getCustomer(),"factorial");

		System.out.println("Indira: " + new String(objectFromS3));
	}

	private static void createRandomCustomer(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
		Faker faker = new Faker();
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		Random random = new Random();
		int age = random.nextInt(16, 75);
		String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@factorial.com";
		Customer customer = Customer.builder()
				.firstname(firstName)
				.lastname(lastName)
				.age(age)
				.email(email)
				.password(passwordEncoder.encode("secret"))
				.role(Role.USER)
				.build();
		customerRepository.save(customer);
        log.info("Saving customer {} to database", customer.getEmail());
	}
}
