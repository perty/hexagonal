package se.artcomputer.edu.hexagonal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DependencyTest {

    public static final String PACKAGE_ROOT = "se.artcomputer.edu.hexagonal";
    public static final String ADAPTER_OUT = PACKAGE_ROOT + ".adapter.out";
    public static final String PORT_OUT = PACKAGE_ROOT + ".application.port.out";
    public static final String DOMAIN = PACKAGE_ROOT + ".domain";
    private static final String ADAPTER = PACKAGE_ROOT + ".adapter";
    private static final String APPLICATION = PACKAGE_ROOT + ".application";
    private static Map<String, Set<String>> collectedDependencies;

    @BeforeAll
    static void beforeAll() throws IOException {
        collectedDependencies = new HashMap<>();
        visitJavaFiles(new File("src/main/java"), collectedDependencies);
        dumpDependencies();
    }

    private static void dumpDependencies() {
        collectedDependencies
                .keySet()
                .stream()
                .sorted()
                .forEach(key -> System.out.println(key + ": " + collectedDependencies.get(key)));
    }

    @Test
    void domain_must_not_depend_on_other_packages_in_the_system() {
        assertDependencies(getPackages(DOMAIN), p -> p.startsWith(DOMAIN));
    }

    @Test
    void application_must_only_depend_on_domain() {
        assertDependencies(getPackages(APPLICATION), p -> (p.startsWith(DOMAIN) || p.startsWith(APPLICATION)));
    }

    @Test
    void adapter_out_must_only_depend_on_domain_and_port_out() {
        assertDependencies(getPackages(ADAPTER_OUT), p -> p.startsWith(DOMAIN) || p.startsWith(PORT_OUT));
    }

    @Test
    void adapter_must_only_depend_on_domain_and_application() {
        assertDependencies(getPackages(ADAPTER), p -> p.startsWith(DOMAIN) || p.startsWith(APPLICATION) || p.startsWith(ADAPTER));
    }

    private static void assertDependencies(Set<String> currentPackages, Predicate<String> acceptedDependencies) {
        for (String currentPackage : currentPackages) {
            Set<String> illicitInPackage = collectedDependencies.get(currentPackage).stream()
                    .filter(acceptedDependencies.negate())
                    .collect(Collectors.toSet());
            assertEquals(0, illicitInPackage.size(), "Package " + currentPackage + " depends on " + illicitInPackage);
        }
    }

    private static Set<String> getPackages(String packagePrefix) {
        Set<String> packages = collectedDependencies.keySet().stream().filter(k -> k.startsWith(packagePrefix)).collect(Collectors.toSet());
        assertTrue(packages.size() > 0, "No packages found with prefix: " + packagePrefix);
        return packages;
    }

    private static void visitJavaFiles(File current, Map<String, Set<String>> collectedDependencies) throws IOException {
        if (current.isFile()) {
            if (current.getPath().endsWith("java")) {
                String currentPackage = getJavaPackage(current);
                Set<String> soFar = collectedDependencies.getOrDefault(currentPackage, new HashSet<>());
                soFar.addAll(visitJavaFile(current));
                collectedDependencies.put(currentPackage, soFar);
            }
        }
        if (current.isDirectory()) {
            for (File f : Objects.requireNonNull(current.listFiles())) {
                visitJavaFiles(f, collectedDependencies);
            }
        }
    }

    private static Set<String> visitJavaFile(File current) throws IOException {
        try (Stream<String> lines = Files.lines(current.toPath())) {
            return lines
                    .filter(s -> s.startsWith("import " + PACKAGE_ROOT))
                    .map(DependencyTest::extractDependency)
                    .collect(Collectors.toSet());
        }
    }

    private static String getJavaPackage(File current) throws IOException {
        try (Stream<String> lines = Files.lines(current.toPath())) {
            return lines
                    .filter(s -> s.startsWith("package " + PACKAGE_ROOT))
                    .findFirst()
                    .map(s -> s.substring("package ".length(), s.indexOf(';')))
                    .orElse("");
        }
    }

    private static String extractDependency(String importStatement) {
        return importStatement.substring("import ".length(), importStatement.lastIndexOf('.'));
    }
}