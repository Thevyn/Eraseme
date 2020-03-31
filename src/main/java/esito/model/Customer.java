package esito.model;

public class Customer {

    private String creditCard;
    private int customerNo;
    private String email;
    private String name;
    private String password;
    private String phone;

    public Customer(String creditCard, int customerNo, String email, String name, String password, String phone) {
        this.creditCard = creditCard;
        this.customerNo = customerNo;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public int getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(int customerNo) {
        this.customerNo = customerNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "creditCard='" + creditCard + '\'' +
                ", customerNo=" + customerNo +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
