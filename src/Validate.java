public class Validate {

    public static boolean name(String name) {
        return !name.matches("^[A-Za-z]+$");
    }

    public static boolean number(String number) {
        return number.matches("^[0-9]{6,}$");
    }

    public static boolean age(String age) {
        return age.matches("^[0-99]+$");
    }

    public static boolean email(String email) {
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    public static boolean address(String address) {
        return address.matches("^[A-Za-z0-9]+$");
    }
}