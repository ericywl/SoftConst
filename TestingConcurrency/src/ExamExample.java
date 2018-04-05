import java.util.Random;
import java.util.concurrent.Phaser;

public class ExamExample {
    public static void main(String[] args) {
        final int numOfStudents = 5;
        Phaser phaser = new Phaser();

        Examiner examiner = new Examiner(phaser);
        Student[] students = new Student[numOfStudents];
        for (int i = 0; i < students.length; i++)
            students[i] = new Student(phaser, i + 1);

        examiner.start();
        for (Student student : students)
            student.start();

        try {
            examiner.join();
            for (Student student : students)
                student.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Examiner extends Thread {
        private final Phaser phaser;

        Examiner(Phaser phaser) {
            this.phaser = phaser;
            phaser.register();
        }

        @Override
        public void run() {
            System.out.println("Examiner arrived at the hall.");
            // wait for students to arrive
            phaser.arriveAndAwaitAdvance();
            System.out.println("All students have arrived. Starting exam!\n");
            System.out.println("After one hour...");

            // wait for students to finish
            phaser.arriveAndAwaitAdvance();
            System.out.println("\nAll students have finished the exam!");
            System.out.println("Collecting papers...");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("\nEXAM OVER");
        }
    }

    static class Student extends Thread {
        private final Phaser phaser;
        private final int index;

        Student(Phaser phaser, int index) {
            this.phaser = phaser;
            this.index = index;
            phaser.register();
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                // wait for other students
                phaser.arriveAndAwaitAdvance();

                Thread.sleep(new Random().nextInt(5000) + 1000);
                System.out.println("Student " + index + " finished the exam and left the hall.");
                // finished exam
                phaser.arriveAndDeregister();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
