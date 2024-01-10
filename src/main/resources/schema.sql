create table if not exists Taco_Order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    delivery_Name varchar(50) not null,
    delivery_Street varchar(50) not null,
    delivery_City varchar(50) not null,
    delivery_State varchar(2) not null,
    delivery_Zip varchar(10) not null,
    cc_number varchar(16) not null,
    cc_expiration varchar(5) not null,
    cc_cvv varchar(3) not null,
    placed_at timestamp not null
);

create table if not exists Taco (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name varchar(50) not null,
    taco_order int not null,
    taco_order_key int not null,
    foreign key (taco_order) references taco_order(id),
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
    taco_key int not null,
    foreign key (ingredient) references ingredient(id),
    foreign key (taco) references taco(id)
);