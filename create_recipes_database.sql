drop table if exists ingredients;
drop table if exists closet;
drop table if exists materials;
drop table if exists measurements;
drop table if exists recipes;

drop table if exists users;


CREATE TABLE recipes (
	recipe_id BIGINT NOT NULL AUTO_INCREMENT,
	title VARCHAR(100) NOT NULL,
	portionsize INT DEFAULT 1,
	minutes INT DEFAULT 0,
	recipe TEXT NOT NULL,
	PRIMARY KEY (recipe_id),
	CONSTRAINT recipes_title UNIQUE(title) 
);

CREATE TABLE measurements (
	measure_id BIGINT NOT NULL AUTO_INCREMENT,
	measure VARCHAR(50),
	short_text   VARCHAR(5),
	PRIMARY KEY (measure_id),
	CONSTRAINT measures_measure UNIQUE(measure) 
);


CREATE TABLE materials (
	material_id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	PRIMARY KEY(material_id),
	CONSTRAINT materials_name UNIQUE(name) 
);


CREATE TABLE ingredients (
	ingredient_id BIGINT NOT NULL AUTO_INCREMENT,
	recipe_id BIGINT NOT NULL,
	material_id BIGINT NOT NULL,
	measure_id  BIGINT NOT NULL,
	amount      DOUBLE NOT NULL,
	PRIMARY KEY (ingredient_id),
	FOREIGN KEY (recipe_id) REFERENCES recipes(recipe_id),
	FOREIGN KEY (material_id) REFERENCES materials(material_id),
	FOREIGN KEY (measure_id) REFERENCES measurements(measure_id)
);




CREATE TABLE closet (
	closet_id BIGINT NOT NULL AUTO_INCREMENT,
	user_id BIGINT NOT NULL,
	material_id BIGINT NOT NULL,
	PRIMARY KEY (closet_id),
	FOREIGN KEY (user_id) REFERENCES user(id),
	FOREIGN KEY (material_id) REFERENCES materials(material_id)
);





INSERT INTO measurements (measure_id, measure, short_text) VALUES
(1, 'kappale', 'kpl'),
(2, 'centtilitra', 'cl'),
(3, 'desilitra', 'dl'),
(4, 'litra', 'l'),
(5, 'gramma', 'g'),
(6, 'kilogramma', 'kg'),
(7, 'teelusikka', 'tl'),
(8, 'Ruokalusikka', 'rkl'),
(9, 'tölkki', 'tlk');

INSERT INTO materials(material_id, name) VALUES
(1, 'Vesi'),
(2, 'Suola'),
(3, 'Maito'),
(4, 'Muna'),
(5, 'Vehnäjauho'),
(6, 'Öljy'),
(7, 'Kerma'),
(8, 'Sokeri'),
(9, 'Leivinjauhe'),
(10, 'Vaniljajäätelö'),
(11, 'Banaani'),
(12, 'Mansikka'),
(13, 'Omena'),
(14, 'Päärynä'),
(15, 'Persikka'),
(16, 'Viinirypäle'),
(17, 'Kiivi'),
(18, 'Appelsiini'),
(19, 'Voita'),
(20, 'Vaniliinisokeria'),
(21, 'Kaakaojauhetta');



INSERT INTO closet(closet_id, user_id, material_id) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 1, 5),
(6, 1, 6),
(7, 1,10),
(8, 1,11);

INSERT INTO recipes(recipe_id, title, portionsize, minutes, recipe) VALUES 
(1, 'Banaanipirtelö', 1, 15, 'Lisää aineet korkereunaiseen astiaan ja sekoita sauvasekoittimella kuohkeaksi. Vaihtoehtoisesti sekoita ainekset tehosekoittimella. Tarjoa heti.'),
(2, 'Mansikkapirtelö', 1, 14, 'Lisää aineet korkereunaiseen astiaan ja sekoita sauvasekoittimella kuohkeaksi. Vaihtoehtoisesti sekoita ainekset tehosekoittimella. Tarjoa heti.'),
(3, 'Lettutaikina', 2 ,75, 'Vatkaa munien rakenne rikki. Lisää maito ja muut aineet ja vatkaa tasaiseksi. Anna taikinan turvota noin puolituntia. Rasvaa pannu ja paista lettuja. '),
(4, 'Hedelmäsalaatti', 8, 15, 'Kuori hedelmät ja huuhtele viinirypäleet. Paloittele makusi mukaan ja lisää hieman lientä, mikäli olet ostanut tölkkihedelmiä. Tarjoile pienissä kulhoissa.'),
(5, 'Tiikerikakku', 8, 75, 'Sulata rasva ja vaahdota se sokerin kanssa. Lisää vaahtoon munat yksi kerrallaan ja vatkaa hyvin. Lisää vehnäjauhot, vaniliinisokeri ja leivinjauhe ja sekoita taikina tasaiseksi. Ota taikinasta 1/3 toiseen kulhoon, jossa sekoitat taikinan joukkoon kaakaojauheen ja kerman. Levitä rengaskakkuvuoan pohjalle voita ja ripottele päälle korppujauhoja. Levitä aluksi kakkuvuoan pohjalle ohut kerros vaaleaa taikinaa, päälle tummaa taikinaa ja lopuksi levitä loput vaaleasta taikinasta. Paista tiikerikakku uunin alaosassa 175 asteessa noin tunnin ajan. Kun otat sen uunista, niin kumoa kakku alustalle ja anna jäähtyä.');


INSERT into ingredients(ingredient_id, recipe_id, material_id, measure_id, amount) VALUES 
(1,  1,  3,  3, 2),
(2,  1, 11,  1, 1),
(3,  1, 10,  3, 0.5),

(4,  2,  3,  3, 2),
(5,  2, 12,  5, 500),
(6,  2, 10,  3, 0.5),

(7,  3,  4,  1, 3),
(8,  3,  3,  3, 6),
(9,  3,  5,  3, 3),
(10, 3,  8,  8, 1),
(11, 3,  2,  7, 1),

(12,  4, 13, 1, 2),
(13,  4, 14, 1, 2),
(14,  4, 15, 9, 1),
(15,  4, 16, 5, 100),
(16,  4, 17, 1, 2),
(17,  4, 18, 1, 2),
(18,  4, 11, 1, 2),

(19, 5, 19,  5, 250 ),
(20, 5,  8,  3, 2),
(21, 5,  4,  1, 3),
(22, 5,  5,  3, 3.5),
(23, 5, 20,  7, 2),
(24, 5,  9,  7, 2),
(25, 5, 21,  8, 3),
(26, 5,  7,  8, 3);




CREATE TABLE user (
	id BIGINT NOT NULL AUTO_INCREMENT,
	username VARCHAR(250) NOT NULL,
	password VARCHAR(250) NOT NULL,
	role VARCHAR(20) NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT user_username UNIQUE(username) 
);


INSERT INTO user (id, username, password, role) 
VALUES (1, "user", "$2a$10$1DTvwpXVBArGFixHBuzVJObjTuXhIOkx5pse6KsYs8/C2ckxnGEou", "USER"), 
(2, "admin", "$2a$10$cDZgyF4xaPMmmoRW3OVcmuf.8o2YSx8.M7CeRKqi.1PVw.t3E8uEC", "ADMIN");











