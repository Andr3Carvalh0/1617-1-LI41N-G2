if object_id('dbo.tag_checklist', 'U') is not null drop table tag_checklist;
if object_id('dbo.tag', 'U') is not null drop table tag;
if object_id('template_task', 'U') is not null drop table template_task;
if object_id('dbo.checklist_task', 'U') is not null drop table checklist_task;
if object_id('dbo.checklist', 'U') is not null drop table checklist;
if object_id('dbo.template', 'U') is not null drop table template;

create table template(
	Tp_id int identity (0, 1), 
	Tp_name varchar(80) NOT NULL, 
	Tp_desc varchar(4000) NOT NULL,
primary key(Tp_id)
)

create table checklist(
	Cl_id int identity (0, 1), 
	Cl_name varchar(80) NOT NULL, 
	Cl_desc varchar(4000) NOT NULL, 
	Cl_closed bit default 0, 
	Cl_duedate datetime, 
	Tp_id int, 
primary key(Cl_id),
foreign key(Tp_id)REFERENCES template(Tp_id) on delete cascade
)

create table checklist_task(
	Cl_Task_id int identity (0, 1), 
	Cl_id int, 
	Cl_Task_index int, 
	Cl_Task_closed bit default 0, 
	Cl_Task_name varchar(80) NOT NULL, 
	Cl_Task_desc varchar(4000) NOT NULL, 
	Cl_Task_duedate datetime,
primary key(Cl_id, Cl_Task_id),
foreign key(Cl_id)REFERENCES checklist(Cl_id) on delete cascade
)

create table template_task(
	Tp_id int, 
	Tp_Task_id int identity(0, 1), 
	Tp_Task_name varchar(80) NOT NULL, 
	Tp_Task_desc varchar(4000) NOT NULL,
primary key(Tp_id, Tp_Task_id),
foreign key(Tp_id)REFERENCES template(Tp_id) on delete cascade
)

create table tag(
	Tg_id int identity (0, 1),
	Tg_name varchar(80) NOT NULL,
	Tg_color varchar(10) NOT NULL,
	primary key(Tg_id)
)

create table tag_checklist(
	Tg_id int,
	Cl_id int,
	foreign key(Tg_id)REFERENCES tag(Tg_id) ,
	foreign key(Cl_id)REFERENCES checklist(Cl_id)
)