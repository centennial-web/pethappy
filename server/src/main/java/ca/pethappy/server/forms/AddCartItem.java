package ca.pethappy.server.forms;

public class AddCartItem {
    private String deviceId;
    private Long userId;
    private Long productId;

    public AddCartItem(String deviceId, Long userId, Long productId) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.productId = productId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
