public abstract class Form {

    public static final String FIRST_NAME = "\nEnter your name: ";
    public static final String LAST_NAME = "Enter your last name: ";
    public static final String AGE = "Enter your age: ";
    public static final String PHONE_NUMBER = "Enter your phone number: ";
    public static final String EMAIL = "Enter your email: ";
    public static final String ADDRESS = "Enter your address: ";

    public abstract String askName();
    public abstract String askLastName();
    public abstract String askAge();
    public abstract String askPhoneNumber();
    public abstract String askEmail();
    public abstract String askAddress();

}
