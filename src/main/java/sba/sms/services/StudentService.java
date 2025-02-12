package sba.sms.services;

import lombok.extern.java.Log;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hibernate.Hibernate.list;

/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public List<Student> getAllStudents() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("from Student", Student.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void createStudent(Student student) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        try(Session session = sessionFactory.openSession()) {
            return session.get(Student.class, email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean validateStudent(String email, String password) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            if (email == null || password == null)
                return false;
            else {
                Student student = session.get(Student.class, email);
                if (student != null) {
                    if (student.getPassword().equals(password))
                        return true;
                    else
                        return false;
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Student student = session.get(Student.class, email);
            Course course = session.get(Course.class, courseId);

            if ((student == null) || (course == null)) {
                System.out.println("Invalid student or course");
            }
            if (student != null && course != null) {
                if (!student.getCourses().contains(course)) {
                    student.getCourses().add(course);
                    session.merge(student);
                }
                else {
                    System.out.println("Student already registered for the course");
                }
                tx.commit();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        try(Session session = sessionFactory.openSession()) {

            Student student = session.get(Student.class, email);
            List<Course> coursesSet = student.getCourses();
            return new ArrayList<>(coursesSet);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}