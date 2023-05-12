package id.co.qualitas.qubes.model;

/**
 * Created by Natalia on 2/24/2017.
 */

public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private String iconName;

    public NavDrawerItem() {

    }

    public NavDrawerItem(String title) {
        this.title = title;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
