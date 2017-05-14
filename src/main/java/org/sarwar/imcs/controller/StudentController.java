package org.sarwar.imcs.controller;

import org.sarwar.imcs.model.Student;
import org.sarwar.imcs.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class StudentController {
	private static final long serialVersionUID = 1L;
	@Autowired
	private StudentRepository studentRepository;

	@RequestMapping(value = "/")
    public ModelAndView listStudents(ModelAndView modelAndView) throws IOException {
		List<Student> students = studentRepository.getStudents();
		modelAndView.addObject("studentList", students);
        modelAndView.setViewName("list-students");
	    return modelAndView;
	}

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchStudents(HttpServletRequest request) {
        String searchName = request.getParameter("search");
        List<Student> students = studentRepository.searchStudents(searchName);
        ModelAndView modelAndView = new ModelAndView("list-students");
        modelAndView.addObject("studentList", students);
        modelAndView.addObject("viewBackButton", "show");
        return modelAndView;
    }

    @RequestMapping(value = "/newStudent", method = RequestMethod.GET)
    public ModelAndView newStudent(ModelAndView modelAndView) {
        modelAndView.addObject("NEW_STUDENT", new Student());
        modelAndView.setViewName("new-student-form");
        return modelAndView;
    }

    @RequestMapping(value = "/deleteStudent", method = RequestMethod.GET)
    public ModelAndView deleteStudent(HttpServletRequest request) {
        String studentId = request.getParameter("studentId");
        studentRepository.deleteStudent(studentId);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/addStudent", method = RequestMethod.POST)
    public ModelAndView addStudent(@ModelAttribute Student student) {
        if(student.getFirstName().length() == 0 || student.getLastName().length() == 0
                || student.getEmail().length() == 0){
            return new ModelAndView("redirect:/");
        }
        studentRepository.addStudent(student);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/saveUpdate", method = RequestMethod.POST)
    public ModelAndView saveStudent(@ModelAttribute Student student) {
        if(student.getFirstName().length() == 0 || student.getLastName().length() == 0
                || student.getEmail().length() == 0){
            return new ModelAndView("redirect:/");
        }
        studentRepository.updateStudent(student);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/updateStudent", method = RequestMethod.GET)
    public ModelAndView upDateStudent(HttpServletRequest request) {
        String studentId = request.getParameter("studentId");
        Student student = studentRepository.getStudent(studentId);
        ModelAndView modelAndView = new ModelAndView("update-student-form");
        modelAndView.addObject("STUDENT", student);
        return modelAndView;
    }


}