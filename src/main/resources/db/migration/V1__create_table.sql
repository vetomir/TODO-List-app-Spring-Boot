drop table if exists tasks;
create table tasks(
    id int primary key auto_increment,
    description varchar(35) not null,
    done bit,
    deadline datetime null
)
