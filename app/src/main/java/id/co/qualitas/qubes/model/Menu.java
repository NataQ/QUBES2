package id.co.qualitas.qubes.model;

/**
 * Created by Foo on 9/5/2016.
 */

public class Menu {
    private String txtMenu;
    private int imgMenu;

    public Menu(String txtMenu, int imgMenu) {
        this.txtMenu = txtMenu;
        this.imgMenu = imgMenu;
    }

    public int getImgMenu() {
        return imgMenu;
    }

    public void setImgMenu(int imgMenu) {
        this.imgMenu = imgMenu;
    }

    public String getTxtMenu() {
        return txtMenu;
    }

    public void setTxtMenu(String txtMenu) {
        this.txtMenu = txtMenu;
    }
}
