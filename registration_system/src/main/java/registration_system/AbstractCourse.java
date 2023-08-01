package registration_system;
public abstract class AbstractCourse {
    private String code;
    private String name;

    public AbstractCourse(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return code + ": " + name;
    }

    public abstract void saveToDatabase();

    
    public abstract void removeFromDatabase();
}
