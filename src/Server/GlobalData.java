package Server;

import java.io.File;

public class GlobalData {
    static final File WEB_ROOT = new File("web");
    static final String DEFAULT_FILE = "signup/index.html";
    static final String FILE_NOT_FOUND = "errors/404.html";
    static final String METHOD_NOT_SUPPORTED = "errors/not_supported.html";
    static final int PORT = 8080;
    static final boolean verbose = true;  // Debug switch
}
