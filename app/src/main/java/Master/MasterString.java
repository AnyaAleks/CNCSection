package Master;

public class MasterString {

    // the resource ID for the imageView
    private String name;

    // create constructor to set the values for all the parameters of the each single view
    public MasterString(String nm) {
        name = nm;
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

}