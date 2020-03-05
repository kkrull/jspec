public class FizzBuzz {
  public static String encode(int number) {
    if(number == 3)
      return "fizz";
    else if(number == 5)
      return "buzz";

    return String.valueOf(number);
  }
}
