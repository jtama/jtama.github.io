import io.quarkiverse.roq.data.runtime.annotations.DataMapping;

import java.net.URL;
import java.util.List;

@DataMapping(value = "talks", parentArray = true)
public record Talks(List<Talk> list) {

    public record Talk(String title, String description, URL slides, URL code, String post, List<Conference> conferences) {

        public record Conference(String name, URL video) {

        }
    }

}
