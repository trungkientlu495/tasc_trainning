# INNER JOIN 
select * from student x inner join class y on x.idClass = y.id
# LEFT JOIN
select * from student x left join class y on x.idClass = y.id
# RIGHT JOIN 
select * from student x right join class y on x.idClass = y.id
# FULL OUTER JOIN
select * from student x full outer join class y on x.idClass = y.id
#CROSS JOIN 
select * from student x cross join class y
# SUBQUERY
#SUBQUERY trong where
# thường dùng với exist , in , not in - exist chỉ cần row có tồn tại ít nhất 1 bản ghi trong subquery -> true
# với in check các row nằm trong subquery - not in ngược lại
select * from student x where x.id in ( select y.id from student y where y.age=22)
#SUBQUERY select
select x.id,x.name,(select count(*) from class x1)  as count_class from student x 
#SUBQUERY FROM - Dùng làm 1 bảng tạm
select * from (select * from student x where x.age = 22) as student_age_22
#SUBQUERY HAVING - Dùng để lọc nhóm sau khi đã group by
SELECT student_id, COUNT(*) AS subjects
FROM Enrollments
GROUP BY student_id
HAVING COUNT(*) > (
    SELECT AVG(subject_count)
    FROM (
        SELECT COUNT(*) AS subject_count
        FROM Enrollments
        GROUP BY student_id
    ) AS t
);
