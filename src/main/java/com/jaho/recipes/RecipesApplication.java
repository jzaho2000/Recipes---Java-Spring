package com.jaho.recipes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.jaho.recipes.domain.Closet;
import com.jaho.recipes.domain.ClosetDAO;
import com.jaho.recipes.domain.ClosetDAOImpl;
import com.jaho.recipes.domain.IngredientsDAOImpl;
import com.jaho.recipes.domain.Materials;
import com.jaho.recipes.domain.MaterialsRepository;
import com.jaho.recipes.domain.Measurements;
import com.jaho.recipes.domain.MeasurementsRepository;
import com.jaho.recipes.domain.Recipes;
import com.jaho.recipes.domain.RecipesRepository;
import com.jaho.recipes.domain.User;
import com.jaho.recipes.domain.UserRepository;

@SpringBootApplication
public class RecipesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipesApplication.class, args);
	}

	
	@Bean
	public CommandLineRunner demo1(MeasurementsRepository repository) {
		return (args) -> {
			// Database is created by using resources/schema.sql
			
			// Insert some demo data
			try {
				repository.save(new Measurements(1L, "kappale", "kpl"));
				repository.save(new Measurements(2L, "centtilitra", "cl"));
				repository.save(new Measurements(3L, "desilitra", "dl"));
				repository.save(new Measurements(4L, "litra", "l"));
				repository.save(new Measurements(5L, "gramma", "g"));
				repository.save(new Measurements(6L, "kilogramma", "kg"));
				repository.save(new Measurements(7L, "teelusikka", "tl"));
				repository.save(new Measurements(8L, "Ruokalusikka", "rkl"));
				repository.save(new Measurements(9L, "tölkki", "tlk"));
			} catch (Exception e) {
				
			}
	       
		};
	}	
	
	
	
	@Bean
	public CommandLineRunner demo2(MaterialsRepository repository) {
		return (args) -> {
			// Database is created by using resources/schema.sql
			
			// Insert some demo data
			try {
				repository.save(new Materials(1L, "Vesi"));
				repository.save(new Materials(2L, "Suola"));
				repository.save(new Materials(3L, "Maito"));
				repository.save(new Materials(4L, "Muna"));
				repository.save(new Materials(5L, "Vehnäjauho"));
				repository.save(new Materials(6L, "Öljy"));
				repository.save(new Materials(7L, "Kerma"));
				repository.save(new Materials(8L, "Sokeri"));
				repository.save(new Materials(9L, "Leivinjauhe"));
				repository.save(new Materials(10L, "Vaniljajäätelö"));
				repository.save(new Materials(11L, "Banaani"));
				repository.save(new Materials(12L, "Mansikka"));
				repository.save(new Materials(13L, "Omena"));
				repository.save(new Materials(14L, "Päärynä"));
				repository.save(new Materials(15L, "Persikka"));
				repository.save(new Materials(16L, "Viinirypäle"));
				repository.save(new Materials(17L, "Kiivi"));
				repository.save(new Materials(18L, "Appelsiini"));
				repository.save(new Materials(19L, "Voita"));
				repository.save(new Materials(20L, "Vaniliinisokeria"));
				repository.save(new Materials(21L, "Kaakaojauhetta"));
			} catch (Exception e) {
				
			}
			
	       
		};
	}
	
	
	@Bean
	public CommandLineRunner demo3(ClosetDAOImpl repository) {
		return (args) -> {
			// Database is created by using resources/schema.sql
			
			// Insert some demo data
			try {
				repository.save(1L, 1L, 1L);
				repository.save(2L, 1L, 2L);
				repository.save(3L, 1L, 3L);
				repository.save(4L, 1L, 4L);
				repository.save(5L, 1L, 5L);
				repository.save(6L, 1L, 6L);
				repository.save(7L, 1L,10L);
				repository.save(8L, 1L,11L);
			} catch (Exception e) {
				
			}
			
		};
	}	
	
	
	@Bean
	public CommandLineRunner demo4(RecipesRepository repository) {
		return (args) -> {
			// Database is created by using resources/schema.sql
			
			// Insert some demo data
			try {
				repository.save(new Recipes(1L, "Banaanipirtelö", 1, 15, 
						  "Lisää aineet korkereunaiseen astiaan ja sekoita sauvasekoittimella kuohkeaksi. Vaihtoehtoisesti sekoita ainekset tehosekoittimella. Tarjoa heti."));
				repository.save(new Recipes(2L, "Mansikkapirtelö", 1, 14, 
						"Lisää aineet korkereunaiseen astiaan ja sekoita sauvasekoittimella kuohkeaksi. Vaihtoehtoisesti sekoita ainekset tehosekoittimella. Tarjoa heti."));
				repository.save(new Recipes(3L, "Lettutaikina", 2 ,75, 
						"Vatkaa munien rakenne rikki. Lisää maito ja muut aineet ja vatkaa tasaiseksi. Anna taikinan turvota noin puolituntia. Rasvaa pannu ja paista lettuja. "));
				repository.save(new Recipes(4L, "Hedelmäsalaatti", 8, 15, 
						"Kuori hedelmät ja huuhtele viinirypäleet. Paloittele makusi mukaan ja lisää hieman lientä, mikäli olet ostanut tölkkihedelmiä. Tarjoile pienissä kulhoissa."));
				repository.save(new Recipes(5L, "Tiikerikakku", 8, 75, 
						"Sulata rasva ja vaahdota se sokerin kanssa. Lisää vaahtoon munat yksi kerrallaan ja vatkaa hyvin. Lisää vehnäjauhot, vaniliinisokeri ja leivinjauhe ja sekoita taikina tasaiseksi. Ota taikinasta 1/3 toiseen kulhoon, jossa sekoitat taikinan joukkoon kaakaojauheen ja kerman. Levitä rengaskakkuvuoan pohjalle voita ja ripottele päälle korppujauhoja. Levitä aluksi kakkuvuoan pohjalle ohut kerros vaaleaa taikinaa, päälle tummaa taikinaa ja lopuksi levitä loput vaaleasta taikinasta. Paista tiikerikakku uunin alaosassa 175 asteessa noin tunnin ajan. Kun otat sen uunista, niin kumoa kakku alustalle ja anna jäähtyä."));
			} catch (Exception e) {
				
			}
	       
		};
	}	
	
	
	@Bean
	public CommandLineRunner demo5(IngredientsDAOImpl repository) {
		return (args) -> {
			// Database is created by using resources/schema.sql
			
			// Insert some demo data
			try {
				repository.createData(1,  1,  3,  3, 2);
				repository.createData(2,  1, 11,  1, 1);
				repository.createData(3,  1, 10,  3, 0.5);
				
				repository.createData(4,  2,  3,  3, 2);
				repository.createData(5,  2, 12,  5, 500);
				repository.createData(6,  2, 10,  3, 0.5);
	
				repository.createData(7,  3,  4,  1, 3);
				repository.createData(8,  3,  3,  3, 6);
				repository.createData(9,  3,  5,  3, 3);
				repository.createData(10, 3,  8,  8, 1);
				repository.createData(11, 3,  2,  7, 1);
	
				repository.createData(12,  4, 13, 1, 2);
				repository.createData(13,  4, 14, 1, 2);
				repository.createData(14,  4, 15, 9, 1);
				repository.createData(15,  4, 16, 5, 100);
				repository.createData(16,  4, 17, 1, 2);
				repository.createData(17,  4, 18, 1, 2);
				repository.createData(18,  4, 11, 1, 2);
	
				repository.createData(19, 5, 19,  5, 250 );
				repository.createData(20, 5,  8,  3, 2);
				repository.createData(21, 5,  4,  1, 3);
				repository.createData(22, 5,  5,  3, 3.5);
				repository.createData(23, 5, 20,  7, 2);
				repository.createData(24, 5,  9,  7, 2);
				repository.createData(25, 5, 21,  8, 3);
				repository.createData(26, 5,  7,  8, 3);
			} catch (Exception e) {
				
			}
		};
	}	
	
	
	
	@Bean
	public CommandLineRunner demo6(UserRepository repository) {
		return (args) -> {
			// Database is created by using resources/schema.sql
			
			// Insert some demo data
			try {
				repository.save(new User(1L, "user", "$2a$10$1DTvwpXVBArGFixHBuzVJObjTuXhIOkx5pse6KsYs8/C2ckxnGEou", "USER"));
				repository.save(new User(2L, "admin", "$2a$10$cDZgyF4xaPMmmoRW3OVcmuf.8o2YSx8.M7CeRKqi.1PVw.t3E8uEC", "ADMIN"));
			} catch (Exception e) {
				
			}

		};
	}	
	
}
