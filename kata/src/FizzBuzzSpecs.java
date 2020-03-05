import static info.javaspec.lang.lambda.FunctionalDsl.describe;
import static info.javaspec.lang.lambda.FunctionalDsl.it;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FizzBuzzSpecs {
  {
    describe("FizzBuzz", () -> {
      describe("#encode", () -> {
        it("translates 1 as-is", () -> assertThat(FizzBuzz.encode(1), equalTo("1")));

        it("translates 2 as-is", () -> assertThat(FizzBuzz.encode(2), equalTo("2")));

        it("translates 3 to 'fizz'", () -> assertThat(FizzBuzz.encode(3), equalTo("fizz")));

        it("translates 5 to 'buzz'", () -> assertThat(FizzBuzz.encode(5), equalTo("buzz")));
      });
    });
  }
}
