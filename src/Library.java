import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.books.v1.Books;
import com.google.api.services.books.v1.BooksRequestInitializer;

import java.io.IOException;

public class Library {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static void main(String[] args) throws IOException {
        Librarian librarian = new Librarian();
        Librarian.startsRegistration(librarian.firstName, librarian.lastName ,librarian.age,
                librarian.phone, librarian.email, librarian.address, librarian.books);
        Librarian.getRequestedBook(librarian.firstName, librarian.lastName, librarian.age);
    }

    public static Books books() {
        return new Books.Builder(HTTP_TRANSPORT, JSON_FACTORY, null)
                .setApplicationName("Public Library")
                .setGoogleClientRequestInitializer(
                        new BooksRequestInitializer(System.getenv().get("BOOKS_API_KEY")))
                .build();
    }
}
