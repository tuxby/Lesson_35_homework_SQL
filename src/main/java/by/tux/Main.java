package by.tux;

public class Main {
    public static void main(String[] args) {
        System.out.println("Lesson 35, SQL");
        System.out.println("Homework");

        Homework35 homework35 =new Homework35();
        homework35.recreateTables();
        homework35.fillTablesByExample();
        homework35.print();
        homework35.printMoneyByManagers();
        System.out.println("Add Carolina and Monica users and print DATA");
        homework35.addUser("Carolina",9000,4);
        homework35.addUser("Monica",5000,5);
        homework35.printMoneyByManagers();
        System.out.println("Change Monica sum and print DATA");
        homework35.userUpdateSum("Monica",99999);
        homework35.print();
        homework35.printMoneyByManagers();
    }
}