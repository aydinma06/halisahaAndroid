package halisahaandroid.mammy.halisahaandroid.activity;

public class MenuItemClass {
    String title;
    int imageId;

    public MenuItemClass(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

}
