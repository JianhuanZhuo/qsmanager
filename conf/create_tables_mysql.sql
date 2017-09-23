CREATE TABLE custom
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  serial CHAR(32) DEFAULT '未编号',
  name CHAR(64) DEFAULT '无' NOT NULL,
  phone CHAR(64),
  fax CHAR(64),
  accpb CHAR(64),
  accpv CHAR(64),
  addr CHAR(128),
  note VARCHAR(255),
  psw CHAR(32),
  acc CHAR(32) DEFAULT ' ' NOT NULL,
  utype INT DEFAULT 2 NOT NULL,
  namefull CHAR(128),
  bccpb CHAR(128),
  bccpv CHAR(128)
);
CREATE TABLE supply
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  serial CHAR(32) DEFAULT '未编号',
  phone CHAR(64),
  fax CHAR(64),
  accpb CHAR(64),
  accpv CHAR(64),
  addr CHAR(128),
  name CHAR(64) DEFAULT '无' NOT NULL,
  note VARCHAR(255),
  namefull CHAR(128),
  bccpb CHAR(128),
  bccpv CHAR(128)
);
CREATE TABLE cust_annual
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  cid INTEGER NOT NULL,
  year INT NOT NULL,
  remainder DECIMAL(15,5) DEFAULT 0 NOT NULL,
  CONSTRAINT sup_annual_supply_id_fk FOREIGN KEY (cid) REFERENCES custom (id)
);

CREATE INDEX cust_annual_id_cid_index ON cust_annual (cid, year);


CREATE TABLE cust_annual_mon
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  caid INTEGER NOT NULL,
  mon INT NOT NULL,
  total DECIMAL(15,5) DEFAULT 0,
  billunit CHAR(64),
  billdate timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  billtotal DECIMAL(15,5) DEFAULT 0 NOT NULL,
  rate DECIMAL(8,5) DEFAULT 0 NOT NULL,
  remitunit CHAR(64),
  pattern CHAR(32),
  remitdate timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  paytotal DECIMAL(15,5) DEFAULT 0 NOT NULL,
  note VARCHAR(255),
  CONSTRAINT cust_annul_mon_cust_annual_id_fk FOREIGN KEY (caid) REFERENCES cust_annual (id)
);

CREATE UNIQUE INDEX custom_acc_uindex ON custom (acc);


CREATE TABLE material
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  sid INTEGER NOT NULL,
  name CHAR(32),
  color CHAR(32),
  spec CHAR(64),
  price DECIMAL(15,5) DEFAULT 0.0 NOT NULL,
  unit CHAR(16) DEFAULT '个' NOT NULL,
  serial CHAR(32),
  CONSTRAINT material_supply_id_fk FOREIGN KEY (sid) REFERENCES supply (id)
);

CREATE INDEX material_sid_id_index ON material (sid, id);

CREATE TABLE mons
(
  mon CHAR(2) PRIMARY KEY
);

CREATE UNIQUE INDEX mons_mon_uindex ON mons (mon);
CREATE TABLE product
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  serial CHAR(32),
  name CHAR(32),
  detail CHAR(32),
  pack INT,
  price DECIMAL(15,5) DEFAULT 0 NOT NULL,
  unit CHAR(16) DEFAULT '个' NOT NULL,
  picurl VARCHAR(255)
);


CREATE TABLE order_favor
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  cid INTEGER NOT NULL,
  pid INTEGER NOT NULL,
  CONSTRAINT order_favor_supply_id_fk FOREIGN KEY (cid) REFERENCES custom (id),
  CONSTRAINT order_favor_product_id_fk FOREIGN KEY (pid) REFERENCES product (id)
);


CREATE TABLE orders
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  serial CHAR(32) NOT NULL,
  cid INTEGER NOT NULL,
  orderdate timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  note CHAR(64),
  deli BOOLEAN DEFAULT false NOT NULL,
  building BOOLEAN DEFAULT FALSE NOT NULL,
  CONSTRAINT order_custom_id_fk FOREIGN KEY (cid) REFERENCES custom (id)
);

CREATE TABLE order_item
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  oid INTEGER NOT NULL,
  num DECIMAL(15,5) DEFAULT 0 NOT NULL,
  rate DECIMAL(8,5) DEFAULT 0 NOT NULL,
  rebate DECIMAL(10,5) DEFAULT 0 NOT NULL,
  delifee DECIMAL(15,5) DEFAULT 0 NOT NULL,
  serial CHAR(32),
  name CHAR(32),
  detail CHAR(32),
  pack INT DEFAULT 1 NOT NULL,
  packDefault INT DEFAULT 1 NOT NULL,
  price DECIMAL(15,5) DEFAULT 0 NOT NULL,
  unit CHAR(16) DEFAULT '个' NOT NULL,
  picurl VARCHAR(255),
  note CHAR(64),
  CONSTRAINT order_item_orders_id_fk FOREIGN KEY (oid) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE UNIQUE INDEX orders_serial_uindex ON orders (serial);


CREATE TABLE receipt
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  sid INTEGER NOT NULL,
  serial CHAR(32) NOT NULL,
  rdate timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  CONSTRAINT receipt_supply_id_fk FOREIGN KEY (sid) REFERENCES supply (id)
);

CREATE TABLE receipt_detail
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  rid INTEGER NOT NULL,
  serial CHAR(32),
  name CHAR(32),
  color CHAR(32),
  price DECIMAL(15,5) DEFAULT 0 NOT NULL,
  unit CHAR(16) DEFAULT '个' NOT NULL,
  num DECIMAL(15,5) DEFAULT 0 NOT NULL,
  spec CHAR(32),
  CONSTRAINT receipt_detail_receipt_id_fk FOREIGN KEY (rid) REFERENCES receipt (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX receipt_detail_rid_id_index ON receipt_detail (rid, id);

CREATE TABLE sup_annual
(
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  sid INTEGER NOT NULL,
  year INT NOT NULL,
  remainder DECIMAL(15,5) DEFAULT 0 NOT NULL,
  CONSTRAINT table_name_supply_id_fk FOREIGN KEY (sid) REFERENCES supply (id)
);


CREATE UNIQUE INDEX table_name_sid_year_uindex ON sup_annual (sid, year);
CREATE TABLE sup_annual_mon
(
  said INTEGER NOT NULL,
  mon INT NOT NULL,
  total DECIMAL(15,5) DEFAULT 0,
  billunit CHAR(64),
  billdate timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  billtotal DECIMAL(15,5) DEFAULT 0 NOT NULL,
  rate DECIMAL(8,5) DEFAULT 0 NOT NULL,
  remitunit CHAR(64),
  pattern CHAR(32),
  remitdate timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  paytotal DECIMAL(15,5) DEFAULT 0 NOT NULL,
  note VARCHAR(255),
  CONSTRAINT sup_annual_mon_said_mon_pk PRIMARY KEY (said, mon),
  CONSTRAINT sup_annul_mon_sup_annual_id_fk FOREIGN KEY (said) REFERENCES sup_annual (id)
);

CREATE UNIQUE INDEX sup_annual_mon_said_mon_uindex ON sup_annual_mon (said, mon);