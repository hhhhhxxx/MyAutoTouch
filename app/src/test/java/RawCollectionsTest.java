import com.google.gson.Gson;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.utils.GsonUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RawCollectionsTest {
    private static final String TAG = "main" ;

    static class Event {
        private String name;
        private String source;
        private Event(String name, String source) {
            this.name = name;
            this.source = source;
        }
        @Override
        public String toString() {
            return String.format("(name=%s, source=%s)", name, source);
        }
    }

    @Test
    public void testTest() {
        ArrayList<String> list = new ArrayList<>();

        list.add(null);
        list.add("123");
        list.add(null);


        list.removeIf(Objects::isNull);

        System.out.println(GsonUtils.beanToJson(list));
    }
}
