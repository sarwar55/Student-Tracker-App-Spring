package org.sarwar.imcs.repository;

import org.sarwar.imcs.model.Student;

import java.util.List;

/**
 * Created by A S M Sarwar Zahan on 5/13/17.
 */
public interface StudentRepository {

    void addStudent(Student student);
    Student getStudent(String studentId);
    void updateStudent(Student student);
    void deleteStudent(String studentId);
    List<Student> searchStudents(String searchName);
    List<Student> getStudents();
}
