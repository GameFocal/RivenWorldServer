package com.gamefocal.rivenworld.entites.util;

import com.gamefocal.rivenworld.DedicatedServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {
    /**
     * Parameters of the method to add an URL to the System classes.
     */
    private static final Class<?>[] parameters = new Class[]{URL.class};

    /**
     * Adds a file to the classpath.
     *
     * @param s a String pointing to the file
     * @throws IOException
     */
    public static void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }

    /**
     * Adds a file to the classpath
     *
     * @param f the file to be added
     * @throws IOException
     */
    public static void addFile(File f) throws IOException {
        addURL(f.toURI().toURL());
    }

    /**
     * Adds the content pointed by the URL to the classpath.
     *
     * @param u the URL pointing to the content to be added
     * @throws IOException
     */
    public static void addURL(URL u) throws IOException {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{u});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }

    public static void extractResourcesFromJar(String folderInJar, String outputDirectory) {
        // Get the jar URL
        String jarFilePath = DedicatedServer.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try (JarFile jar = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                // Check if the entry is in the specified folder and is not a directory
                if (name.startsWith(folderInJar) && !name.endsWith("/")) {
                    // Extract only the filename without the folder prefix
                    String fileName = Paths.get(name).getFileName().toString();
                    Path outputPath = Paths.get(outputDirectory, fileName);
                    try (InputStream is = jar.getInputStream(entry);
                         OutputStream os = Files.newOutputStream(outputPath)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
