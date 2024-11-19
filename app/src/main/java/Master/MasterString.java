package Master;

public class MasterString {

    // the resource ID for the imageView
    private String name;
    private int id;
    // create constructor to set the values for all the parameters of the each single view
    public MasterString(String nm,int d) {
        name = nm;
        id=d;
    }

    // getter method for returning the ID of the imageview
    public String getName() {
        return name;
    }
    public void setName(String nm) {
        name=nm;
    }

    public void Delete()
    {
        name = "empty";
    }

    public int getId() { // Добавлен геттер для роли
        return this.id;
    }

}