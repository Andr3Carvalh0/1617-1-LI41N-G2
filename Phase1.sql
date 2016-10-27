/*
Cl = Checklist
Tp = Template
*/
use LS_test
use LS

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
	foreign key(Tg_id)REFERENCES tag(Tg_id) on delete cascade, --confirmar
	foreign key(Cl_id)REFERENCES checklist(Cl_id) on delete cascade --confirmar
)



------------------------------------------------------------------
insert into checklist(Cl_name, Cl_duedate, Cl_desc) values ('teste', cast('06-10-2016' as datetime), 'jdfklmfgsdnfgsdoijgf')
select * from checklist
select * from checklist_task
select * from template
select * from template_task

delete from checklist
delete from checklist_task
delete from template_task
delete from template
drop table template

select Cl_Task_index from checklist_task order by Cl_Task_index DESC
select * from checklist
select * from checklist_task
select max(Cl_Task_id) from checklist_task
insert into checklist_task(Cl_id, Cl_Task_name, Cl_Task_desc)
                    values (18, 'teste', 'desc teste')


select * from checklist_task where Cl_id = 18 and Cl_Task_closed = 0
select COUNT(*) from checklist_task where Cl_id = 17 and Cl_Task_closed = 0




-- aux scripts --
select * from checklist
select * from checklist_task
select * from template_task
select * from template
select * from tag
select * from tag_checklist

drop table tag_checklist
drop table tag
drop table checklist_task
drop table checklist
drop table template_task
drop table template

insert into template values ('template_test','template_for_test')
insert into template_task values(0, 'task_test', 'task_for_test')

delete from template

UPDATE checklist SET Cl_closed = 1 WHERE Cl_id = 0

insert into checklist values('one', 'the one', 0, '2016-10-31',null)
insert into checklist values('two', 'the second', 0, '2016-10-21',null)
insert into checklist values('three', 'the third', 0, '2017-10-31',null)
insert into checklist values('four', 'the fourth', 0, '2016-11-4',null)



select * from checklist
where Cl_closed = 0
order by Cl_duedate
--1032

delete from checklist where Cl_id = -1