package ca.pethappy.server.forms;

public class UserSettings {
    private Long id;
    private boolean use2fa;

    public UserSettings() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isUse2fa() {
        return use2fa;
    }

    public void setUse2fa(boolean use2fa) {
        this.use2fa = use2fa;
    }

}
