package sandbox;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static support.ResourceAccessor.excelFileWithName;

public class PrimeFactorsScenariosFileTest {

    File file;

    @Before
    public void thisFile() {
        file = new File(excelFileWithName("prime-factors-scenarios"));
    }

    @Test
    public void exists() {
        assertThat(file.exists(), equalTo(true));
    }

    @Test
    public void isWritable() {
        assertThat(file.canWrite(), equalTo(true));
    }

    @Test
    public void isReadable() {
        assertThat(file.canRead(), equalTo(true));
    }

    @Test
    public void canBeUsedWithApachePoi() throws Exception {
        WorkbookFactory.create(file);
    }
}
