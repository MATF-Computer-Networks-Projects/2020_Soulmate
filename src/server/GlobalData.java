package server;

import java.io.File;

public class GlobalData {
    public static final File WEB_ROOT = new File("web");
    public static final String DEFAULT_FILE = "signup/index.html";
    public static final String FILE_NOT_FOUND = "errors/404.html";
    public static final String METHOD_NOT_SUPPORTED = "errors/not_supported.html";
    public static final int PORT = 8080;
    public static final boolean verbose = true;  // Debug switch
}
