package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


class StudentServiceTest {

    private static StudentService studentService;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @BeforeEach
    public void setUp() {

        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        studentService = new StudentService();

    }

    @AfterEach
    public void tearDown() {
        if (transaction!=null) {
            transaction.rollback();
        }
        if (session!=null) {
            session.close();
        }
        if (sessionFactory!=null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student();
        student.setEmail("sheila@gmail.com");
        student.setName("SheilaNancy");
        student.setPassword("password");
        studentService.createStudent(student);

        Student fetchedStudent = studentService.getStudentByEmail(student.getEmail());

        assertEquals("Email should match the one assigned",fetchedStudent.getEmail(),"sheila@gmail.com");

    }

}