import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@IncludeCategory(ThreadTests.class)
@Suite.SuiteClasses({PlayerThreadWinsInstantlyTest.class})
public class TestSuite {
    // Empty
}
