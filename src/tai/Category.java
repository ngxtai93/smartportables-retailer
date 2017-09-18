package tai;

import java.io.Serializable;

public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String imageName;

    public Category(String id, String name, String imageName) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageName() {
        return this.imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}