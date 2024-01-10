create table if not exists Taco_Order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    delivery_Name varchar(50) not null,
    delivery_Street varchar(50) not null,
    delivery_City varchar(50) not null,
    delivery_State varchar(2) not null,
    delivery_Zip varchar(10) not null,
    cc_Number varchar(16) not null,
    cc_Expiration varchar(5) not null,
    ccCvv varchar(3) not null,
    placed_at timestamp not null
);

create table if not exists Taco (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name varchar(50) not null,
    taco_order_id int not null,
    foreign key (taco_order_id) references taco_order(id),
    created_at timestamp not null
);

create table if not exists Ingredient (
    id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(25) not null,
    type varchar(10) not null
);

create table if not exists Ingredient_Ref (
    ingredient int not null,
    taco int not null,
    foreign key (ingredient) references ingredient(id),
    foreign key (taco) references taco(id)
);