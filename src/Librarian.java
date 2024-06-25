import com.google.api.services.books.v1.model.Volume;
import com.google.api.services.books.v1.model.Volumes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Librarian extends Form {

    String firstName, lastName, age, phone, email, address;
    JSONArray books;

    public Librarian() {
        System.out.println("\nWelcome to the library, please fill out the following information");
        this.firstName = askName();
        this.lastName = askLastName();
        this.age = askAge();
        this.phone = askPhoneNumber();
        this.email = askEmail();
        this.address = askAddress();
        this.books = new JSONArray();
    }

    public static void startsRegistration(String firstName, String lastName, String age,
                                          String phone, String email, String address, JSONArray books) {
        Person person = new Person(firstName, lastName, age, phone, email, address, books);

        if (!isPersonRegistered(person)) {
            addUserRecord(person);
        } else {
            System.out.println("\nYou are already registered, please choose a book of your preference\n");
        }
    }

    private static boolean isPersonRegistered (Person person) {
        try(BufferedReader reader = new BufferedReader(new FileReader("src/personRegistry.json"))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            JSONTokener tokenizer = new JSONTokener(jsonContent.toString());
            JSONArray jsonArray = new JSONArray(tokenizer);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("first_name").equals(person.firstName()) &&
                        jsonObject.getString("last_name").equals(person.lastName()) &&
                        jsonObject.getString("age").equals(person.age())) {
                    System.out.println("Index of element is: " + i);
                    return true;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private static void addUserRecord(Person person) {
        try {
            String jsonContent =
                    new String (Files.readAllBytes(Paths.get("src/personRegistry.json")));

            JSONArray jsonArray = new JSONArray(jsonContent);

            JSONObject newRegistry = new JSONObject();
            newRegistry.put("first_name", person.firstName());
            newRegistry.put("last_name", person.lastName());
            newRegistry.put("age", person.age());
            newRegistry.put("phone", person.phone());
            newRegistry.put("email", person.email());
            newRegistry.put("address", person.address());
            newRegistry.put("books", person.books());

            jsonArray.put(newRegistry);

            FileWriter fileWriter = new FileWriter("src/personRegistry.json");
            fileWriter.write(jsonArray.toString());
            fileWriter.flush();
            fileWriter.close();

            System.out.println("\nRegistry loaded successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getRequestedBook(String firstName, String lastName, String age) throws IOException {
        System.out.println("What's the book title of your preference?\n");
        Scanner nameQuestion = new Scanner(System.in);
        String bookName = nameQuestion.next();
        System.out.println("What's the book author?\n");
        Scanner authorQuestion = new Scanner(System.in);
        String bookAuthor = authorQuestion.next();

        Volumes response = Library.books().volumes().list(bookName)
                .setQ("intitle:" + bookName + " inauthor:" + bookAuthor).execute();

        if (response.getItems() == null || response.getItems().isEmpty()) {
            System.out.println("\nNo results found, please search for another book\n");
            getRequestedBook(firstName, lastName, age);
        } else {
            System.out.println("\nHey! here's the book:\n");
            registerTheBook(firstName, lastName, age, response);
        }
    }

    private static void registerTheBook(String firstName, String lastName, String age, Volumes response) {
        Volume volume = response.getItems().get(0);

        try(BufferedReader reader = new BufferedReader(new FileReader("src/personRegistry.json"))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            JSONTokener tokenizer = new JSONTokener(jsonContent.toString());
            JSONArray jsonArray = new JSONArray(tokenizer);

            for(int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("first_name").equals(firstName) &&
                        jsonObject.getString("last_name").equals(lastName) &&
                        jsonObject.getString("age").equals(age)) {
                    JSONArray booksArray = jsonObject.getJSONArray("books");

                    JSONObject selectedBook = new JSONObject();
                    selectedBook.put("title", volume.getVolumeInfo().getTitle());
                    selectedBook.put("author", volume.getVolumeInfo().getAuthors().get(0));

                    booksArray.put(selectedBook);

                    Files.write(Paths.get("src/personRegistry.json"), jsonArray.toString().getBytes());
                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("The book was successfully registered\nPlease remember that you have 30 days to return it");
    }

    public String askName() {
        Scanner scanner = new Scanner(System.in);
        String answer;

        do {
            System.out.println(FIRST_NAME);
            answer = scanner.next();
        } while (Validate.name(answer));

        return answer.toLowerCase();
    }

    public String askLastName() {
        Scanner scanner = new Scanner(System.in);
        String answer;

        do {
            System.out.println(LAST_NAME);
            answer = scanner.next();
        } while (Validate.name(answer));

        return answer.toLowerCase();
    }

    public String askAge() {
        Scanner scanner = new Scanner(System.in);
        String answer;

        do {
            System.out.println(AGE);
            answer = scanner.next();
        } while (!Validate.age(answer));

        return answer.toLowerCase();
    }

    public String askPhoneNumber() {
        Scanner scanner = new Scanner(System.in);
        String answer;

        do {
            System.out.println(PHONE_NUMBER);
            answer = scanner.next();
        } while (!Validate.number(answer));

        return answer.toLowerCase();
    }

    public String askEmail() {
        Scanner scanner = new Scanner(System.in);
        String answer;

        do {
            System.out.println(EMAIL);
            answer = scanner.next();
        } while (!Validate.email(answer));

        return answer.toLowerCase();
    }

    public String askAddress() {
        Scanner scanner = new Scanner(System.in);
        String answer;

        do {
            System.out.println(ADDRESS);
            answer = scanner.next();
        } while (!Validate.address(answer));

        return answer.toLowerCase();
    }

}
