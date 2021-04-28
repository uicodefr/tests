package test.uicode.scanresource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@SpringBootApplication
public class ScanresourceApplication implements CommandLineRunner {

    @Autowired
    private ResourceLoader resourceLoader;

    public static void main(String[] args) {
        SpringApplication.run(ScanresourceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Resource mainFolder = resourceLoader.getResource("classpath:scanauto");

        List<String> resultList = new ArrayList<>();

        URL urlMainFolder = mainFolder.getURL();
        System.out.println("url=" + urlMainFolder.toString());

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources("classpath:scanauto/**/fichier.txt");
        for (Resource r : resources) {
            String[] split = r.getURL().getFile().split("/");
            resultList.add(split[split.length - 3] + "/" + split[split.length - 2] + "/" + split[split.length - 1]);
        }

        for (String result : resultList) {
            System.out.println("-> " + result);
        }
    }

    private static void toto(Resource mainFolder, URL urlMainFolder, List<String> resultList) throws IOException {

        // Ancien fonctionnement

        if (urlMainFolder.getProtocol().equals("file")) {
            resultList.addAll(Arrays.asList(mainFolder.getFile().list()));
        } else if (urlMainFolder.getProtocol().equals("jar")) {
            /*
             * Test 1: ne fonctionne pas
             * resultList.addAll(IOUtils.readLines(getClass().getClassLoader().
             * getResourceAsStream("scanauto"), StandardCharsets.UTF_8));
             */
            /**
             * Test 2 : ne fonctionne que pour les fichiers Reflections reflections = new
             * Reflections("scanauto", new ResourcesScanner());
             * resultList.addAll(reflections.getResources(Pattern.compile(".*")));
             */

        }
    }

}
