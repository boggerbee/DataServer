drop table TankEvent;
create table TankEvent (
	ts timestamp default CURRENT_TIMESTAMP,
	id varchar(20),
	level float,
	flow float,
	state varchar(10),
	pumpState varchar(10),
	valveState varchar(10)
)