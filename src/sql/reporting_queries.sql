/* Put your final project reporting queries here */
USE cs_hu_310_final_project;

SELECT 
	 students.first_name, 
    students.last_name,
	 COUNT(class_registration_id) AS number_of_classes,
    SUM(convert_to_grade_point(grades.letter_grade)) AS total_grade_points_earned,
    AVG(convert_to_grade_point(grades.letter_grade)) AS GPA
FROM class_registrations
LEFT JOIN students ON class_registrations.student_id = students.student_id
LEFT JOIN grades ON class_registrations.grade_id = grades.grade_id
WHERE students.student_id = 1;
                               
SELECT 
	students.first_name, 
    students.last_name,
	COUNT(class_registration_id) AS number_of_classes,
    SUM(convert_to_grade_point(grades.letter_grade)) AS total_grade_points_earned,
    AVG(convert_to_grade_point(grades.letter_grade)) AS GPA
FROM class_registrations
LEFT JOIN students ON class_registrations.student_id = students.student_id
LEFT JOIN grades ON class_registrations.grade_id = grades.grade_id
GROUP BY students.student_id;
                               
SELECT 
	classes.code, 
    classes.name,
	COUNT(class_registrations.grade_id) AS number_of_grades,
	SUM(convert_to_grade_point(grades.letter_grade)) AS total_grade_points,
    AVG(convert_to_grade_point(grades.letter_grade)) AS AVG_GPA
FROM class_registrations
LEFT JOIN class_sections ON class_registrations.class_section_id = class_sections.class_section_id
LEFT JOIN classes ON classes.class_id = class_sections.class_id
LEFT JOIN grades ON class_registrations.grade_id = grades.grade_id
GROUP BY classes.class_id;                               
                               
SELECT 
	classes.code, 
    classes.name,
    terms.name,
	COUNT(class_registrations.grade_id) AS number_of_grades,
	SUM(convert_to_grade_point(grades.letter_grade)) AS total_grade_points,
    AVG(convert_to_grade_point(grades.letter_grade)) AS AVG_GPA
FROM class_registrations
LEFT JOIN class_sections ON class_registrations.class_section_id = class_sections.class_section_id
LEFT JOIN classes ON classes.class_id = class_sections.class_id
LEFT JOIN grades ON class_registrations.grade_id = grades.grade_id
LEFT JOIN terms ON class_sections.term_id = terms.term_id
GROUP BY class_sections.class_section_id;
                               
SELECT 
	first_name,
    last_name,
    academic_titles.title,
    classes.code,
    classes.name AS class_name,
    terms.name
FROM instructors
LEFT JOIN academic_titles ON academic_titles.academic_title_id = instructors.academic_title_id
LEFT JOIN class_sections ON class_sections.instructor_id = instructors.instructor_id
LEFT JOIN classes ON classes.class_id = class_sections.class_id
LEFT JOIN terms ON class_sections.term_id = terms.term_id
WHERE instructors.instructor_id = 1;

SELECT 
	classes.code, 
    classes.name,
    terms.name,
    first_name,
    last_name
FROM instructors
LEFT JOIN academic_titles ON academic_titles.academic_title_id = instructors.academic_title_id
LEFT JOIN class_sections ON class_sections.instructor_id = instructors.instructor_id
LEFT JOIN classes ON classes.class_id = class_sections.class_id
LEFT JOIN terms ON class_sections.term_id = terms.term_id;
                               
SELECT 
	classes.code, 
    classes.name,
    terms.name,
    COUNT(class_registrations.class_registration_id) AS enrolled_students,
    (classes.maximum_students - COUNT(class_registrations.class_registration_id)) AS space_remaining
FROM class_registrations
LEFT JOIN class_sections ON class_sections.class_section_id = class_registrations.class_section_id
LEFT JOIN classes ON classes.class_id = class_sections.class_id 
LEFT JOIN terms ON terms.term_id = class_sections.term_id
GROUP BY class_registrations.class_section_id;
                               
