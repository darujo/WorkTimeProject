UPDATE work_time_project."work"  w
SET w.develop_end_fact  = 
(
   select work_date  from work_time wt where task_id in (select id from task t where work_id = w.id)
                                         and (wt.type = 1 or wt.type = 4)
                                         and (wt.work_date <= w.issue_prototype_fact or w.issue_prototype_fact isnull)  
   order by wt.work_date desc limit 1
)
where true;