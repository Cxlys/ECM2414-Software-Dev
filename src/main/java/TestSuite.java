import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@IncludeCategory(ThreadTests.class)
@Suite.SuiteClasses({PlayerWinsInstantlyTest.class, PlayerTest.class})
public class TestSuite {
    // Empty
}
