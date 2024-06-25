import org.json.JSONArray;

public record Person(String firstName, String lastName, String age,
                     String phone, String email, String address, JSONArray books) {
}
