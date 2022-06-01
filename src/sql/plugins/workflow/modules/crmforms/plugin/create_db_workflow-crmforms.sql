--
-- Table structure for table workflow_task_notify_crm_forms_cf
--
DROP TABLE IF EXISTS workflow_task_notify_crm_forms_cf;
CREATE TABLE workflow_task_notify_crm_forms_cf(
  id_task INT DEFAULT NULL,
  status_text varchar(255) DEFAULT  '' NOT NULL,
  id_status_crm INT DEFAULT 0 NOT NULL,
  sender varchar(255) DEFAULT  '' NOT NULL,
  object varchar(255) DEFAULT  '' NOT NULL,
  message LONG VARCHAR,
  is_demand_crm_creation SMALLINT DEFAULT 1 NOT NULL,
  PRIMARY KEY (id_task)
);
