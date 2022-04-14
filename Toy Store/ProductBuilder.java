

public class ProductBuilder {
    String uniqueId;
    String name;
    Manufacturer manufacturer;
    double price;
    int quantity;
    Discount discount;

    public ProductBuilder() {
    }

    public ProductBuilder(String uniqueId, String name, Manufacturer manufacturer, double price, int quantity, Discount discount) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
    }

    public ProductBuilder setUniqueID(String uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }

    public ProductBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public ProductBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public ProductBuilder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductBuilder setDiscount(Discount discount) {
        this.discount = discount;
        return this;
    }

    public Product build() {
        return new Product(uniqueId, name, manufacturer, price, quantity);
    }
}
