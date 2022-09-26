package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
public class StudentController
{
   
   @Autowired
   CourseRepository courseRepository;
   
   @Autowired
   StudentRepository studentRepository;
   
   @Autowired
   EnrollmentRepository enrollmentRepository;
   
   @Autowired
   GradebookService gradebookService;
   
   // TODO: Add a Student
   @PostMapping("/student")
   @Transactional
   public Student addStudent( @RequestBody StudentDTO studentDTO  ) { 

      Student student = studentRepository.findByEmail( studentDTO.email);
      
      if (student!=null) {
         throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student email has already been registered" );
      } else if (studentDTO.name == null || studentDTO.email == null) {
         throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student name and email are required.");
      } else {
         
         Student new_student = new Student();
         new_student.setName(studentDTO.name);
         new_student.setEmail(studentDTO.email);
      
         return studentRepository.save(new_student);
      }
   }
   
   // TODO: Place hold on student
   @PutMapping(value = "/student/addhold")
   @Transactional
   public Student placeHold(@PathVariable StudentDTO studentDTO) {
      Student student = studentRepository.findByEmail( studentDTO.email);
      
      if(student != null) {
         student.setStatus("hold");
         student.setStatusCode(1);
         studentRepository.save(student);
         return studentRepository.save(student);
      } else {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student is not registered or does not exist.");
      }

   }
   
   //TODO: Remove hold on student
   @GetMapping(value = "/student/removehold")
   public Student removeHold(@PathVariable StudentDTO studentDTO) {
      Student student = studentRepository.findByEmail( studentDTO.email);
      if (student != null) {
         student.setStatus("");
         student.setStatusCode(0);
         studentRepository.save(student);            
         return studentRepository.save(student); 
     } else {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student email not found: " + studentDTO.email);
     }
   }
}


