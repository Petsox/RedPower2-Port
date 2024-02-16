
package com.eloraam.redpower.core;

import com.eloraam.redpower.*;
import cpw.mods.fml.common.*;
import java.io.*;

public class Config
{
    private static File configDir;
    private static File configFile;
    private static TagFile config;

    public static void loadConfig() {
        config = new TagFile();
        InputStream is = RedPowerCore.class.getResourceAsStream("/assets/rpcore/default.cfg");
        config.readStream(is);
        File file;
        if (configDir == null) {
            file = Loader.instance().getConfigDir();
            file = new File(file, "/redpower/");
            file.mkdir();
            configDir = file;
            configFile = new File(file, "redpower.cfg");
        }

        if (configFile.exists()) {
            config.readFile(configFile);
        }

        config.commentFile("RedPower 2 Configuration");
    }

    public static void saveConfig() {
        Config.config.saveFile(Config.configFile);
    }

    public static int getInt(final String name) {
        return Config.config.getInt(name);
    }

    public static int getInt(final String name, final int _default) {
        return Config.config.getInt(name, _default);
    }

    public static String getString(final String name) {
        return Config.config.getString(name);
    }

    public static String getString(final String name, final String _default) {
        return Config.config.getString(name, _default);
    }
}
