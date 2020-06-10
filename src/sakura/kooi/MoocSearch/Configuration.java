package sakura.kooi.MoocSearch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Cleanup;
import sakura.kooi.MoocSearch.sources.QuestionSources;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class Configuration implements Serializable {
    private static transient Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
    private static transient Configuration instance = load();

    private static Configuration load() {
        File config = new File("MoocSearch.json");
        if (!config.exists()) return new Configuration();
        Gson gson = new Gson();
        try {
            @Cleanup Reader reader = Files.newBufferedReader(config.toPath(), StandardCharsets.UTF_8);
            return gson.fromJson(reader, Configuration.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new Configuration();
        }
    }
    public static void save() {
        try {
            File config = new File("MoocSearch.json");
            @Cleanup Writer writer = Files.newBufferedWriter(config.toPath(), StandardCharsets.UTF_8);
            writer.write(gson.toJson(instance));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Boolean> sourceEnabled = new HashMap<>();

    public boolean isSourceEnabled(QuestionSources source) {
        return sourceEnabled.computeIfAbsent(source.name(), s->true);
    }
}
