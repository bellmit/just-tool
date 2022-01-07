import com.liugs.tool.util.FileTypeConverter;
import org.junit.Test;

/**
 * @ClassName TestUtil
 * @Description
 * @Author liugs
 * @Date 2022/1/6 10:55
 */
public class TestUtil {

    @Test
    public void testFileCovert() throws Exception {

        String tempFilePath = "E:\\tool\\test\\结算-价格汇总单群采20220105150104.xml";
        String targetFilePath = "E:\\tool\\test\\结算-价格汇总单群采20220105150104.xlsx";

        FileTypeConverter.xmlToExcel(tempFilePath, targetFilePath);
    }
}
