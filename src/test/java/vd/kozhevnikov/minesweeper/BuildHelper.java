package vd.kozhevnikov.minesweeper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BuildHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @SneakyThrows
    public static String readResourceToString(String filePath) {
        return IOUtils.resourceToString(filePath, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public static <T> T buildObject(String filePath, Class<T> type) {
        return MAPPER.readValue(readResourceToString(filePath), type);
    }
}
