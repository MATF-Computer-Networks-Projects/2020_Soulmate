package server;

import java.io.File;

public class GlobalData {
    public static final File WEB_ROOT = new File("web");
    public static final String DEFAULT_FILE = "signup/index.html";
    public static final String FILE_NOT_FOUND = "errors/404.html";
    public static final String METHOD_NOT_SUPPORTED = "errors/not_supported.html";
    public static final int PORT = 5252;
    public static final boolean verbose = true;  // Debug switch

    public static final String KS_PATH = "config/keystore.jks";
    public static final String KS_PASS = "soulmate";
    public static final String TS_PATH = "config/cacerts.jks";
    public static final String TS_PASS = "soulmate";
}
