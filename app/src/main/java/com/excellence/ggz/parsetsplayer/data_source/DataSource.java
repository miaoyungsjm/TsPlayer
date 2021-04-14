package com.excellence.ggz.parsetsplayer.data_source;

/**
 * @author ggz
 * @date 2021/4/14
 */
public class DataSource {

    private String name;
    private String filePath;

    public DataSource(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return " \n" +
                "[DataSource] name: " + name + "\n" +
                "[DataSource] filePath: " + filePath + "\n";
    }
}
