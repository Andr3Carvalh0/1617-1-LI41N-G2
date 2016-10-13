/*
Cl = Checklist
Tp = Template
*/
use LS

create table template(Tp_id int, Tp_name varchar(80) NOT NULL, Tp_desc varchar(4000) NOT NULL,
primary key(Tp_id)
)

create table checklist(Cl_id int identity (0, 1), Cl_name varchar(80) NOT NULL, Cl_desc varchar(4000) NOT NULL, Cl_closed bit default 0, Cl_duedate datetime, Tp_id int, 
primary key(Cl_id),
foreign key(Tp_id)REFERENCES template(Tp_id)
)

create table checklist_task(Cl_id int, Cl_Task_id int, Cl_Task_index int, Cl_Task_closed bit default 0, Cl_Task_name varchar(80) NOT NULL, Cl_Task_desc varchar(4000) NOT NULL, Cl_Task_duedate datetime,
primary key(Cl_id, Cl_Task_id),
foreign key(Cl_id)REFERENCES checklist(Cl_id)
)

create table template_task(Tp_id int, Tp_Task_id int, Tp_Task_name varchar(80) NOT NULL, Tp_Task_desc varchar(4000) NOT NULL,
primary key(Tp_id, Tp_Task_id),
foreign key(Tp_id)REFERENCES template(Tp_id)
)

insert into checklist(Cl_name, Cl_duedate, Cl_desc) values ('teste', cast('06-10-2016' as datetime), 'jdfklmfgsdnfgsdoijgf')
select * from checklist
delete from checklist