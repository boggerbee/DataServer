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

-- alter table TankEvent add switchState varchar(10);
-- update TankEvent set switchState = 'OPEN';
-- alter table TankEvent add primary key (ts,id);