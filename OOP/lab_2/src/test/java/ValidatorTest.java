import org.junit.jupiter.api.Test;
import parsers.SchemeValidator;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorTest {
    @Test
    public void validateCorrectFile() {
        assertTrue(SchemeValidator.validate("src/main/resources/tariffs.xml", "src/main/resources/tariffs.xsd"));
    }
}