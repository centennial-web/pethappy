package ca.pethappy.server.forms;

import ca.pethappy.server.models.User;
import ca.pethappy.server.validators.ValidPassword;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.*;

@ScriptAssert(lang = "javascript",
        script = "_this.password.equals(_this.repeatPassword)",
        message = "Password does not match")
public class UserRegistration {
    @Email
    private String email;

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    private String repeatPassword;

    @Size(min = 2, message = "First name should have at least 2 characters")
    private String firstName;

    @Size(min = 2, message = "Last name should have at least 2 characters")
    private String lastName;

    @Size(min = 10, message = "Phone number is invalid")
    @Pattern(regexp = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$", message = "Phone number is invalid")
    private String cellPhone;

    @NotBlank(message = "Address is invalid")
    private String address;

    private String unit;

    @NotNull(message = "Postal code is required")
    @Size(min = 7, max = 7, message = "Postal code size is invalid")
    @Pattern(regexp = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$", message = "Postal code is invalid")
    private String postalCode;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Province/Territory is required")
    @Pattern(regexp = "^(?:AB|BC|MB|N[BLTSU]|ON|PE|QC|SK|YT)*$", message = "Province/Territory does not exists")
    private String province;

    private String buzzer;

    public UserRegistration() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getBuzzer() {
        return buzzer;
    }

    public void setBuzzer(String buzzer) {
        this.buzzer = buzzer;
    }

    public static User toUser(UserRegistration userRegistration) {
        User user = new User();
        user.setActive(true);
        user.setPassword(userRegistration.password);
        user.setEmail(userRegistration.email);
        user.setFirstName(userRegistration.firstName);
        user.setLastName(userRegistration.lastName);
        user.setCellPhone(userRegistration.cellPhone);
        user.setAddress(userRegistration.address);
        user.setUnit(userRegistration.unit);
        user.setPostalCode(userRegistration.postalCode);
        user.setCity(userRegistration.city);
        user.setProvince(userRegistration.province);
        user.setBuzzer(userRegistration.buzzer);
        return user;
    }
}
