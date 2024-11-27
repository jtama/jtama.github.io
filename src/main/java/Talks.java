import io.quarkiverse.roq.data.runtime.annotations.DataMapping;

import java.net.URL;
import java.util.List;

@DataMapping(value = "talks", parentArray = true)
public record Talks(List<Talk> list) {

    public record Talk(String title, String description, URL slides, URL code, List<Conference> conferences) {

        @Override
        public String description() {
            return description;
        }

        public record Conference(String name, URL video) {

        }
    }

}
