package com.sammidev;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@SpringBootApplication
public class CrudRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudRestApplication.class, args);
	}
}


@RestController
@RequestMapping("/api/student")
class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	public StudentController(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		return new ResponseEntity<>("Hollaaa", HttpStatus.OK);
	}

	@PostMapping("/createStudent")
	public ResponseEntity<?> addStudent(@RequestBody Student student) {
		studentRepository.save(student);
		return new ResponseEntity<>(student, HttpStatus.CREATED);
	}

	@GetMapping("/getAllStudent")
	public ResponseEntity<?> getAllStudent() {
		return new ResponseEntity<>(studentRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/getStudentById/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable String id) {
		Student student = studentRepository.findById(id).orElse(null);
		if (student == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(student,HttpStatus.OK);
	}

	@PutMapping("/updateStudentById/{id}")
	public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody Student student) {
		student.setId(id);
		studentRepository.save(student);
		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	@DeleteMapping("/deleteStudentById/{id}")
	public ResponseEntity<?> deleteStudentById(@PathVariable String id) {
		studentRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/deleteStudent")
	public ResponseEntity<?> deleteStudent(@RequestBody Student student) {
		studentRepository.delete(student);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/deleteAll")
	public ResponseEntity<?> deleteAll(@RequestBody Student student) {
		studentRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@GetMapping("/count")
	public Long count() {
		return studentRepository.count();
	}

	@GetMapping("/getByName/{firstname}")
	public Student getByFirstName(@PathVariable String firstname) {
		Student student = null;
		for (int i = 0; i < studentRepository.findAll().size(); i++) {
			if (studentRepository.findAll().get(i).getFirstname().equalsIgnoreCase(firstname)) {
				return student = studentRepository.findAll().get(i);
			}
		}
		return null;
	}
}


@Repository
interface StudentRepository extends JpaRepository<Student, String> {}

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
class Student {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String nim;
	private String firstname;
	private String lastname;
	private String email;
}