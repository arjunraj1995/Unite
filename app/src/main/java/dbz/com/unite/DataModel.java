package dbz.com.unite;

/**
 * Created by kcarj on 24-03-2017.
 */

public class DataModel {

    String name;
    String lang;
    String online;

    public DataModel(String name, String lang, String online ) {
        this.name=name;
        this.lang=lang;
        this.online=online;


    }

    public String getName() {
        return name;
    }

    public String getLang() {
        return lang;
    }

    public String getOnline() {
        return online;
    }


}
