import com.google.gson.Gson;
import com.hhhhhx.autotouch.bean.ForTask;
import com.hhhhhx.autotouch.bean.Task;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.utils.GsonUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RawCollectionsTest {
    @Test
    public void testTest() {
        Task task = new ForTask(3, null);

        ForTask task1 = (ForTask) task;

        int repeatTime = task1.getRepeatTime();

        System.out.println(repeatTime);
    }
}
