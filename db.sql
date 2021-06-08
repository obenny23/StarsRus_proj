SET foreign_key_checks = 0;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Account;
DROP TABLE IF EXISTS Market_Account;
DROP TABLE IF EXISTS Stock_Account;
DROP TABLE IF EXISTS Transactions;
DROP TABLE IF EXISTS buy;
DROP TABLE IF EXISTS sell;
DROP TABLE IF EXISTS Withdrawal;
DROP TABLE IF EXISTS deposit;
DROP TABLE IF EXISTS accrue_interest;
DROP TABLE IF EXISTS Stock;
DROP TABLE IF EXISTS Actor;
DROP TABLE IF EXISTS Contracts;
SET foreign_key_checks = 1;

CREATE TABLE Customers(
	tid int(9) UNIQUE, 
	username varchar(255) PRIMARY KEY, 
	password varchar(255),
	cname varchar(100),
	state char(2),
	phonenumber varchar(11),
	email varchar(64),
    ssn varchar(11)
	);

CREATE TABLE Account(
	aid int(100) AUTO_INCREMENT,
	uid varchar(64) NOT NULL,
	PRIMARY KEY(aid, uid),
	FOREIGN KEY(uid) REFERENCES Customers(username)
		ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE Market_Account(
	aid int(100) NOT NULL PRIMARY KEY,
	balance float(100, 3) NOT NULL,
	avg_daily float(100, 3),
	FOREIGN KEY (aid) REFERENCES Account(aid)
		ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE Stock_Account(
	aid int(100) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	uid int(100) NOT NULL,
	sid int(100) NOT NULL,
	num_shares int(100),
	FOREIGN KEY (uid) REFERENCES Account(aid)
		ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (sid) REFERENCES Stock(sid) 
        ON DELETE CASCADE
	);

CREATE TABLE Transactions(
	transaction_id int(100) AUTO_INCREMENT PRIMARY KEY,
	aid int(100) NOT NULL,
	sys_date varchar(100),
	date bigint(255) NOT NULL,
	FOREIGN KEY (aid) REFERENCES Account(aid)
		ON DELETE CASCADE
	);

CREATE TABLE deposit(
	tr_id int(100) NOT NULL PRIMARY KEY,
	amount float(13, 3) NOT NULL,
	FOREIGN KEY(tr_id) REFERENCES Transactions(transaction_id)
		ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE Withdrawal(
	tr_id int(100) NOT NULL PRIMARY KEY,
	amount float(13,3) NOT NULL,
	FOREIGN KEY (tr_id) REFERENCES Transactions(transaction_id)
		ON UPDATE CASCADE ON DELETE CASCADE
	);

CREATE TABLE buy(
	tr_id int(100) NOT NULL PRIMARY KEY,
  	sid int(100) NOT NULL,
  	amount float(13, 3) NOT NULL,
  	price float(13, 3) NOT NULL,
  	FOREIGN KEY (tr_id)	REFERENCES Transactions(transaction_id)
    	ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (sid) REFERENCES Stock(sid) 
        ON DELETE CASCADE
	);

CREATE TABLE sell (
	tr_id int(100) NOT NULL PRIMARY KEY,
  	sid int(100) NOT NULL,
  	amount float(13, 3) NOT NULL,
  	price float(13, 3) NOT NULL,
  	FOREIGN KEY (tr_id) REFERENCES Transactions(transaction_id)
    	ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (sid) REFERENCES Stock(sid) 
        ON DELETE CASCADE
	);

CREATE TABLE accrue_interest (
  	tr_id int(100) NOT NULL PRIMARY KEY,
  	amount float(7, 3),
  	FOREIGN KEY (tr_id) REFERENCES Transactions(transaction_id)
    	ON UPDATE CASCADE ON DELETE CASCADE
  	);
  
CREATE TABLE Stock (
	sid int(100) AUTO_INCREMENT PRIMARY KEY,
	aid int(100) NOT NULL,
	ssym char(3) NOT NULL,
	closing_price float(13,3),
	current_price float(13,3),
	active enum('0', '1')
    FOREIGN KEY (aid) REFERENCES Actor(aid) 
        ON UPDATE CASCADE
  );
  
CREATE TABLE Actor (
	aid int(100) AUTO_INCREMENT PRIMARY KEY,
	aname varchar(100),
	sid int(100),
	dob varchar(100),
	FOREIGN KEY (sid) REFERENCES Stock(sid)
	    ON UPDATE CASCADE
  );
  
CREATE TABLE Contracts (
	mid int(100) NOT NULL, 
	aid int(100) NOT NULL,
	title varchar(255) NOT NULL,
	role enum('Actor', 'Director', 'Both'),
	year_released int(100),
	value int(100) NOT NULL,
	PRIMARY KEY(mid, aid),
  	FOREIGN KEY (aid)
    	REFERENCES Actor(aid)
  );


--Data Insertions-- 


INSERT INTO Customers(cname, username, password, address, state, phonenumber, email, tid, ssn)
	VALUES  ("John Admin","admin","secret","Stock Company SB","CA","(805)6374632","admin@stock.com",1000,"606-60-6060"),
            ("Alfred Hitchcock","alfred","hi","6667 El Colegio #40 SB","CA","(805)2574499", "alfred@hotmail.com",1022,"606-76-7900"),
            ("Billy Clinton","billy","cl","5777 Hollister SB","CA","(805)5629999","billy@yahoo.com",3045,"606-76-7903"),
            ("Cindy Laugher","cindy","la","7000 Hollister SB","CA","(805)6930011","cindy@hotmail.com",2034,"606-70-7900"),
            ("David Copperfill","david","co","1357 State St SB","CA","(805)8240011","david@yahoo.com",4093,"506-78-7900"),
            ("Elizabeth Sailor","sailor","sa","4321 State St SB","CA","(805)1234567","sailor@hotmail.com",1234,"436-76-7900"),
            ("George Brush","brush","br","5346 Foothill Av","CA","(805)1357999","george@hotmail.com",8956,"632-45-7900"),
            ("Ivan Stock","ivan","st","1235 Johnson Dr","NJ","(805)3223243","ivan@yahoo.com",2341,"609-23-7900"),
            ("Joe Pepsi","joe","pe","3210 State St","CA","(805)5668123","pepsi@pepsi.com",0456,"646-76-3430"),
            ("Magic Jordon","magic","jo,3852 Court Rd","NJ","(805)4535539","jordon@jordon.org",3455,"646-76-8843"),
            ("Olive Stoner","olive","st,6689 El Colegio #151","CA","(805)2574499","olive@yahoo.com",1123,"645-34-7900"),
            ("Frank Olson","frank,ol,6910 Whittier Dr","CA","(805)3456789","frank@gmail.com",3306,"345-23-2134");

INSERT INTO Account(uid)
	VALUES ("admin"),
		   ("alfred"),
		   ("billy"),
		   ("cindy"),
		   ("david"),
		   ("sailor"),
		   ("brush"),
		   ("ivan"),
		   ("joe"),
		   ("magic"),
		   ("olive"),
		   ("frank");

INSERT INTO Actor(ssym, aname, dob)
	VALUES ("SKB","Kim Basinger", "12-08-1958"),
            ("SMD","Michael Douglas","09-25-1944"),
            ("STC","Tom Cruise","07-03-1962");

INSERT INTO Contracts(mid, aid, title, role, year_released, value)
	VALUES (1, 1, "L.A. Confidential", "Actor", 1997, 5000000),
		   (2, 2, "Perfect Murder", "Actor", 1998, 10000000),
		   (3, 3, "Jerry Maguire", "Actor", 1996, 5000000);

INSERT INTO Stock(aid, symbol, closing_price, current_price, active)
	VALUES (1, "SKB", 40.00, 40.00, '1'),
		   (2, "SMD", 71.00, 71.00, '1'),
		   (3, "STC", 32.50, 32.50, '1');

INSERT INTO Market_Account(tid, aid, balance)
	VALUES (1022, 001,10000),
		    (3045,002,100000),
            (2034,003,50000),
            (4093,004,45000),
            (1234,005,200000),
            (8956,006,5000),
            (2341,007,2000),
            (0456,008,10000),
            (3455,009,130200),
            (1123,010,35000),
            (3306,011,30500);

INSERT INTO Stock_Account(uid, num_shares, sid)
	VALUES (2, 100, 1),
		   (3, 500, 2),
		   (3, 100, 3),
		   (4, 250, 3),
		   (5, 100, 1),
		   (5, 500, 2),
		   (5, 50, 3),
		   (6, 1000, 2),
		   (7, 100, 1),
		   (8, 300, 2),
		   (9, 500, 1),
		   (9, 100, 3),
		   (9, 200, 2),
		   (10, 1000, 1),
		   (11, 100, 1),
		   (11, 100, 2),
		   (11, 100, 3),
		   (12, 100, 1),
		   (12, 200, 3),
		   (12, 100, 2);

UPDATE Actor SET sid = 1 WHERE aid = 1;
UPDATE Actor SET sid = 2 WHERE aid = 2;
UPDATE Actor SET sid = 3 WHERE aid = 3;