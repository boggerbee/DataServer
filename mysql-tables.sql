drop table TankEvent;
create table TankEvent (
	ts timestamp default CURRENT_TIMESTAMP,
	id varchar(20),
	level float,
	flow float,
	state varchar(10),
	pumpState varchar(10),
	valveState varchar(10),
	switchState varchar(10)
);
alter table TankEvent add primary key (ts,id);

-- alter table TankEvent add switchState varchar(10);
-- update TankEvent set switchState = 'OPEN';
drop table ControllerEvent;
create table ControllerEvent (
	ts timestamp default CURRENT_TIMESTAMP,
	id varchar(20),
	mode varchar(10),
	flow bigint, 
	_key varchar(10),
	_value varchar(10)
);
alter table ControllerEvent add primary key (ts,id,_key);
alter table ControllerEvent add mode varchar(10);
update ControllerEvent set mode = 'FULL';
alter table ControllerEvent add flow bigint;
update ControllerEvent set flow=0;

