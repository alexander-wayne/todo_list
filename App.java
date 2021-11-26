package com.wayneapps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;



class Main {

    static ArrayList<String> list = new ArrayList<String>();
    static ArrayList<Item> list2 = new ArrayList<Item>();

    static Dotenv dotenv = Dotenv.load();
    public static void main(String[] args){
    	
    	System.out.println("Connecting to database");          

        try {
            Connection connection = DriverManager.getConnection(dotenv.get("URL"), dotenv.get("USERNAME"), dotenv.get("PASSWORD"));
            System.out.println("Database connected");
         } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect to database", e);
        }

        boolean active = true;
        Scanner scanner = new Scanner(System.in);
        String answer;

        
        System.out.print("Welcome, ");
        
        DateFormat df = new SimpleDateFormat("EEEEE, MMMM d");
        Date date = new Date();
        System.out.printf("today is " + df.format(date));

        while(active){
            System.out.println("\n\n What would you like to do?");
            System.out.println("new - Add a new item to your todo list");
            System.out.println("read - See all items in your todo list");
            System.out.println("delete - Delete an item in your todo list");
            System.out.println("clear - Clear your todo list");
            System.out.println("exit - Exit the program");
            
            String choice = scanner.nextLine();

            switch(choice) {
                case "new": 
                    System.out.println("Please enter what you need to do");
                    answer = scanner.nextLine();
                    // list.add(answer);
                    addToList(answer);
                    System.out.println(answer + " has been added to your todo list");
                    break;

                case "read":
                    // printItems();
                    viewAll();
                    break;

                case "delete":
                    printItems();
                    System.out.println("Please specify which item you would like to delete");
                    int index = scanner.nextInt();
                    deleteFromList(index);
                    // list.remove(index - 1);
                    // System.out.println("Item successfully removed");
                    break;

                case "clear":
                    System.out.println("Do you really want to erase your entire todo list? (yes / no)");
                    answer = scanner.nextLine();
                    if(answer.equalsIgnoreCase("yes")) {
                        // list.clear();
                        // System.out.println("Items successfully removed");
                        deleteAll();
                    }
                    break;

                case "exit":
                    System.out.println("Goodbye");
                    active = false;
                    scanner.close();

                default:
                    break;
            }
        }

    }

    static void printItems(){
        for(int i = 0; i < list.size(); i++){
            System.out.printf("%d : " + list.get(i) + "\n", i + 1);
        }
    }

    static void addToList(String item){
        try {
            Connection connection = DriverManager.getConnection(dotenv.get("URL"), dotenv.get("USERNAME"), dotenv.get("PASSWORD"));
            PreparedStatement statement = connection.prepareStatement("INSERT INTO todo_list (id, item) VALUES (NULL, \"" + item + "\")");
            statement.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }

    static void deleteFromList(int id){
        try {
            Connection connection = DriverManager.getConnection(dotenv.get("URL"), dotenv.get("USERNAME"), dotenv.get("PASSWORD"));
            PreparedStatement statement = connection.prepareStatement("DELETE FROM todo_list WHERE id=" + id);
            if(statement.executeUpdate() > 0) {
                System.out.println("Item deleted succesfully");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }

    static void deleteAll() {
        String sql = "TRUNCATE TABLE todo_list";
        try {
            Connection connection = DriverManager.getConnection(dotenv.get("URL"), dotenv.get("USERNAME"), dotenv.get("PASSWORD"));
            PreparedStatement statement = connection.prepareStatement(sql);
            if(statement.executeUpdate() > 0) {
                System.out.println("Items deleted succesfully");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }

    static void viewAll(){
        String sql = "SELECT * FROM todo_list";
        try {
            Connection connection = DriverManager.getConnection(dotenv.get("URL"), dotenv.get("USERNAME"), dotenv.get("PASSWORD"));
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()) {
                Item temp = new Item(Integer.parseInt(result.getString("id")), result.getString("item"));
                list2.add(temp);
                System.out.println("ID: " + result.getString("id") + "\nItem: " + result.getString("item"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}