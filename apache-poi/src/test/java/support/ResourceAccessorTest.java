package support;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class ResourceAccessorTest {

    @Test
    public void canAccessExcelFileInResourcesFolder() {
        String filename = ResourceAccessor.excelFileWithName("prime-factors-scenarios");

        assertThat(filename, endsWith("/target/classes/prime-factors-scenarios.xlsx"));
    }
}
