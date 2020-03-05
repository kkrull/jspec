import static info.javaspec.lang.lambda.FunctionalDsl.describe;
import static info.javaspec.lang.lambda.FunctionalDsl.it;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FizzBuzzSpecs {
  {
    describe("FizzBuzz", () -> {
      describe("::encode", () -> {
        it("translates multiples of 3 to 'fizz'", () -> {
          assertThat(FizzBuzz.encode(3), equalTo("fizz"));
          assertThat(FizzBuzz.encode(6), equalTo("fizz"));
        });

        it("translates multiples of 5 to 'buzz'", () -> {
          assertThat(FizzBuzz.encode(5), equalTo("buzz"));
          assertThat(FizzBuzz.encode(10), equalTo("buzz"));
        });

        it("translates multiples of both 3 and 5 to 'fizzbuzz'", () -> {
          assertThat(FizzBuzz.encode(15), equalTo("fizzbuzz"));
          assertThat(FizzBuzz.encode(30), equalTo("fizzbuzz"));
        });

        it("translates other numbers as-is", () -> {
          assertThat(FizzBuzz.encode(1), equalTo("1"));
          assertThat(FizzBuzz.encode(2), equalTo("2"));
        });
      });
    });
  }
}
