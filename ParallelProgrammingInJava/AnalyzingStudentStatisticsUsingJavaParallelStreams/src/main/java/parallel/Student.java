package parallel;

import lombok.Getter;

/**
 * A class representing a single student in a single class.
 */
@Getter
public final class Student {
    /**
     * First name of the student.
     */
    private final String firstName;
    /**
     * Surname of the student.
     */
    private final String lastName;
    /**
     * Age of the student.
     */
    private final double age;
    /**
     * Grade the student has received in the class so far.
     */
    private final int grade;
    /**
     * Whether the student is currently enrolled, or has already completed the
     * course.
     */
    private final boolean isCurrent;

    /**
     * Constructor.
     * @param setFirstName Student first name
     * @param setLastName Student last name
     * @param setAge Student age
     * @param setGrade Student grade in course
     * @param setIsCurrent Student currently enrolled?
     */
    public Student(final String setFirstName, final String setLastName,
            final double setAge, final int setGrade,
            final boolean setIsCurrent) {
        this.firstName = setFirstName;
        this.lastName = setLastName;
        this.age = setAge;
        this.grade = setGrade;
        this.isCurrent = setIsCurrent;
    }

    /**
     * Check if this student is active, or has taken the course in the past.
     * @return true if the student is currently enrolled, false otherwise
     */
    public boolean getIsCurrent() {
        return isCurrent;
    }
}
