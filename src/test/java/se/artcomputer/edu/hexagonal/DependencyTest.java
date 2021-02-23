package se.artcomputer.edu.hexagonal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DependencyTest {

    public static final String PACKAGE_ROOT = "se.artcomputer.edu.hexagonal.";
    public static final String ADAPTER_OUT = PACKAGE_ROOT + "adapter.out";
    public static final String PORT_OUT = PACKAGE_ROOT + "application.port.out";
    public static final String DOMAIN = PACKAGE_ROOT + "domain";
    private static final String ADAPTER = PACKAGE_ROOT + "adapter";
    private static final String APPLICATION = PACKAGE_ROOT + "application";
    private static Map<String, Set<String>> collectDependencies;

    @BeforeAll
    static void beforeAll() throws IOException {
        File start = new File("src/main/java");
        // Visit all Java files and check dependencies
        collectDependencies = new HashMap<>();
        visitJavaFiles(start, collectDependencies);
        collectDependencies
                .keySet()
                .stream()
                .sorted()
                .forEach(key -> System.out.println(key + ": " + collectDependencies.get(key)));
    }

    @Test
    void domain_must_not_depend_on_other_packages_in_the_system() {
        Set<String> keys = collectDependencies.keySet().stream().filter(k -> k.startsWith(DOMAIN)).collect(Collectors.toSet());
        assertTrue(keys.size() > 0);

        for (String k : keys) {
            Set<String> illicitInPackage = collectDependencies.get(k).stream()
                    .filter(p -> !p.startsWith(DOMAIN))
                    .collect(Collectors.toSet());
            assertEquals(0, illicitInPackage.size(), "Package " + k + " depends on " + illicitInPackage);
        }
    }

    @Test
    void application_must_only_depend_on_domain() {
        Set<String> keys = collectDependencies.keySet().stream().filter(k -> k.startsWith(PACKAGE_ROOT + "application")).collect(Collectors.toSet());
        assertTrue(keys.size() > 0);

        for (String k : keys) {
            Set<String> illicitInPackage = collectDependencies.get(k).stream()
                    .filter(p -> !p.startsWith(DOMAIN))
                    .filter(p -> !p.startsWith(APPLICATION))
                    .collect(Collectors.toSet());
            assertEquals(0, illicitInPackage.size(), "Package " + k + " depends on " + illicitInPackage);
        }
    }

    @Test
    void adapter_out_must_only_depend_on_domain_and_port_out() {
        Set<String> keys = collectDependencies.keySet().stream().filter(k -> k.startsWith(ADAPTER_OUT)).collect(Collectors.toSet());
        assertTrue(keys.size() > 0);

        for (String k : keys) {
            Set<String> illicitInPackage = collectDependencies.get(k).stream()
                    .filter(p -> !p.startsWith(DOMAIN))
                    .filter(p -> !p.startsWith(PORT_OUT))
                    .collect(Collectors.toSet());
            assertEquals(0, illicitInPackage.size(), "Package " + k + " depends on " + illicitInPackage);
        }
    }

    @Test
    void adapter_must_only_depend_on_domain_and_application() {
        Set<String> keys = collectDependencies.keySet().stream().filter(k -> k.startsWith(ADAPTER)).collect(Collectors.toSet());
        assertTrue(keys.size() > 0);

        for (String k : keys) {
            Set<String> illicitInPackage = collectDependencies.get(k).stream()
                    .filter(p -> !p.startsWith(DOMAIN))
                    .filter(p -> !p.startsWith(APPLICATION))
                    .filter(p -> !p.startsWith(ADAPTER))
                    .collect(Collectors.toSet());
            assertEquals(0, illicitInPackage.size(), "Package " + k + " depends on " + illicitInPackage);
        }
    }

    private static void visitJavaFiles(File current, Map<String, Set<String>> collectDependencies) throws IOException {
        if (current.isFile()) {
            if (current.getPath().endsWith("java")) {
                String javaPackage = getJavaPackage(current);
                Set<String> soFar = collectDependencies.getOrDefault(javaPackage, new HashSet<>());
                soFar.addAll(visitJavaFile(current));
                collectDependencies.put(javaPackage, soFar);
            }
        }
        if (current.isDirectory()) {
            File[] files = current.listFiles();
            for (File f : Objects.requireNonNull(files)) {
                visitJavaFiles(f, collectDependencies);
            }
        }
    }

    private static Set<String> visitJavaFile(File current) throws IOException {
        return Files.lines(current.toPath()).filter(s -> s.startsWith("import " + PACKAGE_ROOT)).map(DependencyTest::extractDependency).collect(Collectors.toSet());
    }

    private static String getJavaPackage(File current) throws IOException {
        return Files.lines(current.toPath()).filter(s -> s.startsWith("package " + PACKAGE_ROOT)).findFirst().map(s -> s.substring("package ".length(), s.indexOf(';'))).orElse("");
    }

    private static String extractDependency(String importStatement) {
        return importStatement.substring("import ".length(), importStatement.lastIndexOf('.'));
    }
}